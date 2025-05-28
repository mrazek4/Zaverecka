import java.io.*;
import java.util.ArrayList;

/**
 * Trida pro spravu rezervaci - pridavani, mazani, ukladani a nacitani ze souboru.
 */
public class RezervaceSpravce {

    private static ArrayList<Rezervace> seznamRezervaci = new ArrayList<>(); //static protoze je napric cele aplikace

    /**
     * Prida rezervaci do seznamu.
     *
     * @param rezervace rezervace k pridani
     */
    public static void pridatRezervaci(Rezervace rezervace) {
        seznamRezervaci.add(rezervace);
    }

    /**
     * Odstrani rezervaci ze seznamu.
     *
     * @param rezervace rezervace k odstraneni
     */
    public static void odstranitRezervaci(Rezervace rezervace) {
        seznamRezervaci.remove(rezervace);
    }
    /**
     * Overi, zda uz existuje rezervace na dany termin.
     * @param termin datum a cas
     * @return true pokud rezervace existuje
     */
    public static boolean existujeRezervaceNaTermin(String termin) {
        for (Rezervace r : seznamRezervaci) {
            if (!r.isZrusena())
                if (r.getTermin().equals(termin)) {
                    return true;
                }
        }
        return false;
    }
    /**
     * Zjisti, zda je dany den plne obsazen.
     * @param datum datum ve formatu YYYY-MM-DD
     * @return true pokud je dosazeno maximalniho poctu rezervaci
     */
    public static boolean jeDenPlneObsazen(String datum) {
        int pocet = 0;
        for (Rezervace r : seznamRezervaci) {
            if (r.getTermin().startsWith(datum)) { //startsWith kontroluje jestli je to datum
                pocet++;
            }
        }
        return pocet >= 7; // Maximálně 7 termínů za den
    }

    public static ArrayList<Rezervace> getSeznamRezervaci() {
        return seznamRezervaci;
    }
    /**
     * Ulozi seznam rezervaci do souboru.
     * @param cesta cesta k souboru
     */
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
    /**
     * Nacte seznam rezervaci ze souboru.
     * @param cesta cesta k souboru
     */
    public static void nacistZeSouboru(String cesta) {
        seznamRezervaci.clear();

        File soubor = new File(cesta);
        if (!soubor.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            String radek;
            while ((radek = br.readLine()) != null) {
                Rezervace r = Rezervace.fromFileString(radek); //prevedeni na obejkt
                if (r != null) {
                    seznamRezervaci.add(r);
                }
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání rezervací: " + e.getMessage());
        }
    }
}
