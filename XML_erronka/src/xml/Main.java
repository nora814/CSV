package xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;

/**
 * Main klasea, datuak kontsultatu eta XML fitxategian esportatzen dituena.
 */
public class Main {

    /**
     * Main metodoa, aplikazioaren hasiera.
     * 
     * @param args Argumentuak (herria/probintzia eta bilaketa mota).
     */
    public static void main(String[] args) {
        // Argumentuak egiaztatu
        if (args.length != 2) {
            System.err.println("Errorea: Herria edo probintzia eta bilaketa mota ematea beharrezkoa da.");
            return;
        }

        String searchTerm = args[0];  // "Araba/Álava";
        String searchType = args[1];  //  "herria";

        // Datu basearen konfigurazioa fitxategitik irakurri
        String configFilePath = "Fitx/konfigurazio.txt";  // Konfigurazio fitxategiaren ibilbidea
        Config config = new Config(configFilePath);

        // Datu basearekin konektatzeko inicializazioa
        DB db = new DB(config.getDbUrl(), config.getDbUser(), config.getDbPassword());
        Connection connection = db.getConnection();

        if (connection == null) {
            System.err.println("Errorea datu basearekin konektatzean.");
            return;
        }

        try {
            // SQL kontsulta eraiki bilaketa motaren arabera
            String query = buildQuery(searchTerm, searchType);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Datuak XML fitxategian esportatu
            XMLExporter.exportToXML(resultSet);

            System.out.println("Exportazioa amaitua.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SQL kontsulta eraikitzen du bilaketa motaren arabera.
     * 
     * @param searchTerm Bilatzeko terminoa (herria edo probintzia).
     * @param searchType Bilaketa mota ("herria" edo "probintzia").
     * @return SQL kontsulta.
     */
    private static String buildQuery(String searchTerm, String searchType) {
        String baseQuery = "SELECT K.*, H.IZENA AS HERRIA, P.IZENA AS PROBINTZIA "
                         + "FROM KANPINAK K "
                         + "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA "
                         + "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA "
                         + "WHERE ";

        // Bilaketa motaren arabera kontsulta eraiki
        if (searchType.equalsIgnoreCase("herria")) {
            baseQuery += "H.IZENA = '" + searchTerm + "'";
        } else if (searchType.equalsIgnoreCase("probintzia")) {
            baseQuery += "P.IZENA = '" + searchTerm + "'";
        } else {
            throw new IllegalArgumentException("Bilaketa mota baliogabea: " + searchType);
        }
        
        return baseQuery;
    }
}
