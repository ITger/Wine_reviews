import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import pl.itger.delayedExec.StreamsTests;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Stream_Test1 {
    private final static Logger LOGGER = Logger.getLogger(StreamsTests.class.getName());
    private static List<Employee> le = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        Faker f = new Faker();
        for (int i = 0; i < 100 * 1000; i++) {
            Employee.Full_Part_Time fp = f.random().nextBoolean() ? Employee.Full_Part_Time.Full_time : Employee.Full_Part_Time.Part_time;
            Employee.Dep dep = Employee.Dep.values()[f.random().nextInt(0, 2)];
            le.add(new Employee(f.name().firstName(),
                    f.name().lastName(),
                    f.job().position(),
                    dep,
                    fp,
                    f.random().nextInt(0, 100000)));
        }
    }

    @Test
    @Order(1)
    void test_1() {
        final int[] i2 = {0};
        le.forEach(k -> {
            if (Employee.Dep.Police.equals(k.getDepartment())
                    && Employee.Full_Part_Time.Full_time.equals(k.getFull_Part())
                    && k.getSalary_Hourly() > 10000
                    && k.getSalary_Hourly() < 15000)
                i2[0]++;
        });
        LOGGER.info(Integer.toString(i2[0]));
    }

    @Test
    @Order(2)
    void test_2() {
        long police = le.stream()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.Full_time.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000)
                .count();
        LOGGER.info(Long.toString(police));
    }

    @Test
    @Order(3)
    void test_2a() {
        Stream<Employee> se = le.stream()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.Full_time.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000);
        long police = se.count();
        LOGGER.info(Long.toString(police));
    }

    @Test
    @Order(4)
    void test_3() {
        long police = le.stream().parallel()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.Full_time.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000)
                .count();
        LOGGER.info(Long.toString(police));
    }

    @Test
    @Order(5)
    void test_3a() {
        Stream<Employee> se = le.stream().parallel()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.Full_time.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000);
        long police = se.count();
        LOGGER.info(Long.toString(police));
    }

    @Test
    @Order(6)
    void test_4() {
        Stream<Employee> se = le.stream().parallel()
                .filter(n -> Employee.Dep.Police.equals(n.getDepartment()))
                .filter(n -> Employee.Full_Part_Time.Full_time.equals(n.getFull_Part()))
                .filter(n -> n.getSalary_Hourly() > 10000)
                .filter(n -> n.getSalary_Hourly() < 15000);
        long police = se.collect(Collectors.counting());

        LOGGER.info(Long.toString(police));
    }
}

class Employee {
    String f_Name;
    String l_Name;
    String Job_Titles;
    Employee.Dep Department;
    Employee.Full_Part_Time Full_Part;
    Integer Salary_Hourly;

    public Employee(String f_Name, String l_Name, String job_Titles, Employee.Dep department, Employee.Full_Part_Time full_Part, Integer salary_Hourly) {
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

    public Employee.Dep getDepartment() {
        return Department;
    }

    public Employee.Full_Part_Time getFull_Part() {
        return Full_Part;
    }

    public Integer getSalary_Hourly() {
        return Salary_Hourly;
    }


    public enum Full_Part_Time {
        Full_time, Part_time;
    }

    public enum Dep {
        Police, Fire, Law;
    }

}
