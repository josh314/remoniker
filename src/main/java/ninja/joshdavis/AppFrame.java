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
    private TextField currentDirInput;
    private JCheckBox showHidden;
    private JCheckBox globalSearch;
    private JCheckBox caseInsensitiveSearch;
    private JCheckBox regexSearch;
    private JButton alterFiles;
    private LinkedHashMap<File,File> renameMap;

    private class InputListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            updateEditor();
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
            int confirmOption = JOptionPane.showConfirmDialog(null, "Confirm file modifications?", "Are you sure?", JOptionPane.OK_CANCEL_OPTION);
            if(confirmOption == JOptionPane.OK_OPTION) {
                for(Entry<File,File> entry: renameMap.entrySet()) {
                    entry.getKey().renameTo(entry.getValue());
                }
                searchInput.setText("");
                replaceInput.setText("");
                updateEditor();
                updateFilePanes();
            }
        }
    }

    private void updateEditor() {
        String searchInputString = searchInput.getText();
        String replaceInputString = replaceInput.getText();
        //TODO: sanitize inputs
        editor.setSearchString(searchInputString);
        editor.setReplaceString(replaceInputString);
        editor.setGlobalSearch(globalSearch.isSelected());
        editor.setCaseInsensitiveSearch(caseInsensitiveSearch.isSelected());
        editor.setLiteralSearch(!regexSearch.isSelected());
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

    private void setCurrentDir(File file) {
        if(file.exists() && file.isDirectory()) {
            currentDir = file;
            currentDirInput.setText(file.getAbsolutePath());
            updateFilePanes();
        }
    }

    public AppFrame() {
        super("Remoniker");
        setLayout(new FlowLayout());
        /* File panes */
        srcFileListPane = new FileListPane();
        addWithTitledBorder(srcFileListPane, "Files");

        destFileListPane = new FileListPane(); 
        addWithTitledBorder(destFileListPane, "Preview");        

        /* Editor */
        editor = new Editor();

        /* Directory pane */
        JPanel dirPane = new JPanel();
        dirPane.setBorder(BorderFactory.createTitledBorder("Current directory"));
        add(dirPane);

        currentDirInput = new TextField(20);
        dirPane.add(currentDirInput);
        
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        ActionListener dirChooserListener = new DirChooserListener();
        dirBrowseButton = new JButton("Browse");
        dirBrowseButton.addActionListener(dirChooserListener);
        dirPane.add(dirBrowseButton);

        /* Search & replace fields */
        ActionListener inputListener = new InputListener();
        searchInput = addNewTextField("Search", inputListener);
        replaceInput = addNewTextField("Replace", inputListener);

        /* Options */
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

        /* Rename action button */
        alterFiles = new JButton("Rename files");
        add(alterFiles);
        ActionListener alterFilesListener = new AlterFilesListener();
        alterFiles.addActionListener(alterFilesListener);

        // Init rename map
        renameMap = new LinkedHashMap<File,File>();

        // Set initial directory
        setCurrentDir(new File(System.getProperty("user.home"))); 
    }

    private void addWithTitledBorder(Component comp, String title) {
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(title));
        pane.add(comp);
        add(pane);
    }

    private TextField addNewTextField(String title, ActionListener listener) {
        TextField res = new TextField(20);
        res.addActionListener(listener);
        addWithTitledBorder(res,title);
        return res;
    }
    
}
