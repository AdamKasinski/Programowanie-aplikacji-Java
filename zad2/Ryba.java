package sample;

import java.time.LocalDate;

public class Ryba {

    private String gatunek;
    private Float rozmiar;
    private LocalDate dataZlowienia;
    private Boolean czyWymiarowa;
    private String wdkrz;
    private Person person;


    public Ryba(String gatunek, Float rozmiar, LocalDate dataZlowienia, Boolean czyWymiarowa, Person person) {
        this.gatunek = gatunek;
        this.rozmiar = rozmiar;
        this.dataZlowienia = dataZlowienia;
        this.czyWymiarowa = czyWymiarowa;
        this.person = person;
    }


    public Ryba(String gatunek, Float rozmiar, LocalDate dataZlowienia, Boolean czyWymiarowa, String wdkrz) {
        this.gatunek = gatunek;
        this.rozmiar = rozmiar;
        this.dataZlowienia = dataZlowienia;
        this.czyWymiarowa = czyWymiarowa;
        this.wdkrz = wdkrz;
    }



    public String getGatunek() {
        return gatunek;
    }

    public Float getRozmiar() {
        return rozmiar;
    }

    public LocalDate getDataZlowienia() {
        return dataZlowienia;
    }

    public Boolean getCzyWymiarowa() {
        return czyWymiarowa;
    }

    public String getWdkrz() {
        return wdkrz;
    }
}
