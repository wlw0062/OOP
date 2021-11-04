import java.util.LinkedList;

public interface Command {
    void execute();
}

class SetTextCommand implements Command{
    private final Application application;
    private final String initText;

    public SetTextCommand(Application application, String initText){
        this.application = application;
        this.initText = initText;
    }

    @Override
    public void execute(){
        application.resetEditorText(initText);
    }
}

//显示当前编辑的字符串的命令
class ShowCommand implements Command{
    private final Editor editor;

    public ShowCommand(Application application){
        this.editor = application.getEditor();
    }

    @Override
    public void execute() {
        System.out.println(editor.getText());
    }
}

// 列出最近执行的修改类命令列表的命令
class ListModifyCommand implements Command{
    private final Application application;
    private final int listNum;

    public ListModifyCommand(Application application, int listNum){
        this.application = application;
        this.listNum = listNum;
    }

    @Override
    public void execute() {
        application.listModifyCommand(listNum);
    }
}

// 取消上一步操作（修改类命令）的命令
class UndoCommand implements Command{
    private final Application application;

    public UndoCommand(Application application){
        this.application = application;
    }

    @Override
    public void execute() {
        application.undoModifyCommand();
    }
}

// 重做上一步undo取消的操作（修改类命令）的命令
class RedoCommand implements Command{
    private final Application application;

    public RedoCommand(Application application){
        this.application = application;
    }

    @Override
    public void execute() {
        application.redoModifyCommand();
    }
}

// 将最近执行的n个修改类命令组成一个宏命令（修改类命令）的命令
class DefineMacroCommand implements Command{
    private final Application application;
    private final int commandNum;
    private final String commandName;

    public DefineMacroCommand(Application application, int commandNum, String commandName){
        this.application = application;
        this.commandNum = commandNum;
        this.commandName = commandName;
    }

    @Override
    public void execute() {
        LinkedList<Revocable> modifyCommands = application.getReversedModifyCommandHistory(commandNum);
        MacroCommand macroCommand = new MacroCommand(commandName, modifyCommands);
        application.addToMacroCommandMap(macroCommand);
    }
}