package sample;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.naming.Binding;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class Ryby {
    @FXML
    TableView wedkarze, ryby;
    @FXML
    TableColumn imie;   //,nazwisko,czyDoloczyl,kiedyWydana;
    @FXML
    TableColumn nazwisko;
    @FXML
    TableColumn czyDoloczyl;
    @FXML
    TableColumn kiedyWydana;
    @FXML
    TableColumn dodajRybe;
    @FXML
    TableColumn gatunek,rozmiar,czyZweryfikowana,ktoZlowil,dataZlowienia;
    @FXML TextField imieWedkarza,nazwiskoWedkarza;
    @FXML DatePicker dataUzyskania;
    @FXML CheckBox czyNalezy;
    @FXML Button dodajFanatyka;
    @FXML Button zainicjuj;

    int ileRyb = 0;
    Baza wedkarz = null;
    Connection connection;
    ObservableList<Person> persons = FXCollections.observableArrayList();
    ObservableList<Ryba> rybas = FXCollections.observableArrayList();


    private void czyOk(){ // przycisk dodania jest zablokowany aż do uzupełnienia wszystkich pól
        dodajFanatyka.disableProperty().bind(imieWedkarza.textProperty().isEmpty().or(nazwiskoWedkarza.textProperty().isEmpty()).or(dataUzyskania.valueProperty().isNull()));
    }


    public void zainicjuj() {
        czyOk();
        try {
            wedkarz = new Baza();
            connection = wedkarz.connect();
            wedkarz.wedkarzeTabela(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        imieWedkarza.setDisable(false);
        nazwiskoWedkarza.setDisable(false);
        czyNalezy.setDisable(false);
        dataUzyskania.setDisable(false);
        zainicjuj.setDisable(true);
    }

    public void wezDane(){ //zbiera dane, umieszcza w bazie i wypisuje
        Person person = wedkarz.addPerson(imieWedkarza.getText(),nazwiskoWedkarza.getText(),dataUzyskania.getValue(),czyNalezy.isSelected(),connection);
        persons.add(person);
        imie.setCellValueFactory(new PropertyValueFactory<Person, String>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<Person, String>("nazwisko"));
        czyDoloczyl.setCellValueFactory(new PropertyValueFactory<Person,Boolean>("czy_nalezy"));
        kiedyWydana.setCellValueFactory(new PropertyValueFactory<Person, LocalDate>("data_wydania_karty"));
        dodajRybe.setCellFactory(new Callback<TableColumn<Person,Boolean>, TableCell<Person,Boolean>>() {
            @Override
            public TableCell<Person, Boolean> call(TableColumn<Person, Boolean> personBooleanTableColumn) {
                return new ButtonAdd(wedkarze);
            }
        });
        imieWedkarza.clear();
        nazwiskoWedkarza.clear();
        dataUzyskania.setValue(null);
        czyNalezy.setSelected(false);
        wedkarze.setItems(persons);
    }


    private class ButtonAdd extends TableCell<Person,Boolean>{
        final Button addButton = new Button("dodaj rybę");
        ButtonAdd(TableView<Person> wedkarze){
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dodajRybe.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(loader.load(),700,600));
                        DodajRybe dodajRybe = loader.getController();
                        dodajRybe.numericOnly();
                        dodajRybe.czyRybaOk();
                        dodajRybe.polocz(connection,stage,persons.get(getIndex()));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        protected void updateItem(Boolean t, boolean empty){
            super.updateItem(t,empty);
            if(!empty){
                setGraphic(addButton);
            }else{
                setGraphic(null);
                setText("");
            }
        }
    }

    public void wypiszRyby(){ //wypisuje ryby w tabeli - za to odpowiada przycisk zmiany zakładek
        try {
            int theCount = 0;
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM ZLOWIONE_RYBY");
            if (rs.next()) {
                theCount = rs.getInt(1);
            }
            if (theCount != ileRyb) {
                ileRyb = theCount;
                String wdkrz;
                rs = connection.createStatement().executeQuery("SELECT ZLOWIONE_RYBY.GATUNEK, ZLOWIONE_RYBY.ROZMIAR,ZLOWIONE_RYBY.CZY_ZWERYFIKOWANA,ZLOWIONE_RYBY.DATA_ZLOWIENIA, FANATYCY_WEDKARSTWA.IMIE, FANATYCY_WEDKARSTWA.NAZWISKO FROM ZLOWIONE_RYBY JOIN FANATYCY_WEDKARSTWA ON ZLOWIONE_RYBY.WEDKARZ = FANATYCY_WEDKARSTWA.ID ORDER BY ZLOWIONE_RYBY.ID DESC LIMIT 1");
                while (rs.next()) {
                    LocalDate data = rs.getDate("DATA_ZLOWIENIA").toLocalDate();
                    wdkrz = rs.getString("FANATYCY_WEDKARSTWA.IMIE") + " " + rs.getString("FANATYCY_WEDKARSTWA.NAZWISKO");
                    Ryba ryba = new Ryba(rs.getString("GATUNEK"), rs.getFloat("ROZMIAR"), data, rs.getBoolean("CZY_ZWERYFIKOWANA"),wdkrz);
                    rybas.add(ryba);
                }
                gatunek.setCellValueFactory(new PropertyValueFactory<Ryba, String>("gatunek"));
                rozmiar.setCellValueFactory(new PropertyValueFactory<Ryba, Float>("rozmiar"));
                czyZweryfikowana.setCellValueFactory(new PropertyValueFactory<Ryba, Boolean>("CzyWymiarowa"));
                ktoZlowil.setCellValueFactory(new PropertyValueFactory<Ryba, String>("wdkrz"));
                dataZlowienia.setCellValueFactory(new PropertyValueFactory<Ryba, LocalDate>("dataZlowienia"));
                ryby.setItems(rybas);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}