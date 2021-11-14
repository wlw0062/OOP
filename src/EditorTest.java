import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EditorTest {
    @Test
    void checkSpell() {
        String[] wordList = { "red", "blue", "yellow" };
        Dictionary dictionary = new MockDictionary(wordList);
        Editor editor = new Editor("red green yellow purple", Common.TXT_FORMAT, dictionary);
        String[] result = editor.checkSpell();
        String[] expect = { "green", "purple" };
        assertArrayEquals(expect, result);
    }

    @Test
    void checkSpellAndMark() {
        String[] wordList = { "red", "blue", "yellow" };
        Dictionary dictionary = new MockDictionary(wordList);
        Editor editor = new Editor("red green yellow purple", Common.TXT_FORMAT, dictionary);
        String result = editor.checkSpellAndMark();
        String expect = "red *[green] yellow *[purple]";
        assertEquals(expect, result);
        assertEquals("red green yellow purple", editor.getText());
    }

    @Test
    void checkSpellAndDelete() {
        String[] wordList = { "red", "blue", "yellow" };
        Dictionary dictionary = new MockDictionary(wordList);
        Editor editor = new Editor("red green yellow purple", Common.TXT_FORMAT, dictionary);
        // 设置editor内的text为删除错词后的内容，返回editor原来的text
        String result = editor.checkSpellAndDelete();
        String expect = "red  yellow ";
        assertEquals("red green yellow purple", result);
        assertEquals(expect, editor.getText());
    }

    @Test
    void addFromHead() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("blue", Common.TXT_FORMAT, dictionary);
        editor.addFromHead("red ");
        assertEquals("red blue", editor.getText());
    }

    @Test
    void addFromTail() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("blue", Common.TXT_FORMAT, dictionary);
        editor.addFromTail(" green");
        assertEquals("blue green", editor.getText());
    }

    @Test
    void deleteFromHead() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("red blue", Common.TXT_FORMAT, dictionary);
        String deleted = editor.deleteFromHead(4);
        assertEquals("blue", editor.getText());
        assertEquals("red ", deleted);
    }

    @Test
    void deleteFromTail() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("red blue", Common.TXT_FORMAT, dictionary);
        String deleted = editor.deleteFromTail(4);
        assertEquals("red ", editor.getText());
        assertEquals("blue", deleted);
    }
}
