public interface Format {
    String[] getWords(String text);
    Integer[] getWordStartIndexes(String text);
}

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
