import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Archiver {

    protected void toArchive (String fileName, String filter, String sourcePath, String resultPath, int setLevel) throws IOException {

        // хранилище путей к исходным данным
        ArrayList<Path> filePaths = new ArrayList();

        Path inputPath = Paths.get(sourcePath);
        Path outputPath = Paths.get(resultPath);

        // название папки с архивом
        //String newDir = "2021-10-19 HH-mm";                                                                           // на работе нет возможности соединиться с сервером времени
        String newDir = getTime();

        // абсолютный путь к папке в которой будет храниться архив
        Path zipFolderPath = Paths.get(outputPath + "\\" + newDir + "\\");
        // абсолютный путь к архиву
        Path zipFilePath = Paths.get(outputPath + "\\" + newDir + "\\" + fileName);

        // получение списка файлов, соответствующих шаблону filter и запись их абсолютных путей в массив
        DirectoryStream<Path> entries = Files.newDirectoryStream(inputPath, filter);

        for (Path entry : entries) {

            Path temp = Paths.get(String.valueOf(entry));
            filePaths.add(temp);
        }

        // кто вы, мистер Ansys.Products.2021.R2.Win64-SSQ, папка или файл?
        if (filePaths.size() >= 1) {

            for (int i = 0; i <= filePaths.size() - 1; i++) {

                if (!Files.isRegularFile(filePaths.get(i))) {

                    filePaths.remove(i);
                }
            }
        }

        if (filePaths.size() == 1) {

            if (!Files.isRegularFile(filePaths.get(0))) {
                filePaths.remove(0);
            }
        }

        // запись в архив
        // проверка на пустой список файлов
        if (filePaths.size() != 0) {

            // создание папки
            Files.createDirectories(zipFolderPath);

            // создание архива
            FileOutputStream fileOut = new FileOutputStream(zipFilePath.toFile());
            ZipOutputStream zipOut = new ZipOutputStream(fileOut);

            // степень сжатия. от 0 до 9 - минимум и максимум соответственно
            zipOut.setLevel(setLevel);

            // добавление записей в архив
            for (Path file : filePaths) {

                // а если файл удалили после составления списка?
                if (!Files.exists(file)) {
                    continue;
                }

                FileInputStream fis= new FileInputStream(file.toString());
                ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                zipOut.putNextEntry(zipEntry);

                // считываем содержимое файла в массив byte
                byte[] buffer = new byte[getBufferSize(fis.available())];

                int length;
                while((length = fis.read(buffer)) != -1) {

                    zipOut.write(buffer, 0, length);
                }

                // закрываем текущую запись
                zipOut.closeEntry();

            }

            zipOut.close();
            fileOut.close();

            filePaths.clear();

            System.out.println("Your file " + fileName + " has been generated!");
            System.out.println("Path: " + zipFilePath);
            System.out.println();

        } else {

            System.out.println("Archive " + fileName + " not created. The folder is empty or there are no matching files.");
            System.out.println();
        }
    }

    private String getTime () throws IOException {

        // Протокол обмена – NTP. Если трактовать шаблон времени из задания как чч.мм, то погрешность в 1 – 1,5 сек. приемлема для решения поставленной задачи.
        // При этом протокол NTP дает значительно меньшую погрешность.
        // Сервер – случайная коллекция из национальной зоны (ru).
        // Благодаря чему можно снизить погрешность, связанную с латентностью сети.

        // адрес сервера времени
        String TIME_SERVER = "0.ru.pool.ntp.org";
        //String TIME_SERVER = "0.time-a.nist.gov";
        NTPUDPClient timeClient = new NTPUDPClient();

        // время ожидания отклика, мс
        timeClient.setDefaultTimeout(10_000);
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
        Date time = new Date(returnTime);

        // шаблон
        String pattern = "yyyy-MM-dd HH-mm";

        String timePattern = new SimpleDateFormat(pattern).format(time);
        return timePattern;
    }

    // размер буфера в зависимости от размера файла
    private int getBufferSize (int fileSize) {

        if (fileSize >= 500_000_000) {

            return 67_108_864;
        }

        if (fileSize >= 40_000_000) {

            return 16_777_216;
        }

        if (fileSize >= 10_000_000) {

            return 8_388_608;
        }

        // 0 - 10_000_000
        return fileSize;
    }

}

