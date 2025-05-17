import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class HlavniOkno extends JFrame {
    private boolean jeAdminPrihlasen = false;
    private Uzivatel prihlasenyUzivatel = null;
    private ArrayList<Uzivatel> uzivatele = new ArrayList<>();

    public HlavniOkno() {
        setTitle("Rezervace do ordinace");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel nadpis = new JLabel("Vítejte v rezervačním systému", JLabel.CENTER);
        panel.add(nadpis, BorderLayout.NORTH);

        // Tlačítka
        JButton btnRezervovat = new JButton("Rezervovat termín");
        JButton btnZobrazit = new JButton("Zobrazit rezervace");
        JButton btnPrihlasitAdmin = new JButton("Přihlásit se jako admin");
        JButton btnPrihlasitPacient = new JButton("Přihlásit se jako pacient");
        JButton btnRegistrovat = new JButton("Registrovat se");

        JPanel tlacitkaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        tlacitkaPanel.add(btnRezervovat);
        tlacitkaPanel.add(btnZobrazit);
        tlacitkaPanel.add(btnPrihlasitAdmin);
        tlacitkaPanel.add(btnPrihlasitPacient);
        tlacitkaPanel.add(btnRegistrovat);

        panel.add(tlacitkaPanel, BorderLayout.CENTER);
        add(panel);

        // === AKCE ===

        // Rezervace – jen pokud je pacient přihlášen
        btnRezervovat.addActionListener(e -> {
            if (prihlasenyUzivatel != null && !prihlasenyUzivatel.isJeAdmin()) {
                new RezervaceFormular(prihlasenyUzivatel).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Pro rezervaci se musíte přihlásit jako pacient.", "Nepřihlášen", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Zobrazit rezervace – admin nebo přihlášený pacient
        btnZobrazit.addActionListener(e -> {
            if (jeAdminPrihlasen || prihlasenyUzivatel != null) {
                new ZobrazitRezervace(prihlasenyUzivatel, jeAdminPrihlasen).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Přístup odepřen. Přihlaste se prosím.", "Nepřihlášen", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Přihlášení admina
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
                        JOptionPane.showMessageDialog(this, "Přihlášení jako admin bylo úspěšné.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Chyba přihlášení: nesprávné údaje nebo nejste admin.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Přihlášení pacienta
        btnPrihlasitPacient.addActionListener(e -> {
            new PrihlaseniFormular(uzivatele, uzivatel -> {
                prihlasenyUzivatel = uzivatel;
                jeAdminPrihlasen = false;
            }).setVisible(true);
        });

        // Registrace pacienta
        btnRegistrovat.addActionListener(e -> {
            new RegistraceFormular(uzivatele).setVisible(true);
        });
    }

    // ===== SOUBOROVÉ OPERACE =====

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
    }
}
