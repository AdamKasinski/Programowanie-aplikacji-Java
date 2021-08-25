import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbTester {

/*
    public void createTable(Statement s) throws SQLException {
        s.execute("CREATE TABLE KANDYDAT_WYBORY( IMIE varchar(255), " +
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


 */
    @Test
    public void testCreateTable() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                s.execute("CREATE TABLE KANDYDAT_WYBORY( IMIE varchar(255), " +
                        "NAZWISKO varchar(255), " +
                        "WYNIK_1_TURA float," +
                        "WYNIK_2_TURA float, " +
                        "CZY_WYGRAL bit)");
                s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('JAN','KOWALSKI')");
                s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('PIOTR','NOWAK')");
                s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('TOMASZ','IKSINSKI')");
                s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('MAREK','IGREK')");
                s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('MACIEJ','TOMASZEWSKI')");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(Statement s) throws SQLException {
        s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = 12.0 WHERE IMIE = 'JAN' AND NAZWISKO = 'KOWALSKI'");
        s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = 25.0 WHERE IMIE = 'PIOTR' AND NAZWISKO = 'NOWAK' ");
        s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = 20.0 WHERE IMIE = 'TOMASZ' AND NAZWISKO = 'IKSINSKI'");
        s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = 13.0 WHERE IMIE = 'MAREK' AND NAZWISKO = 'IGREK'");
        s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA = 35.0 WHERE IMIE = 'MACIEJ' AND NAZWISKO = 'TOMASZEWSKI'");
    }


    @Test
    public void testUpdate() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                //createTable(se);
                update(s);
                List<Integer> wyniki = new ArrayList<>();
                try (ResultSet rs = s.executeQuery("SELECT KANDYDAT_WYBORY.WYNIK_1_TURA FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
                    while (rs.next()) {
                        Integer row;
                        row = rs.getInt("WYNIK_1_TURA");
                        wyniki.add(row);
                    }
                }
                Assertions.assertEquals(25, wyniki.get(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testPierwszaTura() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement se = c.createStatement()) {
                //createTable(se);
                //update(se);
                List<Object[]> ludzie = new ArrayList<>();
                try (ResultSet rs = se.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
                    while (rs.next()) {
                        Object[] row = new Object[2];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");
                        ludzie.add(row);
                    }
                }
                Assertions.assertArrayEquals(new Object[]{"PIOTR", "NOWAK"}, ludzie.get(1));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void drugaTura() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement se = c.createStatement()) {
                update(se);
                ArrayList<Object[]> ludzie = new ArrayList<>();
                try (ResultSet rs = se.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
                    while (rs.next()) {
                        Object[] row = new Object[2];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");
                        ludzie.add(row);
                    }
                    c.setAutoCommit(false);
                    PreparedStatement wybierz = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA = ? WHERE IMIE = ? AND NAZWISKO = ?");
                    wybierz.setString(2, String.valueOf(ludzie.get(0)[0]));
                    wybierz.setString(3, String.valueOf(ludzie.get(0)[1]));
                    wybierz.setFloat(1, 56);
                    PreparedStatement wy = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA = ? WHERE IMIE = ? AND NAZWISKO = ?");
                    wy.setString(2, String.valueOf(ludzie.get(1)[0]));
                    wy.setString(3, String.valueOf(ludzie.get(1)[1]));
                    wy.setFloat(1, 44);
                    wybierz.execute();
                    wy.execute();
                    se.execute("UPDATE KANDYDAT_WYBORY  SET WYNIK_2_TURA = 0 WHERE WYNIK_2_TURA IS NULL ");
                    c.rollback();

                    PreparedStatement druga = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA = ? WHERE IMIE = ? AND NAZWISKO = ?");
                    druga.setString(2, String.valueOf(ludzie.get(1)[0]));
                    druga.setString(3, String.valueOf(ludzie.get(1)[1]));
                    druga.setFloat(1, 56);
                    PreparedStatement w = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA = ? WHERE IMIE = ? AND NAZWISKO = ?");
                    w.setString(2, String.valueOf(ludzie.get(0)[0]));
                    w.setString(3, String.valueOf(ludzie.get(0)[1]));
                    w.setFloat(1, 44);
                    druga.execute();
                    w.execute();
                    se.execute("UPDATE KANDYDAT_WYBORY  SET WYNIK_2_TURA = 0 WHERE WYNIK_2_TURA IS NULL ");
                    c.commit();

                }
                ArrayList<Float> wyn2 = new ArrayList<>();
                ArrayList<String> imie2 = new ArrayList<>();
                try (ResultSet res = se.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.WYNIK_2_TURA FROM KANDYDAT_WYBORY")) {
                    while (res.next()) {
                        wyn2.add(res.getFloat("WYNIK_2_TURA"));
                        imie2.add(res.getString("IMIE"));

                    }
                }

                Assertions.assertEquals("PIOTR", imie2.get(0));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void aktualizuj() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement se = c.createStatement()) {
                //ArrayList<String[]> zwyciezca = new ArrayList<>();
                try (ResultSet s = se.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
                    String imie = "";
                    String nazwisko = "";
                    while (s.next()) {
                        imie = s.getString("IMIE");
                        nazwisko = s.getString("NAZWISKO");
                    }
                    try (PreparedStatement wyg = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL = TRUE WHERE IMIE = ? AND NAZWISKO = ? ")) {
                        wyg.setString(1, imie);
                        wyg.setString(2, nazwisko);
                        wyg.execute();
                    }
                    try (PreparedStatement wyg = c.prepareStatement("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL = FALSE WHERE IMIE != ? AND NAZWISKO != ? ")) {
                        wyg.setString(1, imie);
                        wyg.setString(2, nazwisko);
                        wyg.execute();
                    }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void czy_widac() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement se = c.createStatement()) {
                testCreateTable();
                update(se);
                testPierwszaTura();
                //drugaTura();
                aktualizuj();
                try (ResultSet s = se.executeQuery("SELECT KANDYDAT_WYBORY.IMIE, KANDYDAT_WYBORY.NAZWISKO FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
                    String imie = "";
                    String nazwisko = "";
                    while (s.next()) {
                        imie = s.getString("IMIE");
                        nazwisko = s.getString("NAZWISKO");
                    }
                    try (PreparedStatement zwy = c.prepareStatement("SELECT KANDYDAT_WYBORY.CZY_WYGRAL FROM KANDYDAT_WYBORY WHERE IMIE = ? AND NAZWISKO = ?")){
                        zwy.setString(1,imie);
                        zwy.setString(2,nazwisko);
                        zwy.execute();
                        Boolean czywygral = false;
                        ResultSet a = zwy.executeQuery();
                        while (a.next()){
                            czywygral = a.getBoolean("CZY_WYGRAL");
                        }
                        Assertions.assertEquals(czywygral,true);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}