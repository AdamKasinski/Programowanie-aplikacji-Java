package sample;

import java.sql.*;
import java.time.LocalDate;

public class Baza {


    public Connection connect() throws SQLException { //tworzy połączenie
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
        return connection;
    }

    public void wedkarzeTabela(Connection connection) throws SQLException { //tworzy tabele w bazie
        Statement s = connection.createStatement();
        s.execute("CREATE TABLE FANATYCY_WEDKARSTWA (ID INTEGER IDENTITY PRIMARY KEY,IMIE varchar(255), " +
                "NAZWISKO varchar(255), " +
                "CZY_NALEZY bit," +
                "DATA_UZYSKANIA date)");

        s.execute("CREATE TABLE ZLOWIONE_RYBY (ID INTEGER IDENTITY PRIMARY KEY, GATUNEK varchar(255), " +
                "ROZMIAR float, " +
                "CZY_ZWERYFIKOWANA bit," +
                "DATA_ZLOWIENIA date,"+
                "WEDKARZ INT)");
    }


    public Person addPerson(String imie, String nazwisko, LocalDate data, Boolean czyNalezy, Connection connection){ //dodaje fanatyków do bazy i zwraca obiek Person
        Person person;
        int ID = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO FANATYCY_WEDKARSTWA (IMIE,NAZWISKO,CZY_NALEZY,DATA_UZYSKANIA) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,imie);
            statement.setString(2,nazwisko);
            statement.setBoolean(3, czyNalezy);
            statement.setDate(4, Date.valueOf(data));
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                ID = Math.toIntExact(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        person = new Person(imie,nazwisko,data,czyNalezy,ID);
        return person;
    }
}
