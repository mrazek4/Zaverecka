import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.jdatepicker.impl.*;

public class FormularZmenaTerminu extends JFrame {
    public FormularZmenaTerminu(Rezervace rezervace) {
        setTitle("Změna termínu");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] casy = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00"};
        JComboBox<String> casBox = new JComboBox<>(casy);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dnes");
        p.put("text.month", "Měsíc");
        p.put("text.year", "Rok");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JButton potvrdit = new JButton("Potvrdit změnu");
        potvrdit.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            String cas = (String) casBox.getSelectedItem();

            if (vybraneDatum == null || cas == null) {
                JOptionPane.showMessageDialog(this, "Vyberte datum a čas.", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.Date selectedDate = (java.util.Date) vybraneDatum;
            LocalDate datum = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            String novyTermin = datum.format(DateTimeFormatter.ISO_DATE) + " " + cas;

            if (RezervaceSpravce.existujeRezervaceNaTermin(novyTermin)) {
                JOptionPane.showMessageDialog(this, "Tento termín je již obsazen.", "Chyba", JOptionPane.WARNING_MESSAGE);
                return;
            }

            rezervace.setTermin(novyTermin);
            RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
            JOptionPane.showMessageDialog(this, "Termín byl úspěšně změněn.");
            dispose();
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nové datum:"), gbc);
        gbc.gridx = 1;
        panel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Nový čas:"), gbc);
        gbc.gridx = 1;
        panel.add(casBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(potvrdit, gbc);

        add(panel);
    }

    // === Formátovač pro JDatePicker ===
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
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
