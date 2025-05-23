import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.jdatepicker.impl.*;
/**
 * Formular pro zmenu terminu stavajici rezervace.
 */
public class FormularZmenaTerminu extends JFrame {

    private JComboBox<String> casBox;
    private JDatePickerImpl datePicker;
    private Rezervace rezervace;
    /**
     * Vytvori formular pro zmenu terminu rezervace.
     * @param rezervace rezervace, ktera se ma upravit
     */
    public FormularZmenaTerminu(Rezervace rezervace) {
        this.rezervace = rezervace;

        setTitle("Změna termínu");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] vsechnyCasy = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00"};

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dnes");
        p.put("text.month", "Měsíc");
        p.put("text.year", "Rok");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        casBox = new JComboBox<>();

        datePicker.addActionListener(e -> aktualizujVolneCasy());

        JButton potvrdit = new JButton("Potvrdit změnu");
        potvrdit.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            String cas = (String) casBox.getSelectedItem();

            if (vybraneDatum == null || cas == null || cas.equals("Žádný čas není volný")) {
                JOptionPane.showMessageDialog(this, "Vyberte datum a čas.", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate datum = new java.sql.Date(((java.util.Date) vybraneDatum).getTime()).toLocalDate();
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

        // === Rozvržení ===
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

    private void aktualizujVolneCasy() {
        String[] vsechnyCasy = {"08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00"};
        List<String> volneCasy = new ArrayList<>();

        Object vybraneDatum = datePicker.getModel().getValue();
        if (vybraneDatum == null) return;

        LocalDate datum = new java.sql.Date(((java.util.Date) vybraneDatum).getTime()).toLocalDate();
        String datumText = datum.format(DateTimeFormatter.ISO_DATE);

        for (String cas : vsechnyCasy) {
            String termin = datumText + " " + cas;

            // Povolit čas, pokud:
            // - je volný
            // - nebo pokud je to ten původní čas této rezervace
            if (!RezervaceSpravce.existujeRezervaceNaTermin(termin) || rezervace.getTermin().equals(termin)) {
                volneCasy.add(cas);
            }
        }

        if (volneCasy.isEmpty()) {
            casBox.setModel(new DefaultComboBoxModel<>(new String[]{"Žádný čas není volný"}));
            casBox.setEnabled(false);
        } else {
            casBox.setModel(new DefaultComboBoxModel<>(volneCasy.toArray(new String[0])));
            casBox.setEnabled(true);
        }
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) {
            return LocalDate.parse(text, formatter);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                LocalDate date = new java.sql.Date(cal.getTimeInMillis()).toLocalDate();
                return formatter.format(date);
            }
            return "";
        }
    }
}
