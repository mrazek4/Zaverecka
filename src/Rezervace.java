public class Rezervace {
    private String jmeno;
    private String prijmeni;
    private String email;
    private int telefonC;

    @Override
    public String toString() {
        return "Rezervace{" +
                "jmeno='" + jmeno + '\'' +
                ", prijmeni='" + prijmeni + '\'' +
                ", email='" + email + '\'' +
                ", telefonC=" + telefonC +
                '}';
    }

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

    public Rezervace(String jmeno, String prijmeni, String email, int telefonC) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.email = email;
        this.telefonC = telefonC;
    }
}
