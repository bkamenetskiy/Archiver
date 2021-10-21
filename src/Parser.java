import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    protected void getPath (ArrayList<String> sourcePaths, ArrayList<String> resultPaths, String xmlPath) throws ParserConfigurationException, IOException, SAXException {

        sourcePaths.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlPath));

        // что ищем
        NodeList pathSource = document.getDocumentElement().getElementsByTagName("sourcefolder");
        NodeList pathResult = document.getDocumentElement().getElementsByTagName("resultfolder");

        //  пути к исходным данным
        for (int i = 0; i <= pathSource.getLength() - 1; i++) {

            Node path = pathSource.item(i);

            // Получение атрибутов каждого элемента
            NamedNodeMap attributes = path.getAttributes();

            // Добавление пути
            sourcePaths.add(attributes.getNamedItem("path").getNodeValue());
        }

        // пути к результатам
        for (int i = 0; i <= pathResult.getLength() - 1; i++) {

            Node path = pathResult.item(i);

            // Получение атрибутов каждого элемента
            NamedNodeMap attributes1 = path.getAttributes();

            // Добавление пути
            resultPaths.add(attributes1.getNamedItem("path").getNodeValue());
        }
        // решение не изящное, но задача маленькая и нет смысла писать обработчик в общем виде


        // Вывод информации из листа
/*
        for (String result : sourcePaths) {
            System.out.println(result);
            System.out.print("    ");
        }

        for (String result : resultPaths) {
            System.out.println(result);
            System.out.print("    ");
        }
 */
    }
}
