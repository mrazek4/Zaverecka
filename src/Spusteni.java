import javax.swing.*;

public class Spusteni {

    public void spusteni() {

        SwingUtilities.invokeLater(() -> {
            HlavniOkno okno = new HlavniOkno();

            // === Načti uživatele a rezervace ===
            okno.nacistUzivatele("uzivatele.txt");
            RezervaceSpravce.nacistZeSouboru("rezervace.txt");

            // === Při zavření ulož všechno ===
            okno.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    okno.ulozitUzivatele("uzivatele.txt");
                    RezervaceSpravce.ulozitDoSouboru("rezervace.txt");
                }
            });

            okno.setVisible(true);
        });
    }
}