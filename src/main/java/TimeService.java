import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeService  {

    public String getTime () throws IOException {

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
        String pattern = "yyyy-MM-dd HH-mm-ss";
        String timePattern = new SimpleDateFormat(pattern).format(time);

        return timePattern;
    }

}
