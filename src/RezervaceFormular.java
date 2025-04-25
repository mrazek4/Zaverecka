import javax.swing.*;
import java.awt.*;

public class RezervaceFormular extends JFrame {
    public RezervaceFormular() {
        setTitle("Nová rezervace");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField jmenoF = new JTextField(20);
        JTextField emailF = new JTextField(20);
        JTextField telefonF = new JTextField(20);

        JComboBox<TypNavstevy>typNavstevy = new JComboBox<>(TypNavstevy.values());

        String[] dostupneTerminy = {
                "2025-04-20 08:00", "2025-04-20 09:00", "2025-04-20 10:00"
        };
        JComboBox<String>terminy = new JComboBox<>(dostupneTerminy);
        JButton rezervovat = new JButton("Rezervovat");
        rezervovat.addActionListener(e -> {
            String jmeno = jmenoF.getText().trim();
            String email = emailF.getText().trim();
            String telefon = telefonF.getText().trim();
            TypNavstevy typ = (TypNavstevy) typNavstevy.getSelectedItem();
            String termin = (String) terminy.getSelectedItem();
            try {
                int tel = Integer.parseInt(telefon);
                Rezervace nova = new Rezervace(jmeno, "", email, tel); // zatím bez příjmení
                RezervaceSpravce.pridatRezervaci(nova);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Telefonní číslo musí být číslo", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (jmeno.isEmpty() || email.isEmpty() || telefon.isEmpty() || typ == null || termin == null) {
                JOptionPane.showMessageDialog(this, "Vyplňte prosím všechny pole", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("=== NOVÁ REZERVACE ===");
            System.out.println("Jméno: " + jmeno);
            System.out.println("E-mail: " + email);
            System.out.println("Telefon: " + telefon);
            System.out.println("Typ návštěvy: " + typ.name() + " (" + typ.getPopis() + ")");
            System.out.println("Termín: " + termin);

            JOptionPane.showMessageDialog(this, "Rezervace byla úspěšně vytvořena!");
            dispose();
        });
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Jméno:"), gbc);
        gbc.gridx = 1; panel.add(jmenoF, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; panel.add(emailF, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Telefon:"), gbc);
        gbc.gridx = 1; panel.add(telefonF, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Typ návštěvy:"), gbc);
        gbc.gridx = 1; panel.add(typNavstevy, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Termín:"), gbc);
        gbc.gridx = 1; panel.add(terminy, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(rezervovat, gbc);

        add(panel);
    }
    }

