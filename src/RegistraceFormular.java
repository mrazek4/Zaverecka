import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RegistraceFormular extends JFrame {

    public RegistraceFormular(ArrayList<Uzivatel> uzivatele) {
        setTitle("Registrace nového pacienta");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === Vstupní pole ===
        JTextField jmenoF = new JTextField(20);
        JTextField prijmeniF = new JTextField(20);
        JTextField emailF = new JTextField(20);
        JTextField telefonF = new JTextField(20);
        JPasswordField hesloF = new JPasswordField(20);
        JComboBox<Pojistovna> pojistovnaBox = new JComboBox<>(Pojistovna.values());

        JButton registrovatBtn = new JButton("Registrovat");

        registrovatBtn.addActionListener(e -> {
            String jmeno = jmenoF.getText().trim();
            String prijmeni = prijmeniF.getText().trim();
            String email = emailF.getText().trim();
            String telefon = telefonF.getText().trim();
            String heslo = new String(hesloF.getPassword()).trim();
            Pojistovna pojistovna = (Pojistovna) pojistovnaBox.getSelectedItem();

            // === Validace ===
            if (jmeno.isEmpty() || prijmeni.isEmpty() || email.isEmpty() || telefon.isEmpty() || heslo.isEmpty() || pojistovna == null) {
                JOptionPane.showMessageDialog(this, "Vyplňte prosím všechna pole", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Zadejte platný e-mail", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!telefon.matches("^(\\+420|00420)?\\s?\\d{3}\\s?\\d{3}\\s?\\d{3}$")) {
                JOptionPane.showMessageDialog(this, "Zadejte platné telefonní číslo", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Uzivatel u : uzivatele) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "Uživatel s tímto e-mailem už existuje!", "Chyba", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (u.getTelefon().equalsIgnoreCase(telefon)) {
                    JOptionPane.showMessageDialog(this, "Uživatel s tímto cislem už existuje", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            }

            Uzivatel novy = new Uzivatel(
                    jmeno,
                    prijmeni,
                    email,
                    telefon.replaceAll("\\s+", ""),
                    heslo,
                    false,
                    pojistovna
            );

            uzivatele.add(novy);

            JOptionPane.showMessageDialog(this, "Registrace proběhla úspěšně! Nyní se můžete přihlásit.");
            dispose();
        });

        // === Rozvržení ===
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Jméno:"), gbc);
        gbc.gridx = 1;
        panel.add(jmenoF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Příjmení:"), gbc);
        gbc.gridx = 1;
        panel.add(prijmeniF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        panel.add(emailF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Telefon:"), gbc);
        gbc.gridx = 1;
        panel.add(telefonF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Heslo:"), gbc);
        gbc.gridx = 1;
        panel.add(hesloF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Pojišťovna:"), gbc);
        gbc.gridx = 1;
        panel.add(pojistovnaBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(registrovatBtn, gbc);

        add(panel);
    }
}
