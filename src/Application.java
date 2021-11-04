import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Application {
    private final Editor editor;
    private LinkedList<Revocable> modifyCommandHistory;
    private LinkedList<Revocable> undoCommandHistory;
    private final Map<String, MacroCommand> macroCommandMap;

    public Application(){
        this.editor = new Editor();
        this.modifyCommandHistory = new LinkedList<>();
        this.undoCommandHistory = new LinkedList<>();
        this.macroCommandMap = new HashMap<>();
    }

    public Editor getEditor(){
        return editor;
    }

    public void resetEditorText(String initText){
        editor.setText(initText);
        modifyCommandHistory = new LinkedList<>();
        undoCommandHistory = new LinkedList<>();
    }

    public void listModifyCommand(int listNum){
        int length = modifyCommandHistory.size();
        if(listNum > length){
            listNum = length;
        }
        for(int i = 0; i < listNum; i++)
        {
            System.out.println((i+1) + " " + modifyCommandHistory.get(i).toString());
        }
    }

    public void executeNormalCommand(Command normalCommand){
        normalCommand.execute();
    }

    public void executeModifyCommand(Revocable modifyCommand, ShowCommand echo){
        modifyCommand.execute();
        modifyCommandHistory.push(modifyCommand);
        undoCommandHistory = new LinkedList<>();
        echo.execute();
    }

    public void undoModifyCommand(){
        if (modifyCommandHistory.isEmpty()){
            return;
        }
        Revocable undoneCommand = modifyCommandHistory.pop();
        undoneCommand.undo();
        undoCommandHistory.push(undoneCommand);
    }

    public void redoModifyCommand(){
        if (undoCommandHistory.isEmpty()){
            return;
        }
        Revocable redoneCommand = undoCommandHistory.pop();
        redoneCommand.execute();
        modifyCommandHistory.push(redoneCommand);
    }

    public void addToMacroCommandMap(MacroCommand macroCommand){
        macroCommandMap.put(macroCommand.toString(), macroCommand);
    }

    public MacroCommand findMacroCommand(String macroCommandName){
        if (macroCommandMap.containsKey(macroCommandName)) {
            return macroCommandMap.get(macroCommandName);
        }
        return null;
    }

    public LinkedList<Revocable> getReversedModifyCommandHistory(int num){
        int length = modifyCommandHistory.size();
        if (num > length){
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
