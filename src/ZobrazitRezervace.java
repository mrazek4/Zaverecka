import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ZobrazitRezervace extends JFrame {

    public ZobrazitRezervace() {
        setTitle("Seznam rezervací");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        // Sem načteme všechny rezervace
        ArrayList<Rezervace> rezervaceList = RezervaceSpravce.getSeznamRezervaci();

        if (rezervaceList.isEmpty()) {
            textArea.setText("Žádné rezervace nebyly nalezeny.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Rezervace r : rezervaceList) {
                sb.append(r).append("\n\n");
            }
            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}

