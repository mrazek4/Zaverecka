import javax.swing.*;
import java.awt.*;

public class HlavniOkno extends JFrame {
    public HlavniOkno() {
        setTitle("Rezervace do ordinace");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel nadpis = new JLabel("Vítejte v rezervačním systému", JLabel.CENTER);
        panel.add(nadpis, BorderLayout.NORTH);

        JButton btnRezervovat = new JButton("Rezervovat termín");
        JButton btnZobrazit = new JButton("Zobrazit rezervace");

        JPanel tlacitkaPanel = new JPanel();
        tlacitkaPanel.add(btnRezervovat);
        tlacitkaPanel.add(btnZobrazit);

        panel.add(tlacitkaPanel, BorderLayout.CENTER);

        add(panel);




}}
