package ninja.joshdavis;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Launcher for the graphical interface
 *
 */
public class Remoniker {
    private static void createAndShowGUI() {
        AppFrame frame = new AppFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }
}
