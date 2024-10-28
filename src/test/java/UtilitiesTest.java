import ar.edu.uade.Utilities;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UtilitiesTest {

    @Test
    public void testConvertArray() {
        List<String> input = new ArrayList<>(
                Arrays.asList("Hola mundo", "{\"mensaje\":\"Hola mundo\"}")
        );

        String output = "";

        try {
            output = Utilities.convertArray(input);
        } catch (Exception e) {
            System.out.println("Ocurrió un error.");
        }

        String target = "Hola mundo--!--##-->>DELIMITER<<--##--!--{\"mensaje\":\"Hola mundo\"}";

        assertEquals(target, output);
    }

    @Test
    public void testConvertString() {
        String input = "Hola mundo--!--##-->>DELIMITER<<--##--!--{\"mensaje\":\"Hola mundo\"}";

        List<String> output = new ArrayList<>();

        try {
            output = Utilities.convertString(input);
        } catch (Exception e) {
            System.out.println("Ocurrió un error.");
        }

        List<String> target = new ArrayList<>(
                Arrays.asList("Hola mundo", "{\"mensaje\":\"Hola mundo\"}")
        );

        assertEquals(target, output);
    }
}
