package pl.itger.wine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

@Configuration
@Getter
public class WineData {
    private final static Logger logger = LoggerFactory.getLogger(WineData.class);
    private ArrayList<LinkedTreeMap<String, Object>> wineData;
    private Set<String> propertiesSet;

    @Value("${wine.data.file}")
    private String wineDataFile;


    public Set<String> getPropertiesSet() {
        return propertiesSet;
    }

    @PostConstruct
    public void init() throws IOException {
        logger.info("init() START");
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, Object>>>() {
        }.getType();
        Resource resource = new ClassPathResource(wineDataFile);
        this.wineData = gson.fromJson(new FileReader(resource.getFile()), listType);
        logger.info("***  optionalLinkedTreeMaps.isEmpty(): " + wineData.isEmpty());
        logger.info("init() END");
    }
}
