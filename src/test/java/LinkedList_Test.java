import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkedList_Test {

    @Test
    void test_1() {
        List li = new LinkedList();
        li.addAll(Stream.iterate(1, i->i*2).limit(11).collect(Collectors.toList()));
        System.out.println(li.size());
        System.out.println(li.size()/2);
        System.out.println(li.get(5));
    }

    @Test
    void test_2() {
        List li = new ArrayList();
        li.addAll(Stream.iterate(1, i->i*2).limit(11).collect(Collectors.toList()));
        System.out.println(li.size());
    }
}
