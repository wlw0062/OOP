import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Invoker {
    private final Editor editor;
    private final ShowCommand echo; //用于执行完命令后回显出当前文本内容

    public Invoker(Editor editor) {
        this.editor = editor;
        this.echo=new ShowCommand(this.editor);
    }

    public void takeCommand(String input)
    {
        char commandName=input.charAt(0);
        switch (commandName) {
            case 's' -> takeShowCommand();
            case 'A' -> takeAddLastCommand(input.substring(3,input.length()-1));
            case 'a' -> takeAddFirstCommand(input.substring(3,input.length()-1));
            case 'D' -> takeDelLastCommand(Integer.parseInt(input.substring(2)));
            case 'd' -> takeDelFirstCommand(Integer.parseInt(input.substring(2)));
            case 'l' -> takeListModifyCommand(Integer.parseInt(input.substring(2)));
            case 'u' -> takeUndoCommand();
            case 'r' -> takeRedoCommand();
            case 'm' -> {int index=input.indexOf(" ",2);takeDefineMacroCommand(Integer.parseInt(input.substring(2,index)), input.substring(index+1));}
            case '$' -> takeMacroCommand(input.replaceFirst("\\$", ""));
            default -> System.out.println("Invalid command.");
        }
    }

    public void takeShowCommand()
    {
        ShowCommand command=new ShowCommand(editor);
        command.execute();
    }

    public void takeAddLastCommand(String string)
    {
        AddLastCommand command=new AddLastCommand(editor, string);
        editor.pushIntoModifyCommandHistory(command);
        command.execute();
        echo.execute();
    }

    public void takeAddFirstCommand(String string)
    {
        AddFirstCommand command=new AddFirstCommand(editor, string);
        editor.pushIntoModifyCommandHistory(command);
        command.execute();
        echo.execute();

    }

    public void takeDelLastCommand(int charNum)
    {
        DelLastCommand command=new DelLastCommand(editor,charNum);
        editor.pushIntoModifyCommandHistory(command);
        command.execute();
        echo.execute();

    }

    public void takeDelFirstCommand(int charNum)
    {
        DelFirstCommand command=new DelFirstCommand(editor,charNum);
        editor.pushIntoModifyCommandHistory(command);
        command.execute();
        echo.execute();

    }

    public void takeListModifyCommand(int num)
    {
        ListModifyCommand command=new ListModifyCommand(editor,num);
        command.execute();
    }

    public void takeUndoCommand()
    {
        UndoCommand command=new UndoCommand(editor);
        command.execute();
        echo.execute();
    }

    public void takeRedoCommand()
    {
        RedoCommand command=new RedoCommand(editor);
        command.execute();
        echo.execute();

    }

    public void takeDefineMacroCommand(int commandNum,String commandName)
    {
        DefineMacroCommand command=new DefineMacroCommand(editor,commandNum,commandName);
        command.execute();
    }

    public void takeMacroCommand(String commandName)
    {
        Redoable macroCommand=editor.findMacroCommand(commandName);
        editor.pushIntoModifyCommandHistory(macroCommand);
        macroCommand.execute();
        echo.execute();
    }

    public static void main(String[] args)
    {
        Invoker invoker=new Invoker(new Editor());
        Scanner scan=new Scanner(System.in);
        while(scan.hasNextLine())
        {
            String input=scan.nextLine();
            invoker.takeCommand(input);
        }
    }
}
