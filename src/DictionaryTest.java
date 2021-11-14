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
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongIndexes() {
        String[] wordList = { "red", "blue", "yellow" };
        MockDictionary mockDictionary = new MockDictionary(wordList);
        String[] checkList = { "yellow", "green", "purple" };
        Integer[] result = mockDictionary.getWrongIndexes(checkList);
        Integer[] expect = { 1, 2 };
        assertArrayEquals(expect, result);
    }
}

class FileDictionaryTest {
    @Test
    void getWordList() {
        String path = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\3-Bonus\\TestCase01\\eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path);
        String[] result = fileDictionary.getWordList();
        String[] expect = { "The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog" ,"title" };
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongWords() {
        String path = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\3-Bonus\\TestCase01\\eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path);
        String[] checkList = { "tha", "lazy", "faz" };
        String[] result = fileDictionary.getWrongWords(checkList);
        String[] expect = { "tha", "faz" };
        assertArrayEquals(expect, result);
    }

    @Test
    void getWrongIndexes() {
        String path = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\3-Bonus\\TestCase01\\eng.txt";
        FileDictionary fileDictionary = new FileDictionary(path);
        String[] checkList = { "tha", "lazy", "faz" };
        Integer[] result = fileDictionary.getWrongIndexes(checkList);
        Integer[] expect = { 0, 2 };
        assertArrayEquals(expect, result);
    }
}
