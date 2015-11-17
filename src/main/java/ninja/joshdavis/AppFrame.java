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
    private FileListPane srcFileListPane;
    private FileListPane destFileListPane;
    private SearchInput searchInput;
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
        srcFileListPane.setText(srcText);

        String destText = "";
        for(String file: contents) {
            String newName = editor.edit(file);
            if(newName != null && !newName.isEmpty()) {
                destText = destText + newName + "\n";
            }
        }
        destFileListPane.setText(destText);
    }
    
    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());

        srcFileListPane = new FileListPane();
        add(srcFileListPane);
        //TODO: set handlers - update from directory contents        

        destFileListPane = new FileListPane(); 
        add(destFileListPane);        
        //TODO: set handlers - update from directory contents   

        searchInput = new SearchInput("");
        
        //
        editor = new Editor();

        setDirectory("/");
    }

}
