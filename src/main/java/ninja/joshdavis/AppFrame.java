package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main window manager
 *
 */
public class AppFrame extends JFrame {
    private FileTextArea srcFileTextArea;
    private FileTextArea destFileTextArea;
        
    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());

        srcFileTextArea = new FileTextArea(); 
        add(srcFileTextArea);        
        //TODO: set handlers - update from directory contents        

        destFileTextArea = new FileTextArea(); 
        add(destFileTextArea);        
        //TODO: set handlers - update from directory contents   
    }

}
