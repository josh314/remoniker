package ninja.joshdavis;

import java.util.regex.*;

public class Editor {
    private boolean globalSearch;
    private boolean caseInsensitiveSearch;
    private boolean literalSearch;
    private String searchString;
    private Pattern searchPattern;
    private String replaceString;

    public Editor() {
        //Options defaults
        globalSearch = false;
        caseInsensitiveSearch = false;
        literalSearch = true;

        //Empty initial search and replace strings
        
    }

    private void updatePattern() {
        int searchFlags = 0;
        if(caseInsensitiveSearch) {
            searchFlags |= Pattern.CASE_INSENSITIVE;
        }
        if(literalSearch) {
            searchFlags |= Pattern.LITERAL;
        }
        
        searchPattern = Pattern.compile(searchString, searchFlags);
    }

    public void setSearchString(String str) {
        searchString = str;
        updatePattern();
    }

    public void setReplaceString(String str) {
        replaceString = str;
    }

    public void setGlobalSearch(boolean val) {
        globalSearch = val;
    }

    public void setCaseInsensitiveSearch(boolean val) {
        caseInsensitiveSearch = val;
        updatePattern();
    }

    public void setLiteralSearch(boolean val) {
        literalSearch = val;
        updatePattern();
    }
    
    public String edit(String srcText) {
        Matcher m = searchPattern.matcher(srcText);
        String res;
        if(!m.find()) {
            res = "";
        }
        else if(globalSearch) {
            res = m.replaceAll(replaceString);
        }
        else {
            res = m.replaceFirst(replaceString);
        }
        return res;
    }
}
