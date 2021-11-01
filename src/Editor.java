public class Editor {
    private String text;

    public Editor(){
        this.text = "";
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public void addFromHead(String headText){
        this.text = headText + this.text;
    }

    public void addFromTail(String tailText){
        this.text = this.text + tailText;
    }

    public String deleteFromHead(int deleteNum){
        if(deleteNum > this.text.length()){
            deleteNum = this.text.length();
        }
        String deletedText = this.text.substring(0, deleteNum);
        this.text = this.text.substring(deleteNum);
        return deletedText;
    }

    public String deleteFromTail(int deleteNum){
        if(deleteNum > this.text.length()){
            deleteNum = this.text.length();
        }
        String deletedText = this.text.substring(this.text.length() - deleteNum);
        this.text = this.text.substring(0, this.text.length() - deleteNum);
        return deletedText;
    }
}
