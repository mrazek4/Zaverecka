public class Uzivatel {
    private String jmeno;
    private String prijmeni;
    private String email;
    private String telefon;
    private String heslo;
    private boolean jeAdmin;

    public Uzivatel(String jmeno, String prijmeni, String email, String telefon, String heslo, boolean jeAdmin) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefon = telefon;
        this.heslo = heslo;
        this.jeAdmin = jeAdmin;
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

    public String getHeslo() {
        return heslo;
    }

    public boolean isJeAdmin() {
        return jeAdmin;
    }

    // Pomocná metoda pro export do řádku v souboru
    public String toFileString() {
        return jmeno + ";" + prijmeni + ";" + email + ";" + telefon + ";" + heslo + ";" + jeAdmin;
    }

    // Načtení uživatele ze souboru
    public static Uzivatel fromFileString(String radek) {
        String[] casti = radek.split(";");
        if (casti.length != 6) return null;

        String jmeno = casti[0];
        String prijmeni = casti[1];
        String email = casti[2];
        String telefon = casti[3];
        String heslo = casti[4];
        boolean jeAdmin = Boolean.parseBoolean(casti[5]);

        return new Uzivatel(jmeno, prijmeni, email, telefon, heslo, jeAdmin);
    }

    @Override
    public String toString() {
        return jmeno + " " + prijmeni + " (" + email + ")";
    }
}