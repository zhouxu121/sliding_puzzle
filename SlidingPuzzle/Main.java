
import javax.swing.SwingUtilities;

/**
 * Application entry point for the sliding puzzle game.
 * 
 * SwingUtilities.invokeLater(...) schedules the GUI creation on Swing's
 * Event Dispatch Thread(EDT). Swing components should be created and updated
 * on this thread to keep GUI operations thread-safe.
 */
public class Main
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameJFrame();
        });
    }
}
