import javax.swing.*;


/**
 * Trida zodpovedna za inicializaci a spusteni GUI aplikace.
 */
public class Spusteni {
    /**
     * Spousti hlavni okno aplikace a zajistuje nacteni dat.
     */
    public void spusteni() {//chatGPT

        SwingUtilities.invokeLater(() -> {
            HlavniOkno okno = new HlavniOkno();

            //Nacti uzivatele a rezervace
            okno.nacistUzivatele("uzivatele.txt");
            RezervaceSpravce.nacistZeSouboru("rezervace.txt");

            //Pri zavreni uloz vsechno
            okno.addWindowListener(new java.awt.event.WindowAdapter() { //automaticky se vsechno uklada pri zavreni
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