public class Editor {
    private String text;
    private Format format;
    private Dictionary dictionary;

    public Editor() {
        this.text = "";
        // 默认格式为txt
        this.format = Common.TXT_FORMAT;
        // 默认语言为英语
        this.dictionary = new FileDictionary(Common.ENG_FILE_PATH, "eng");
    }

    public Editor(String text, Format format, Dictionary dictionary) {
        this.text = text;
        this.format = format;
        this.dictionary = dictionary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    // 进行拼写检查，并返回拼写错误的单词列表
    public String[] checkSpell() {
        return dictionary.getWrongWords(format.getWords(text));
    }

    // 进行拼写检查，并返回编辑出拼写错误的文本（不改变编辑器中文本）
    public String checkSpellAndMark() {
        // 得到根据对应格式过滤后的单词列表
        String[] words = format.getWords(text);
        // 每个单词的开始序号
        Integer[] startPositions = format.getWordStartIndexes(text);
        // 单词列表中拼写错误的序号
        Integer[] errorIndexes = dictionary.getWrongIndexes(words);
        StringBuilder stringBuilder = new StringBuilder(text);
        // 从后向前遍历（为了不改变单词开始位置），将错误部分根据单词序号、开始位置、长度进行标记
        for (int i = errorIndexes.length - 1; i >= 0; i--) {
            int index = errorIndexes[i];
            // 如将helo替换为*[helo]
            stringBuilder.replace(startPositions[index], startPositions[index] + words[index].length(), "*[" + words[index] + "]");
        }
        return stringBuilder.toString();
    }

    // 将编辑器中文本修改为删除拼写错误单词后的内容，返回原来的文本（为了能够撤销）
    public String checkSpellAndDelete() {
        String origin = text;
        // 得到根据对应格式过滤后的单词列表
        String[] words = format.getWords(text);
        // 每个单词的开始序号
        Integer[] startPositions = format.getWordStartIndexes(text);
        // 单词列表中拼写错误的序号
        Integer[] errorIndexes = dictionary.getWrongIndexes(words);
        StringBuilder stringBuilder = new StringBuilder(text);
        // 从后向前遍历（为了不改变单词开始位置），将错误部分根据序号、开始位置、长度进行删除
        for (int i = errorIndexes.length - 1; i >= 0; i--) {
            int index = errorIndexes[i];
            // 将拼写错误单词删除
            stringBuilder.replace(startPositions[index], startPositions[index] + words[index].length(), "");
        }
        text = stringBuilder.toString();
        return origin;
    }

    // 将编辑器中文本从头部添加指定内容
    public void addFromHead(String headText) {
        text = headText + text;
    }

    // 将编辑器中文本从尾部添加指定内容
    public void addFromTail(String tailText) {
        text = text + tailText;
    }

    // 从编辑器文本的头部删除指定长度字符串，返回删除的部分
    public String deleteFromHead(int deleteNum) {
        // 若删除长度大于字符串当前长度，将其调整为当前长度
        if(deleteNum > text.length()) {
            deleteNum = text.length();
        }
        String deletedText = text.substring(0, deleteNum);
        text = text.substring(deleteNum);
        return deletedText;
    }

    // 从编辑器文本的尾部删除指定长度字符串，返回删除的部分
    public String deleteFromTail(int deleteNum) {
        // 若删除长度大于字符串当前长度，将其调整为当前长度
        if(deleteNum > text.length()) {
            deleteNum = text.length();
        }
        String deletedText = text.substring(text.length() - deleteNum);
        text = text.substring(0, text.length() - deleteNum);
        return deletedText;
    }
}
