import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ZobrazitRezervace extends JFrame {

    private DefaultListModel<Rezervace> model;

    public ZobrazitRezervace(Uzivatel prihlasenyUzivatel, boolean jeAdmin) {
        setTitle("Seznam rezervací");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultListModel<>();
        JList<Rezervace> seznam = new JList<>(model);
        seznam.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(seznam);

        // Načíst rezervace
        ArrayList<Rezervace> vsechny = RezervaceSpravce.getSeznamRezervaci();
        for (Rezervace r : vsechny) {
            // Admin vidí vše, pacient jen své
            if (jeAdmin || (prihlasenyUzivatel != null && r.getEmail().equalsIgnoreCase(prihlasenyUzivatel.getEmail()))) {
                model.addElement(r);
            }
        }

        JPanel hlavniPanel = new JPanel(new BorderLayout());
        hlavniPanel.add(scrollPane, BorderLayout.CENTER);

        // Pokud je admin, přidáme tlačítko pro smazání
        if (jeAdmin) {
            JButton btnSmazat = new JButton("Zrušit vybranou rezervaci");
            btnSmazat.addActionListener(e -> {
                Rezervace vybrana = seznam.getSelectedValue();
                if (vybrana != null) {
                    int potvrzeni = JOptionPane.showConfirmDialog(this, "Opravdu chcete zrušit tuto rezervaci?", "Potvrdit", JOptionPane.YES_NO_OPTION);
                    if (potvrzeni == JOptionPane.YES_OPTION) {
                        RezervaceSpravce.odstranitRezervaci(vybrana);
                        model.removeElement(vybrana);
                        RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
                        JOptionPane.showMessageDialog(this, "Rezervace byla zrušena.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vyberte prosím rezervaci ke zrušení.");
                }
            });
            hlavniPanel.add(btnSmazat, BorderLayout.SOUTH);
        }

        add(hlavniPanel);
    }
}
