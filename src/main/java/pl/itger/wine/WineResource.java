package pl.itger.wine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import org.springframework.context.annotation.Description;
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


/**
 * http://localhost:8080/v2/api-docs
 * http://localhost:8080/swagger-ui.html
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
     * @param stringMapQueryPredicates
     * @return List<LinkedTreeMap < String, ?>>
     */
    @GetMapping(path = "/wineSelection", produces = MediaType.APPLICATION_JSON_VALUE)
    @Description(value = "Sample request:\n" +
            "      {\n" +
            "      \"country\": \"Spain\",\n" +
            "      \"description\":\"horseradish\",\n" +
            "      \"variety\": \"Tempranillo\"\n" +
            "      }")
    public ResponseEntity<List<LinkedTreeMap<String, ?>>> wineSelection(
            @RequestParam Map<String, String> stringMapQueryPredicates) {
        LOGGER.config("wineSelection predicates: ".concat(stringMapQueryPredicates.toString()));
        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
        stringMapQueryPredicates.forEach((k, v) -> {
            predicateList.add(x -> {
                return Objects.nonNull(x.get(k));
            });
            predicateList.add(x -> {
                return x.get(k).toString().contains(v);
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

//    @ResponseStatus(value = HttpStatus.OK)
//    @GetMapping(path = "/wineSelection_XXX", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "xxx")})
//    public WineRequestData wineSelection_XXX() {
//        WineRequestData a = new WineRequestData();
//        Map<String, Set<String>> m = new HashMap<>();
//        m.put("XXX", Stream.of("aaa", "bbb", "ccc").collect(Collectors.toCollection(HashSet::new)));
//        m.put("ZZZ", Stream.of("xxx", "yyy", "zzz").collect(Collectors.toCollection(HashSet::new)));
//        a.setQry(m);
//        return a;
//    }

    @ResponseStatus(value = HttpStatus.OK)
    @Description(value = "curl --location --request GET 'http://localhost:8080/api/WineGlass/wineSelection_2' --header 'Content-Type: application/json' --data-raw '{\n" +
            "    \"qry\": {\n" +
            "        \"country\": [\n" +
            "            \"Spain\"\n" +
            "        ],\n" +
            "        \"description\": [\n" +
            "            \"berry\",\n" +
            "            \"gummy\",\n" +
            "            \"chocolaty\"\n" +
            "        ],\n" +
            "        \"variety\": [\n" +
            "        \t\"Garnacha\"\n" +
            "        ]\n" +
            "    }\n" +
            "}'")
    @GetMapping(path = "/wineSelection_2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LinkedTreeMap<String, ?>>> wineSelection_2(
            @RequestBody final WineRequestData wineRequestData) {
        LOGGER.config(()-> "wineSelection predicates: ".concat(wineRequestData.toString()));
        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
        wineRequestData.getQry().forEach((String k, Set<String> v) -> {
            predicateList.add(x -> {
                return Objects.nonNull(x.get(k));
            });
            v.forEach(y -> {
                predicateList.add(xx -> {
                    return xx.get(k).toString().contains(y.toString());
                });
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
    Map<String, Set<String>> qry = new HashMap<>();
}