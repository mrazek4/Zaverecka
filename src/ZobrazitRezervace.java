import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ZobrazitRezervace extends JFrame {

    private DefaultListModel<Rezervace> model;
    private JList<Rezervace> seznam;
    private JComboBox<TypNavstevy> filtrTyp;
    private JTextField hledatField;

    private Uzivatel prihlasenyUzivatel;
    private boolean jeAdmin;

    public ZobrazitRezervace(Uzivatel prihlasenyUzivatel, boolean jeAdmin) {
        this.prihlasenyUzivatel = prihlasenyUzivatel;
        this.jeAdmin = jeAdmin;

        setTitle("Seznam rezervací");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Panel s filtry ===
        JPanel filtrPanel = new JPanel(new FlowLayout());

        filtrTyp = new JComboBox<>(TypNavstevy.values());
        filtrTyp.insertItemAt(null, 0); // možnost "všechny"
        filtrTyp.setSelectedIndex(0);

        hledatField = new JTextField(15);

        filtrPanel.add(new JLabel("Typ návštěvy:"));
        filtrPanel.add(filtrTyp);
        filtrPanel.add(new JLabel("Hledat:"));
        filtrPanel.add(hledatField);

        add(filtrPanel, BorderLayout.NORTH);

        // === Seznam rezervací ===
        model = new DefaultListModel<>();
        seznam = new JList<>(model);
        seznam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(seznam);
        add(scrollPane, BorderLayout.CENTER);

        // === Tlačítko pro smazání (admin) ===
        if (jeAdmin) {
            JButton btnSmazat = new JButton("Zrušit vybranou rezervaci");
            btnSmazat.addActionListener(e -> {
                Rezervace vybrana = seznam.getSelectedValue();
                if (vybrana != null) {
                    int potvrzeni = JOptionPane.showConfirmDialog(this,
                            "Opravdu chcete zrušit tuto rezervaci?",
                            "Potvrdit zrušení",
                            JOptionPane.YES_NO_OPTION);

                    if (potvrzeni == JOptionPane.YES_OPTION) {
                        RezervaceSpravce.odstranitRezervaci(vybrana);
                        RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
                        aktualizujSeznam();
                        JOptionPane.showMessageDialog(this, "Rezervace byla zrušena.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vyberte prosím rezervaci ke zrušení.");
                }
            });
            add(btnSmazat, BorderLayout.SOUTH);
        }

        // === Posluchače filtrů ===
        filtrTyp.addActionListener(e -> aktualizujSeznam());

        hledatField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aktualizujSeznam(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aktualizujSeznam(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aktualizujSeznam(); }
        });

        // === První načtení seznamu ===
        aktualizujSeznam();
    }

    private void aktualizujSeznam() {
        model.clear();
        TypNavstevy vybranyTyp = (TypNavstevy) filtrTyp.getSelectedItem();
        String hledat = hledatField.getText().toLowerCase();

        ArrayList<Rezervace> vsechny = RezervaceSpravce.getSeznamRezervaci();
        vsechny.sort((r1, r2) -> r1.getTermin().compareTo(r2.getTermin()));

        for (Rezervace r : vsechny) {
            boolean jeViditelna =
                    (jeAdmin || (prihlasenyUzivatel != null && r.getEmail().equalsIgnoreCase(prihlasenyUzivatel.getEmail()))) &&
                            (vybranyTyp == null || r.getTypNavstevy() == vybranyTyp) &&
                            (r.getJmeno().toLowerCase().contains(hledat) || r.getEmail().toLowerCase().contains(hledat));

            if (jeViditelna) {
                model.addElement(r);
            }
        }

        if (model.isEmpty()) {
            model.addElement(new Rezervace("Žádné rezervace", "", "", 0, "N/A", TypNavstevy.PREVENTIVNI));
        }
    }
}
