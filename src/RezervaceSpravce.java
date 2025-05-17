import java.io.*;
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
    public static void ulozitDoSouboru(String nazevSouboru) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazevSouboru))) {
            for (Rezervace r : seznamRezervaci) {
                writer.write(r.getJmeno() + ";" + r.getPrijmeni() + ";" + r.getEmail() + ";" + r.getTelefonC() + ";" + r.getTermin());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Chyba při ukládání rezervací: " + e.getMessage());
        }
    }
    public static void nacistZeSouboru(String nazevSouboru) {
        seznamRezervaci.clear();
        File file = new File(nazevSouboru);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(nazevSouboru))) {
            String radek;
            while ((radek = reader.readLine()) != null) {
                String[] casti = radek.split(";");
                if (casti.length == 5) {
                    String jmeno = casti[0];
                    String prijmeni = casti[1];
                    String email = casti[2];
                    int telefon = Integer.parseInt(casti[3]);
                    String termin = casti[4];

                    seznamRezervaci.add(new Rezervace(jmeno, prijmeni, email, telefon, termin));
                }
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání rezervací: " + e.getMessage());
        }
}}
