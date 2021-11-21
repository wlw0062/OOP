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
        // 检验命令执行后是否成功修改编辑文本，是否清空执行历史、undo历史
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
        // 测试outContent中是否包含期望的结果，即是否显示编辑文本
        assertEquals("Hello", outContent.toString().trim());
        // 将输出流都改回标准流
        System.setOut(oldOut);
    }
}

class ListModifyCommandTest {
    @Test
    void execute() {
        // 记录标准输出流的位置
        final PrintStream oldOut = System.out;
        // 设置输出流
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        // 创造执行修改类命令的历史
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.executeModifyCommand(new DeleteFromTailCommand(application, 2), echo);
        // 指定列出长度大于执行历史长度
        ListModifyCommand command = new ListModifyCommand(application, 3);
        command.execute();
        String[] lines = outContent.toString().split("\n");
        // outContent的最后两行分别是最近2次执行的命令（新执行的在前）
        assertEquals("1 D 2", lines[lines.length - 2].trim());
        assertEquals("2 a \"head\"", lines[lines.length - 1].trim());
        // 将输出流都改回标准流
        System.setOut(oldOut);
    }
}

class UndoCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        // 测试执行undo前编辑文本、执行历史、undo历史是否符合预期
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
        UndoCommand command = new UndoCommand(application);
        command.execute();
        // 测试执行undo命令后，是否撤销最近执行的修改类命令，是否从执行历史中删除，是否加入undo历史
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
        // 检查执行redo前编辑文本、执行历史、修改历史是否符合预期
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("a \"head\"", application.getUndoCommandHistory().get(0).toString());
        RedoCommand command = new RedoCommand(application);
        command.execute();
        // 测试执行redo后是否成功重做最近一次undo的命令，是否加入执行历史，是否从undo历史中移除
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
        // 创造执行修改类命令的历史
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.executeModifyCommand(new DeleteFromTailCommand(application, 2), echo);
        DefineMacroCommand command = new DefineMacroCommand(application, 2, "m2");
        command.execute();
        // 检验application的宏命令映射表中是否正确存储命令名称与宏命令的映射关系（包括宏命令包含的命令列表正确-新执行在后）
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
        // 检查默认语言为英语
        assertEquals("eng", application.getEditor().getDictionary().getLanguage());
        // 检查语言有效时是否成功切换语言
        SetLanguageCommand command = new SetLanguageCommand(application, "fra");
        command.execute();
        assertEquals("fra", application.getEditor().getDictionary().getLanguage());
        // 检查语言无效时是否保持原语言
        SetLanguageCommand invalid = new SetLanguageCommand(application, "ita");
        invalid.execute();
        assertEquals("fra", application.getEditor().getDictionary().getLanguage());
    }
}

class SetFormatCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        // 检查默认格式为txt
        assertTrue(application.getEditor().getFormat() instanceof TxtFormat);
        // 检查格式有效时是否成功切换格式
        SetFormatCommand command = new SetFormatCommand(application, "xml");
        command.execute();
        assertTrue(application.getEditor().getFormat() instanceof XmlFormat);
        // 检查格式无效时是否保持原格式
        SetFormatCommand invalid = new SetFormatCommand(application, "xml");
        invalid.execute();
        assertTrue(application.getEditor().getFormat() instanceof XmlFormat);
    }
}

class CheckSpellCommandTest {
    @Test
    void execute() {
        // 记录标准输出流的位置
        final PrintStream oldOut = System.out;
        // 设置输出流
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        application.resetEditorText("Hello my word.");
        // 为了避免文件读取操作，使用mock的词表进行测试
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellCommand command = new CheckSpellCommand(application);
        command.execute();
        // 检验是否成功拼写输出错误的单词
        String[] lines = outContent.toString().split("\n");
        assertEquals("my", lines[lines.length - 2].trim());
        assertEquals("word", lines[lines.length - 1].trim());
        // 将输出流都改回标准流
        System.setOut(oldOut);
    }
}

class CheckSpellAndMarkCommandTest {
    @Test
    void execute() {
        // 记录标准输出流的位置
        final PrintStream oldOut = System.out;
        // 设置输出流
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        application.resetEditorText("Hello word.");
        // 为了避免文件读取操作，使用mock的词表进行测试
        String[] wordList = {"Hello", "hello", "World", "world"};
        MockDictionary mockDictionary = new MockDictionary(wordList);
        application.getEditor().setDictionary(mockDictionary);
        CheckSpellAndMarkCommand command = new CheckSpellAndMarkCommand(application);
        command.execute();
        // 检验是否成功标记输出错误的单词，并且没有改变编辑文本的内容
        String[] lines = outContent.toString().split("\n");
        assertEquals("Hello *[word].", lines[lines.length - 1].trim());
        assertEquals("Hello word.", application.getEditor().getText());
        // 将输出流都改回标准流
        System.setOut(oldOut);
    }
}
