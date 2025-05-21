import java.io.*;
import java.util.ArrayList;

public class RezervaceSpravce {

    private static ArrayList<Rezervace> seznamRezervaci = new ArrayList<>();

    public static void pridatRezervaci(Rezervace rezervace) {
        seznamRezervaci.add(rezervace);
    }

    public static void odstranitRezervaci(Rezervace rezervace) {
        seznamRezervaci.remove(rezervace);
    }

    public static boolean existujeRezervaceNaTermin(String termin) {
        for (Rezervace r : seznamRezervaci) {
            if (r.getTermin().equals(termin)) {
                return true;
            }
        }
        return false;
    }

    public static boolean jeDenPlneObsazen(String datum) {
        int pocet = 0;
        for (Rezervace r : seznamRezervaci) {
            if (r.getTermin().startsWith(datum)) {
                pocet++;
            }
        }
        return pocet >= 7; // Maximálně 7 termínů za den
    }

    public static ArrayList<Rezervace> getSeznamRezervaci() {
        return seznamRezervaci;
    }

    public static void ulozitDoSouboru(String cesta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(cesta))) {
            for (Rezervace r : seznamRezervaci) {
                bw.write(r.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Chyba při ukládání rezervací: " + e.getMessage());
        }
    }

    public static void nacistZeSouboru(String cesta) {
        seznamRezervaci.clear();

        File soubor = new File(cesta);
        if (!soubor.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            String radek;
            while ((radek = br.readLine()) != null) {
                Rezervace r = Rezervace.fromFileString(radek);
                if (r != null) {
                    seznamRezervaci.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání rezervací: " + e.getMessage());
        }
    }
}
