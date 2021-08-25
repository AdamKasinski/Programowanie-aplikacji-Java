package sample;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import sun.util.calendar.BaseCalendar;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Person {
    private String imie;
    private String nazwisko;
    private LocalDate data_wydania_karty;
    private Boolean czy_nalezy;
    private int ID;

    public Person(String imie, String nazwisko, LocalDate data_wydania_karty, Boolean czy_nalezy, int ID) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.data_wydania_karty = data_wydania_karty;
        this.czy_nalezy = czy_nalezy;
        this.ID = ID;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public LocalDate getData_wydania_karty() {
        return data_wydania_karty;
    }

    public Boolean getCzy_nalezy() {
        return czy_nalezy;
    }

    public int getID(){
        return ID;
    }

}
