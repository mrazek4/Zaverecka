import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Hlavni okno aplikace, ktere slouzi jako vychozi bod pro prihlaseni, registraci a navigaci.
 */
public class HlavniOkno extends JFrame {
    private boolean jeAdminPrihlasen = false;
    private Uzivatel prihlasenyUzivatel = null;
    private ArrayList<Uzivatel> uzivatele = new ArrayList<>();

    private JPanel mainPanel; //hlavni panel okna
    private CardLayout cardLayout; //prepinani obrazovek

    /**
     * Vytvori nove hlavni okno aplikace a inicializuje vsechny komponenty a posluchace.
     */
    public HlavniOkno() {
        setTitle("Rezervace do ordinace");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ukoncnei aplikace pri zavreni

        JLabel nadpis = new JLabel("Vítejte v rezervačním systému", JLabel.CENTER);
        nadpis.setFont(new Font("SansSerif", Font.BOLD, 18)); //font na nadpis
        add(nadpis, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);

        // Přihlašovací panel
        JPanel panelPrihlaseni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnPrihlasitPacient = new JButton("Přihlásit se jako pacient");
        JButton btnPrihlasitAdmin = new JButton("Přihlásit se jako admin");
        JButton btnRegistrovat = new JButton("Registrovat se");
        JButton btnOAplikaci = new JButton("O aplikaci");

        panelPrihlaseni.add(btnPrihlasitPacient);
        panelPrihlaseni.add(Box.createVerticalStrut(10));
        panelPrihlaseni.add(btnPrihlasitAdmin);
        panelPrihlaseni.add(Box.createVerticalStrut(10));
        panelPrihlaseni.add(btnRegistrovat);
        panelPrihlaseni.add(Box.createVerticalStrut(20));
        panelPrihlaseni.add(btnOAplikaci);

        // Panel pro pacienta
        JPanel panelPacient = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnRezervovat = new JButton("Rezervovat termín");
        JButton btnZobrazit = new JButton("Zobrazit rezervace");
        JButton btnZmenaHesla = new JButton("Změnit heslo");
        JButton btnOdhlasit = new JButton("Odhlásit se");

        panelPacient.add(btnRezervovat);
        panelPacient.add(btnZobrazit);
        panelPacient.add(btnZmenaHesla);
        panelPacient.add(btnOdhlasit);

        // Panel pro admina
        JPanel panelAdmin = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnZobrazitA = new JButton("Zobrazit rezervace");
        JButton btnZmenaHeslaA = new JButton("Změnit heslo");
        JButton btnStatistika = new JButton("Zobrazit statistiku");
        JButton btnOdhlasitA = new JButton("Odhlásit se");

        panelAdmin.add(btnZobrazitA);
        panelAdmin.add(btnZmenaHeslaA);
        panelAdmin.add(btnStatistika);
        panelAdmin.add(btnOdhlasitA);

        // Přidání panelů
        mainPanel.add(panelPrihlaseni, "login");
        mainPanel.add(panelPacient, "pacient");
        mainPanel.add(panelAdmin, "admin");

        // Přihlášení pacient
        btnPrihlasitPacient.addActionListener(e -> {
            new PrihlaseniFormular(uzivatele, uzivatel -> {
                prihlasenyUzivatel = uzivatel;
                jeAdminPrihlasen = false;
                cardLayout.show(mainPanel, "pacient");
            }).setVisible(true);
        });

        // Přihlášení admin
        btnPrihlasitAdmin.addActionListener(e -> {
            JPanel loginPanel = new JPanel(new GridLayout(2, 2)); //2 radky 2 sloupce
            JTextField jmenoField = new JTextField();
            JPasswordField hesloField = new JPasswordField();
            loginPanel.add(new JLabel("Jméno:"));
            loginPanel.add(jmenoField);
            loginPanel.add(new JLabel("Heslo:"));
            loginPanel.add(hesloField);

            int result = JOptionPane.showConfirmDialog(this, loginPanel, "Přihlášení admina", JOptionPane.OK_CANCEL_OPTION);//otevre okno s formularem
            if (result == JOptionPane.OK_OPTION) {// pokud kliknul na ok
                String jmeno = jmenoField.getText();
                String heslo = new String(hesloField.getPassword());//vytvari nove heslo protoye getPassword vraci char

                for (Uzivatel u : uzivatele) {
                    if (u.getJmeno().equals(jmeno) && u.getHeslo().equals(heslo) && u.isJeAdmin()) {
                        jeAdminPrihlasen = true;//overeni ze je admin
                        prihlasenyUzivatel = null;
                        cardLayout.show(mainPanel, "admin");
                        JOptionPane.showMessageDialog(this, "Přihlášení jako admin bylo úspěšné.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Chyba přihlášení: nesprávné údaje nebo nejste admin.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Registrace
        btnRegistrovat.addActionListener(e -> new RegistraceFormular(uzivatele).setVisible(true));

        // === O aplikaci ===
        btnOAplikaci.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "O aplikaci:\n" +
                            "Rezervační systém pro ordinaci\n" +
                            "Autor: Mrazek Jakub\n" +
                            "Verze: 1.0\n" +
                            "Datum: 2025\n\n" +
                            "Funkce:\n" +
                            "- Přihlášení a registrace\n" +
                            "- Rezervace termínů\n" +
                            "- Statistika a historie\n" +
                            "- Ruseni terminu\n",
                    "O aplikaci",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Akce pacienta
        btnRezervovat.addActionListener(e -> new RezervaceFormular(prihlasenyUzivatel).setVisible(true));
        btnZobrazit.addActionListener(e -> new ZobrazitRezervace(prihlasenyUzivatel, false).setVisible(true));
        btnZmenaHesla.addActionListener(e -> zmenitHeslo(prihlasenyUzivatel));
        btnOdhlasit.addActionListener(e -> {
            prihlasenyUzivatel = null;
            cardLayout.show(mainPanel, "login");
            JOptionPane.showMessageDialog(this, "Byl jste odhlášen.");
        });

        // Akce admina
        btnZobrazitA.addActionListener(e -> new ZobrazitRezervace(null, true).setVisible(true));
        btnZmenaHeslaA.addActionListener(e -> zmenitHeslo(najdiAdmina()));
        btnStatistika.addActionListener(e -> new StatistikaOkno().setVisible(true));
        btnOdhlasitA.addActionListener(e -> {
            jeAdminPrihlasen = false;
            cardLayout.show(mainPanel, "login");
            JOptionPane.showMessageDialog(this, "Admin byl odhlášen.");
        });

        // Načti uživatele
        nacistUzivatele("uzivatele.txt");
    }

    /**
     * Zmeni heslo pro daneho uzivatele.
     *
     * @param uzivatel uzivatel, ktery chce zmenit heslo
     */
    private void zmenitHeslo(Uzivatel uzivatel) {
        if (uzivatel == null) {
            JOptionPane.showMessageDialog(this, "Nejste přihlášen.", "Chyba", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JPasswordField stareF = new JPasswordField();
        JPasswordField noveF = new JPasswordField();
        JPasswordField noveZnovuF = new JPasswordField();

        panel.add(new JLabel("Staré heslo:"));
        panel.add(stareF);
        panel.add(new JLabel("Nové heslo:"));
        panel.add(noveF);
        panel.add(new JLabel("Znovu nové heslo:"));
        panel.add(noveZnovuF);

        int vysledek = JOptionPane.showConfirmDialog(this, panel, "Změna hesla", JOptionPane.OK_CANCEL_OPTION);
        if (vysledek == JOptionPane.OK_OPTION) {
            String stare = new String(stareF.getPassword()).trim();
            String nove = new String(noveF.getPassword()).trim();
            String noveZnovu = new String(noveZnovuF.getPassword()).trim();

            if (!uzivatel.getHeslo().equals(stare)) {
                JOptionPane.showMessageDialog(this, "Staré heslo není správné.", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nove.equals(stare)) {
                JOptionPane.showMessageDialog(this, "Nové heslo nesmí být stejné jako staré.", "Chyba", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!nove.equals(noveZnovu)) {
                JOptionPane.showMessageDialog(this, "Nová hesla se neshodují.", "Chyba", JOptionPane.WARNING_MESSAGE);
                return;
            }

            uzivatel.setHeslo(nove);
            ulozitUzivatele("uzivatele.txt");
            JOptionPane.showMessageDialog(this, "Heslo bylo úspěšně změněno.");
        }
    }

    /**
     * Vrati prvniho uzivatele, ktery ma roli administratora.
     *
     * @return admin uzivatel nebo null pokud neni nalezen
     */
    private Uzivatel najdiAdmina() {
        return uzivatele.stream().filter(Uzivatel::isJeAdmin).findFirst().orElse(null);
    }

    /**
     * Ulozi seznam uzivatelu do souboru.
     *
     * @param soubor cesta k souboru
     */
    public void ulozitUzivatele(String soubor) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(soubor))) {
            HashSet<String> emaily = new HashSet<>();
            for (Uzivatel u : uzivatele) {
                if (emaily.add(u.getEmail().toLowerCase())) {
                    writer.write(u.toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Chyba při ukládání uživatelů: " + e.getMessage());
        }
    }

    /**
     * Nacte seznam uzivatelu ze souboru a doplni defaultniho admina pokud chybi.
     *
     * @param soubor cesta k souboru
     */
    public void nacistUzivatele(String soubor) {
        File f = new File(soubor);
        if (!f.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String radek;
            while ((radek = reader.readLine()) != null) {
                Uzivatel u = Uzivatel.fromFileString(radek);
                if (u != null) uzivatele.add(u);
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání uživatelů: " + e.getMessage());
        }

        boolean existujeAdmin = uzivatele.stream().anyMatch(Uzivatel::isJeAdmin);
        if (!existujeAdmin) {
            uzivatele.add(new Uzivatel("admin", "Admin", "admin@ordinace.cz", "123456789", "heslo123", true, Pojistovna.VZP));
        }
    }
}