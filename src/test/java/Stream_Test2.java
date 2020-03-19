import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pl.itger.delayedExec.StreamsTests;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Stream_Test2 {
    private final static Logger LOGGER = Logger.getLogger(StreamsTests.class.getName());

    @Test
    void test_1() {
        LOGGER.setLevel(Level.SEVERE);
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9);
        Stream<Integer> list_2 = Stream.iterate(1, i -> i*2);
        LOGGER.info(()-> getMsg(list));
        LOGGER.info(getMsg(list));
    }

    <T> String getMsg(T t) {
        return String.valueOf(t);
    }
}
