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
        // 检验是否成功返回所有拼写错误的单词列表
        assertArrayEquals(expect, result);
    }

    @Test
    void checkSpellAndMark() {
        String[] wordList = { "red", "blue", "yellow" };
        Dictionary dictionary = new MockDictionary(wordList);
        Editor editor = new Editor("red green yellow purple", Common.TXT_FORMAT, dictionary);
        String result = editor.checkSpellAndMark();
        String expect = "red *[green] yellow *[purple]";
        // 检查是否输出标记所有拼写错误单词的内容，并且没有改变编辑器中文本
        assertEquals(expect, result);
        assertEquals("red green yellow purple", editor.getText());
    }

    @Test
    void checkSpellAndDelete() {
        String[] wordList = { "red", "blue", "yellow" };
        Dictionary dictionary = new MockDictionary(wordList);
        Editor editor = new Editor("red green yellow purple", Common.TXT_FORMAT, dictionary);
        // 该函数设置editor内的text为删除错词后的内容，返回editor原来的text
        String result = editor.checkSpellAndDelete();
        String expect = "red  yellow ";
        // 检验是否输出删除了所有拼写错误的文本，并且同时修改了编辑器中的文本、返回原本的文本
        assertEquals("red green yellow purple", result);
        assertEquals(expect, editor.getText());
    }

    @Test
    void addFromHead() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("blue", Common.TXT_FORMAT, dictionary);
        editor.addFromHead("red ");
        // 检验是否将编辑器中文本成功从头部添加指定字符串
        assertEquals("red blue", editor.getText());
    }

    @Test
    void addFromTail() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("blue", Common.TXT_FORMAT, dictionary);
        editor.addFromTail(" green");
        // 检验是否将编辑器中文本成功从尾部添加指定字符串
        assertEquals("blue green", editor.getText());
    }

    @Test
    void deleteFromHead() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("red blue", Common.TXT_FORMAT, dictionary);
        String deleted = editor.deleteFromHead(4);
        // 检验编辑器中文本是否是从头部删除了指定长度的内容，函数是否返回删除部分
        assertEquals("blue", editor.getText());
        assertEquals("red ", deleted);
    }

    @Test
    void deleteFromTail() {
        Dictionary dictionary = new MockDictionary(new String[0]);
        Editor editor = new Editor("red blue", Common.TXT_FORMAT, dictionary);
        String deleted = editor.deleteFromTail(4);
        // 检验编辑器中文本是否是从尾部删除了指定长度的内容，函数是否返回删除部分
        assertEquals("red ", editor.getText());
        assertEquals("blue", deleted);
    }
}
