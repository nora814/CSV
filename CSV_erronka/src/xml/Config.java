package xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Konfigurazio fitxategi bat irakurtzen duen eta datu baseko konfigurazioak eskuratzen dituen klasea.
 */
public class Config {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    /**
     * Konfigurazio fitxategiaren ibilbidea emanda, konfigurazioak kargatzen ditu.
     *
     * @param configFilePath Konfigurazio fitxategiaren ibilbidea.
     */
    public Config(String configFilePath) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
            dbUrl = properties.getProperty("db_url");
            dbUser = properties.getProperty("db_user");
            dbPassword = properties.getProperty("db_password");
        } catch (IOException e) {
            System.err.println("Errorea konfigurazio fitxategia irakurtzean: " + e.getMessage());
        }
    }

    /**
     * Datu basearen URL-a itzultzen du.
     *
     * @return Datu basearen URL-a.
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Datu baseko erabiltzaile izena itzultzen du.
     *
     * @return Datu baseko erabiltzaile izena.
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Datu baseko pasahitza itzultzen du.
     *
     * @return Datu baseko pasahitza.
     */
    public String getDbPassword() {
        return dbPassword;
    }
}
