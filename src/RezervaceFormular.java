import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import org.jdatepicker.impl.*;

public class RezervaceFormular extends JFrame {

    public RezervaceFormular(Uzivatel prihlasenyUzivatel) {
        if (prihlasenyUzivatel == null) {
            JOptionPane.showMessageDialog(this, "Pro vytvoření rezervace se musíte přihlásit!", "Nepřihlášen", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Nová rezervace");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField jmenoF = new JTextField(prihlasenyUzivatel.getJmeno(), 20);
        JTextField emailF = new JTextField(prihlasenyUzivatel.getEmail(), 20);
        JTextField telefonF = new JTextField(prihlasenyUzivatel.getTelefon(), 20);

        jmenoF.setEditable(false);
        emailF.setEditable(false);
        telefonF.setEditable(false);

        JComboBox<TypNavstevy> typNavstevy = new JComboBox<>(TypNavstevy.values());

        // === Datum ===
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dnes");
        p.put("text.month", "Měsíc");
        p.put("text.year", "Rok");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // === Kontrola obsazenosti dne ===
        datePicker.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            if (vybraneDatum != null) {
                java.util.Date selectedDate = (java.util.Date) vybraneDatum;
                LocalDate datum = new java.sql.Date(selectedDate.getTime()).toLocalDate();
                String datumText = datum.format(DateTimeFormatter.ISO_DATE);

                if (RezervaceSpravce.jeDenPlneObsazen(datumText)) {
                    JOptionPane.showMessageDialog(this, "Tento den je plně obsazen a není možné vytvořit novou rezervaci.", "Upozornění", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // === Čas ===
        String[] casy = { "08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00" };
        JComboBox<String> casBox = new JComboBox<>(casy);

        JButton rezervovat = new JButton("Rezervovat");
        rezervovat.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            String cas = (String) casBox.getSelectedItem();
            TypNavstevy typ = (TypNavstevy) typNavstevy.getSelectedItem();

            if (vybraneDatum == null || cas == null || typ == null) {
                JOptionPane.showMessageDialog(this, "Vyplňte prosím všechna pole", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.Date selectedDate = (java.util.Date) vybraneDatum;
            LocalDate datum = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            String formatovanyDatumCas = datum.format(DateTimeFormatter.ISO_DATE) + " " + cas;

            if (RezervaceSpravce.existujeRezervaceNaTermin(formatovanyDatumCas)) {
                JOptionPane.showMessageDialog(this, "Na tento termín už existuje rezervace!", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Rezervace novaRezervace = new Rezervace(
                    prihlasenyUzivatel.getJmeno(),
                    prihlasenyUzivatel.getPrijmeni(),
                    prihlasenyUzivatel.getEmail(),
                    Integer.parseInt(prihlasenyUzivatel.getTelefon()),
                    formatovanyDatumCas
            );

            RezervaceSpravce.pridatRezervaci(novaRezervace);
            JOptionPane.showMessageDialog(this, "Rezervace byla úspěšně vytvořena!");
            dispose();
        });

        // === Rozvržení ===
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

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Datum:"), gbc);
        gbc.gridx = 1; panel.add(datePicker, gbc);

        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Čas:"), gbc);
        gbc.gridx = 1; panel.add(casBox, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(rezervovat, gbc);

        add(panel);
    }

    // === Vnitřní třída pro formátování datumu ===
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) {
            return LocalDate.parse(text, formatter);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                LocalDate date = new java.sql.Date(cal.getTimeInMillis()).toLocalDate();
                return formatter.format(date);
            }
            return "";
        }
    }
}
