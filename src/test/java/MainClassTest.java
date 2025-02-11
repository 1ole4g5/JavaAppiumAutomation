import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainClassTest {

    int actual = new MainClass().getLocalNumber();
    int expected = 14;

    @Test
    public void testGetLocalNumber() {
        assertEquals(actual, expected, "Число " + actual + " не равно числу " + expected);
    }
}
