import java.util.Arrays;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class Common {
    public static final Format TXT_FORMAT = new TxtFormat();
    public static final Format XML_FORMAT = new XmlFormat();
    // 手动第二部分case1："./files/手工测试用例/2-拼写检查部分/TestCase01/eng.txt"
    //                 "./files/手工测试用例/2-拼写检查部分/TestCase01/fra.txt"
    // 手动第二部分case2："./files/手工测试用例/2-拼写检查部分/TestCase02/eng.txt"
    //                 "./files/手工测试用例/2-拼写检查部分/TestCase02/fra.txt"
    // 手动第三部分case1："./files/手工测试用例/3-Bonus/TestCase01/eng.txt"
    //                 "./files/手工测试用例/3-Bonus/TestCase01/fra.txt"
    // 自动："./files/自动测试用例/eng.txt"
    //      "./files/自动测试用例/fra.txt"
    public static final String ENG_FILE_PATH = "./files/自动测试用例/eng.txt";
    public static final String FRA_FILE_PATH = "./files/自动测试用例/fra.txt";

    // 存储格式名称-格式处理类映射的格式映射表，作为全局常量被其余类调用
    public static final Map<String, Format> FORMAT_MAP = new HashMap<String, Format>(){{put("txt",TXT_FORMAT);put("xml",XML_FORMAT);}};
    // 存储语言名称-语言词表文件路径映射的语言映射表，作为全局常量被其余类调用
    public static final Map<String, String> FILE_PATH_MAP = new HashMap<String, String>(){{put("eng",ENG_FILE_PATH);put("fra",FRA_FILE_PATH);}};

    // 获取以分隔符（正则表达式）分割的句子中，由每个单词的首位位置组成的数组
    public static Integer[] getWordStartIndexes(String text, String separator) {
        LinkedList<Integer> indexList = new LinkedList<>();
        Pattern pattern = Pattern.compile(separator);
        Matcher matcher = pattern.matcher(text);
        // 即使从第0位其就开始匹配，也将第0位视为单词首位位置（此时单词长度为0），这是因为字符串的split函数会将这部分视为单词
        indexList.add(0);
        while (matcher.find()) {
            // 当找到匹配时，匹配最后一位就是其后一个单词的首位位置（当其长度超过文本长度时，防止字符串读取越界，不将其后一位视作单词首位，同时也与字符串split函数的结果一致）
            if (matcher.end() < text.length()) {
                indexList.add(matcher.end());
            }
        }
        return indexList.toArray(new Integer[0]);
    }

    // 获取给定单词列表中，不符合预先设定词表拼写的单词列表
    public static String[] getWrongWords(String[] correctWords, String[] wordsToCheck) {
        // 将正确单词数组转化为列表格式，方便查找其是否包含某一特定单词
        LinkedList<String> dictionary = new LinkedList<>(Arrays.asList(correctWords));
        // 以列表形式存储无效单词，为了能够修改其长度，进行添加单词操作
        LinkedList<String> invalidList = new LinkedList<>();
        // 对待检验单词遍历，将不存在于正确单词列表且不为空（为了不标记出内容为空的部分）的单词存入无效单词列表
        for (String word : wordsToCheck) {
            if (!dictionary.contains(word) && !word.equals("")) {
                invalidList.add(word);
            }
        }
        return invalidList.toArray(new String[0]);
    }

    // 获取给定单词列表中，不符合预先设定词表拼写的单词序号，方便标记出错误单词
    public static Integer[] getWrongIndexes(String[] correctWords, String[] wordsToCheck) {
        // 将正确单词数组转化为列表格式，方便查找其是否包含某一特定单词
        LinkedList<String> dictionary = new LinkedList<>(Arrays.asList(correctWords));
        // 以列表形式存储无效单词，为了能够修改其长度，进行添加单词操作
        LinkedList<Integer> indexList = new LinkedList<>();
        // 对待检验单词遍历，将不存在于正确单词列表且不为空（为了不标记出内容为空的部分）的单词序号存入无效序号列表
        for (int i = 0; i < wordsToCheck.length; i++) {
            if (!dictionary.contains(wordsToCheck[i]) && !wordsToCheck[i].equals("")) {
                indexList.add(i);
            }
        }
        return indexList.toArray(new Integer[0]);
    }
}
