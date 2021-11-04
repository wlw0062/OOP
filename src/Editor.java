public class Editor {
    private String text;

    public Editor(){
        this.text = "";
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public void addFromHead(String headText){
        text = headText + text;
    }

    public void addFromTail(String tailText){
        text = text + tailText;
    }

    public String deleteFromHead(int deleteNum){
        if(deleteNum > text.length()){
            deleteNum = text.length();
        }
        String deletedText = text.substring(0, deleteNum);
        text = text.substring(deleteNum);
        return deletedText;
    }

    public String deleteFromTail(int deleteNum){
        if(deleteNum > text.length()){
            deleteNum = text.length();
        }
        String deletedText = text.substring(text.length() - deleteNum);
        text = text.substring(0, text.length() - deleteNum);
        return deletedText;
    }
}
