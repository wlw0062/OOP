public class Editor {
    private String text;
    private Format format;
    private Dictionary dictionary;

    public Editor() {
        this.text = "";
        this.format = Common.TXT_FORMAT;
        this.dictionary = new FileDictionary(Common.ENG_FILE_PATH);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String[] checkSpell() {
        return dictionary.getWrongWords(format.getWords(text));
    }

    public String checkSpellAndMark() {
        String[] words = format.getWords(text);
        Integer[] startPositions = format.getWordStartIndexes(text);
        Integer[] errorIndexes = dictionary.getWrongIndexes(words);
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = errorIndexes.length - 1; i >= 0; i--) {
            int index = errorIndexes[i];
            stringBuilder.replace(startPositions[index], startPositions[index] + words[index].length(), "*[" + words[index] + "]");
        }
        return stringBuilder.toString();
    }

    // 在text中删除拼写错误的单词，返回原来的text
    public String checkSpellAndDelete() {
        String origin = text;
        String[] words = format.getWords(text);
        Integer[] startPositions = format.getWordStartIndexes(text);
        Integer[] errorIndexes = dictionary.getWrongIndexes(words);
        StringBuilder stringBuilder = new StringBuilder(text);
        for (int i = errorIndexes.length - 1; i >= 0; i--) {
            int index = errorIndexes[i];
            stringBuilder.replace(startPositions[index], startPositions[index] + words[index].length(), "");
        }
        text = stringBuilder.toString();
        return origin;
    }

    public void addFromHead(String headText) {
        text = headText + text;
    }

    public void addFromTail(String tailText) {
        text = text + tailText;
    }

    public String deleteFromHead(int deleteNum) {
        if(deleteNum > text.length()) {
            deleteNum = text.length();
        }
        String deletedText = text.substring(0, deleteNum);
        text = text.substring(deleteNum);
        return deletedText;
    }

    public String deleteFromTail(int deleteNum) {
        if(deleteNum > text.length()) {
            deleteNum = text.length();
        }
        String deletedText = text.substring(text.length() - deleteNum);
        text = text.substring(0, text.length() - deleteNum);
        return deletedText;
    }
}
