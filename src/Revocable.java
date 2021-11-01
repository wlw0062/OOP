public interface Revocable extends Command, Cloneable{
    void undo();
    Object clone();
}

// 在尾部添加字符串的命令（修改类命令）
class AddFromTailCommand implements Revocable{
    private final Editor editor;
    private final String tailText;

    public AddFromTailCommand(Application application, String tailText){
        this.editor = application.getEditor();
        this.tailText = tailText;
    }

    @Override
    public void execute() {
        this.editor.addFromTail(tailText);
    }

    @Override
    public void undo() {
        this.editor.deleteFromTail(tailText.length());
    }

    @Override
    public Object clone() {
        AddFromTailCommand command = null;
        try{
            command = (AddFromTailCommand)super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString(){
        return "A \"" + tailText + "\"";
    }
}

// 在头部添加字符串的命令（修改类命令）
class AddFromHeadCommand implements Revocable{
    private final Editor editor;
    private final String headText;

    public AddFromHeadCommand(Application application, String headText){
        this.editor = application.getEditor();
        this.headText = headText;
    }

    @Override
    public void execute() {
        this.editor.addFromHead(headText);
    }

    @Override
    public void undo() {
        this.editor.deleteFromHead(headText.length());
    }

    @Override
    public Object clone() {
        AddFromHeadCommand command = null;
        try{
            command = (AddFromHeadCommand)super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString(){
        return "a \"" + headText + "\"";
    }
}

// 从尾部删除指定数量的字符的命令（修改类命令）
class DeleteFromTailCommand implements Revocable{
    private final Editor editor;
    private final int deleteNum;
    private String deletedText;

    public DeleteFromTailCommand(Application application, int deleteNum){
        this.editor = application.getEditor();
        this.deleteNum = deleteNum;
    }

    @Override
    public void execute(){
        this.deletedText = this.editor.deleteFromTail(deleteNum);
    }

    @Override
    public void undo(){
        this.editor.addFromTail(deletedText);
    }

    @Override
    public Object clone(){
        DeleteFromTailCommand command = null;
        try{
            command = (DeleteFromTailCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString(){
        return "D " + deleteNum;
    }
}

// 从头部删除指定数量的字符的命令（修改类命令）
class DeleteFromHeadCommand implements Revocable{
    private final Editor editor;
    private final int deleteNum;
    private String deletedText;

    public DeleteFromHeadCommand(Application application, int deleteNum){
        this.editor = application.getEditor();
        this.deleteNum = deleteNum;
    }

    @Override
    public void execute(){
        this.deletedText = this.editor.deleteFromHead(deleteNum);
    }

    @Override
    public void undo(){
        this.editor.addFromHead(deletedText);
    }

    @Override
    public Object clone(){
        DeleteFromHeadCommand command = null;
        try{
            command = (DeleteFromHeadCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString(){
        return "d " + deleteNum;
    }
}
