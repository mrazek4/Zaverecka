import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdatepicker.impl.*;

public class RezervaceFormular extends JFrame {

    private static final String[] VSECHNY_CASY = {
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00"
    };

    private JComboBox<String> casBox;
    private JDatePickerImpl datePicker;
    private JComboBox<TypNavstevy> typNavstevyBox;

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

        typNavstevyBox = new JComboBox<>(TypNavstevy.values());

        // === Datum ===
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Dnes");
        p.put("text.month", "Měsíc");
        p.put("text.year", "Rok");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // === Čas ===
        casBox = new JComboBox<>();
        aktualizujCasyProDatum(null); // prázdné na začátku

        // === Reakce na výběr data ===
        datePicker.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            if (vybraneDatum != null) {
                java.util.Date selectedDate = (java.util.Date) vybraneDatum;
                LocalDate datum = new java.sql.Date(selectedDate.getTime()).toLocalDate();
                String datumText = datum.format(DateTimeFormatter.ISO_DATE);
                aktualizujCasyProDatum(datumText);

                if (RezervaceSpravce.jeDenPlneObsazen(datumText)) {
                    JOptionPane.showMessageDialog(this, "Tento den je plně obsazen.", "Upozornění", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton rezervovat = new JButton("Rezervovat");
        rezervovat.addActionListener(e -> {
            Object vybraneDatum = datePicker.getModel().getValue();
            String cas = (String) casBox.getSelectedItem();
            TypNavstevy typ = (TypNavstevy) typNavstevyBox.getSelectedItem();

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

            if (RezervaceSpravce.jeDenPlneObsazen(datum.format(DateTimeFormatter.ISO_DATE))) {
                JOptionPane.showMessageDialog(this, "Tento den je již zcela obsazen!", "Chyba", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Rezervace nova = new Rezervace(
                    prihlasenyUzivatel.getJmeno(),
                    prihlasenyUzivatel.getPrijmeni(),
                    prihlasenyUzivatel.getEmail(),
                    Integer.parseInt(prihlasenyUzivatel.getTelefon()),
                    formatovanyDatumCas,
                    typ
            );

            RezervaceSpravce.pridatRezervaci(nova);
            RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
            JOptionPane.showMessageDialog(this, "Rezervace byla úspěšně vytvořena!");
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
        panel.add(new JLabel("Typ návštěvy:"), gbc);
        gbc.gridx = 1;
        panel.add(typNavstevyBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Datum:"), gbc);
        gbc.gridx = 1;
        panel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Čas:"), gbc);
        gbc.gridx = 1;
        panel.add(casBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(rezervovat, gbc);

        add(panel);
    }

    private void aktualizujCasyProDatum(String datum) {
        List<String> volneCasy = new ArrayList<>();
        if (datum == null) {
            casBox.setModel(new DefaultComboBoxModel<>(new String[0]));
            return;
        }

        for (String cas : VSECHNY_CASY) {
            String termin = datum + " " + cas;
            if (!RezervaceSpravce.existujeRezervaceNaTermin(termin)) {
                volneCasy.add(cas);
            }
        }

        if (volneCasy.isEmpty()) {
            volneCasy.add("Žádné volné časy");
        }

        casBox.setModel(new DefaultComboBoxModel<>(volneCasy.toArray(new String[0])));
    }

    // === Formátovač pro JDatePicker ===
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
