package ninja.joshdavis;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for App.
 */
public class AppTest {
    @Test
    public void AppFrameTest() {
        AppFrame frame = new AppFrame();
        assertNotNull(frame);
    }
    @Test
    public void FileTextAreaTest() {
        FileTextArea srcFileTextArea= new FileTextArea();
        assertNotNull(srcFileTextArea);
    }
    @Test
    public void EditorTest() {
        Editor mrEd = new Editor();
        assertNotNull(mrEd);
        mrEd.setSearchString("foo");
        mrEd.setReplaceString("baz");

        String str = mrEd.edit("foobarfoo");
        assertEquals("bazbarfoo", str);

        mrEd.setGlobalSearch(true);
        str = mrEd.edit("foobarfoo");
        assertEquals("bazbarbaz", str);

        mrEd.setCaseInsensitiveSearch(true);
        str = mrEd.edit("FoobarfOo");
        assertEquals("bazbarbaz", str);

        mrEd.setCaseInsensitiveSearch(false);
        mrEd.setLiteralSearch(false);
        mrEd.setSearchString("f.o");
        str = mrEd.edit("foobarfao");
        assertEquals("bazbarbaz", str);

        mrEd.setLiteralSearch(true);
        mrEd.setSearchString("foo");
        mrEd.setReplaceString("\\$1");
        str = mrEd.edit("foobarfoo");
        assertEquals("$1bar$1", str);
        
        mrEd.setLiteralSearch(false);
        mrEd.setSearchString("([0-9]+)foo([0-9]+)");
        
        mrEd.setReplaceString("$1bar$2");
        str = mrEd.edit("123foo456");
        assertEquals("123bar456", str);

        mrEd.setSearchString("([0-9]+)foo([0-9]+)");
        mrEd.setReplaceString("$1bar\\$2");//Must escape $ and \ to get literal
        str = mrEd.edit("123foo456");
        assertEquals("123bar$2", str);
    }
}
