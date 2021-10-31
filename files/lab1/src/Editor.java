import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Editor {
    private StringBuffer content;
    //新的在list前面
    private LinkedList<Redoable> ModifyCommandHistory;
    private LinkedList<Redoable> UndoCommandHistory;
    private Map<String,MacroCommand> MacroCommands;

    public Editor() {
        this.content = new StringBuffer();
        this.ModifyCommandHistory=new LinkedList<>();
        this.UndoCommandHistory=new LinkedList<>();
        this.MacroCommands=new HashMap<>();
    }

    public Editor(String content) {
        this.content = new StringBuffer(content);
    }

    @Override
    public String toString() {
        return content.toString();
    }

    void addFirst(String string)
    {
        content.insert(0,string);
    }

    void addLast(String string)
    {
        content.append(string);
    }

    String delFirst(int charNum)//返回背截掉的部分
    {
       if(charNum>content.length())
       {
           charNum=content.length();
       }
       String deleted=content.substring(0,charNum);
       content=new StringBuffer(content.substring(charNum));
       return deleted;
    }

    String delLast(int charNum)
    {
        if(charNum>content.length())
        {
            charNum=content.length();
        }
        String deleted=content.substring(content.length()-charNum);
        content =new StringBuffer(content.substring(0,content.length()-charNum));
        return deleted;
    }

    Redoable popFromModifyCommandHistory()
    {
        return ModifyCommandHistory.pop();
    }

    void pushIntoModifyCommandHistory(Redoable command)
    {
        ModifyCommandHistory.push(command);
    }

    Redoable popFromUndoCommandHistory()
    {
        return UndoCommandHistory.pop();
    }

    void pushIntoUndoCommandHistory(Redoable command)
    {
        UndoCommandHistory.push(command);
    }

    void listCommand(int num)
    {
        for(int i=0;i<num;i++)
        {
            System.out.println((i+1)+" "+ModifyCommandHistory.get(i).toString());
        }
    }

    LinkedList<Redoable> getSubListCommand(int num)//新的在后面
    {
        LinkedList<Redoable> sublist=new LinkedList<>(ModifyCommandHistory.subList(0, num));
        LinkedList<Redoable> reverse=new LinkedList<>();
        for (Redoable command:
                sublist) {
            reverse.addFirst((Redoable) command.clone());
        }
        return reverse;
    }

    void addToMacroCommands (MacroCommand macroCommand)
    {
        this.MacroCommands.put(macroCommand.getMacroCommandName(),macroCommand);
    }

    MacroCommand findMacroCommand (String commandName)
    {
        return MacroCommands.get(commandName);
    }


}


