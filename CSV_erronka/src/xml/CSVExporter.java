package xml;

import java.sql.ResultSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Datuak CSV fitxategi batean esportatzen dituen klasea.
 */
public class CSVExporter {

    /**
     * Datuak CSV fitxategi batera esportatzen ditu.
     * 
     * @param resultSet Datu baseko emaitzak, CSV-ra esportatu behar direnak.
     * @param searchTerm Bilaketa terminoaren izena (herria edo probintzia).
     */
    public static void exportToCSV(ResultSet resultSet, String searchTerm) {
        // Bilaketa terminoaren segurtasun prozesua, karaktere bereziak ordezkatuz
        String safeSearchTerm = searchTerm.replaceAll("[^a-zA-Z0-9-_]", "_");

        // CSV fitxategiaren ibilbidea definitzen dugu
        String filePath = "Fitx/Export_" + safeSearchTerm + ".csv";  // Fitxetik exportatuko da

        // "Fitx" karpeta sortzen dugu, ez badago
        File dir = new File("Fitx");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();  // Karpeta sortzen saiatzen gara
            if (created) {
                System.out.println("Karpeta 'Fitx' arrakastaz sortu da.");
            } else {
                System.out.println("Ez da posible izan karpeta 'Fitx' sortzea.");
            }
        } else {
            System.out.println("Karpeta 'Fitx' jada existitzen da.");
        }

        // CSV fitxategiaren ibilbidea inprimatzen dugu egiaztatzeko
        System.out.println("CSV fitxategiaren ibilbidea: " + new File(filePath).getAbsolutePath());

        try (FileWriter writer = new FileWriter(filePath)) {
            // CSV-ren goiburua idazten dugu
            writer.append("Kodea,Izena,Kokalekua,Helbidea,PostaKodea,Herria,Probintzia,Kategoria,Edukiera\n");

            // ResultSet bakoitzeko datuak CSV-ra idazten ditugu
            while (resultSet.next()) {
                // Emaitzetatik datuak lortzen ditugu
                String kodea = resultSet.getString("KODEA");
                String izena = resultSet.getString("IZENA");
                String kokalekua = resultSet.getString("KOKALEKUA");
                String helbidea = resultSet.getString("HELBIDEA");
                String postaKodea = resultSet.getString("POSTAKODEA");
                String herria = resultSet.getString("HERRIA");
                String probintzia = resultSet.getString("PROBINTZIA");
                String kategoria = resultSet.getString("KATEGORIA");
                String edukiera = resultSet.getString("EDUKIERA");

                // CSV-ra datuak idazten ditugu
                writer.append(escapeCSV(kodea) + ",");
                writer.append(escapeCSV(izena) + ",");
                writer.append(escapeCSV(kokalekua) + ",");
                writer.append(escapeCSV(helbidea) + ",");
                writer.append(escapeCSV(postaKodea) + ",");
                writer.append(escapeCSV(herria) + ",");
                writer.append(escapeCSV(probintzia) + ",");
                writer.append(escapeCSV(kategoria) + ",");
                writer.append(escapeCSV(edukiera) + "\n");
            }

            System.out.println("CSV fitxategia arrakastaz esportatu da " + filePath);
        } catch (IOException | SQLException e) {
            System.err.println("Errorea CSV fitxategia idaztean: " + e.getMessage());
        }
    }

    /**
     * CSV fitxategian karaktere bereziak behar den bezala tratatzeko laguntza metodoa.
     *
     * @param value Balio bat, CSV formatua egokia izan dadin.
     * @return CSV-ra egokitutako balioa.
     */
    private static String escapeCSV(String value) {
        if (value == null) return "";
        value = value.trim();
        // Balioak koma, lerro haustura edo goiko zurrunbiloak baditu, goiko zurrunbiloak gehiatzen ditugu
        if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            value = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
