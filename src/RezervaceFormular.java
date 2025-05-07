import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import org.jdatepicker.impl.*;


public class RezervaceFormular extends JFrame {

    public RezervaceFormular() {
        setTitle("Nová rezervace");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextField jmenoF = new JTextField(20);
        JTextField emailF = new JTextField(20);
        JTextField telefonF = new JTextField(20);
        JComboBox<TypNavstevy> typNavstevy = new JComboBox<>(TypNavstevy.values());

        //Nastavení JDatePicker, delano pomoci chatGPT
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dnes");
        p.put("text.month", "Měsíc");
        p.put("text.year", "Rok");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // ==== Výběr času ====
        String[] casy = { "08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00" };
        JComboBox<String> casBox = new JComboBox<>(casy);

        JButton rezervovat = new JButton("Rezervovat");
        rezervovat.addActionListener(e -> {
            String jmeno = jmenoF.getText().trim();
            String email = emailF.getText().trim();
            String telefon = telefonF.getText().trim();
            TypNavstevy typ = (TypNavstevy) typNavstevy.getSelectedItem();
            Object vybraneDatum = datePicker.getModel().getValue();
            String cas = (String) casBox.getSelectedItem();

            if (jmeno.isEmpty() || email.isEmpty() || telefon.isEmpty() || typ == null || vybraneDatum == null || cas == null) {
                JOptionPane.showMessageDialog(this, "Vyplňte prosím všechna pole", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Získání data ve formátu LocalDate
            java.util.Date selectedDate = (java.util.Date) vybraneDatum;
            LocalDate datum = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            String formatovanyDatumCas = datum.format(DateTimeFormatter.ISO_DATE) + " " + cas;



            System.out.println("=== NOVÁ REZERVACE ===");
            System.out.println("Jméno: " + jmeno);
            System.out.println("E-mail: " + email);
            System.out.println("Telefon: " + telefon);
            System.out.println("Typ návštěvy: " + typ.name() + " (" + typ.getPopis() + ")");
            System.out.println("Termín: " + formatovanyDatumCas);

            JOptionPane.showMessageDialog(this, "Rezervace byla úspěšně vytvořena!");
            dispose();
        });

        // ==== Rozvržení ====
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

    // Formátovač data pro JDatePicker
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