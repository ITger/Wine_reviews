import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import pl.itger.delayedExec.StreamsTests;

import java.io.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Stream_Json {
    //private static  JSONObject jsonObject;
    private final static Logger LOGGER = Logger.getLogger(StreamsTests.class.getName());
    static Optional<ArrayList<LinkedTreeMap<String, ?>>> optionalLinkedTreeMaps = Optional.empty();
    private static ArrayList<LinkedTreeMap<String, ?>> list = new ArrayList<>();

    //private static ArrayList<Map<String, ?>> list_2 = null;

    {
        LOGGER.setLevel(Level.WARNING);
    }

    @Autowired
    ResourceLoader resourceLoader;

    @BeforeAll
    static void setUp() throws IOException {
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, ?>>>() {
        }.getType();
        JsonElement jsonElement = null;
        Gson gson = new GsonBuilder().create();
        Resource resource = new ClassPathResource("winemag-data-130k-v2.json");
        final WeakReference<Resource> resourceWeakReference = new WeakReference<>(resource);
        InputStream inputStream = resourceWeakReference.get().getInputStream();
        resource = null;
        while (resourceWeakReference.get() != null) System.gc();
        try {
            Reader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            final WeakReference<Reader> readerWeakReference = new WeakReference<>(bufferedReader);
            try {
                //list = gson.fromJson(bufferedReader, listType);
                list = gson.fromJson(readerWeakReference.get(), listType);
                //optionalLinkedTreeMaps = Optional.ofNullable(gson.fromJson(in, listType));
            } finally {
                bufferedReader.close();
                bufferedReader = null;
                while (readerWeakReference.get() != null) System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //LOGGER.info("*** list: " + list);
//        int ii =0;
//        try(JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"))) {
//            LOGGER.info("*** 4");
//            jsonReader.beginArray();
//            LOGGER.info("*** 5");
//            while (jsonReader.hasNext()){
//                LOGGER.info(String.valueOf(ii));
//                //gson.fromJson(jsonReader, listType);
//                list.add( gson.fromJson(jsonReader, listType));
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

//        Object object = new Object();
//        final WeakReference<Object> ref = new WeakReference<>(object);
//        object = null;
//        while (ref.get() != null) {
//            System.gc();
//        }

//        try(BufferedReader in = new BufferedReader(new FileReader(file))){
//            jsonElement = JsonParser.parseReader(in);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Type type = new TypeToken<ArrayList<Map<String, ?>>>() {}.getType();
        //list = gson.fromJson(jsonElement, listType);
        //list_2 = gson.fromJson(jsonElement, type);
        //optionalLinkedTreeMaps = Optional.ofNullable(gson.fromJson(jsonElement, listType));
    }

    private static void accept(LinkedTreeMap<String, ?> e) {
        LOGGER.info(() -> getMsg("Filtered value: " + e));
    }

    private static <T> String getMsg(T t) {
        return String.valueOf(t);
    }

    @Test
    void test_0() {
        Stream<LinkedTreeMap<String, ?>> stream = list.stream();
        long count = stream.count();
        assert count == 129971;
    }

    @RepeatedTest(5)
    void test_1() {
        Stream<LinkedTreeMap<String, ?>> stream = list.stream();
//        .filter((Map<String, Object> m) ->
//                ((Long) m.get("ct")) != 1L)

        /**
         * .filter((Map<String, Object> s) -> {
         *                  return (Long) s.get("versions") > 1;
         *             })
         */
//        stream.filter((LinkedTreeMap<String, String> m) -> {
//                    return ((String) m.get("points")).equals("87");
//                }
//        )
        Stream stream1 = stream
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                //.peek(Stream_Json::accept)
                //.peek( e -> LOGGER.info(()->getMsg("Filtered value: " + e)))
                .filter(variety -> variety.get("variety").equals("White Blend"))
                //.peek(Stream_Json::accept)
                ;

        Long count = stream1.count();
        assert count == 2360;
    }

    @RepeatedTest(5)
    void test_1_parallel() {
        Stream<LinkedTreeMap<String, ?>> stream = list.parallelStream();
//        .filter((Map<String, Object> m) ->
//                ((Long) m.get("ct")) != 1L)

        /**
         * .filter((Map<String, Object> s) -> {
         *                  return (Long) s.get("versions") > 1;
         *             })
         */
//        stream.filter((LinkedTreeMap<String, String> m) -> {
//                    return ((String) m.get("points")).equals("87");
//                }
//        )
        Stream stream1 = stream
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                //.peek(Stream_Json::accept)
                .filter(variety -> variety.get("variety").equals("White Blend"))
                //.peek(Stream_Json::accept)
                ;

        Long count = stream1.count();
        assert count == 2360;
    }

    @RepeatedTest(5)
    void test_2() {
        LOGGER.setLevel(Level.INFO);
        Stream<LinkedTreeMap<String, ?>> stream = list.stream();
//        .filter((Map<String, Object> m) ->
//                ((Long) m.get("ct")) != 1L)

        /**
         * .filter((Map<String, Object> s) -> {
         *                  return (Long) s.get("versions") > 1;
         *             })
         */
//        stream.filter((LinkedTreeMap<String, String> m) -> {
//                    return ((String) m.get("points")).equals("87");
//                }
//        )
        Stream<LinkedTreeMap<String, ?>> stream1 = stream.filter(points -> points.get("points").equals("87"))
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .filter(variety -> variety.get("variety").equals("White Blend"))
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)));
        Long count = stream1.count();
        assert count == 383;
    }

    @RepeatedTest(5)
    void test_2_parallel() {
        LOGGER.setLevel(Level.INFO);
        Stream<LinkedTreeMap<String, ?>> stream1 = list
                .parallelStream()
                .filter(points -> points.get("points").equals("87"))
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .filter(variety -> variety.get("variety").equals("White Blend"))
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)));
        Long count = stream1.count();
        assert count == 383;
    }

    @RepeatedTest(5)
    void test_4() {
        /**
         * returns fields name array
         * parallel stream
         */
        LOGGER.setLevel(Level.INFO);
        Stream<LinkedTreeMap<String, ?>> stream = list.parallelStream();

        Set<String> strings = stream
                .limit(1)
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .map(e -> e.keySet())
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        LOGGER.info(strings.toString());
    }

    @RepeatedTest(5)
    void test_5() {
        /**
         * returns fields name array
         * use of optional
         * use of parallel stream
         */
        LOGGER.setLevel(Level.INFO);
        Stream<LinkedTreeMap<String, ?>> stream;
        if (optionalLinkedTreeMaps.isPresent()) {
            stream = optionalLinkedTreeMaps.get().parallelStream();
        } else {
            stream = Stream.empty();
        }

        Set<String> stringSet = stream
                //Map<String, String> stringStringMap = stream
                .limit(1)
                .map(e -> e.keySet())
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
        //.collect(Collectors.toUnmodifiableMap(e-> e.toString(),e->e.toString()));
        LOGGER.info(stringSet.toString());
    }

    @RepeatedTest(5)
    void test_6() {
        LOGGER.setLevel(Level.ALL);
        List<Predicate<String>> allPredicates = new ArrayList<Predicate<String>>();
        allPredicates.add(str -> str.startsWith("A"));
        List<String> stream1 = list
                .parallelStream()
                .filter(v -> Objects.nonNull(v.get("country")))
                .filter(v -> v.get("country").toString().equalsIgnoreCase("Spain"))
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                .map(v -> v.get("variety").toString())
                //.peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .distinct()
                .collect(Collectors.toList());
        int count = stream1.size();
        assert count == 125;
    }

    @RepeatedTest(5)
    void test_7() {
        LOGGER.setLevel(Level.CONFIG);
        List<Predicate<String>> allPredicates = new ArrayList<Predicate<String>>();
        allPredicates.add(str -> str.startsWith("A"));
        List<String> stream1 = list
                .stream()
                .filter(v -> Objects.nonNull(v.get("country")))
                .filter(v -> v.get("country").toString().equalsIgnoreCase("Spain"))
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                .map(v -> v.get("variety").toString())
                .distinct()
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .collect(Collectors.toList());
        int count = stream1.size();
        assert count == 125;
    }

    @RepeatedTest(5)
    void test_8() {
        LOGGER.setLevel(Level.WARNING);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("country", "Spain");
        stringMap.put("variety", "Tempranillo");
        List<Predicate<Map<String, String>>> predicateList = new ArrayList<>();
        //predicateList.add(stringStringMap -> {stringStringMap.get()});
        //predicateList.add(stringStringMap -> stringStringMap.containsKey("country"), stringStringMap.containsValue("Spain")));
        //allPredicates.add( str -> str.startsWith("A"));
        List<String> stream1 = list
                .parallelStream()
                .filter(v -> Objects.nonNull(v.get("country")))
                .filter(v -> v.get("country").toString().equalsIgnoreCase("Spain"))
                .filter(variety -> Objects.nonNull(variety.get("variety")))
                .map(v -> v.get("variety").toString())
                .distinct()
                .sorted()
                .peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                .collect(Collectors.toList());
        int count = stream1.size();
        //LOGGER.info(() -> getMsg("Filtered value: " + stream1.toString()));
        assert count == 125;
    }

    @RepeatedTest(5)
    void test_9() {
        LOGGER.setLevel(Level.ALL);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("country", "Spain");
        stringMap.put("variety", "Tempranillo");
        List<Predicate<LinkedTreeMap<String, ?>>> predicateList = new ArrayList<>();
        //List< Predicate<ArrayList<TypeToken<Map<String, ?>>>>> predicateList_2 = new ArrayList<>();
        stringMap.forEach((k, v) -> {
            predicateList.add(x -> {
                return Objects.nonNull(x.get(k));
            });
            predicateList.add(x -> {
                return x.get(k).toString().contains(v);
            });
        });
        Predicate<LinkedTreeMap<String, ?>> compositePredicate = predicateList.get(0);
        //Predicate<ArrayList<TypeToken<Map<String, ?>>>> arrayListPredicate = predicateList_2.get(0);
        for (int i = 1; i < predicateList.size(); i++) {
            compositePredicate = compositePredicate.and(predicateList.get(i));
        }
        //compositePredicate = predicateList.stream().reduce(w -> true, Predicate::and);
        List<LinkedTreeMap<String, ?>> strings = list
                .parallelStream()
                .filter(compositePredicate)
                .peek(System.out::println)
                //.distinct()
                //.sorted()
                //.peek(e -> LOGGER.config(() -> getMsg("Filtered value: " + e)))
                //.map(v -> v.get("variety").toString())
                .collect(Collectors.toList());
                //.flatMap(Set::stream)
                //.collect(Collectors.toSet());

        List<LinkedTreeMap<String, ?>> stream1 = list
                .parallelStream()
                .filter(v -> {
                    boolean country = Objects.nonNull(v.get("country"));
                    return country;
                })
                .filter(v -> {
                    boolean b = v.get("country").toString().equalsIgnoreCase("Spain");
                    return b;
                })
                //.filter(variety -> Objects.nonNull(variety.get("variety")))
                //.map(v -> v.get("variety").toString())
                //.distinct()
                //.sorted()
//                .forEach(x -> {
//                    System.out.println(x);
//                });
                .collect(Collectors.toList());
        int count = stream1.size();
        //LOGGER.info(() -> getMsg("Filtered value: " + stream1.toString()));
        assert count ==  6645;
    }
}
