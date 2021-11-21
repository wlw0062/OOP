import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommonTest {
    @Test
    void getWordStartIndexes() {
        String text = "red yellow green purple blue";
        String separator = " ";
        Integer[] result = Common.getWordStartIndexes(text, separator);
        Integer[] expect = { 0, 4, 11, 17, 24 };
        // 检验是否正确得到每个单词的开头位置
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongWords() {
        String[] dictionary = { "red", "blue", "yellow" };
        String[] checkList = { "yellow", "green", "purple" };
        String[] result = Common.getWrongWords(dictionary, checkList);
        String[] expect = { "green", "purple" };
        // 检验是否返回所有错误单词的数组
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongIndexes() {
        String[] dictionary = { "red", "blue", "yellow" };
        String[] checkList = { "yellow", "green", "purple" };
        Integer[] result = Common.getWrongIndexes(dictionary, checkList);
        Integer[] expect = { 1, 2 };
        // 检验是否返回所有错误单词序号的数组
        assertArrayEquals(expect, result);
    }
}
