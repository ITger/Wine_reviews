import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip_Test {

    private static final int BUFFER_SIZE = 4098;

    //@Test
    void name() throws IOException {
        File file = new File(
                Objects.requireNonNull(Stream_Json.class.getClassLoader().getResource("winemag-data-130k-v2.zip")).getFile()
        );
        InputStream fileInputStream = new FileInputStream(file);
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        int data = gzipInputStream.read();
        while (data != -1) {
            //do something with data
            data = gzipInputStream.read();
        }
    }

    //@Test
    public void zipToString() throws IOException {
        File file = new File(
                Objects.requireNonNull(Stream_Json.class.getClassLoader().getResource("winemag-data-130k-v2.zip")).getFile()
        );
        ZipInputStream zipIn = null;
        //String wine_data = null;
        InputStream is = null;
        try {
            zipIn = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = Objects.requireNonNull(zipIn).getNextEntry();
            if (entry != null && !entry.isDirectory()) {
                //wine_data = convertZipInputStreamToInputStream(zipIn);
                is = convertZipInputStreamToInputStream_2(zipIn);
            }
            zipIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Path tempFile =  Files.createTempDirectory("").resolve(UUID.randomUUID().toString() + ".json");
//        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()));
//        writer.write(wine_data);
//        writer.close();
        //assert  wine_data.length() == 145039360;
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        JsonElement jsonElement = null;
        Type listType = new TypeToken<ArrayList<LinkedTreeMap<String, ?>>>() {}.getType();

        //jsonElement = JsonParser.parseString(wine_data);
        //
        //byte[] encoded = Files.readAllBytes(Paths.get("<your path>"));
        //Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        //Map<String, Object> firstMap = new Gson().fromJson(new String(encoded, Charset.defaultCharset()), listType);
        //

        Reader targetReader = new InputStreamReader(is, Charset.forName("UTF-8"));
        is.close();
        //Reader targetReader = new InputStreamReader(is, Charset.defaultCharset());
        jsonElement = JsonParser.parseReader(targetReader);
        targetReader.close();

        Optional<ArrayList<LinkedTreeMap<String, ?>>> optionalLinkedTreeMaps = Optional.empty();
        optionalLinkedTreeMaps = Optional.ofNullable(gson.fromJson(jsonElement, listType));
        Stream<LinkedTreeMap<String, ?>> stream = gson.fromJson(jsonElement, listType);
        //
        assert stream.count() == 129971;
    }

    private String convertZipInputStreamToInputStream(ZipInputStream in) throws IOException {
        final int BUFFER = 4096;
        byte[] data = new byte[BUFFER];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (in.read(data, 0, BUFFER) != -1) {
            out.write(data);
        }
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (is, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }

    private InputStream convertZipInputStreamToInputStream_2(ZipInputStream in) throws IOException {
        final int BUFFER = 2048;
        int count = 0;
        byte data[] = new byte[BUFFER];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((count = in.read(data, 0, BUFFER)) != -1) {
            out.write(data);
        }
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        out.close();
        return is;
    }
}
