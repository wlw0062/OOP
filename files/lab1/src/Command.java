import java.util.LinkedList;

public interface Command{
    void execute();
}

class ShowCommand implements Command //显示当前编辑的字符串
{
    Editor editor;

    public ShowCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute()
    {
        System.out.println(editor.toString());
    }
}

class AddLastCommand implements Redoable //在尾部添加字符串（修改类命令）
{
    Editor editor;
    String string;

    public AddLastCommand(Editor editor,String string) {
        this.editor = editor;
        this.string = string;
    }

    @Override
    public String toString() {
        return "A \"" + string+"\"";
    }

    @Override
    public void execute()
    {
        editor.addLast(string);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        editor.delLast(string.length());
    }

    @Override
    public Object clone() {
        AddLastCommand cmd = null;
        try{
            cmd = (AddLastCommand)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cmd;
    }
}

class AddFirstCommand implements Redoable
{
    Editor editor;
    String string;

    public AddFirstCommand(Editor editor, String string) {
        this.editor = editor;
        this.string = string;
    }

    @Override
    public String toString() {
        return "a \"" + string+"\"";
    }

    @Override
    public void execute()
    {
        editor.addFirst(string);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        editor.delFirst(string.length());
    }

    @Override
    public Object clone() {
        AddFirstCommand cmd = null;
        try{
            cmd = (AddFirstCommand)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cmd;
    }
}

class DelFirstCommand implements Redoable
{
    Editor editor;
    int charNum;
    String deleted;

    public DelFirstCommand(Editor editor, int charNum) {
        this.editor = editor;
        this.charNum = charNum;
    }

    @Override
    public String toString() {
        return "d " + charNum;
    }

    @Override
    public void execute()
    {
        this.deleted=editor.delFirst(charNum);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        editor.addFirst(deleted);
    }

    @Override
    public Object clone() {
        DelFirstCommand cmd = null;
        try{
            cmd = (DelFirstCommand)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cmd;
    }
}

class DelLastCommand implements Redoable
{
    Editor editor;
    int charNum;
    String deleted;

    public DelLastCommand(Editor editor, int charNum) {
        this.editor = editor;
        this.charNum = charNum;
    }

    @Override
    public String toString() {
        return "D " + charNum;
    }

    @Override
    public void execute()
    {
        this.deleted=editor.delLast(charNum);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        editor.addLast(deleted);
    }

    @Override
    public Object clone() {
        DelLastCommand cmd = null;
        try{
            cmd = (DelLastCommand)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cmd;
    }
}

class ListModifyCommand implements Command
{
    Editor editor;
    int num;

    public ListModifyCommand(Editor editor, int num) {
        this.editor = editor;
        this.num=num;
    }

    @Override
    public void execute()
    {
        editor.listCommand(num);
    }
}

class UndoCommand implements Command
{
    Editor editor;

    public UndoCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        Redoable command=editor.popFromModifyCommandHistory();
        command.undo();
        editor.pushIntoUndoCommandHistory(command);
    }
}

class RedoCommand implements Command
{
    Editor editor;

    public RedoCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() {
        Redoable command=editor.popFromUndoCommandHistory();
        command.redo();
        editor.pushIntoModifyCommandHistory(command);
    }
}

class DefineMacroCommand implements Command //将最近执行的修改类命令组成一个修改类宏命令
{
    Editor editor;
    int commandNum;
    String macroCommandName;

    public DefineMacroCommand(Editor editor, int commandNum, String macroCommandName) {
        this.editor = editor;
        this.commandNum = commandNum;
        this.macroCommandName = macroCommandName;
    }

    @Override
    public void execute()
    {
        MacroCommand macroCommand=new MacroCommand(editor,editor.getSubListCommand(commandNum),macroCommandName);
        editor.addToMacroCommands(macroCommand);
    }
}

class MacroCommand implements Redoable //宏命令
{
    Editor editor;
    LinkedList<Redoable> commands;
    String MacroCommandName;

    public MacroCommand(Editor editor,LinkedList<Redoable> commands, String MacroCommandName) {
        this.editor=editor;
        this.commands = commands;
        this.MacroCommandName=MacroCommandName;
    }

    @Override
    public void execute() {
        for (Redoable command:
             commands) {
            command.execute();
        }
    }

    @Override
    public void undo() {
        LinkedList<Redoable> reverse=new LinkedList<>();
        for (Redoable command:
                commands) {
            reverse.addFirst(command);
        }
        for(Redoable command:reverse)
        {
            command.undo();
        }
    }

    @Override
    public void redo() {
        for (Redoable command:
                commands) {
            command.redo();
        }
    }

    @Override
    public String toString() {
        return MacroCommandName;
    }

    @Override
    public Object clone() {
        MacroCommand cmd = null;
        try{
            cmd = (MacroCommand)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cmd;
    }

    public String getMacroCommandName()
    {
        return this.MacroCommandName;
    }
}
