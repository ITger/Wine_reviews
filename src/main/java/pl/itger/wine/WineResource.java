package pl.itger.wine;

import com.google.gson.internal.LinkedTreeMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * http://localhost:8081/v3/api-docs
 * http://localhost:8081/swagger-ui.html
 */
@RestController
@RequestMapping("/api/WineGlass")
@Tag(name = "WineGlass",
     description = "the Wine Selection API")
public class WineResource {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(WineData.class);

    @Autowired
    private WineData wineData;

    private Set<String> propertiesSet;

    @Cacheable(value = "wineItemsCount")
    @Operation(summary = "Returns total count of wine items",
               tags = {"WineGlass"})
    @GetMapping(path = "/count",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count() {
        final Integer[] count = new Integer[1];
        //wineData.getWineData().ifPresentOrElse(linkedTreeMaps -> count[0] = linkedTreeMaps.size(), () -> count[0] = 0);
        int s = wineData.getWineData().size();
        //logger.info("total count of wine items: " + count[0]);
        logger.info("total count of wine items: " + s);
        return ResponseEntity.of(Optional.of(s));
    }

    @Cacheable(value = "wine_properties")
    @Operation(summary = "Returns the available properties name set",
               description = "Properties name set",
               tags = {"WineGlass"})
    @GetMapping(path = "/available_properties",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> available_properties() {
        logger.info("available_properties call");
        Set<String> keySet = this.wineData.getWineData().stream().limit(1)
                .map(LinkedTreeMap::keySet)
                .sorted()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
        return ResponseEntity.of(Optional.of(keySet));
    }

    /**
     * Sample request (where 'description and 'country' are properties from 'available_properties' service):
     * { "wineSelection": {
     * "description": [ "blackberry finish", "chocolate", "tannic" ],
     * "country": [ "Argentina" ]
     * } }
     *
     * @return List<LinkedTreeMap < String, ?>>
     */
    @Cacheable(value = "wine_Selection")
    @Operation(summary = "Returns wine selection list based on criteria",
               description = "wine selection list",
               tags = {"WineGlass"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PostMapping(path = "/wineSelection",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LinkedTreeMap<String, ?>>> wineSelection(
            @Parameter(required = true,
                       description = "Sample json query:  { \"wineSelection\": { \"description\": [\"blackberry finish\", \"chocolate\", \"tannic\"], \"country\": [\"Argentina\"] } }")
            @Valid @RequestBody() final WineRequestData wineRequestData) {
        logger.info("Predicate: " + wineRequestData.getWineSelection().toString());
        if (null == propertiesSet) {
            available_properties();
        }
        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
        BiConsumer<String, Set<String>> stringSetBiConsumer = (String k, Set<String> v) -> {
            boolean add = predicateList.add(x -> Objects.nonNull(x.get(k)));
            v.forEach(y -> predicateList.add(xx -> xx.get(k).toString().contains(y)));
        };

        wineRequestData.getWineSelection().forEach(stringSetBiConsumer);
        Predicate<LinkedTreeMap<String, ?>> compositePredicate = predicateList.get(0);
        for (int i = 1; i < predicateList.size(); i++) {
            compositePredicate = compositePredicate.and(predicateList.get(i));
        }

        Stream<LinkedTreeMap<String, Object>> stream = wineData.getWineData().stream();//.orElseGet(Stream::empty);
        Optional<List<LinkedTreeMap<String, ?>>> linkedTreeMaps = Optional.ofNullable(stream
                .filter(compositePredicate)
                .limit(100)
                .collect(Collectors.toList()));
        return ResponseEntity.of(linkedTreeMaps);
    }
}

@Data
class WineRequestData implements Serializable {
    private static final long serialVersionUID = -5064574995574971723L;
    Map<String, Set<String>> wineSelection = new HashMap<>();
}