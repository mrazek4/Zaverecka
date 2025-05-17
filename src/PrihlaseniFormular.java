import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PrihlaseniFormular extends JFrame {
    public interface PrihlaseniListener {
        void onUzivatelPrihlasen(Uzivatel uzivatel);
    }

    public PrihlaseniFormular(ArrayList<Uzivatel> uzivatele, PrihlaseniListener listener) {
        setTitle("Přihlášení pacienta");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField emailF = new JTextField(20);
        JPasswordField hesloF = new JPasswordField(20);
        JButton prihlasitBtn = new JButton("Přihlásit");

        prihlasitBtn.addActionListener(e -> {
            String email = emailF.getText().trim();
            String heslo = new String(hesloF.getPassword()).trim();

            if (email.isEmpty() || heslo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Zadejte e-mail i heslo.", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Uzivatel u : uzivatele) {
                if (u.getEmail().equalsIgnoreCase(email) && u.getHeslo().equals(heslo)) {
                    if (u.isJeAdmin()) {
                        JOptionPane.showMessageDialog(this, "Nelze se přihlásit jako admin zde.", "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    JOptionPane.showMessageDialog(this, "Přihlášení úspěšné!");
                    listener.onUzivatelPrihlasen(u);
                    dispose();
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Neplatný e-mail nebo heslo.", "Chyba", JOptionPane.ERROR_MESSAGE);
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; panel.add(emailF, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Heslo:"), gbc);
        gbc.gridx = 1; panel.add(hesloF, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(prihlasitBtn, gbc);

        add(panel);
    }
}

