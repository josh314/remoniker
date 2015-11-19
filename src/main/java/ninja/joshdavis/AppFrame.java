package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
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
    private LinkedHashMap<File,File> renameMap;


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
            dirChooser.setFileHidingEnabled(!showHidden.isSelected());
            dirChooser.showDialog(AppFrame.this, null);
            File file = dirChooser.getSelectedFile();
            if(file != null) {
                setCurrentDir(file);
            }
        }
    }

    private class AlterFilesListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            //TODO: Are you sure?
            for(Entry<File,File> entry: renameMap.entrySet()) {
                entry.getKey().renameTo(entry.getValue());
            }
        }
    }

    private void setCurrentDir(File file) {
        if(file.isDirectory()) {
            currentDir = file;
            updateFilePanes();
        }
    }

    private void updateRenameMap() {
        renameMap.clear();
        File[] allFiles = currentDir.listFiles();
        for(File srcFile: allFiles) {
            if( !srcFile.isHidden() || showHidden.isSelected() ) {
                String destName = editor.edit(srcFile.getName());
                if(destName != null && !destName.isEmpty()) {
                    File destFile = new File(currentDir,destName);
                    renameMap.put(srcFile,destFile);   
                }
            }
        }
    }

    private void updateFilePanes() {
        updateRenameMap();
        String srcText = "";
        String destText = "";
        for(Entry<File,File> entry: renameMap.entrySet()) {
            srcText = srcText + entry.getKey().getName() + "\n";
            destText = destText + entry.getValue().getName() + "\n";
        }
        srcFileListPane.setText(srcText);
        destFileListPane.setText(destText);
    }
    
    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());

        srcFileListPane = new FileListPane();
        addWithTitledBorder(srcFileListPane, "Files");
        //TODO: set handlers - update from directory contents        

        destFileListPane = new FileListPane(); 
        addWithTitledBorder(destFileListPane, "Preview");        
        //TODO: set handlers - update from directory contents   

        editor = new Editor();
        
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        ActionListener dirChooserListener = new DirChooserListener();
        dirBrowseButton = new JButton("Browse");
        dirBrowseButton.addActionListener(dirChooserListener);
        add(dirBrowseButton);

        searchInput = new TextField(20);
        replaceInput = new TextField(20);
        
        ActionListener inputListener = new InputListener();
        searchInput.addActionListener(inputListener);
        replaceInput.addActionListener(inputListener);
        addWithTitledBorder(searchInput,"Search");
        addWithTitledBorder(replaceInput,"Replace");
        
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
        alterFiles.addActionListener(alterFilesListener);

        renameMap = new LinkedHashMap<File,File>();
        
        setCurrentDir(new File(System.getProperty("user.home"))); 
    }

    private void addWithTitledBorder(Component comp, String title) {
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.add(comp);
        add(pane);
    }
    
}
