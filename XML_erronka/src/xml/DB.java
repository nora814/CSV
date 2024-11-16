package xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Datu basearekin konektatzeko erabiliko den klasea.
 */
public class DB {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     * Datu basearen URL-a, erabiltzaile izena eta pasahitza ematen ditu.
     * 
     * @param dbUrl      Datu basearen URL-a.
     * @param dbUser     Datu baseko erabiltzaile izena.
     * @param dbPassword Datu baseko pasahitza.
     */
    public DB(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Datu basearekin konektatzen da eta konexioa itzultzen du.
     * 
     * @return Datu basearen konexioa.
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
