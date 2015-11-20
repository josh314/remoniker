package ninja.joshdavis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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
    private TextField dirInput;
    private File currentDir;
    private JCheckBox showHidden;
    private JCheckBox globalSearch;
    private JCheckBox caseInsensitiveSearch;
    private JCheckBox regexSearch;
    private JButton alterFiles;
    private LinkedList<FileRenamePair> renameList;

    private class FileRenamePair {
        public File srcFile;
        public File destFile;
        public FileRenamePair(File src, File dest) {
            srcFile = src;
            destFile = dest;
        }
    }
    
    private class OptionsPanel extends JPanel {
        private ActionListener listener;

        public OptionsPanel(ActionListener aListener) {
            super(new GridLayout(4,1));
            listener = aListener;
            setBorder(BorderFactory.createTitledBorder("Options"));
        }
        
        public JCheckBox addNewOption(String title) {
            JCheckBox res = new JCheckBox(title);
            res.addActionListener(listener);
            add(res);
            return res;
        }
    }
    
    private class InputListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            updateEditor();
            updateFilePanes();
        }
    }

    private class AlterFilesListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            int confirmOption = JOptionPane.showConfirmDialog(null, "Confirm file modifications?", "Are you sure?", JOptionPane.OK_CANCEL_OPTION);
            if(confirmOption == JOptionPane.OK_OPTION) {
                for(FileRenamePair pair: renameList) {
                    pair.srcFile.renameTo(pair.destFile);
                }
                searchInput.setText("");
                replaceInput.setText("");
                updateEditor();
                updateFilePanes();
            }
        }
    }

    private class DirChooserListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            dirChooser.setFileHidingEnabled(!showHidden.isSelected());
            dirChooser.showDialog(AppFrame.this, null);
            File file = dirChooser.getSelectedFile();
            setCurrentDir(file);
        }
    }

    private class DirInputListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            File file = new File(dirInput.getText());
            setCurrentDir(file);
        }
    }

    private void setCurrentDir(File file) {
        if(file == null) {
            return;
        }
        else if(file.exists() && file.isDirectory()) {
            currentDir = file;
            dirInput.setText(file.getAbsolutePath());
            updateFilePanes();
        }
        else {
            JOptionPane.showMessageDialog(null, file.getAbsolutePath()+ " is not a valid directory.", "File error", JOptionPane.ERROR_MESSAGE);
            dirInput.setText(currentDir.getAbsolutePath());
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
        renameList.clear();
        File[] allFiles = currentDir.listFiles();
        for(File srcFile: allFiles) {
            if( !srcFile.isHidden() || showHidden.isSelected() ) {
                String destName = editor.edit(srcFile.getName());
                if(destName != null && !destName.isEmpty()) {
                    File destFile = new File(currentDir,destName);
                    renameList.add(new FileRenamePair(srcFile,destFile));
                }
            }
        }
    }

    private void updateFilePanes() {
        updateRenameMap();
        String srcText = "";
        String destText = "";
        for(FileRenamePair pair: renameList) {
            srcText = srcText + pair.srcFile.getName() + "\n";
            destText = destText + pair.destFile.getName() + "\n";
        }
        srcFileListPane.setText(srcText);
        destFileListPane.setText(destText);
    }

    public AppFrame() {
        setTitle("Remoniker");
        setLayout(new FlowLayout());
        /* File panes */
        srcFileListPane = new FileListPane();
        addWithTitledBorder(srcFileListPane, "Files");

        destFileListPane = new FileListPane();
        addWithTitledBorder(destFileListPane, "Preview");

        /* Editor */
        editor = new Editor();

        /* Directory choosing pane */
        JPanel dirPane = new JPanel();
        dirPane.setBorder(BorderFactory.createTitledBorder("Current directory"));
        add(dirPane);

        dirInput = new TextField(20);
        dirPane.add(dirInput);
        ActionListener dirInputListener = new DirInputListener();
        dirInput.addActionListener(dirInputListener);
        
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

        /* Options panel */
        OptionsPanel options = new OptionsPanel(inputListener);
        showHidden =  options.addNewOption("Show hidden files");
        globalSearch = options.addNewOption("Global search");
        caseInsensitiveSearch = options.addNewOption("Case insensitive");
        regexSearch = options.addNewOption("Use regular expressions");
        add(options);

        /* Rename action button */
        alterFiles = new JButton("Rename files");
        add(alterFiles);
        ActionListener alterFilesListener = new AlterFilesListener();
        alterFiles.addActionListener(alterFilesListener);

        // Init rename map
        renameList = new LinkedList<FileRenamePair>();

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
