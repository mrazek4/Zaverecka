import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Okno pro zobrazeni seznamu rezervaci s moznosti filtrovani, hledani a ruseni.
 */
public class ZobrazitRezervace extends JFrame {

    private DefaultListModel<Rezervace> model;
    private JList<Rezervace> seznam;
    private JComboBox<TypNavstevy> filtrTyp;
    private JTextField hledatField;
    private JCheckBox zobrazitZruseneCheck;

    private Uzivatel prihlasenyUzivatel;
    private boolean jeAdmin;

    /**
     * Vytvori okno pro zobrazeni rezervaci pro uzivatele nebo admina.
     *
     * @param prihlasenyUzivatel aktualne prihlaseny uzivatel (null pokud je admin)
     * @param jeAdmin            true pokud je prihlaseny uzivatel administrator
     */
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
        zobrazitZruseneCheck = new JCheckBox("Zobrazit zrušené");

        filtrPanel.add(new JLabel("Typ návštěvy:"));
        filtrPanel.add(filtrTyp);
        filtrPanel.add(new JLabel("Hledat:"));
        filtrPanel.add(hledatField);
        filtrPanel.add(zobrazitZruseneCheck);

        add(filtrPanel, BorderLayout.NORTH);

        // === Seznam rezervací ===
        model = new DefaultListModel<>();
        seznam = new JList<>(model);
        seznam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(seznam);
        add(scrollPane, BorderLayout.CENTER);

        // === Tlačítka ===
        JPanel tlacitkaPanel = new JPanel();

        if (!jeAdmin && prihlasenyUzivatel != null) {
            JButton btnZmenitTermin = new JButton("Změnit termín");
            btnZmenitTermin.addActionListener(e -> {
                Rezervace vybrana = seznam.getSelectedValue();
                if (vybrana != null && !vybrana.isZrusena()) {
                    new FormularZmenaTerminu(vybrana).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Vyberte platnou rezervaci.");
                }
            });
            tlacitkaPanel.add(btnZmenitTermin);
        }

        if (jeAdmin) {
            JButton btnZrusit = new JButton("Zrušit rezervaci");
            btnZrusit.addActionListener(e -> {
                Rezervace vybrana = seznam.getSelectedValue();
                if (vybrana != null && !vybrana.isZrusena()) {
                    vybrana.setZrusena(true);
                    RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
                    JOptionPane.showMessageDialog(this, "Rezervace byla zrušena.");
                    aktualizujSeznam();
                } else {
                    JOptionPane.showMessageDialog(this, "Vyberte platnou rezervaci.");
                }
            });
            tlacitkaPanel.add(btnZrusit);
        }

        //  Přidání panelu vždy
        add(tlacitkaPanel, BorderLayout.SOUTH);

        // === Posluchače filtrů ===
        filtrTyp.addActionListener(e -> aktualizujSeznam());

        hledatField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                aktualizujSeznam();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                aktualizujSeznam();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                aktualizujSeznam();
            }
        });

        zobrazitZruseneCheck.addActionListener(e -> aktualizujSeznam());

        // === První načtení seznamu ===
        aktualizujSeznam();
    }

    private void aktualizujSeznam() {
        model.clear();
        TypNavstevy vybranyTyp = (TypNavstevy) filtrTyp.getSelectedItem();
        String hledat = hledatField.getText().toLowerCase();
        boolean zobrazitZrusene = zobrazitZruseneCheck.isSelected();

        ArrayList<Rezervace> vsechny = RezervaceSpravce.getSeznamRezervaci();
        vsechny.sort((r1, r2) -> r1.getTermin().compareTo(r2.getTermin()));

        for (Rezervace r : vsechny) {
            boolean patriUzivateli = jeAdmin || (
                    prihlasenyUzivatel != null &&
                            r.getEmail().equalsIgnoreCase(prihlasenyUzivatel.getEmail())
            );

            boolean odpovidaTypu = (vybranyTyp == null || r.getTypNavstevy() == vybranyTyp);
            boolean odpovidaHledani = hledat.isEmpty()
                    || r.getJmeno().toLowerCase().contains(hledat)
                    || r.getEmail().toLowerCase().contains(hledat);
            boolean jeViditelna = zobrazitZrusene || !r.isZrusena();

            if (patriUzivateli && odpovidaTypu && odpovidaHledani && jeViditelna) {
                model.addElement(r);
            }
        }

        if (model.isEmpty()) {
            seznam.setListData(new Rezervace[]{});
            seznam.setEnabled(false);
        } else {
            seznam.setEnabled(true);
        }
    }

}
