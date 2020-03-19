package pl.itger.delayedExec;

import com.github.javafaker.Faker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;



public class StreamsTests {
    private final static Logger LOGGER = Logger.getLogger(StreamsTests.class.getName());

static int i;
    public static void main(String[] args) {
        LOGGER.setLevel(Level.INFO);
        short s = 28;
                //float f = 2.3;
                double d = 2.3;
                int I = '1';
        StreamsTests xxx = new StreamsTests();
        try {
//            xxx.test1();
//            xxx.test2();
//            xxx.test1_parallel();
//            xxx.test2_parallell();
            //xxx.test3();
            //xxx.test4();
            xxx.test5();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPrime(int number) {
        if (number <= 1) return false;
        return IntStream.rangeClosed(2, number / 2).noneMatch(i -> isEven(number));
    }

    private static boolean isEven(Integer n) {
        return n % 2 == 0;
    }

    private void test1() throws FileNotFoundException {
        File file = new File(
                getClass().getClassLoader().getResource("duzyPlik.txt").getFile()
        );

        BufferedReader br = new BufferedReader(new FileReader(file));
        long startTime = System.nanoTime();
        String longestLine = br.lines().reduce((x, y) -> {
            if (x.length() > y.length()) return x;
            else return y;
        }).get();
        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
        LOGGER.info(longestLine);
    }

    private void test1_parallel() throws FileNotFoundException {
        File file = new File(
                getClass().getClassLoader().getResource("duzyPlik.txt").getFile()
        );

        BufferedReader br = new BufferedReader(new FileReader(file));
        long startTime = System.nanoTime();
        String longestLine = br.lines().parallel().reduce((x, y) -> {
            if (x.length() > y.length()) return x;
            else return y;
        }).get();
        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
        LOGGER.info(longestLine);
    }

    private void test2() throws FileNotFoundException {
        File file = new File(
                getClass().getClassLoader().getResource("duzyPlik.txt").getFile()
        );

        BufferedReader br = new BufferedReader(new FileReader(file));
        long startTime = System.nanoTime();
        String longestLine = br.lines().max(Comparator.comparingInt(String::length)).get();
        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
        LOGGER.info(longestLine);
    }

    private void test2_parallell() throws FileNotFoundException {
        File file = new File(
                getClass().getClassLoader().getResource("duzyPlik.txt").getFile()
        );

        BufferedReader br = new BufferedReader(new FileReader(file));
        long startTime = System.nanoTime();
        String longestLine = br.lines().parallel().max(Comparator.comparingInt(String::length)).get();
        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
        LOGGER.info(longestLine);
    }

    public void test3() {
        long startTime = System.nanoTime();
        long count = Stream.iterate(0, n -> n + 1)
                .limit(1_000_000)
                .parallel()   //with this 23s, without this 1m 10s
                .filter(StreamsTests::isPrime)
                //.peek(x -> System.out.format("%s\t", x))
                .count();
        LOGGER.info("Execution time in nanoseconds  : " + (System.nanoTime() - startTime));
        System.out.println("\nTotal: " + count);

    }

    public void test4() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer result = 0;
        long startTime = System.nanoTime();
        for (int i = 0; i < numbers.size(); i++) {
            Integer n = numbers.get(i);
            if (n > 5 && isEven(n) && n < 9 && n * 2 > 15) {
                result = n;
                break;
            }
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println(result + " " + elapsedTime);

        startTime = System.nanoTime();
        result = numbers.stream()
                .filter(n -> n > 5)
                .filter(n -> isEven(n))
                .filter(n -> n < 9)
                .filter(n -> n * 2 > 15)
                .findFirst()
                .get();
        elapsedTime = System.nanoTime() - startTime;
        System.out.println(result + " " + elapsedTime / 1000000);

        startTime = System.nanoTime();
        result = numbers.parallelStream()
                .filter(n -> n > 5)
                .filter(n -> isEven(n))
                .filter(n -> n < 9)
                .filter(n -> n * 2 > 15)
                .findFirst()
                .get();
        elapsedTime = System.nanoTime() - startTime;
        System.out.println(result + " " + elapsedTime / 1000000);
    }

    public void test5() {
        long startTime = System.nanoTime();
        List<Employee> le = gen();
        LOGGER.info(String.valueOf(le.size()));
        //LOGGER.info("Execution time in nanoseconds  : " + ((System.nanoTime() - startTime) / 1000000));

        final int[] i2 = {0};
        startTime = System.nanoTime();
        le.forEach(k -> {
            if (Employee.Dep.Police.equals(k.getDepartment())
                    && Employee.Full_Part_Time.F.equals(k.getFull_Part())
                    && k.getSalary_Hourly()>10000
                    && k.getSalary_Hourly()<15000)
                i2[0]++;
        });
        LOGGER.info(i2[0] + "; 1) Execution time in nanoseconds  : " + ((System.nanoTime() - startTime) / 1000000));

        startTime = System.nanoTime();
        long police = le.stream()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.F.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000)
                .count();
        LOGGER.info(police + "; 2) Execution time in nanoseconds  : " + ((System.nanoTime() - startTime) / 1000000));

        startTime = System.nanoTime();
        police = le.stream().parallel()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.F.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000)
                .count();
        LOGGER.info(police + "; 3) Execution time in nanoseconds  : " + ((System.nanoTime() - startTime) / 1000000));
    }

    private List<Employee> gen() {
        Faker f = new Faker();
        List<Employee> le = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            Employee.Full_Part_Time fp = f.random().nextBoolean() ? Employee.Full_Part_Time.F : Employee.Full_Part_Time.P;
            Employee.Dep dep = Employee.Dep.values()[f.random().nextInt(0, 2)];
            le.add(new Employee(f.name().firstName(),
                    f.name().lastName(),
                    f.job().position(),
                    dep,
                    fp,
                    f.random().nextInt(0, 100000)));
        }
        return le;
    }


    private class thr implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                Thread.currentThread().isInterrupted();
            }
        }
    }

}

class Employee {
    String f_Name;
    String l_Name;
    String Job_Titles;
    Dep Department;
    Full_Part_Time Full_Part;
    Integer Salary_Hourly;

    public Employee(String f_Name, String l_Name, String job_Titles, Dep department, Full_Part_Time full_Part, Integer salary_Hourly) {
        this.f_Name = f_Name;
        this.l_Name = l_Name;
        Job_Titles = job_Titles;
        Department = department;
        Full_Part = full_Part;
        Salary_Hourly = salary_Hourly;
    }

    public String getF_Name() {
        return f_Name;
    }

    public String getL_Name() {
        return l_Name;
    }

    public String getJob_Titles() {
        return Job_Titles;
    }

    public Dep getDepartment() {
        return Department;
    }

    public Full_Part_Time getFull_Part() {
        return Full_Part;
    }

    public Integer getSalary_Hourly() {
        return Salary_Hourly;
    }


    public enum Full_Part_Time {
        F, P;
    }

    public static enum Dep {
        Police, Fire, law;
    }

}
