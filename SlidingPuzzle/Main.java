
import javax.swing.SwingUtilities;


/**
 * Beschreiben Sie hier die Klasse Main.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Main
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameJFrame();
        });
    }
}