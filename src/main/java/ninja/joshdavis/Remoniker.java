package ninja.joshdavis;

import javax.swing.JFrame;

/**
 * Launcher for the graphical interface
 *
 */
public class Remoniker {
    public static void main(String[] args) {
        AppFrame frame = new AppFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640,480);
        frame.setVisible(true);
    }
}
