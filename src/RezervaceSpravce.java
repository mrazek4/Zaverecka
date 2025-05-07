import java.util.ArrayList;

public class RezervaceSpravce {
    private static ArrayList<Rezervace> seznamRezervaci = new ArrayList<>();

    public static void pridatRezervaci(Rezervace rezervace) {
        seznamRezervaci.add(rezervace);
    }
    public static boolean existujeRezervaceNaTermin(String termin) {
        for (Rezervace r : seznamRezervaci) {
            if (r.getTermin().equals(termin)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Rezervace> getSeznamRezervaci() {
        return seznamRezervaci;
    }
}
