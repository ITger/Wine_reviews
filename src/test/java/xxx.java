import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class xxx {

    @Test
    void name() {
        //System.out.println(""+52!/44!*(52-44)!);
        //52! / (38! x 14!)
        //24
        //System.out.println(" " + factorial(52).divide(factorial(6)).multiply(factorial(52-6)));
        System.out.println(" " +
                factorial(52)
                .divide(factorial(52 - 24)
                        .multiply(factorial(24)))
        );
    }

    public BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++)
            result = result.multiply(BigInteger.valueOf(i));
        return result;
    }
}
