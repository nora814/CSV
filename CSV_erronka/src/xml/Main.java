package xml;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Programa nagusia, bilaketa egiten du eta CSV fitxategira esportatzen ditu datuak.
 */
public class Main {

    public static void main(String[] args) {

        String searchTerm = args[0];  // Bilaketa terminoaren izena (herria edo probintzia)
        String searchType = args[1];  // Bilaketa mota: "herria" edo "probintzia"

        // Konfigurazio fitxategia irakurtzen dugu
        String configFilePath = "Fitx/konfigurazio.txt";  // Konfigurazioaren ibilbidea
        Config config = new Config(configFilePath);

        // Datu basearekin konektatzen gara
        DB db = new DB(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        Connection connection = db.getConnection();

        if (connection == null) {
            System.err.println("Errorea datu basearekin konektatzean.");
            return;
        }

        try {
            // SQL kontsulta sortzen dugu bilaketa motaren arabera
            String query = buildQuery(searchTerm, searchType);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Datuak CSV-ra esportatzen ditugu
            CSVExporter.exportToCSV(resultSet, searchTerm);

            System.out.println("Exportazioa amaituta.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SQL kontsulta sortzen du bilaketa termino eta motaren arabera.
     *
     * @param searchTerm Bilaketa terminoaren izena.
     * @param searchType Bilaketa motaren izena (herria edo probintzia).
     * @return SQL kontsulta.
     */
    private static String buildQuery(String searchTerm, String searchType) {
        String baseQuery = "SELECT K.KODEA, K.IZENA, K.KOKALEKUA, K.HELBIDEA, K.POSTAKODEA, " +
                           "P.IZENA AS HERRIA, H.IZENA AS PROBINTZIA, K.KATEGORIA, K.EDUKIERA " +
                           "FROM KANPINAK K " +
                           "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA " +
                           "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA ";  // Arreglada

        // Bilaketa mota kontuan hartuta, filtratzen dugu
        if (searchType.equalsIgnoreCase("herria")) {
            baseQuery += "WHERE H.IZENA = '" + searchTerm + "'";
        } else if (searchType.equalsIgnoreCase("probintzia")) {
            baseQuery += "WHERE P.IZENA = '" + searchTerm + "'";
        } else {
            throw new IllegalArgumentException("Bilaketa mota ez da zuzena: " + searchType);
        }

        return baseQuery;
    }
}
