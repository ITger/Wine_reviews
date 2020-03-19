import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class testy_Darka {
    @BeforeAll
    static void setUp() {


    }

    @Test
    void test_1() {
        Mammal pies = new Mammal(Mammal.Rodzaj.Dog);
        Mammal krowa = new Mammal(Mammal.Rodzaj.Cow);
        List<Mammal> mammals = new ArrayList<>();
        mammals.add(pies);
        mammals.add(krowa);
        mammals.add(pies);
        System.out.println(pies);
    }
}

class Mammal {
    public Rodzaj type;

    public Mammal(Rodzaj r) {
        type = r;
    }

    public enum Rodzaj {
        Cat, Dog, Cow;
    }
}
