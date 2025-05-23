import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
/**
 * Okno pro zobrazeni statistik o rezervacich.
 */
public class StatistikaOkno extends JFrame {
    /**
     * Vytvori okno se statistikou rezervaci.
     */
    public StatistikaOkno() {
        setTitle("Statistika rezervací");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ArrayList<Rezervace> seznam = RezervaceSpravce.getSeznamRezervaci();
        int celkem = seznam.size();
        int zrusene = 0;

        EnumMap<TypNavstevy, Integer> poTypu = new EnumMap<>(TypNavstevy.class);
        for (TypNavstevy t : TypNavstevy.values()) {
            poTypu.put(t, 0);
        }

        for (Rezervace r : seznam) {
            if (r.isZrusena()) zrusene++;
            TypNavstevy typ = r.getTypNavstevy();
            poTypu.put(typ, poTypu.get(typ) + 1);
        }

        JTextArea vystup = new JTextArea();
        vystup.setEditable(false);
        vystup.append("📊 STATISTIKA\n\n");
        vystup.append("Celkový počet rezervací: " + celkem + "\n");
        vystup.append("Zrušené rezervace: " + zrusene + "\n\n");
        vystup.append("Rozdělení podle typu:\n");

        for (TypNavstevy typ : TypNavstevy.values()) {
            vystup.append("- " + typ.getPopis() + ": " + poTypu.get(typ) + "\n");
        }

        add(new JScrollPane(vystup), BorderLayout.CENTER);
    }
}
