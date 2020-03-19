import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.itger.delayedExec.StreamsTests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test1 {

    private final static Logger LOGGER = Logger.getLogger(Test1.class.getName());

//    @Test
//    public void testAllToUpperCase() throws FileNotFoundException {
//        File file = new File(
//                getClass().getClassLoader().getResource("duzyPlik.txt").getFile()
//        );
//
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        long startTime = System.nanoTime();
//        String longestLine = br.lines().reduce((x, y) -> {
//            if (x.length() > y.length()) return x;
//            else return y;
//        }).get();
//        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
//        LOGGER.info(longestLine);
//        assertEquals(1, 1);
//    }

    @BeforeEach
    void setUp() {

    }
}
