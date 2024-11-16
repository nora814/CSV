package xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Datu basearekiko konexioa kudeatzen duen klasea.
 */
public class DB {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     * Datu basearekin konektatzeko konfigurazioa ezartzen du.
     *
     * @param dbUrl Datu basearen URL-a.
     * @param dbUser Datu baseko erabiltzaile izena.
     * @param dbPassword Datu baseko pasahitza.
     */
    public DB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Datu basearekin konektatzen da eta konexio objektua itzultzen du.
     *
     * @return Datu basearekin konektatutako konexio objektua.
     */
    public Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            System.err.println("Errorea JDBC gidaria kargatzean: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errorea datu basearekin konektatzean: " + e.getMessage());
        }
        return null;
    }
}
