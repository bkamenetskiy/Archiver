import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.net.URL;

public class Main {


    public static void main(String[] args) throws IOException {

        // хранилище путей к исходным данным
        String sourcePaths;

        // хранилище путей к результатам
        String resultPaths;

        // путь к конфигу
        URL xmlUrl = Main.class.getResource("config.xml");

        // пути к исходным данным и результатам
        Configurations config = new Configurations();
        XMLConfiguration configXML = null;

        try {
            configXML = config.xml(xmlUrl.getPath());
        } catch (ConfigurationException e) {
            System.out.println(e);
        }

        sourcePaths = configXML.getString("paths.sourcefolders.sourcefolder[@path]");
        resultPaths = configXML.getString("paths.resultfolders.resultfolder[@path]");

        // Архив
        Archiver archiver = new Archiver();

        // степень сжатия. от 0 до 9 - минимум и максимум соответственно
        int setLevel = 0;

        // условия и названия
        String[] filter = new String[] {"[a-mA-M]*.*", "[n-zN-Z]*.*", "[0-9][0-9][0-9][0-9]*.*"};
        String[] name = new String[] {"am.zip", "nz.zip", "digit.zip"};

        // название папки для хранения архивов. шаблон "yyyy-MM-dd HH-mm"
        TimeService timeService = new TimeService();
        String newDir = timeService.getTime();

        // архивирование
        if (!sourcePaths.equals("") && !resultPaths.equals("")) {

            for (int i = 0; i <= filter.length - 1; i++) {

                System.out.println("Archiving in " + name[i] + " started");
                archiver.toArchive(name[i], filter[i], sourcePaths, resultPaths, setLevel, newDir);
            }

            System.out.println("Archiving completed");

        } else {

            System.out.println("Archives not created. Check the correct path to folders.");
        }

    }

}
