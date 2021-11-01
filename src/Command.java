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
        System.out.println("当前编辑的字符串已重置为：" + initText);
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
        System.out.println("当前编辑的字符串为：" + editor.getText());
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
        this.application.listModifyCommand(listNum);
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
        this.application.undoModifyCommand();
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
        this.application.redoModifyCommand();
    }
}
