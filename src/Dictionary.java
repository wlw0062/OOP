import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public interface Dictionary {
    String[] getWrongWords(String[] words);
    Integer[] getWrongIndexes(String[] words);
}

class MockDictionary implements Dictionary {
    private final String[] wordList;

    public MockDictionary(String[] wordList) {
        this.wordList = wordList;
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
    private String[] wordList;

    public FileDictionary(String filePath) {
        this.filePath = filePath;
        readDictionaryFile();
    }

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
