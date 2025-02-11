import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainClassTest {

    int actual = new MainClass().getLocalNumber();
    int expected = 14;
    int secondActual = new MainClass().getClassNumber();
    int secondExpected = 45;

    @Test
    public void testGetLocalNumber() {
        assertEquals(actual, expected, "Число " + actual + " не равно числу " + expected);
    }

    @Test
    public void testGetClassNumber() {
        assertTrue(secondActual > secondExpected, "Число " + secondActual + " не больше числа " + secondExpected);
    }
}
