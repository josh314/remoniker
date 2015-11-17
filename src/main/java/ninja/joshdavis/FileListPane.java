package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class FileListPane extends JScrollPane{
    private JTextArea textArea;

    public FileListPane() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(10);
        textArea.setRows(10);

        setViewportView(textArea);
    }

    public void setText(String s) {
        textArea.setText(s);
    }
 }
