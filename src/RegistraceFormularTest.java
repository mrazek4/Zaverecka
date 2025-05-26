import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistraceFormularTest {
    // Regex z registrace
    private final String regexEmail = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private final String regexTelefon = "^(\\+420|00420)?\\s?\\d{3}\\s?\\d{3}\\s?\\d{3}$";
    private final String regexJmeno = "^[A-Z]?[a-z]+$";
    private final String regexPrijmeni = "^[A-Z]?[a-z]+$";

    @Test
    void testPlatneEmaily() {
        assertTrue("neco@domena.cz".matches(regexEmail));
        assertTrue("uzivatel.name@neco.net".matches(regexEmail));
        assertTrue("x@x.io".matches(regexEmail));
    }

    @Test
    void testNeplatneEmaily() {
        assertFalse("neco@".matches(regexEmail));
        assertFalse("neco.cz".matches(regexEmail));
        assertFalse("@neco.com".matches(regexEmail));
        assertFalse("".matches(regexEmail));
        assertFalse(" ".matches(regexEmail));
    }

    @Test
    void testPlatneTelefony() {
        assertTrue("123 456 789".matches(regexTelefon));
        assertTrue("+420 123 456 789".matches(regexTelefon));
        assertTrue("00420 123 456 789".matches(regexTelefon));
    }

    @Test
    void testNeplatneTelefony() {
        assertFalse("123456".matches(regexTelefon));
        assertFalse("telefon".matches(regexTelefon));
        assertFalse("4201234567890".matches(regexTelefon));
        assertFalse("".matches(regexTelefon));
    }

    @Test
    void testPlatneJmeno() {
        assertTrue("Jan".matches(regexJmeno));
        assertTrue("Jakub".matches(regexJmeno));
        assertTrue("buko".matches(regexJmeno));
        assertTrue("buzz".matches(regexJmeno));
    }

    @Test
    void testNeplatneJmeno() {
        assertFalse("777".matches(regexJmeno));
        assertFalse("j4".matches(regexJmeno));
        assertFalse("§§".matches(regexJmeno));
        assertFalse("A".matches(regexJmeno));
    }
}
