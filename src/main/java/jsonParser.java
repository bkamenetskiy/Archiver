import Pojo.Config;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class jsonParser {

    // парсинг json
    public void parsing () throws IOException {

        File configFile = new File("src/main/resources/config.json");                                          // путь к конфигу
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);                                // игнорирование неизвестных параметров вкл.
        Config configJSON = mapper.readValue(configFile, Config.class);                                                 // парсинг json

    }

}
