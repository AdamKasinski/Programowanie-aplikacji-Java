package sample;

public class Kandydat {
    private String imie;
    private String nazwisko;
    protected float pierwszaTura;
    protected float drugaTura;
    protected boolean czyWygral;

    public Kandydat(String imie, String nazwisko, float pierwszaTura, float drugaTura, boolean czyWygral) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pierwszaTura = pierwszaTura;
        this.drugaTura = drugaTura;
        this.czyWygral = czyWygral;
    }

    public void setPierwszaTura(float pierwszaTura) {
        this.pierwszaTura = pierwszaTura;
    }

    public void setDrugaTura(float drugaTura) {
        this.drugaTura = drugaTura;
    }

    public void setCzyWygral(boolean czyWygral) {
        this.czyWygral = czyWygral;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public float getPierwszaTura() {
        return pierwszaTura;
    }

    public float getDrugaTura() {
        return drugaTura;
    }

    public boolean isCzyWygral() {
        return czyWygral;
    }
}
