public class Rezervace {
    private String jmeno;
    private String prijmeni;
    private String email;
    private int telefonC;
    private String termin; // např. "2025-05-15 14:00"

    public Rezervace(String jmeno, String prijmeni, String email, int telefonC, String termin) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefonC = telefonC;
        this.termin = termin;
    }

    // Gettery
    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefonC() {
        return telefonC;
    }

    public String getTermin() {
        return termin;
    }

    // Uložení do souboru
    public String toFileString() {
        return jmeno + ";" + prijmeni + ";" + email + ";" + telefonC + ";" + termin;
    }

    // Načtení ze souboru
    public static Rezervace fromFileString(String radek) {
        String[] casti = radek.split(";");
        if (casti.length != 5) return null;

        String jmeno = casti[0];
        String prijmeni = casti[1];
        String email = casti[2];
        int telefon = Integer.parseInt(casti[3]);
        String termin = casti[4];

        return new Rezervace(jmeno, prijmeni, email, telefon, termin);
    }

    @Override
    public String toString() {
        return "Rezervace:\n" +
                "Jméno: " + jmeno + " " + prijmeni + "\n" +
                "E-mail: " + email + "\n" +
                "Telefon: " + telefonC + "\n" +
                "Termín: " + termin;
    }
}