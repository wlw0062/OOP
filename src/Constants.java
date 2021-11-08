import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final Format TXT_FORMAT = new TxtFormat();
    public static final Format XML_FORMAT = new XmlFormat();

    public static final String ENG_FILE_PATH = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\2-拼写检查部分\\TestCase02\\eng.txt";
    public static final String FRA_FILE_PATH = "D:\\学习\\7\\面向对象\\OOP_Lab1\\files\\手工测试用例\\2-拼写检查部分\\TestCase02\\fra.txt";

    public static final Map<String, Format> FORMAT_MAP = new HashMap<String, Format>(){{put("txt",TXT_FORMAT);put("xml",XML_FORMAT);}};
    public static final Map<String, String> FILE_PATH_MAP = new HashMap<String, String>(){{put("eng",ENG_FILE_PATH);put("fra",FRA_FILE_PATH);}};
}
