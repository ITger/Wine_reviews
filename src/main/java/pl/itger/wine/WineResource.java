package pl.itger.wine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import org.springframework.web.bind.annotation.*;

/**
 * http://localhost:8081/v2/api-docs
 * http://localhost:8081/swagger-ui.html
 */
@RestController
@RequestMapping("/api/WineGlass")
public class WineResource {
    private final static Logger LOGGER = Logger.getLogger(WineResource.class.getName());
    private static Optional<ArrayList<LinkedTreeMap<String, ?>>> optionalLinkedTreeMaps = Optional.empty();
    private static ArrayList<LinkedTreeMap<String, ?>> list = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        LOGGER.setLevel(Level.ALL);
        LOGGER.info("init() START");
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, ?>>>() {
        }.getType();
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement = jsonObject;
        int i = 0;
        LOGGER.info("*** 1");
        Resource resource = new ClassPathResource("winemag-data-130k-v2.json");
        final WeakReference<Resource> ref = new WeakReference<>(resource);
        InputStream inputStream = ref.get().getInputStream();
        resource = null;
        while (ref.get() != null) {
            System.gc();
        }
        LOGGER.info("*** 2");
        try {
            Reader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            final WeakReference<Reader> readerWeakReference = new WeakReference<>(bufferedReader);
            try {
                optionalLinkedTreeMaps = Optional.ofNullable(gson.fromJson(readerWeakReference.get(), listType));
            } finally {
                bufferedReader.close();
                bufferedReader = null;
                while (readerWeakReference.get() != null) {
                    System.gc();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("***  optionalLinkedTreeMaps.isEmpty(): " + optionalLinkedTreeMaps.isEmpty());
        LOGGER.info("init() END");
    }

    @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> count() {
        Stream<LinkedTreeMap<String, ?>> stream;
        stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        Optional<Long> count = Optional.ofNullable(stream.count());
        return ResponseEntity.of(count);
    }

    @GetMapping(path = "/available_fields", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> available_fields() {
        /**
         * returns fields name set
         * use of optional
         * use of parallel stream
         */
        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        Optional<Set<String>> stringSet = Optional.ofNullable(stream
                .limit(1)
                .map(e -> e.keySet())
                .sorted()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet()));
        LOGGER.config("available_fields: " + stringSet.toString());
        return ResponseEntity.of(stringSet);
    }

    /**
     * Sample request:
     * {
     * "country": "Spain",
     * "description":"horseradish",
     * "variety": "Tempranillo"
     * }
     *
     * @return List<LinkedTreeMap < String, ?>>
     */
//    @ResponseStatus(value = HttpStatus.OK)
//    @GetMapping(path = "/wineSelection", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<List<LinkedTreeMap<String, ?>>> wineSelection(
//            @RequestBody Map<String, String> stringMapQueryPredicates) {
//        LOGGER.config("wineSelection predicates: ".concat(stringMapQueryPredicates.toString()));
//        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
//        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
//        stringMapQueryPredicates.forEach((k, v) -> {
//            predicateList.add(x -> {
//                return Objects.nonNull(x.get(k));
//            });
//            predicateList.add(x -> {
//                return x.get(k).toString().contains(v);
//            });
//        });
//        Predicate<LinkedTreeMap<String, ?>> compositePredicate = predicateList.get(0);
//        for (int i = 1; i < predicateList.size(); i++) {
//            compositePredicate = compositePredicate.and(predicateList.get(i));
//        }
//        Optional<List<LinkedTreeMap<String, ?>>> linkedTreeMaps = Optional.ofNullable(stream
//                .filter(compositePredicate)
//                .limit(20)
//                .collect(Collectors.toList()));
//        return ResponseEntity.of(linkedTreeMaps);
//    }
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PostMapping(path = "/wineSelection", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LinkedTreeMap<String, ?>>> wineSelection(
            @RequestBody final WineRequestData wineRequestData) {
        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
        wineRequestData.getWineSelection().forEach((String k, Set<String> v) -> {
            predicateList.add(x -> Objects.nonNull(x.get(k)));
            v.forEach(y -> {
                predicateList.add(xx -> xx.get(k).toString().contains(y));
            });
        });
        Predicate<LinkedTreeMap<String, ?>> compositePredicate = predicateList.get(0);
        for (int i = 1; i < predicateList.size(); i++) {
            compositePredicate = compositePredicate.and(predicateList.get(i));
        }
        Optional<List<LinkedTreeMap<String, ?>>> linkedTreeMaps = Optional.ofNullable(stream
                .filter(compositePredicate)
                .limit(20)
                .collect(Collectors.toList()));
        return ResponseEntity.of(linkedTreeMaps);
    }
}

@Data
class WineRequestData implements Serializable {
    private static final long serialVersionUID = -5064574995574971723L;
    Map<String, Set<String>> wineSelection = new HashMap<>();
}