import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class HlavniOkno extends JFrame {
    private boolean jeAdminPrihlasen = false;
    private Uzivatel prihlasenyUzivatel = null;
    private ArrayList<Uzivatel> uzivatele = new ArrayList<>();

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public HlavniOkno() {
        setTitle("Rezervace do ordinace");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nadpis = new JLabel("Vítejte v rezervačním systému", JLabel.CENTER);
        nadpis.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(nadpis, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);

        // === Panel přihlášení ===
        JPanel panelPrihlaseni = new JPanel(new FlowLayout(FlowLayout.CENTER,20,20));


        JButton btnPrihlasitPacient = new JButton("Přihlásit se jako pacient");
        JButton btnPrihlasitAdmin = new JButton("Přihlásit se jako admin");
        JButton btnRegistrovat = new JButton("Registrovat se");

        panelPrihlaseni.add(btnPrihlasitPacient);
        panelPrihlaseni.add(Box.createVerticalStrut(10));
        panelPrihlaseni.add(btnPrihlasitAdmin);
        panelPrihlaseni.add(Box.createVerticalStrut(10));
        panelPrihlaseni.add(btnRegistrovat);

        // === Panel aplikace ===
        JPanel panelAplikace = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnRezervovat = new JButton("Rezervovat termín");
        JButton btnZobrazit = new JButton("Zobrazit rezervace");
        JButton btnZmenaHesla = new JButton("Změnit heslo");
        JButton btnOdhlasit = new JButton("Odhlásit se");

        panelAplikace.add(btnRezervovat);
        panelAplikace.add(btnZobrazit);
        panelAplikace.add(btnZmenaHesla);
        panelAplikace.add(btnOdhlasit);

        // === Přidání panelů do CardLayout ===
        mainPanel.add(panelPrihlaseni, "login");
        mainPanel.add(panelAplikace, "app");

        // === Akce tlačítek ===

        // Přihlášení pacient
        btnPrihlasitPacient.addActionListener(e -> {
            new PrihlaseniFormular(uzivatele, uzivatel -> {
                prihlasenyUzivatel = uzivatel;
                jeAdminPrihlasen = false;
                cardLayout.show(mainPanel, "app");
            }).setVisible(true);
        });

        // Přihlášení admin
        btnPrihlasitAdmin.addActionListener(e -> {
            JPanel loginPanel = new JPanel(new GridLayout(2, 2));
            JTextField jmenoField = new JTextField();
            JPasswordField hesloField = new JPasswordField();
            loginPanel.add(new JLabel("Jméno:"));
            loginPanel.add(jmenoField);
            loginPanel.add(new JLabel("Heslo:"));
            loginPanel.add(hesloField);

            int result = JOptionPane.showConfirmDialog(this, loginPanel, "Přihlášení admina", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String jmeno = jmenoField.getText();
                String heslo = new String(hesloField.getPassword());

                for (Uzivatel u : uzivatele) {
                    if (u.getJmeno().equals(jmeno) && u.getHeslo().equals(heslo) && u.isJeAdmin()) {
                        jeAdminPrihlasen = true;
                        prihlasenyUzivatel = null;
                        cardLayout.show(mainPanel, "app");
                        JOptionPane.showMessageDialog(this, "Přihlášení jako admin bylo úspěšné.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Chyba přihlášení: nesprávné údaje nebo nejste admin.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Registrace
        btnRegistrovat.addActionListener(e -> {
            new RegistraceFormular(uzivatele).setVisible(true);
        });

        // Rezervace
        btnRezervovat.addActionListener(e -> {
            if (prihlasenyUzivatel != null) {
                new RezervaceFormular(prihlasenyUzivatel).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Rezervace je dostupná pouze pro přihlášeného pacienta.");
            }
        });

        // Zobrazit rezervace
        btnZobrazit.addActionListener(e -> {
            if (jeAdminPrihlasen || prihlasenyUzivatel != null) {
                new ZobrazitRezervace(prihlasenyUzivatel, jeAdminPrihlasen).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Nejste přihlášen.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Odhlášení
        btnOdhlasit.addActionListener(e -> {
            if (prihlasenyUzivatel == null && !jeAdminPrihlasen) {
                JOptionPane.showMessageDialog(this, "Nejste přihlášen.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            prihlasenyUzivatel = null;
            jeAdminPrihlasen = false;

            JOptionPane.showMessageDialog(this, "Byl jste úspěšně odhlášen.");
            cardLayout.show(mainPanel, "login");
        });

        // === Změna hesla ===
        btnZmenaHesla.addActionListener(e -> {
            Uzivatel cil = (jeAdminPrihlasen) ? najdiAdmina() : prihlasenyUzivatel;
            if (cil == null) {
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

                if (!cil.getHeslo().equals(stare)) {
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

                cil.setHeslo(nove);
                ulozitUzivatele("uzivatele.txt");
                JOptionPane.showMessageDialog(this, "Heslo bylo úspěšně změněno.");
            }
        });
    }

    private Uzivatel najdiAdmina() {
        return uzivatele.stream().filter(Uzivatel::isJeAdmin).findFirst().orElse(null);
    }

    // === Soubory ===

    public void ulozitUzivatele(String nazevSouboru) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazevSouboru))) {
            for (Uzivatel u : uzivatele) {
                writer.write(u.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Chyba při ukládání uživatelů: " + e.getMessage());
        }
    }

    public void nacistUzivatele(String nazevSouboru) {
        File soubor = new File(nazevSouboru);
        if (!soubor.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(nazevSouboru))) {
            String radek;
            while ((radek = reader.readLine()) != null) {
                Uzivatel u = Uzivatel.fromFileString(radek);
                if (u != null) {
                    uzivatele.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Chyba při načítání uživatelů: " + e.getMessage());
        }

        boolean adminExistuje = uzivatele.stream().anyMatch(Uzivatel::isJeAdmin);
        if (!adminExistuje) {
            uzivatele.add(new Uzivatel("admin", "Admin", "admin@ordinace.cz", "123456789", "heslo123", true, Pojistovna.VZP));
        }
    }
}
