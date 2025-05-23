/**
 * Trida reprezentujici uzivatele systemu - pacient nebo admin
 */
public class Uzivatel {
    private String jmeno;
    private String prijmeni;
    private String email;
    private String telefon;
    private String heslo;
    private boolean jeAdmin;
    private Pojistovna pojistovna;

    public Uzivatel(String jmeno, String prijmeni, String email, String telefon, String heslo, boolean jeAdmin, Pojistovna pojistovna) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefon = telefon;
        this.heslo = heslo;
        this.jeAdmin = jeAdmin;
        this.pojistovna = pojistovna;
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

    public String getTelefon() {
        return telefon;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public String getHeslo() {
        return heslo;
    }

    public boolean isJeAdmin() {
        return jeAdmin;
    }

    public Pojistovna getPojistovna() {
        return pojistovna;
    }

    // Zápis do souboru
    public String toFileString() {
        return jmeno + ";" + prijmeni + ";" + email + ";" + telefon + ";" + heslo + ";" + jeAdmin + ";" + pojistovna.name();
    }

    /**
     * Vytvori uzivatele ze zaznamu v textovem souboru.
     * @param radek radek ze souboru
     * @return instance uzivatele nebo null pri chybě
     */
    public static Uzivatel fromFileString(String radek) {
        String[] casti = radek.split(";");
        if (casti.length != 7) return null;

        String jmeno = casti[0];
        String prijmeni = casti[1];
        String email = casti[2];
        String telefon = casti[3];
        String heslo = casti[4];
        boolean jeAdmin = Boolean.parseBoolean(casti[5]);
        Pojistovna pojistovna = Pojistovna.valueOf(casti[6]);

        return new Uzivatel(jmeno, prijmeni, email, telefon, heslo, jeAdmin, pojistovna);
    }

    @Override
    public String toString() {
        return jmeno + " " + prijmeni + " (" + email + ")";
    }
}
