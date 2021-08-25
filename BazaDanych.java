package sample;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class BazaDanych {
    Connection c = null;
    Statement s = null;

    public BazaDanych() throws SQLException {
        this.c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
        this.s = c.createStatement();
    }

    public void createTable() throws SQLException {

        s.execute("CREATE TABLE KANDYDAT_WYBORY ( IMIE varchar(255), " +
                "NAZWISKO varchar(255), " +
                "WYNIK_1_TURA float," +
                "WYNIK_2_TURA float, " +
                "CZY_WYGRAL bit)");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('JAN','KOWALSKI')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('PIOTR','NOWAK')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('TOMASZ','IKSINSKI')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('MAREK','IGREK')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('MACIEJ','TOMASZEWSKI')");
    }
}
