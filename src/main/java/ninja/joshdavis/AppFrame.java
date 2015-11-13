package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main window manager
 *
 */
public class AppFrame extends JFrame {
    private SrcTextArea srcTextArea;
        
    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());

        srcTextArea = new SrcTextArea(); 
        add(srcTextArea);        
        //TODO: set handlers - update from directory contents        

    }

}
