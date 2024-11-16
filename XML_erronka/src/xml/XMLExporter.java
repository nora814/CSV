package xml;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Datuak XML fitxategian esportatzeko erabiltzen den klasea.
 */
public class XMLExporter {

    /**
     * Datuak XML fitxategian esportatzen ditu.
     * 
     * @param resultSet Datu baseko emaitzak (ResultSet).
     */
    public static void exportToXML(ResultSet resultSet) {
        try {
            // XML dokumentua sortu
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Root nodoa <kanpinak> sortu
            Element rootElement = doc.createElement("kanpinak");
            doc.appendChild(rootElement);

            // ResultSet-a prozesatu
            while (resultSet.next()) {
                // Nodo bat sortu <kanpina> datu bakoitzeko
                Element kanpina = doc.createElement("kanpina");
                kanpina.setAttribute("id", resultSet.getString("KODEA"));
                rootElement.appendChild(kanpina);

                // Datuekin azpi-elementuak gehitu
                appendChildElement(doc, kanpina, "izena", resultSet.getString("IZENA"));
                appendChildElement(doc, kanpina, "deskribapena", resultSet.getString("DESKRIBAPENA"));
                appendChildElement(doc, kanpina, "kategoria", resultSet.getString("KATEGORIA"));
                appendChildElement(doc, kanpina, "edukiera", resultSet.getString("EDUKIERA"));

                // <kokalekua> elementua gehitu
                Element kokalekua = doc.createElement("kokalekua");
                kokalekua.appendChild(doc.createTextNode(resultSet.getString("KOKALEKUA")));
                kanpina.appendChild(kokalekua);

                // <helbidea> elementua eta azpi-elementuak gehitu
                Element helbidea = doc.createElement("helbidea");
                appendChildElement(doc, helbidea, "kalea", resultSet.getString("HELBIDEA"));
                appendChildElement(doc, helbidea, "postaKodea", resultSet.getString("POSTAKODEA"));

                // Herria eta probintzia egokitu
                Element herria = doc.createElement("herria");
                herria.setAttribute("id", resultSet.getString("PROBINTZIA_KODEA"));
                herria.appendChild(doc.createTextNode(resultSet.getString("PROBINTZIA")));
                helbidea.appendChild(herria);

                Element probintzia = doc.createElement("probintzia");
                probintzia.setAttribute("id", resultSet.getString("HERRI_KODEA"));
                probintzia.appendChild(doc.createTextNode(resultSet.getString("HERRIA")));
                helbidea.appendChild(probintzia);

                // <helbidea> nodoa gehitu <kanpina>-ra
                kanpina.appendChild(helbidea);

                // Beste datuak gehitu
                appendChildElement(doc, kanpina, "telefonoa", resultSet.getString("TELEFONOA"));
                appendChildElement(doc, kanpina, "emaila", resultSet.getString("EMAILA"));
                appendChildElement(doc, kanpina, "webgunea", resultSet.getString("WEBGUNEA"));

                // Estekak
                Element estekak = doc.createElement("estekak");
                appendChildElement(doc, estekak, "friendly", resultSet.getString("FRIENDLY_URL"));
                appendChildElement(doc, estekak, "physical", resultSet.getString("PHYSICAL_URL"));
                kanpina.appendChild(estekak);

                // Fitxategiak
                Element fitxategiak = doc.createElement("fitxategiak");
                appendChildElement(doc, fitxategiak, "dataXML", resultSet.getString("DATA_XML"));
                appendChildElement(doc, fitxategiak, "metadataXML", resultSet.getString("METADATA_XML"));
                appendChildElement(doc, fitxategiak, "zipFile", resultSet.getString("ZIP_FILE"));
                kanpina.appendChild(fitxategiak);
            }

            // Dokumentua XML fitxategian gorde
            saveDocumentToFile(doc);

        } catch (Exception e) {
            System.err.println("Errorea ResultSet datuak prozesatzean edo XML-a sortzean: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * XML elementu bat gehitzen du.
     * 
     * @param doc     XML dokumentua.
     * @param parent  Guraso nodoa.
     * @param tagName Elementuaren etiketaren izena.
     * @param value   Elementuaren balioa.
     */
    private static void appendChildElement(Document doc, Element parent, String tagName, String value) {
        if (value != null && !value.isEmpty()) {
            Element element = doc.createElement(tagName);
            element.appendChild(doc.createTextNode(value));
            parent.appendChild(element);
        }
    }

    /**
     * XML dokumentua fitxategian gorde.
     * 
     * @param doc XML dokumentua.
     * @throws IOException
     */
    private static void saveDocumentToFile(Document doc) throws IOException {
        try {
            // XML fitxategiaren ruta sortu
            File file = new File("Fitx/export.xml");
            FileWriter writer = new FileWriter(file);

            // XML dokumentua string bihurtu eta fitxategian idatzi
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            System.out.println("XML fitxategia esportatuta: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Errorea XML dokumentua gordetzean: " + e.getMessage());
        }
    }
}
