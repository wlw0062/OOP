public interface Format {
    // 得到文本中单词列表的函数
    String[] getWords(String text);
    // 得到单词列表中每个单词在文本中开始位置的函数
    Integer[] getWordStartIndexes(String text);
}

// txt格式
class TxtFormat implements Format {
    @Override
    public String[] getWords(String text) {
        return text.split("[ ,.]+");
    }

    @Override
    public Integer[] getWordStartIndexes(String text) {
        return Common.getWordStartIndexes(text,"[ ,.]+");
    }
}

// xml格式
class XmlFormat implements Format {
    @Override
    public String[] getWords(String text) {
        return text.split("([ ,.]|<.*?>)+");
    }

    @Override
    public Integer[] getWordStartIndexes(String text) {
        return Common.getWordStartIndexes(text, "([ ,.]|<.*?>)+");
    }
}
