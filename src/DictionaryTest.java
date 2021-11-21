import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockDictionaryTest {
    @Test
    void getWrongWords() {
        String[] wordList = { "red", "blue", "yellow" };
        MockDictionary mockDictionary = new MockDictionary(wordList);
        String[] checkList = { "yellow", "green", "purple" };
        String[] result = mockDictionary.getWrongWords(checkList);
        String[] expect = { "green", "purple" };
        // 检验是否成功返回给定词表中拼写错误的单词
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongIndexes() {
        String[] wordList = { "red", "blue", "yellow" };
        MockDictionary mockDictionary = new MockDictionary(wordList);
        String[] checkList = { "yellow", "green", "purple" };
        Integer[] result = mockDictionary.getWrongIndexes(checkList);
        Integer[] expect = { 1, 2 };
        // 检验是否成功返回给定词表中拼写错误的单词序号
        assertArrayEquals(expect, result);
    }
}

class FileDictionaryTest {
    @Test
    void getWordList() {
        String path = "./files/手工测试用例/3-Bonus/TestCase01/eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path, "eng");
        String[] result = fileDictionary.getWordList();
        String[] expect = { "The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog" ,"title" };
        // 检验新建词典类时是否正确读取文件内容
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongWords() {
        String path = "./files/手工测试用例/3-Bonus/TestCase01/eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path, "eng");
        String[] checkList = { "tha", "lazy", "faz" };
        String[] result = fileDictionary.getWrongWords(checkList);
        String[] expect = { "tha", "faz" };
        // 检验是否成功返回给定词表中拼写错误的单词
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongIndexes() {
        String path = "./files/手工测试用例/3-Bonus/TestCase01/eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path, "eng");
        String[] checkList = { "tha", "lazy", "faz" };
        Integer[] result = fileDictionary.getWrongIndexes(checkList);
        Integer[] expect = { 0, 2 };
        // 检验是否成功返回给定词表中拼写错误的单词序号
        assertArrayEquals(expect, result);
    }
}
