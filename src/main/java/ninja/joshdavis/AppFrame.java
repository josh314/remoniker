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
    private Editor editor;
    private TextField searchInput;
    private TextField replaceInput;

    private File currentDir;

    private class InputListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            //TODO: sanitize inputs
            
            String searchInputString = searchInput.getText();
            String replaceInputString = replaceInput.getText();
            editor.setSearchString(searchInputString);
            editor.setReplaceString(replaceInputString);
            updateFilePanes();
        }
    }
    
    private void setCurrentDir(String filename) {
        File file = new File(filename);
        if(file.isDirectory()) {
            currentDir = file;
        }
        updateFilePanes();
    }
    
    private void updateFilePanes() {
        String[] contents = currentDir.list();
        String srcText = "";
        String destText = "";
        for(String file: contents) {
            String newName = editor.edit(file);
            if(newName != null && !newName.isEmpty()) {
                srcText = srcText + file + "\n";
                destText = destText + newName + "\n";
            }
        }
        srcFileListPane.setText(srcText);
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
        editor = new Editor();

        setCurrentDir("/");
        
        searchInput = new TextField(20);
        replaceInput = new TextField(20);
        ActionListener inputListener = new InputListener();
        searchInput.addActionListener(inputListener);
        replaceInput.addActionListener(inputListener);
        add(searchInput);
        add(replaceInput);
    }

}
