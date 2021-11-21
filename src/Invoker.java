import java.util.Scanner;
import java.util.regex.*;

public class Invoker {
    private final Application application;
    private final ShowCommand echo;

    public Invoker(Application application) {
        this.application = application;
        // 用于回显执行修改类命令后的文本
        this.echo = new ShowCommand(this.application);
    }

    public void executeCommand(String commandString) {
        // 先将命令字符串根据命令规则转化为对应类型的命令
        Command command = parseCommandString(commandString);
        // 如果是修改类命令，则调用application的执行修改类命令方法
        if (command instanceof Revocable) {
            application.executeModifyCommand((Revocable) command, echo);
        }
        // 如果不为空（则一定是非修改类命令），则调用application的执行非修改类命令方法
        else if (command != null) {
            application.executeNormalCommand(command);
        }
    }

    public Command parseCommandString(String commandString) {
        // 设置初始文本的命令，例如 -t "init text"
        if (Pattern.matches("(^-t \")([A-Za-z\\s,.<>/]*)(\"$)", commandString)) {
            return new SetTextCommand(application, commandString.substring(4,commandString.length()-1));
        }
        // 显示当前编辑文本的命令，s
        else if (commandString.equals("s")) {
            return new ShowCommand(application);
        }
        // 在尾部添加字符串的命令，例如 A "last word"
        else if (Pattern.matches("(^A \")([A-Za-z\\s,.<>/]*)(\"$)", commandString)) {
            return new AddFromTailCommand(application, commandString.substring(3,commandString.length()-1));
        }
        // 在头部添加字符串的命令，例如 a "first word"
        else if (Pattern.matches("(^a \")([A-Za-z\\s,.<>/]*)(\"$)", commandString)) {
            return new AddFromHeadCommand(application, commandString.substring(3,commandString.length()-1));
        }
        // 从尾部删除指定数量的字符的命令，例如 D 5
        else if (Pattern.matches("(^D )([0-9]+)", commandString)) {
            return new DeleteFromTailCommand(application, Integer.parseInt(commandString.substring(2)));
        }
        // 从头部删除指定数量的字符的命令，例如 d 5
        else if (Pattern.matches("(^d )([0-9]+)", commandString)) {
            return new DeleteFromHeadCommand(application, Integer.parseInt(commandString.substring(2)));
        }
        // 列出最近执行的修改类命令列表的命令，例如 l 10
        else if (Pattern.matches("(^l )([0-9]+)", commandString)) {
            return new ListModifyCommand(application, Integer.parseInt(commandString.substring(2)));
        }
        // undo命令，u
        else if (commandString.equals("u")) {
            return new UndoCommand(application);
        }
        // redo命令，r
        else if (commandString.equals("r")) {
            return new RedoCommand(application);
        }
        // 定义宏命令，例如 m 5 m10，将最近5个执行的修改类命令创建成一个名为m10的宏命令
        else if (Pattern.matches("(^m )([0-9]+ )([\\w]+)", commandString)) {
            String[] strings = commandString.split(" ");
            return new DefineMacroCommand(application, Integer.parseInt(strings[1]), strings[2]);
        }
        // 执行宏命令，如 $m10，执行一个名为m10的宏命令
        else if (Pattern.matches("(^\\$)([\\w]+)", commandString)) {
            return application.findMacroCommand(commandString.substring(1));
        }
        // 指定语言的命令，如lang eng
        else if (Pattern.matches("(^lang )([\\w]+)", commandString)) {
            return new SetLanguageCommand(application, commandString.substring(5));
        }
        // 指定格式的命令，如content txt
        else if (Pattern.matches("(^content )([\\w]+)", commandString)) {
            return new SetFormatCommand(application, commandString.substring(8));
        }
        // 执行拼写检查，列出拼写错误单词的命令，spell
        else if (commandString.equals("spell")) {
            return new CheckSpellCommand(application);
        }
        // 执行拼写检查，标记拼写错误单词的命令，spell-a
        else if (commandString.equals("spell-a")) {
            return new CheckSpellAndMarkCommand(application);
        }
        // 执行拼写检查，删除拼写错误单词的命令，spell-m
        else if (commandString.equals("spell-m")) {
            return new CheckSpellAndDeleteCommand(application);
        }
        return null;
    }

    public static void main(String[] args) {
        Invoker invoker = new Invoker(new Application());
        Scanner scanner = new Scanner(System.in);
        // 每输入一行，分别执行对应命令
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            invoker.executeCommand(input);
        }
    }
}
