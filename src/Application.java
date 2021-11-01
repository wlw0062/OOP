import java.util.LinkedList;

public class Application {
    private final Editor editor;
    private LinkedList<Revocable> modifyCommandHistory;
    private LinkedList<Revocable> undoCommandHistory;

    public Application(){
        this.editor = new Editor();
        this.modifyCommandHistory = new LinkedList<>();
        this.undoCommandHistory = new LinkedList<>();
    }

    public Editor getEditor(){
        return this.editor;
    }

    public void resetEditorText(String initText){
        this.editor.setText(initText);
        this.modifyCommandHistory = new LinkedList<>();
        this.undoCommandHistory = new LinkedList<>();
    }

    public void listModifyCommand(int listNum){
        int length = this.modifyCommandHistory.size();
        if(listNum > length){
            listNum = length;
        }
        System.out.println("最近执行的" + listNum + "条修改类命令分别为: (最近执行的排在前面)");
        for(int i = 0; i < listNum; i++)
        {
            System.out.println((i + 1) + ". " + this.modifyCommandHistory.get(length - (i + 1)).toString());
        }
    }

    public void executeNormalCommand(Command normalCommand){
        normalCommand.execute();
    }

    public void executeModifyCommand(Revocable modifyCommand){
        modifyCommand.execute();
        this.modifyCommandHistory.push(modifyCommand);
        this.undoCommandHistory = new LinkedList<>();
    }

    public void undoModifyCommand(){
        if (modifyCommandHistory.isEmpty()){
            System.out.println("当前暂无可撤销的操作（修改类命令）");
            return;
        }
        Revocable undoneCommand = this.modifyCommandHistory.pop();
        undoneCommand.undo();
        this.undoCommandHistory.push(undoneCommand);
        System.out.println("已撤销最近一次的操作（修改类命令）：" + undoneCommand.toString());
    }

    public void redoModifyCommand(){
        if (undoCommandHistory.isEmpty()){
            System.out.println("当前暂无可重做的已撤销操作（修改类命令）");
            return;
        }
        Revocable redoneCommand = this.undoCommandHistory.pop();
        redoneCommand.execute();
        this.modifyCommandHistory.push(redoneCommand);
        System.out.println("已重做最近一次撤销的操作（修改类命令）：" + redoneCommand.toString());
    }
}
