import java.util.ArrayList;

public class RezervaceSpravce {
    private static ArrayList<Rezervace> seznamRezervaci = new ArrayList<>();

    public static void pridatRezervaci(Rezervace rezervace) {
        seznamRezervaci.add(rezervace);
    }

    public static ArrayList<Rezervace> getSeznamRezervaci() {
        return seznamRezervaci;
    }
}
