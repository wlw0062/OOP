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

    // 设置编辑器中文字，并清空修改类命令历史
    public void resetEditorText(String initText) {
        editor.setText(initText);
        modifyCommandHistory = new LinkedList<>();
        undoCommandHistory = new LinkedList<>();
    }

    // 列出最近执行的修改类命令（新执行的序号小）
    public String[] listModifyCommand(int listNum) {
        int length = modifyCommandHistory.size();
        // 列出长度不能超出历史总长度
        if(listNum > length) {
            listNum = length;
        }
        String[] commandNames = new String[listNum];
        // 命令历史中原本就是新执行的在前，可以顺序读取
        for(int i = 0; i < listNum; i++){
            commandNames[i] = modifyCommandHistory.get(i).toString();
        }
        return commandNames;
    }

    // 执行非修改类命令，直接调用其执行函数
    public void executeNormalCommand(Command normalCommand) {
        normalCommand.execute();
    }

    // 执行修改类命令
    public void executeModifyCommand(Revocable modifyCommand, ShowCommand echo) {
        // 先调用其执行函数
        modifyCommand.execute();
        // 再将其放入执行修改类命令历史的最前面
        modifyCommandHistory.push(modifyCommand);
        // 此时原本undo过的修改类操作已经无法重做，清空undo列表
        undoCommandHistory = new LinkedList<>();
        // 执行显示字符串命令，回显修改后的文本内容
        echo.execute();
    }

    // 撤销最近执行的一条修改类命令
    public void undoModifyCommand() {
        // 如没有可以撤销的命令，则直接返回
        if (modifyCommandHistory.isEmpty()) {
            return;
        }
        // 取出执行历史中最前面（最近执行）的一条命令
        Revocable undoneCommand = modifyCommandHistory.pop();
        // 执行该命令的undo方法
        undoneCommand.undo();
        // 将该命令放到undo历史的最前面
        undoCommandHistory.push(undoneCommand);
    }

    // 重做最近一次撤销的修改类命令
    public void redoModifyCommand() {
        // 若已撤销命令历史为空，直接返回
        if (undoCommandHistory.isEmpty()) {
            return;
        }
        // 取出最近一次撤销的命令（列表最前面）
        Revocable redoneCommand = undoCommandHistory.pop();
        // 调用其执行函数
        redoneCommand.execute();
        // 将其放入执行历史记录的最前方（最近执行）
        modifyCommandHistory.push(redoneCommand);
    }

    // 以宏命令名称-宏命令的键值对的形式将参数的宏命令存入application的宏命令映射表中
    public void addToMacroCommandMap(MacroCommand macroCommand) {
        macroCommandMap.put(macroCommand.getMacroCommandName(), macroCommand);
    }

    // 根据宏命令名称查找映射表中有无对应宏命令，有则返回该命令，否则返回null
    public MacroCommand findMacroCommand(String macroCommandName) {
        if (macroCommandMap.containsKey(macroCommandName)) {
            return macroCommandMap.get(macroCommandName);
        }
        return null;
    }

    // 以反序得到最近n次执行的命令
    public LinkedList<Revocable> getReversedModifyCommandHistory(int num) {
        int length = modifyCommandHistory.size();
        // 列表长度不能超过命令历史的长度
        if (num > length) {
            num = length;
        }
        // 在命令历史中截取需要长度的命令列表（新执行的在前）
        LinkedList<Revocable> sublist=new LinkedList<>(modifyCommandHistory.subList(0, num));
        LinkedList<Revocable> reverse=new LinkedList<>();
        // 将列表以相反顺序存入新列表中（需要得到新执行命令在后的列表）
        for (Revocable command: sublist) {
            reverse.addFirst((Revocable) command.clone());
        }
        return reverse;
    }
}
