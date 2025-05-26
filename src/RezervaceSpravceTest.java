import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RezervaceSpravceTest {
    @BeforeEach
    public void setup() {
        RezervaceSpravce.getSeznamRezervaci().clear(); // vycisteni pred kazdym testem
    }

    @Test
    void testKolizeRezervaceNaStejnyTermin() {
        String termin = "2025-06-01 10:00";
        Rezervace r1 = new Rezervace("Jan", "Novak", "jan@novak.cz", 123456789, termin, TypNavstevy.PREVENTIVNI);
        RezervaceSpravce.pridatRezervaci(r1);

        boolean koliduje = RezervaceSpravce.existujeRezervaceNaTermin(termin);
        assertTrue(koliduje, "Termin by mel byt obsazeny");
    }

    @Test
    void testZrusenaRezervaceNaTerminNepocitaSe() {
        String termin = "2025-06-01 11:00";
        Rezervace r = new Rezervace("Eva", "Malá", "eva@mala.cz", 123456789, termin, TypNavstevy.AKUTNI);
        r.setZrusena(true);
        RezervaceSpravce.pridatRezervaci(r);

        boolean koliduje = RezervaceSpravce.existujeRezervaceNaTermin(termin);
        assertFalse(koliduje, "Zrusena rezervace by nemela blokovat termin");
    }


    @Test
    void testVolnyDen() {
        String datum = "2025-06-03";
        RezervaceSpravce.pridatRezervaci(new Rezervace("Test", "User", "t@t.cz", 1, datum + " 08:00", TypNavstevy.KONTROLA));
        assertFalse(RezervaceSpravce.jeDenPlneObsazen(datum));
    }

    @Test
    void testOdebraniRezervace() {
        Rezervace r = new Rezervace("Adam", "Tester", "adam@test.cz", 123456789, "2025-06-04 09:00", TypNavstevy.AKUTNI);
        RezervaceSpravce.pridatRezervaci(r);
        RezervaceSpravce.odstranitRezervaci(r);

        assertFalse(RezervaceSpravce.getSeznamRezervaci().contains(r), "Rezervace by mela byt odebrana");
    }
}
