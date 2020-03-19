package pl.itger.wine;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, ?>>>() {
        }.getType();
        //String fName = "/winemag-data-130k-v2-";
        File file = null;
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement = jsonObject;
        int i = 0;
        LOGGER.info("*** 1");
        //Resource resource = new ClassPathResource("winemag-data-130k-v2.json");
        Resource resource = new ClassPathResource("winemag-data-130k-v2.json");
        final WeakReference<Resource> ref = new WeakReference<>(resource);
        InputStream inputStream = ref.get().getInputStream();
        resource = null;
        while (ref.get() != null) {
            System.gc();
        }
        LOGGER.info("*** 2");
        //InputStream inputStream = resource.getInputStream();
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
//            jsonElement = JsonParser.parseReader(in);
//        } catch (MalformedJsonException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        try {
            Reader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            final WeakReference<Reader> readerWeakReference = new WeakReference<>(bufferedReader);
            try {
                //list = gson.fromJson(bufferedReader, listType);
                //list = gson.fromJson(readerWeakReference.get(), listType);
                //jsonElement = JsonParser.parseReader(in);
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
//        int ii =0;
//        try(JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
//            LOGGER.info("*** 4");
//            jsonReader.beginArray();
//            LOGGER.info("*** 5");
//            while (jsonReader.hasNext()){
//                LOGGER.info(String.valueOf(ii));
//                gson.fromJson(jsonReader, listType);
//                //list.add( gson.fromJson(jsonReader, listType));
//                //do something real
//            }
//            LOGGER.info("*** 6");
//        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //optionalLinkedTreeMaps = Optional.ofNullable(gson.fromJson(jsonElement, listType));
        //LOGGER.info("*** list.size() " + list.size());
        LOGGER.info("init() END");
    }

    @GetMapping("/count")
    public Long count() {
        Stream<LinkedTreeMap<String, ?>> stream;
        stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        //stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        long count = stream.count();
        LOGGER.config("count: " + count);
        return count;
    }

    @GetMapping("/available_fields")
    public Set<String> available_fields() {
        /**
         * returns fields name set
         * use of optional
         * use of parallel stream
         */
        Stream<LinkedTreeMap<String, ?>> stream = optionalLinkedTreeMaps.map(Collection::parallelStream).orElseGet(Stream::empty);
        Set<String> stringSet = stream
                .limit(1)
                .map(e -> e.keySet())
                .sorted()
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
        LOGGER.config("available_fields: " + stringSet.toString());
        return stringSet;
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
     * @return  List<LinkedTreeMap<String, ?>>
     */
    @GetMapping("/wineSelection")
    public List<LinkedTreeMap<String, ?>> wineSelection(@RequestParam Map<String, String> stringMapQueryPredicates) {
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
        List<LinkedTreeMap<String, ?>> linkedTreeMaps = stream
                .filter(compositePredicate)
                .collect(Collectors.toList());
        return linkedTreeMaps;
    }
}

