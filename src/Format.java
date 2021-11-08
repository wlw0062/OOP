public interface Format {
    String[] getWords(String text);
}

class TxtFormat implements Format {
    @Override
    public String[] getWords(String text) {
        return text.split("[ ,.]+");
    }
}

class XmlFormat implements Format {
    @Override
    public String[] getWords(String text) {
        return text.split("[ ,.]+|<.*?>");
    }
}
