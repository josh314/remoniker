package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class FileListPane extends JTextArea {
    private textArea JTextArea;

    public FileListPane() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(10);
        textArea.setRows(10);
        
    }
    
    public FileTextArea() {
        super();
        setEditable(false);
        setColumns(10);
        setRows(10);
        setText("I am Jack's filler text.");
    }
}
