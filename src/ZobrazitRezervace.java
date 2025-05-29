import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Graficke okno pro zobrazeni rezervaci.
 * Umoznuje filtrovat rezervace podle typu navstevy, hledat podle jmena nebo emailu
 * (pouze admin), zobrazit zrusene rezervace a mazat nebo upravovat je.
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
     * Konstruktor pro vytvoreni okna se seznamem rezervaci.
     *
     * @param prihlasenyUzivatel prihlaseny uzivatel (null pokud admin)
     * @param jeAdmin true pokud je prihlaseny administrator
     */
    public ZobrazitRezervace(Uzivatel prihlasenyUzivatel, boolean jeAdmin) {
        this.prihlasenyUzivatel = prihlasenyUzivatel;
        this.jeAdmin = jeAdmin;

        setTitle("Seznam rezervací");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Panel s filtry
        JPanel filtrPanel = new JPanel(new FlowLayout());

        filtrTyp = new JComboBox<>(TypNavstevy.values());
        filtrTyp.insertItemAt(null, 0);
        filtrTyp.setSelectedIndex(0);

        hledatField = new JTextField(15);
        JLabel hledatLabel = new JLabel("Hledat:");

        zobrazitZruseneCheck = new JCheckBox("Zobrazit zrušené");

        filtrPanel.add(new JLabel("Typ návštěvy:"));
        filtrPanel.add(filtrTyp);

        if (jeAdmin) {
            filtrPanel.add(hledatLabel);
            filtrPanel.add(hledatField);
        }

        filtrPanel.add(zobrazitZruseneCheck);
        add(filtrPanel, BorderLayout.NORTH);

        //Seznam rezervací
        model = new DefaultListModel<>();
        seznam = new JList<>(model);
        seznam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(seznam);
        add(scrollPane, BorderLayout.CENTER);

        // Tlacitka
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

        add(tlacitkaPanel, BorderLayout.SOUTH);

        //Posluchace
        filtrTyp.addActionListener(e -> aktualizujSeznam());

        if (jeAdmin) {
            hledatField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { aktualizujSeznam(); }//kdyz se meni text v poli seznam se aktualizuje
                public void removeUpdate(DocumentEvent e) { aktualizujSeznam(); }
                public void insertUpdate(DocumentEvent e) { aktualizujSeznam(); }
            });
        }

        zobrazitZruseneCheck.addActionListener(e -> aktualizujSeznam());

        // Prvotni naplneni seznamu
        aktualizujSeznam();
    }

    /**
     * Aktualizuje seznam rezervaci podle zvolenych filtru.
     * Zobrazi pouze ty, ktere odpovidaji filtru typu, hledanemu textu,
     * a stavu zruseni. Pro pacienta pouze jeho vlastni rezervace.
     */
    private void aktualizujSeznam() { //filtry pres chatGPT
        model.clear();
        TypNavstevy vybranyTyp = (TypNavstevy) filtrTyp.getSelectedItem();
        String hledat = jeAdmin ? hledatField.getText().toLowerCase() : "";
        boolean zobrazitZrusene = zobrazitZruseneCheck.isSelected();

        ArrayList<Rezervace> vsechny = RezervaceSpravce.getSeznamRezervaci();
        vsechny.sort((r1, r2) -> r1.getTermin().compareTo(r2.getTermin()));

        for (Rezervace r : vsechny) {
            boolean patriUzivateli = jeAdmin || (prihlasenyUzivatel != null &&
                    r.getEmail().equalsIgnoreCase(prihlasenyUzivatel.getEmail()));
            boolean odpovidaTypu = (vybranyTyp == null || r.getTypNavstevy() == vybranyTyp);

            boolean odpovidaHledani = true;
            if (jeAdmin && !hledat.isEmpty()) {
                odpovidaHledani = r.getJmeno().toLowerCase().contains(hledat) ||
                        r.getEmail().toLowerCase().contains(hledat);
            }

            boolean odpovidaZruseni = zobrazitZrusene || !r.isZrusena();

            if (patriUzivateli && odpovidaTypu && odpovidaHledani && odpovidaZruseni) {
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
