import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ZobrazitRezervace extends JFrame {

    public ZobrazitRezervace(Uzivatel prihlasenyUzivatel, boolean jeAdmin) {
        setTitle("Seznam rezervací");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        ArrayList<Rezervace> rezervaceList = RezervaceSpravce.getSeznamRezervaci();
        StringBuilder sb = new StringBuilder();

        if (rezervaceList.isEmpty()) {
            sb.append("Žádné rezervace nebyly nalezeny.");
        } else {
            for (Rezervace r : rezervaceList) {
                // Pokud je admin, vypíše vše
                // Pokud je pacient, vypíše jen jeho rezervace (podle e-mailu)
                if (jeAdmin || (prihlasenyUzivatel != null && r.getEmail().equals(prihlasenyUzivatel.getEmail()))) {
                    sb.append(r).append("\n\n");
                }
            }

            if (sb.length() == 0) {
                sb.append("Nemáte žádné rezervace.");
            }
        }

        textArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}