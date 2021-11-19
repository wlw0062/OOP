import java.util.LinkedList;

public interface Revocable extends Command, Cloneable {
    void undo();
    Object clone();
}

// 在尾部添加字符串的命令（修改类命令）
class AddFromTailCommand implements Revocable {
    private final Editor editor;
    private final String tailText;

    public AddFromTailCommand(Application application, String tailText) {
        this.editor = application.getEditor();
        this.tailText = tailText;
    }

    @Override
    public void execute() {
        editor.addFromTail(tailText);
    }

    @Override
    public void undo() {
        editor.deleteFromTail(tailText.length());
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
    public String toString() {
        return "A \"" + tailText + "\"";
    }
}

// 在头部添加字符串的命令（修改类命令）
class AddFromHeadCommand implements Revocable {
    private final Editor editor;
    private final String headText;

    public AddFromHeadCommand(Application application, String headText) {
        this.editor = application.getEditor();
        this.headText = headText;
    }

    @Override
    public void execute() {
        editor.addFromHead(headText);
    }

    @Override
    public void undo() {
        editor.deleteFromHead(headText.length());
    }

    @Override
    public Object clone() {
        AddFromHeadCommand command = null;
        try {
            command = (AddFromHeadCommand)super.clone();
        } catch (CloneNotSupportedException e) {
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
class DeleteFromTailCommand implements Revocable {
    private final Editor editor;
    private final int deleteNum;
    private String deletedText;

    public DeleteFromTailCommand(Application application, int deleteNum) {
        this.editor = application.getEditor();
        this.deleteNum = deleteNum;
    }

    @Override
    public void execute() {
        deletedText = editor.deleteFromTail(deleteNum);
    }

    @Override
    public void undo() {
        editor.addFromTail(deletedText);
    }

    @Override
    public Object clone() {
        DeleteFromTailCommand command = null;
        try{
            command = (DeleteFromTailCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString() {
        return "D " + deleteNum;
    }
}

// 从头部删除指定数量的字符的命令（修改类命令）
class DeleteFromHeadCommand implements Revocable {
    private final Editor editor;
    private final int deleteNum;
    private String deletedText;

    public DeleteFromHeadCommand(Application application, int deleteNum) {
        this.editor = application.getEditor();
        this.deleteNum = deleteNum;
    }

    @Override
    public void execute() {
        deletedText = editor.deleteFromHead(deleteNum);
    }

    @Override
    public void undo() {
        editor.addFromHead(deletedText);
    }

    @Override
    public Object clone() {
        DeleteFromHeadCommand command = null;
        try {
            command = (DeleteFromHeadCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString() {
        return "d " + deleteNum;
    }
}

// 由最近几次执行的修改类命令组成的宏命令（修改类命令）
class MacroCommand implements Revocable {
    private final String macroCommandName;
    private final LinkedList<Revocable> modifyCommands;

    public MacroCommand(String macroCommandName, LinkedList<Revocable> modifyCommands) {
        this.macroCommandName = macroCommandName;
        this.modifyCommands = modifyCommands;
    }

    public LinkedList<Revocable> getModifyCommands() {
        return modifyCommands;
    }

    @Override
    public void execute() {
        for (Revocable modifyCommand : modifyCommands) {
            modifyCommand.execute();
        }
    }

    @Override
    public void undo() {
        LinkedList<Revocable> reverseList = new LinkedList<>();
        for (Revocable modifyCommand : modifyCommands) {
            reverseList.addFirst(modifyCommand);
        }
        for (Revocable modifyCommand : reverseList) {
            modifyCommand.undo();
        }
    }

    @Override
    public Object clone() {
        MacroCommand command = null;
        try {
            command = (MacroCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString() {
        return macroCommandName;
    }
}

// 删除拼写错误单词的命令（修改类命令）
class CheckSpellAndDeleteCommand implements Revocable {
    private final Editor editor;
    private String originText;

    public CheckSpellAndDeleteCommand(Application application) {
        this.editor = application.getEditor();
    }

    @Override
    public void execute() {
        originText = editor.checkSpellAndDelete();
    }

    @Override
    public void undo() {
        editor.setText(originText);
    }

    @Override
    public Object clone() {
        CheckSpellAndDeleteCommand command = null;
        try {
            command = (CheckSpellAndDeleteCommand) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return command;
    }

    @Override
    public String toString() {
        return "spell-m";
    }
}
