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

    protected void toArchive (String fileName, String filter, String sourcePath, String resultPath, int setLevel, String newDir) throws IOException {

        // хранилище путей к архивируемым файлам
        ArrayList<Path> filePaths = new ArrayList();

        Path inputPath = Paths.get(sourcePath);
        Path outputPath = Paths.get(resultPath);

        // название папки с архивом
        // String newDir = "2021-10-19 HH-mm";
        //String newDir = getTime();

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

        // что ты такое, Ansys.Products.2021.R2.Win64-SSQ, папка или файл?

        for (int i = 0; i <= filePaths.size() - 1; i++) {

            // если не файл, то удалить из списка
            if (!Files.isRegularFile(filePaths.get(i))) {

                filePaths.remove(i);
                i --;
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

                // считываем файл в массив byte
                byte[] buffer = new byte[getBufferSize(fis.available())];

                //System.out.println(getBufferSize(fis.available()));
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

