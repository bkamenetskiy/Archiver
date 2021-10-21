import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        // Пути
        Parser parser = new Parser();

        // хранилище путей к исходным данным
        ArrayList<String> sourcePaths = new ArrayList<>();
        // хранилище путей к результатам
        ArrayList<String> resultPaths = new ArrayList<>();
        // путь к конфигу
        String xmlpath = "src\\config.xml";
        // заполнение хранилища
        parser.getPath(sourcePaths, resultPaths, xmlpath);

        // Архив
        Archiver archiver = new Archiver();

        // степень сжатия. от 0 до 9 - минимум и максимум соответственно
        int setLevel = 0;

        // условия и названия
        String[] filter = new String[] {"[a-mA-M]*.*", "[n-zN-Z]*.*", "[0-9][0-9][0-9][0-9]*.*"};
        String[] name = new String[] {"am.zip", "nz.zip", "digit.zip"};

        // архивирование

        if ((sourcePaths.size() != 0) && (resultPaths.size() != 0)) {

            for (int i = 0; i <= filter.length - 1; i++) {

                archiver.toArchive(name[i], filter[i], sourcePaths.get(0), resultPaths.get(0), setLevel);
            }

            System.out.println("All archives are created.");

        } else {

            System.out.println("Archives not created. Check the correct path to folders.");
        }






    }



}
