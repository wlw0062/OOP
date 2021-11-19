import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class SetTextCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        SetTextCommand command = new SetTextCommand(application, "init text");
        command.execute();
        assertEquals("init text", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals(0, application.getUndoCommandHistory().size());
    }
}

class ShowCommandTest {
    @Test
    void execute() {
        // 记录标准输出流的位置
        final PrintStream oldOut = System.out;
        // 设置输出流
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 定义待测试类的实例
        Application application = new Application();
        application.resetEditorText("Hello");
        ShowCommand command = new ShowCommand(application);
        command.execute();
        // 测试outContent中是否包含期望的结果
        assertEquals("Hello", outContent.toString().trim());
        // 将输出流都改回标准流
        System.setOut(oldOut);
    }
}

class ListModifyCommandTest {
    @Test
    void execute() {
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.executeModifyCommand(new DeleteFromTailCommand(application, 2), echo);
        ListModifyCommand command = new ListModifyCommand(application, 3);
        command.execute();
        String[] lines = outContent.toString().split("\n");
        // 输出的最后两行分别是最近2次执行的命令（后执行的在前）
        assertEquals("1 D 2", lines[lines.length - 2].trim());
        assertEquals("2 a \"head\"", lines[lines.length - 1].trim());
        System.setOut(oldOut);
    }
}

class UndoCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
        UndoCommand command = new UndoCommand(application);
        command.execute();
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("a \"head\"", application.getUndoCommandHistory().get(0).toString());
    }
}

class RedoCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.undoModifyCommand();
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("a \"head\"", application.getUndoCommandHistory().get(0).toString());
        RedoCommand command = new RedoCommand(application);
        command.execute();
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
    }
}

class DefineMacroCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.executeModifyCommand(new DeleteFromTailCommand(application, 2), echo);
        DefineMacroCommand command = new DefineMacroCommand(application, 2, "m2");
        command.execute();
        assertEquals(1, application.getMacroCommandMap().size());
        LinkedList<Revocable> commandList = application.getMacroCommandMap().get("m2").getModifyCommands();
        assertEquals(2, commandList.size());
        assertEquals("a \"head\"", commandList.get(0).toString());
        assertEquals("D 2", commandList.get(1).toString());
    }
}

class SetLanguageCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        assertEquals("eng", application.getEditor().getDictionary().getLanguage());
        SetLanguageCommand command = new SetLanguageCommand(application, "fra");
        command.execute();
        assertEquals("fra", application.getEditor().getDictionary().getLanguage());
    }
}

class SetFormatCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        assertTrue(application.getEditor().getFormat() instanceof TxtFormat);
        SetFormatCommand command = new SetFormatCommand(application, "xml");
        command.execute();
        assertTrue(application.getEditor().getFormat() instanceof XmlFormat);
    }
}

class CheckSpellCommandTest {
    @Test
    void execute() {
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        application.resetEditorText("Hello my word.");
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellCommand command = new CheckSpellCommand(application);
        command.execute();
        String[] lines = outContent.toString().split("\n");
        assertEquals("my", lines[lines.length - 2].trim());
        assertEquals("word", lines[lines.length - 1].trim());
        System.setOut(oldOut);
    }
}

class CheckSpellAndMarkCommandTest {
    @Test
    void execute() {
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        application.resetEditorText("Hello word.");
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellAndMarkCommand command = new CheckSpellAndMarkCommand(application);
        command.execute();
        String[] lines = outContent.toString().split("\n");
        assertEquals("Hello *[word].", lines[lines.length - 1].trim());
        assertEquals("Hello word.", application.getEditor().getText());
        System.setOut(oldOut);
    }
}
