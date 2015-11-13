package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SrcTextArea extends JTextArea {
    public SrcTextArea() {
        super();
        setEditable(false);
        setColumns(10);
        setRows(10);
        setText("I am Jack's filler text.");
    }
}
