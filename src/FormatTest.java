import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TxtFormatTest {
    @Test
    void getWords() {
        TxtFormat txtFormat = new TxtFormat();
        String text = "Hi, world.";
        String[] result = txtFormat.getWords(text);
        String[] expect = { "Hi", "world" };
        assertArrayEquals(expect, result);
    }

    @Test
    void getWordStartIndexes() {
        TxtFormat txtFormat = new TxtFormat();
        String text = "Hi, world.";
        Integer[] result = txtFormat.getWordStartIndexes(text);
        Integer[] expect = { 0, 4 };
        assertArrayEquals(expect, result);
    }
}

class XmlFormatTest {
    @Test
    void getWords() {
        XmlFormat xmlFormat = new XmlFormat();
        String text = "Hi,<a><b>world.</b></a>";
        String[] result = xmlFormat.getWords(text);
        String[] expect = { "Hi", "world" };
        assertArrayEquals(expect, result);
    }

    @Test
    void getWordStartIndexes() {
        XmlFormat xmlFormat = new XmlFormat();
        String text = "Hi,<a><b>world.</b></a>";
        Integer[] result = xmlFormat.getWordStartIndexes(text);
        Integer[] expect = { 0, 9 };
        assertArrayEquals(expect, result);
    }
}
