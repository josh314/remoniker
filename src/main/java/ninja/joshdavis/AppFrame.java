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
    private JButton dirBrowseButton;
    private JFileChooser dirChooser;
    private File currentDir;
    private JCheckBox showHidden;

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

    private class DirChooserListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            dirChooser.showDialog(AppFrame.this, null);
            File file = dirChooser.getSelectedFile();
            if( file != null) {
                setCurrentDir(file);
            }
        }
    }
    
    private void setCurrentDir(File file) {
        if(file.isDirectory()) {
            currentDir = file;
            updateFilePanes();
        }
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
        
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setFileHidingEnabled(false);

        ActionListener dirChooserListener = new DirChooserListener();
        dirBrowseButton = new JButton("Browse");
        dirBrowseButton.addActionListener(dirChooserListener);
        add(dirBrowseButton);

        setCurrentDir(new File(System.getProperty("user.home")));
        
        searchInput = new TextField(20);
        replaceInput = new TextField(20);
        ActionListener inputListener = new InputListener();
        searchInput.addActionListener(inputListener);
        replaceInput.addActionListener(inputListener);
        add(searchInput);
        add(replaceInput);

        showHidden = new JCheckBox("Show hidden files");
        add(showHidden);
    }

}
