/**
 * Trida reprezentujici rezervaci pacienta v systemu.
 * Uchovava informace o pacientovi, terminu navstevy, typu navstevy a stavu zruseni.
 */
public class Rezervace {
    private String jmeno;
    private String prijmeni;
    private String email;
    private int telefonC;
    private String termin; // YYYY-MM-DD HH:MM
    private TypNavstevy typNavstevy;
    private boolean zrusena;

    public Rezervace(String jmeno, String prijmeni, String email, int telefonC, String termin, TypNavstevy typNavstevy) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefonC = telefonC;
        this.termin = termin;
        this.typNavstevy = typNavstevy;
        this.zrusena = false;
    }



    public String getJmeno() {
        return jmeno;
    }

    public void setTermin(String termin) {
        this.termin = termin;
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

    public boolean isZrusena() {
        return zrusena;
    }

    public void setZrusena(boolean zrusena) {
        this.zrusena = zrusena;
    }

    // Zapisovani do souboru
    public String toFileString() { //serializace, prevedeni objektu rezervace na radek idealni pro ulozeni do textaku
        return jmeno + ";" + prijmeni + ";" + email + ";" + telefonC + ";" + termin + ";" + typNavstevy.name() + ";" + zrusena;
    }

    /**
     * Vytvori rezervaci z radku v souboru.
     *
     * @param radek radek textu
     * @return objekt rezervace nebo null pri chybe
     */
    public static Rezervace fromFileString(String radek) { //prevedeni zpet na obejkt
        String[] casti = radek.split(";");
        try {
            if (casti.length == 7) {
                String jmeno = casti[0];
                String prijmeni = casti[1];
                String email = casti[2];
                int telefon = Integer.parseInt(casti[3]);
                String termin = casti[4];
                TypNavstevy typ = TypNavstevy.valueOf(casti[5]);
                boolean zrusena = Boolean.parseBoolean(casti[6]);
                Rezervace r = new Rezervace(jmeno, prijmeni, email, telefon, termin, typ);
                r.setZrusena(zrusena);
                return r;
            } else if (casti.length == 6) {
                // fallback: chybí zrusena
                String jmeno = casti[0];
                String prijmeni = casti[1];
                String email = casti[2];
                int telefon = Integer.parseInt(casti[3]);
                String termin = casti[4];
                TypNavstevy typ = TypNavstevy.valueOf(casti[5]);
                return new Rezervace(jmeno, prijmeni, email, telefon, termin, typ);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println("Chyba při načítání rezervace: " + radek + " | " + e.getMessage());
            return null;
        }
    }


    @Override
    public String toString() {
        return (zrusena ? "[ZRUŠENO] " : "") +//zkraceny zapis if else, if zrusena==true, jiank ""
                jmeno + " " + prijmeni +
                ", Email: " + email +
                ", Tel: " + telefonC +
                ", Termín: " + termin +
                ", Typ: " + typNavstevy.getPopis();
    }
}
