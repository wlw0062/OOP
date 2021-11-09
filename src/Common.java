import java.util.Arrays;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Common {
    public static final Format TXT_FORMAT = new TxtFormat();
    public static final Format XML_FORMAT = new XmlFormat();

    public static final String ENG_FILE_PATH = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\2-拼写检查部分\\TestCase02\\eng.txt";
    public static final String FRA_FILE_PATH = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\2-拼写检查部分\\TestCase02\\fra.txt";

    public static final Map<String, Format> FORMAT_MAP = new HashMap<String, Format>(){{put("txt",TXT_FORMAT);put("xml",XML_FORMAT);}};
    public static final Map<String, String> FILE_PATH_MAP = new HashMap<String, String>(){{put("eng",ENG_FILE_PATH);put("fra",FRA_FILE_PATH);}};

    // 获取以分隔符（正则表达式）分割的句子中，由每个单词的首位位置组成的数组
    public static Integer[] getWordStartIndexes(String text, String separator) {
        LinkedList<Integer> indexList = new LinkedList<>();
        Pattern pattern = Pattern.compile(separator);
        Matcher matcher = pattern.matcher(text);
        indexList.add(0);
        while (matcher.find()) {
            if (matcher.end() < text.length()) {
                indexList.add(matcher.end());
            }
        }
        return indexList.toArray(new Integer[0]);
    }

    // 获取给定单词列表中，不符合预先设定词表拼写的单词列表
    public static String[] getWrongWords(String[] correctWords, String[] wordsToCheck) {
        LinkedList<String> dictionary = new LinkedList<>(Arrays.asList(correctWords));
        LinkedList<String> invalidList = new LinkedList<>();
        for (String word : wordsToCheck) {
            if (!dictionary.contains(word) && !word.equals("")) {
                invalidList.add(word);
            }
        }
        return invalidList.toArray(new String[0]);
    }

    // 获取给定单词列表中，不符合预先设定词表拼写的单词序号
    public static Integer[] getWrongIndexes(String[] correctWords, String[] wordsToCheck) {
        LinkedList<String> dictionary = new LinkedList<>(Arrays.asList(correctWords));
        LinkedList<Integer> indexList = new LinkedList<>();
        for (int i = 0; i < wordsToCheck.length; i++) {
            if (!dictionary.contains(wordsToCheck[i]) && !wordsToCheck[i].equals("")) {
                indexList.add(i);
            }
        }
        return indexList.toArray(new Integer[0]);
    }
}
