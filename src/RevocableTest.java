import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AddFromTailCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromTailCommand command = new AddFromTailCommand(application, " new");
        command.execute();
        // 检验是否成功在末尾添加文本
        assertEquals("text new", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromTailCommand command = new AddFromTailCommand(application, " new");
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("text new", application.getEditor().getText());
        command.undo();
        // 检验是否撤销命令的修改
        assertEquals("text", application.getEditor().getText());
    }
}

class AddFromHeadCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromHeadCommand command = new AddFromHeadCommand(application, "new ");
        command.execute();
        // 检验是否成功在头部添加文本
        assertEquals("new text", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromHeadCommand command = new AddFromHeadCommand(application, "new ");
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("new text", application.getEditor().getText());
        command.undo();
        // 检验是否撤销命令的修改
        assertEquals("text", application.getEditor().getText());
    }
}

class DeleteFromTailCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromTailCommand command = new DeleteFromTailCommand(application, 2);
        command.execute();
        // 检验是否成功从尾部删除文本
        assertEquals("te", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromTailCommand command = new DeleteFromTailCommand(application, 2);
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("te", application.getEditor().getText());
        command.undo();
        // 检验是否撤销命令的修改
        assertEquals("text", application.getEditor().getText());
    }
}

class DeleteFromHeadCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromHeadCommand command = new DeleteFromHeadCommand(application, 2);
        command.execute();
        // 检验是否成功从头部删除文本
        assertEquals("xt", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromHeadCommand command = new DeleteFromHeadCommand(application, 2);
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("xt", application.getEditor().getText());
        command.undo();
        // 检验是否撤销命令的修改
        assertEquals("text", application.getEditor().getText());
    }
}

class MacroCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        // 构建宏命令的命令列表
        LinkedList<Revocable> modifyCommands = new LinkedList<>();
        modifyCommands.add(new AddFromHeadCommand(application, "new "));
        modifyCommands.add(new DeleteFromHeadCommand(application, 1));
        MacroCommand command = new MacroCommand("macro", modifyCommands);
        command.execute();
        // 检验宏命令执行是否按顺序正确执行了命令列表的命令
        assertEquals("ew text", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        LinkedList<Revocable> modifyCommands = new LinkedList<>();
        modifyCommands.add(new AddFromHeadCommand(application, "new "));
        modifyCommands.add(new DeleteFromHeadCommand(application, 1));
        MacroCommand command = new MacroCommand("macro", modifyCommands);
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("ew text", application.getEditor().getText());
        command.undo();
        // 检验undo是否撤销命令列表所有的修改
        assertEquals("text", application.getEditor().getText());
    }
}

class CheckSpellAndDeleteCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("Hello word.");
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellAndDeleteCommand command = new CheckSpellAndDeleteCommand(application);
        command.execute();
        // 检验是否成功删除拼写错误的单词
        assertEquals("Hello .", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("Hello word.");
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellAndDeleteCommand command = new CheckSpellAndDeleteCommand(application);
        command.execute();
        // 检验执行后undo前的文本内容
        assertEquals("Hello .", application.getEditor().getText());
        command.undo();
        // 检验undo是否撤销命令列表所有的修改
        assertEquals("Hello word.", application.getEditor().getText());
    }
}
