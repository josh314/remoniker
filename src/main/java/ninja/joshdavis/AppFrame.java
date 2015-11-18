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
    private JCheckBox globalSearch;
    private JCheckBox caseInsensitiveSearch;
    private JCheckBox regexSearch;
    private JButton alterFiles;


    private class InputListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            //TODO: sanitize inputs
            
            String searchInputString = searchInput.getText();
            String replaceInputString = replaceInput.getText();
            editor.setSearchString(searchInputString);
            editor.setReplaceString(replaceInputString);
            editor.setGlobalSearch(globalSearch.isSelected());
            editor.setCaseInsensitiveSearch(caseInsensitiveSearch.isSelected());
            editor.setLiteralSearch(!regexSearch.isSelected());
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

    private class AlterFilesListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {

        }
    }
    
    private void setCurrentDir(File file) {
        if(file.isDirectory()) {
            currentDir = file;
            updateFilePanes();
        }
    }
    
    private void updateFilePanes() {
        File[] files = currentDir.listFiles();
        String srcText = "";
        String destText = "";
        for(File file: files) {
            if( !file.isHidden() || showHidden.isSelected() ) {
                String srcName = file.getName();
                String destName = editor.edit(srcName);
                if(destName != null && !destName.isEmpty()) {
                    srcText = srcText + srcName + "\n";
                    destText = destText + destName + "\n";
                }
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

        searchInput = new TextField(20);
        replaceInput = new TextField(20);
        
        ActionListener inputListener = new InputListener();
        searchInput.addActionListener(inputListener);
        replaceInput.addActionListener(inputListener);
        add(searchInput);
        add(replaceInput);
        
        showHidden = new JCheckBox("Show hidden files");
        showHidden.addActionListener(inputListener);
        add(showHidden);

        globalSearch = new JCheckBox("Global search");
        globalSearch.addActionListener(inputListener);
        add(globalSearch);

        caseInsensitiveSearch = new JCheckBox("Case insensitive");
        caseInsensitiveSearch.addActionListener(inputListener);
        add(caseInsensitiveSearch);

        regexSearch = new JCheckBox("Use regular expressions");
        regexSearch.addActionListener(inputListener);
        add(regexSearch);

        alterFiles = new JButton("Rename files");
        add(alterFiles);
        ActionListener alterFilesListener = new AlterFilesListener();
        alterFiles.addActionListener(inputListener);
            
        setCurrentDir(new File(System.getProperty("user.home"))); 
    }

}
