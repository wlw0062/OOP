import java.util.LinkedList;

public interface Command {
    void execute();
}

// 设置编辑区文字内容的命令
class SetTextCommand implements Command {
    private final Application application;
    private final String initText;

    public SetTextCommand(Application application, String initText) {
        this.application = application;
        this.initText = initText;
    }

    @Override
    public void execute() {
        application.resetEditorText(initText);
    }
}

//显示当前编辑的字符串的命令
class ShowCommand implements Command {
    private final Editor editor;

    public ShowCommand(Application application) {
        this.editor = application.getEditor();
    }

    @Override
    public void execute() {
        System.out.println(editor.getText());
    }
}

// 列出最近执行的修改类命令列表的命令
class ListModifyCommand implements Command {
    private final Application application;
    private final int listNum;

    public ListModifyCommand(Application application, int listNum) {
        this.application = application;
        this.listNum = listNum;
    }

    @Override
    public void execute() {
        String[] commandNames = application.listModifyCommand(listNum);
        for(int i = 0; i < commandNames.length; i++){
            System.out.println((i+1) + " " + commandNames[i]);
        }
    }
}

// 取消上一步操作（修改类命令）的命令
class UndoCommand implements Command {
    private final Application application;

    public UndoCommand(Application application) {
        this.application = application;
    }

    @Override
    public void execute() {
        application.undoModifyCommand();
    }
}

// 重做上一步undo取消的操作（修改类命令）的命令
class RedoCommand implements Command {
    private final Application application;

    public RedoCommand(Application application) {
        this.application = application;
    }

    @Override
    public void execute() {
        application.redoModifyCommand();
    }
}

// 将最近执行的n个修改类命令组成一个宏命令（修改类命令）的命令
class DefineMacroCommand implements Command {
    private final Application application;
    private final int commandNum;
    private final String commandName;

    public DefineMacroCommand(Application application, int commandNum, String commandName) {
        this.application = application;
        this.commandNum = commandNum;
        this.commandName = commandName;
    }

    @Override
    public void execute() {
        // 得到最近n次执行修改类命令的列表（新执行在后，因为按此顺序执行可以重现前n条命令一起作用的效果）
        LinkedList<Revocable> modifyCommands = application.getReversedModifyCommandHistory(commandNum);
        // 以宏命令名称和操作命令列表创建一条新的宏命令
        MacroCommand macroCommand = new MacroCommand(commandName, modifyCommands);
        // 将该宏命令添加到application的宏命令映射表中
        application.addToMacroCommandMap(macroCommand);
    }
}

// 指定当前编辑区中语言的命令
class SetLanguageCommand implements Command {
    private final Editor editor;
    private final String language;

    public SetLanguageCommand(Application application, String language) {
        this.editor = application.getEditor();
        this.language = language;
    }

    @Override
    public void execute() {
        // 如果存在该语言对应的文件路径，则将语言修改为目标语言（同时新建dictionary时会调用读取文件方法获取新语言的词表，实现动态切换语言的效果）
        if (Common.FILE_PATH_MAP.containsKey(language)) {
            editor.setDictionary(new FileDictionary(Common.FILE_PATH_MAP.get(language), language));
        }
    }
}

// 指定当前编辑区中文本格式的命令
class SetFormatCommand implements Command {
    private final Editor editor;
    private final String format;

    public SetFormatCommand(Application application, String format) {
        this.editor = application.getEditor();
        this.format = format;
    }

    @Override
    public void execute() {
        // 如果常量中有该格式对应的类，则切换格式为目标格式
        if (Common.FORMAT_MAP.containsKey(format)) {
            editor.setFormat(Common.FORMAT_MAP.get(format));
        }
    }
}

// 执行拼写检查，列出所有拼写错误单词的命令（不修改文本）
class CheckSpellCommand implements Command {
    private final Editor editor;

    public CheckSpellCommand(Application application) {
        this.editor = application.getEditor();
    }

    @Override
    public void execute() {
        String[] wrongList = editor.checkSpell();
        for (String word : wrongList) {
            System.out.println(word);
        }
    }
}

// 执行拼写检查，标记出所有拼写错误单词的命令（不修改文本）
class CheckSpellAndMarkCommand implements Command {
    private final Editor editor;

    public CheckSpellAndMarkCommand(Application application) {
        this.editor = application.getEditor();
    }

    @Override
    public void execute() {
        System.out.println(editor.checkSpellAndMark());
    }
}
