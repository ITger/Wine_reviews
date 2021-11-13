package pl.itger.wine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
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
@EnableCaching
public class WineData {
    private final static Logger logger = LoggerFactory.getLogger(WineData.class);
    private ArrayList<LinkedTreeMap<String, Object>> wineData;
    private Set<String> propertiesSet;

    public Set<String> getPropertiesSet() {
        return propertiesSet;
    }

    @PostConstruct
    public void init() throws IOException {
        logger.info("init() START");
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, Object>>>() {
        }.getType();
        Resource resource = new ClassPathResource("wine_data/winemag-data-130k-v2.json");
        this.wineData = gson.fromJson(new FileReader(resource.getFile()), listType);
        logger.info("***  optionalLinkedTreeMaps.isEmpty(): " + wineData.isEmpty());
        logger.info("init() END");
    }
}
