package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
/**
 * Main window manager
 *
 */
public class AppFrame extends JFrame {
    private FileTextArea srcFileTextArea;
    private FileTextArea destFileTextArea;
    private String listingsText;
    private String searchString;
    private String replaceString;
    private Editor editor;
        
    public void setDirectory(String dirName) {
        File dir = new File(dirName);
        /*
        if(!dir.isDirectory()) {
            throw new IOException("File is not a directory.");
        }
        */
        String[] contents = dir.list();
        String srcText = "";
        for(String file: contents) {
            srcText = srcText + file + "\n";//Portable line ending?
        }
        srcFileTextArea.setText(srcText);

        String destText = "";
        for(String file: contents) {
            String newName = editor.edit(file);
            if(newName != null && !newName.isEmpty()) {
                destText = destText + newName + "\n";
            }
        }
        destFileTextArea.setText(destText);
    }
    
    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());

        srcFileTextArea = new FileTextArea();
        add(srcFileTextArea);
        //TODO: set handlers - update from directory contents        

        destFileTextArea = new FileTextArea(); 
        add(destFileTextArea);        
        //TODO: set handlers - update from directory contents   

        //
        editor = new Editor();
        editor.setSearchString("e");
        editor.setReplaceString("E");
        editor.setGlobalSearch(true);

        setDirectory("/");
    }

}
