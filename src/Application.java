import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Application {
    private final Editor editor;
    // 执行修改命令的历史
    private LinkedList<Revocable> modifyCommandHistory;
    // 撤销修改命令的历史
    private LinkedList<Revocable> undoCommandHistory;
    // 系统中宏命令名称与内容的Map
    private final Map<String, MacroCommand> macroCommandMap;

    public Application() {
        this.editor = new Editor();
        this.modifyCommandHistory = new LinkedList<>();
        this.undoCommandHistory = new LinkedList<>();
        this.macroCommandMap = new HashMap<>();
    }

    public Editor getEditor() {
        return editor;
    }

    public LinkedList<Revocable> getModifyCommandHistory() {
        return modifyCommandHistory;
    }

    public LinkedList<Revocable> getUndoCommandHistory() {
        return undoCommandHistory;
    }

    public Map<String, MacroCommand> getMacroCommandMap() {
        return macroCommandMap;
    }

    public void resetEditorText(String initText) {
        editor.setText(initText);
        modifyCommandHistory = new LinkedList<>();
        undoCommandHistory = new LinkedList<>();
    }

    public String[] listModifyCommand(int listNum) {
        int length = modifyCommandHistory.size();
        if(listNum > length) {
            listNum = length;
        }
        String[] commandNames = new String[listNum];
        for(int i = 0; i < listNum; i++){
            commandNames[i] = modifyCommandHistory.get(i).toString();
        }
        return commandNames;
    }

    public void executeNormalCommand(Command normalCommand) {
        normalCommand.execute();
    }

    public void executeModifyCommand(Revocable modifyCommand, ShowCommand echo) {
        modifyCommand.execute();
        // 将最近执行的放到最前面
        modifyCommandHistory.push(modifyCommand);
        undoCommandHistory = new LinkedList<>();
        echo.execute();
    }

    public void undoModifyCommand() {
        if (modifyCommandHistory.isEmpty()) {
            return;
        }
        Revocable undoneCommand = modifyCommandHistory.pop();
        undoneCommand.undo();
        undoCommandHistory.push(undoneCommand);
    }

    public void redoModifyCommand() {
        if (undoCommandHistory.isEmpty()) {
            return;
        }
        Revocable redoneCommand = undoCommandHistory.pop();
        redoneCommand.execute();
        modifyCommandHistory.push(redoneCommand);
    }

    public void addToMacroCommandMap(MacroCommand macroCommand) {
        macroCommandMap.put(macroCommand.toString(), macroCommand);
    }

    public MacroCommand findMacroCommand(String macroCommandName) {
        if (macroCommandMap.containsKey(macroCommandName)) {
            return macroCommandMap.get(macroCommandName);
        }
        return null;
    }

    public LinkedList<Revocable> getReversedModifyCommandHistory(int num) {
        int length = modifyCommandHistory.size();
        if (num > length) {
            num = length;
        }
        LinkedList<Revocable> sublist=new LinkedList<>(modifyCommandHistory.subList(0, num));
        LinkedList<Revocable> reverse=new LinkedList<>();
        for (Revocable command: sublist) {
            reverse.addFirst((Revocable) command.clone());
        }
        return reverse;
    }
}
