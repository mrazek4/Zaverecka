public class Rezervace {
    private String jmeno;
    private String prijmeni;
    private String email;
    private int telefonC;
    private String termin;// např. "2025-05-15 14:00"
    private TypNavstevy typNavstevy;

    public Rezervace(String jmeno, String prijmeni, String email, int telefonC, String termin, TypNavstevy typNavstevy) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefonC = telefonC;
        this.termin = termin;
        this.typNavstevy = typNavstevy;

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

    public TypNavstevy getTypNavstevy() {
        return typNavstevy;
    }

    // Uložení do souboru
    public String toFileString() {
        return jmeno + ";" + prijmeni + ";" + email + ";" + telefonC + ";" + termin;
    }

    // Načtení ze souboru
    public static Rezervace fromFileString(String radek) {
        String[] casti = radek.split(";");

        try {
            if (casti.length == 6) {
                // Nový formát
                String jmeno = casti[0];
                String prijmeni = casti[1];
                String email = casti[2];
                int telefon = Integer.parseInt(casti[3]);
                String termin = casti[4];
                TypNavstevy typ = TypNavstevy.valueOf(casti[5]);
                return new Rezervace(jmeno, prijmeni, email, telefon, termin, typ);
            } else if (casti.length == 5) {
                // Starý formát — přiřadíme výchozí typ
                String jmeno = casti[0];
                String prijmeni = casti[1];
                String email = casti[2];
                int telefon = Integer.parseInt(casti[3]);
                String termin = casti[4];
                return new Rezervace(jmeno, prijmeni, email, telefon, termin, TypNavstevy.PREVENTIVNI);
            } else {
                System.err.println("Neplatný řádek: " + radek);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Chyba při načítání rezervace: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Rezervace:\n" +
                "Jméno: " + jmeno + " " + prijmeni + "\n" +
                "E-mail: " + email + "\n" +
                "Telefon: " + telefonC + "\n" +
                "Termín: " + termin +
                typNavstevy.getPopis();
    }
}