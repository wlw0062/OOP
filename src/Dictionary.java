import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public interface Dictionary {
    String[] getWrongWords(String[] words);
}

class MockDictionary implements Dictionary {
    private String[] wordList;

    public MockDictionary(String[] wordList) {
        this.wordList = wordList;
    }

    public String[] getWordList() {
        return wordList;
    }

    public void setWordList(String[] wordList) {
        this.wordList = wordList;
    }

    @Override
    public String[] getWrongWords(String[] words) {
        LinkedList<String> invalidList = new LinkedList<>();
        for (String word : words) {
            boolean flag = false;
            for (String valid : wordList) {
                if (word.equals(valid)) {
                    flag = true;
                    break;
                }
            }
            if (!flag && !word.equals("")) {
                invalidList.add(word);
            }
        }
        return invalidList.toArray(new String[0]);
    }
}

class FileDictionary implements Dictionary {
    private LinkedList<String> wordList;
    private String filePath;

    public FileDictionary(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String[] getWordList() {
        readDictionaryFile();
        return wordList.toArray(new String[0]);
    }

    private void readDictionaryFile() {
        wordList = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                wordList.add(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getWrongWords(String[] wordsToCheck) {
        readDictionaryFile();
        LinkedList<String> invalidList = new LinkedList<>();
        for (String word : wordsToCheck) {
            if (!wordList.contains(word) && !word.equals("")) {
                invalidList.add(word);
            }
        }
        return invalidList.toArray(new String[0]);
    }
}
