public enum TypNavstevy {
    PREVENTIVNI("Preventívní prohlídka"),
    AKUTNI("Akutní problém"),
    KONTROLA("Běžná kontrola");



    private final String popis;

    TypNavstevy(String popis) {
        this.popis = popis;
    }
    public String getPopis() {
        return popis;
    }

    @Override
    public String toString() {
        return "TypNavstevy{" +
                "popis='" + popis + '\'' +
                '}';
    }
}
