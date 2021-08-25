package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class DodajRybe {

    @FXML
    TextField gatunek,rozmiar;
    @FXML
    DatePicker dataZlowienia;
    @FXML
    CheckBox czyZweryfikowana;
    @FXML
    Button dodajRybe;

    Connection connection;
    int wedkarz;
    Stage stage;
    Person person;

    public void polocz(Connection connection, Stage stage, Person person){
        this.connection = connection;
        this.stage = stage;
        this.person = person;
    }

    public void numericOnly() {
        rozmiar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    rozmiar.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void czyRybaOk(){
        dodajRybe.disableProperty().bind(gatunek.textProperty().isEmpty().or(rozmiar.textProperty().isEmpty()).or(dataZlowienia.valueProperty().isNull()));
    }

    public void dodajRybe(){
        rozmiar.getText();
        Ryba ryba = new Ryba(gatunek.getText(),Float.parseFloat(rozmiar.getText()),dataZlowienia.getValue(),czyZweryfikowana.isSelected(),person);
        try {
            Statement s = connection.createStatement();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ZLOWIONE_RYBY (GATUNEK,ROZMIAR,CZY_ZWERYFIKOWANA,DATA_ZLOWIENIA,WEDKARZ) VALUES (?,?,?,?,?)");
            statement.setString(1,ryba.getGatunek());
            statement.setFloat(2,ryba.getRozmiar());
            statement.setBoolean(3, ryba.getCzyWymiarowa());
            statement.setDate(4, Date.valueOf(ryba.getDataZlowienia()));
            //statement.setObject(5,person);
            statement.setObject(5,person.getID());
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        stage.close();
    }
}
