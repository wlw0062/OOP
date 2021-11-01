import java.util.Scanner;

public class Invoker {
    private final Application application;
    private final ShowCommand echo;

    public Invoker(Application application){
        this.application = application;
        this.echo = new ShowCommand(this.application);
    }

    public void executeCommand(String commandLine){
        String[] commandWords = commandLine.split(" ");
        switch (commandWords[0]) {
            case "-t": executeSetTextCommand(commandWords[1].substring(1,commandWords[1].length()-1));break;
            case "s": executeShowCommand();break;
            case "A": executeAddFromTailCommand(commandWords[1].substring(1,commandWords[1].length()-1));break;
            case "a": executeAddFromHeadCommand(commandWords[1].substring(1,commandWords[1].length()-1));break;
            case "D": executeDeleteFromTailCommand(Integer.parseInt(commandWords[1]));break;
            case "d": executeDeleteFromHeadCommand(Integer.parseInt(commandWords[1]));break;
            default: System.out.println("无效，请重新输入命令：");
        }
    }

    private void executeSetTextCommand(String initText){
        SetTextCommand command = new SetTextCommand(this.application, initText);
        this.application.executeNormalCommand(command);
    }

    private void executeShowCommand(){
        ShowCommand command = new ShowCommand(this.application);
        this.application.executeNormalCommand(command);
    }

    private void executeAddFromTailCommand(String tailText){
        AddFromTailCommand command = new AddFromTailCommand(this.application, tailText);
        this.application.executeModifyCommand(command);
        this.echo.execute();
    }

    private void executeAddFromHeadCommand(String headText){
        AddFromHeadCommand command = new AddFromHeadCommand(this.application, headText);
        this.application.executeModifyCommand(command);
        this.echo.execute();
    }

    private void executeDeleteFromTailCommand(int deleteNum){
        DeleteFromTailCommand command = new DeleteFromTailCommand(this.application, deleteNum);
        this.application.executeModifyCommand(command);
        this.echo.execute();
    }

    private void executeDeleteFromHeadCommand(int deleteNum){
        DeleteFromHeadCommand command = new DeleteFromHeadCommand(this.application, deleteNum);
        this.application.executeModifyCommand(command);
        this.echo.execute();
    }

    public static void main(String[] args){
        Invoker invoker = new Invoker(new Application());
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入命令：");
        while (scanner.hasNextLine()){
            String input = scanner.nextLine();
            invoker.executeCommand(input);
        }
    }
}
