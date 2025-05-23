/**
 * Trida Pojistovna, reprezentuje typy pojistoven
 */
public enum Pojistovna {
    VZP("Všeobecná zdravotní pojišťovna"),
    ZPMV("Zdravotní pojišťovna ministerstva vnitra"),
    VOZP("Vojenská zdravotní pojišťovna"),
    CPZP("Česká průmyslová zdravotní pojišťovna"),
    RBP("Revírní bratrská pokladna"),
    OZP("Oborová zdravotní pojišťovna");
    private final String nazev;

    Pojistovna(String nazev) {
        this.nazev = nazev;
    }

    public String getNazev() {
        return nazev;
    }

    @Override
    public String toString() {
        return "Pojistovna" +
                "nazev" + nazev;
    }
}
