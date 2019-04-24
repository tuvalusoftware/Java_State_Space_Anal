package Solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static Solver.Converter.print;
import static Solver.Converter.toInfix;
import static org.junit.Assert.assertEquals;

public class ConverterComplementTest {
    Interpreter ip = new Interpreter();


    @Before
    public void setUp() {
        Converter.init();
    }

    @Test
    public void complement01() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();
        Set<String> system3 = new HashSet<>();

        String[] s1 = {"x 1 <=","x 4 <="};
        String[] s2 = {"x 5 =="};
        String[] s3 = {"x 20 >=","x 25 >="};

        system1.addAll(Arrays.asList(s1));
        system2.addAll(Arrays.asList(s2));
        system3.addAll(Arrays.asList(s3));

        List<Set<String>> systems = new ArrayList<>();
        systems.add(system1);
        systems.add(system2);
        systems.add(system3);

        Map<String,String> vars = new HashMap<>();
        vars.put("x","6");
        vars.put("y","10");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));

        assertEquals(true,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement02() {
        Set<String> system1 = new HashSet<>();

        String[] s1 = {"x 1 >="};

        system1.addAll(Arrays.asList(s1));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);

        Map<String,String> vars = new HashMap<>();
        vars.put("x","6");
        vars.put("y","10");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));

        assertEquals(false,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement03() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();

        String[] s1 = {"x 1 >="};
        String[] s2 = {"x 5 <="};

        system1.addAll(Arrays.asList(s1));
        system2.addAll(Arrays.asList(s2));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);
        systems.add(system2);


        Map<String,String> vars = new HashMap<>();
        vars.put("x","6");
        vars.put("y","10");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));
        assertEquals(false,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement04() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();

        String[] s1 = {"x 1 >="};
        String[] s2 = {"x 5 <=","x 5 =="};

        system1.addAll(Arrays.asList(s1));
        system2.addAll(Arrays.asList(s2));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);
        systems.add(system2);


        Map<String,String> vars = new HashMap<>();
        vars.put("x","6");
        vars.put("y","10");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));
        assertEquals(false,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement05() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();

        String[] s1 = {"x 1 >=", "x 5 <="};

        system1.addAll(Arrays.asList(s1));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);


        Map<String,String> vars = new HashMap<>();
        vars.put("x","3");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));
        assertEquals(false,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement06() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();

        String[] s1 = {"x 1 >=", "x 5 <=", "x 4 !="};

        system1.addAll(Arrays.asList(s1));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);


        Map<String,String> vars = new HashMap<>();
        vars.put("x","4");

        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));
        assertEquals(true,ip.interpretFromString(complement,vars).getBoolean());
    }

    @Test
    public void complement07() {
        Set<String> system1 = new HashSet<>();
        Set<String> system2 = new HashSet<>();
        Set<String> system3 = new HashSet<>();


        String[] s1 = {"x 1 >=", "x 5 <=", "x 4 !="};
        String[] s2 = {"y 1 <="};
        String[] s3 = {"y 5 >="};

        system1.addAll(Arrays.asList(s1));
        system2.addAll(Arrays.asList(s2));
        system3.addAll(Arrays.asList(s3));

        List<Set<String>> systems = new ArrayList<>();

        systems.add(system1);
        systems.add(system2);
        systems.add(system3);

        Map<String,String> vars = new HashMap<>();
        vars.put("x","4");
        vars.put("y","3");


        String complement = Converter.getComplementaryMultipleSystems(systems);
        print(complement);
        print(toInfix(complement));
        assertEquals(true,ip.interpretFromString(complement,vars).getBoolean());
    }

}
