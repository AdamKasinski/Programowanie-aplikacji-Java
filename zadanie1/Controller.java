package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hsqldb.HsqlException;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Controller {
    private ObservableList<Kandydat> kandydats;
    private BazaDanych baza;
    private ArrayList<Float> tura1;

    @FXML
    private TableColumn imie, nazwisko, pierwszaTura, drugaTura, czyWygral;
    @FXML
    private TableView wyniki;
    @FXML
    private Button zal_wst_dane, pokażDanezBazy, przeprowadz1ture, przeprowadz2ture, zatwierdźWyniki;

    private ObservableList<Kandydat> kandydaci(BazaDanych baza) {
        ObservableList<Kandydat> kandydats = FXCollections.observableArrayList();
        try {
            ResultSet rs = baza.s.executeQuery("SELECT * FROM KANDYDAT_WYBORY");
            while (rs.next()) {
                Kandydat k = new Kandydat(rs.getString("IMIE"), rs.getString("NAZWISKO"), rs.getFloat("WYNIK_1_TURA"),
                        rs.getFloat("WYNIK_2_TURA"), rs.getBoolean("CZY_WYGRAL"));
                kandydats.add(k);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return kandydats;
    }

    public void zaladujWstepneDane() throws SQLException {
        baza = null;
        try {
            baza = new BazaDanych();
            baza.createTable();
        } catch (HsqlException e) {
            System.out.println("");
        }
        kandydats = kandydaci(baza);
        imie.setCellValueFactory(new PropertyValueFactory<Kandydat, String>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<Kandydat, String>("nazwisko"));
        pierwszaTura.setCellValueFactory(new PropertyValueFactory<Kandydat, Float>("pierwszaTura"));
        drugaTura.setCellValueFactory(new PropertyValueFactory<Kandydat, Float>("drugaTura"));
        czyWygral.setCellValueFactory(new PropertyValueFactory<Kandydat, Boolean>("czyWygral"));
        zal_wst_dane.setDisable(true);
        pokażDanezBazy.setDisable(false);


    }

    public void wypisz() {
        wyniki.setItems(kandydats);
        przeprowadz1ture.setDisable(false);
    }

    public void losowaniePierwszaTura() {
        zal_wst_dane.setDisable(true);
        pokażDanezBazy.setDisable(true);
        float ile = 0;
        Random random = new Random();
        try {
            baza.c.setAutoCommit(false);
            float now = 1 + random.nextFloat() * (30);
            ile += now;
            PreparedStatement se = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = ? WHERE IMIE = 'JAN' AND NAZWISKO = 'KOWALSKI'");
            se.setFloat(1, now);
            se.execute();

            now = 1 + random.nextFloat() * (30);
            ile += now;
            se = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = ? WHERE IMIE = 'PIOTR' AND NAZWISKO = 'NOWAK'");
            se.setFloat(1, now);
            se.execute();

            now = 1 + random.nextFloat() * (30);
            ile += now;
            se = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = ? WHERE IMIE = 'TOMASZ' AND NAZWISKO = 'IKSINSKI'");
            se.setFloat(1, now);
            se.execute();

            now = 1 + random.nextFloat() * (30);
            ile += now;
            se = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = ? WHERE IMIE = 'MAREK' AND NAZWISKO = 'IGREK'");
            se.setFloat(1, now);
            se.execute();

            se = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = ? WHERE IMIE = 'MACIEJ' AND NAZWISKO = 'TOMASZEWSKI'");
            se.setFloat(1, 100 - ile);
            se.execute();
            try {
                LinkedList<Float> pier = new LinkedList<>();
                ResultSet rs = baza.s.executeQuery("SELECT KANDYDAT_WYBORY.WYNIK_1_TURA FROM KANDYDAT_WYBORY");
                int ktr = 0;
                while (rs.next()) {
                    kandydats.get(ktr).setPierwszaTura(rs.getFloat("WYNIK_1_TURA"));
                    ktr++;
                }

                pierwszaTura.setCellValueFactory(new PropertyValueFactory<Kandydat, Float>("pierwszaTura"));
                wyniki.refresh();
                przeprowadz2ture.setDisable(false);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void losowanieDrugaTura() throws SQLException {
        baza.c.commit();
        przeprowadz1ture.setDisable(true);
        baza.c.setAutoCommit(false);
        ArrayList<Object[]> ludzie = new ArrayList<>();
        try (ResultSet rs = baza.s.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("IMIE");
                row[1] = rs.getString("NAZWISKO");
                ludzie.add(row);
            }
            Random random = new Random();
            float wynik1 = random.nextFloat() * (100);
            float wynik2 = 100 - wynik1;
            baza.s.execute("UPDATE KANDYDAT_WYBORY  SET WYNIK_2_TURA = 0");
            PreparedStatement wybierz = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA = ? WHERE (IMIE = ? AND NAZWISKO = ?)");
            wybierz.setFloat(1, wynik1);
            wybierz.setString(2, String.valueOf(ludzie.get(0)[0]));
            wybierz.setString(3, String.valueOf(ludzie.get(0)[1]));
            wybierz.executeUpdate();
            wybierz.setFloat(1, wynik2);
            wybierz.setString(2, String.valueOf(ludzie.get(1)[0]));
            wybierz.setString(3, String.valueOf(ludzie.get(1)[1]));
            wybierz.executeUpdate();
            for (Kandydat kandydat : kandydats) {
                if (kandydat.getImie() == String.valueOf(ludzie.get(0)[0]) && kandydat.getNazwisko() == String.valueOf(ludzie.get(0)[1])) {
                    kandydat.setDrugaTura(wynik1);
                } else if (kandydat.getImie() == String.valueOf(ludzie.get(1)[0]) && kandydat.getNazwisko() == String.valueOf(ludzie.get(1)[1])) {
                    kandydat.setDrugaTura(wynik2);
                }
            }
            wyniki.refresh();
            zatwierdźWyniki.setDisable(false);

        } catch (SQLException e) {
            e.getSQLState();
        }
    }

    public void zatwierdzWyniki() throws SQLException {
        baza.c.commit();
        przeprowadz2ture.setDisable(true);
        try (ResultSet se = baza.s.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
            String imie = "";
            String nazwisko = "";
            while (se.next()) {
                imie = se.getString("IMIE");
                nazwisko = se.getString("NAZWISKO");
            }
            PreparedStatement wyg = baza.c.prepareStatement("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL = TRUE WHERE IMIE = ? AND NAZWISKO = ? ");
            wyg.setString(1, imie);
            wyg.setString(2, nazwisko);
            wyg.execute();
            for (Kandydat kandydat : kandydats) {
                if (kandydat.getImie() == imie && kandydat.getNazwisko() == nazwisko)
                    kandydat.setCzyWygral(true);
            }
            wyniki.refresh();
        }
    }
}
