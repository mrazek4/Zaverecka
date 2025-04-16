import javax.swing.*;

public class Spusteni {

    public void spusteni() {
        SwingUtilities.invokeLater(() -> {
            new HlavniOkno().setVisible(true);
            new RezervaceFormular().setVisible(true);
        });
    }
}
