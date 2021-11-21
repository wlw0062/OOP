import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public interface Dictionary {
    // 得到语言名称的函数
    String getLanguage();
    // 根据词典内容，返回给定单词列表中拼写错误的单词
    String[] getWrongWords(String[] words);
    // 根据词典内容，返回给定单词列表中拼写错误的单词序号
    Integer[] getWrongIndexes(String[] words);
}

class MockDictionary implements Dictionary {
    private final String[] wordList;

    public MockDictionary(String[] wordList) {
        this.wordList = wordList;
    }

    @Override
    public String getLanguage() {
        return "mock";
    }

    @Override
    public String[] getWrongWords(String[] wordsToCheck) {
        return Common.getWrongWords(wordList, wordsToCheck);
    }

    @Override
    public Integer[] getWrongIndexes(String[] wordsToCheck) {
        return Common.getWrongIndexes(wordList, wordsToCheck);
    }
}

class FileDictionary implements Dictionary {
    private final String filePath;
    private final String language;
    private String[] wordList;

    public FileDictionary(String filePath, String language) {
        this.filePath = filePath;
        this.language = language;
        // 读取路径下的文件，生成词典对应的词表
        readDictionaryFile();
    }

    // 读取路径下的文件，生成词典对应的词表，只在新建dictionary时调用
    private void readDictionaryFile() {
        LinkedList<String> list = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                list.add(line.trim());
            }
            wordList = list.toArray(new String[0]);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public String[] getWordList() {
        return wordList;
    }

    @Override
    public String[] getWrongWords(String[] wordsToCheck) {
        return Common.getWrongWords(wordList, wordsToCheck);
    }

    @Override
    public Integer[] getWrongIndexes(String[] wordsToCheck) {
        return Common.getWrongIndexes(wordList, wordsToCheck);
    }
}
