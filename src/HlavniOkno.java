import javax.swing.*;
import java.awt.*;

public class HlavniOkno extends JFrame {
    private boolean jeAdminPrihlasen = false;

    public HlavniOkno() {
        setTitle("Rezervace do ordinace");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel nadpis = new JLabel("Vítejte v rezervačním systému", JLabel.CENTER);
        panel.add(nadpis, BorderLayout.NORTH);

        JButton btnRezervovat = new JButton("Rezervovat termín");
        JButton btnZobrazit = new JButton("Zobrazit rezervace");
        JButton btnPrihlasit = new JButton("Přihlásit se jako admin");

        JPanel tlacitkaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        tlacitkaPanel.add(btnRezervovat);
        tlacitkaPanel.add(btnZobrazit);
        tlacitkaPanel.add(btnPrihlasit);

        panel.add(tlacitkaPanel, BorderLayout.CENTER);
        add(panel);

        // 💥 Tady přidáváš napojení na formulář
        btnRezervovat.addActionListener(e -> {
            RezervaceFormular formular = new RezervaceFormular();
            formular.setVisible(true);
        });

        btnPrihlasit.addActionListener(e -> {
            prihlasitAdmina();
        });

        btnZobrazit.addActionListener(e -> {
            if (jeAdminPrihlasen) {
                ZobrazitRezervace zobrazitRezervace = new ZobrazitRezervace();
                zobrazitRezervace.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Přístup odepřen. Přihlaste se jako admin!", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void prihlasitAdmina() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField jmenoField = new JTextField();
        JPasswordField hesloField = new JPasswordField();

        panel.add(new JLabel("Uživatelské jméno:"));
        panel.add(jmenoField);
        panel.add(new JLabel("Heslo:"));
        panel.add(hesloField);

        int vysledek = JOptionPane.showConfirmDialog(this, panel, "Admin přihlášení", JOptionPane.OK_CANCEL_OPTION);

        if (vysledek == JOptionPane.OK_OPTION) {
            String jmeno = jmenoField.getText();
            String heslo = new String(hesloField.getPassword());

            if (jmeno.equals("admin") && heslo.equals("heslo123")) {
                jeAdminPrihlasen = true;
                JOptionPane.showMessageDialog(this, "Úspěšně přihlášeno jako admin!");
            } else {
                JOptionPane.showMessageDialog(this, "Špatné přihlašovací údaje.", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        }


    }
}
