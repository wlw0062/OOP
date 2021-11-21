import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    @Test
    void resetEditorText() {
        Application application = new Application();
        application.resetEditorText("new text");
        // 测试是否成功修改文本，是否清空执行历史及undo历史
        assertEquals("new text", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals(0, application.getUndoCommandHistory().size());
    }

    @Test
    void listModifyCommand() {
        Application application = new Application();
        ShowCommand echo = new ShowCommand(application);
        application.executeModifyCommand(new AddFromHeadCommand(application, "head"), echo);
        application.executeModifyCommand(new DeleteFromTailCommand(application, 2), echo);
        // 测试列出命令条数多于执行历史时能否正确列出命令历史
        String[] result = application.listModifyCommand(3);
        String[] expect = { "D 2", "a \"head\"" };
        assertArrayEquals(expect, result);
    }

    @Test
    void executeNormalCommand() {
        Application application = new Application();
        SetTextCommand command = new SetTextCommand(application, "text");
        application.executeNormalCommand(command);
        // 测试执行非修改命令时是否成功执行，是否添加到执行历史中
        assertEquals("text", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
    }

    @Test
    void executeModifyCommand() {
        Application application = new Application();
        AddFromHeadCommand addFromHeadCommand = new AddFromHeadCommand(application, "head");
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(addFromHeadCommand, showCommand);
        // 测试执行修改类命令时是否成功执行，是否添加到执行历史中
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
    }

    @Test
    void undoModifyCommand() {
        Application application = new Application();
        AddFromTailCommand addFromTailCommand = new AddFromTailCommand(application, "head");
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(addFromTailCommand, showCommand);
        // 测试执行undo前编辑文本、执行历史、undo历史是否符合预期
        assertEquals("head", application.getEditor().getText());
        assertEquals("A \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
        application.undoModifyCommand();
        // 测试执行undo命令后，是否撤销最近执行的修改类命令，是否从执行历史中删除，是否加入undo历史
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("A \"head\"", application.getUndoCommandHistory().get(0).toString());
    }

    @Test
    void redoModifyCommand() {
        Application application = new Application();
        AddFromHeadCommand addFromHeadCommand = new AddFromHeadCommand(application, "head");
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(addFromHeadCommand, showCommand);
        application.undoModifyCommand();
        // 检查执行redo前编辑文本、执行历史、修改历史是否符合预期
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("a \"head\"", application.getUndoCommandHistory().get(0).toString());
        application.redoModifyCommand();
        // 测试执行redo后是否成功重做最近一次undo的命令，是否加入执行历史，是否从undo历史中移除
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
    }

    @Test
    void addToMacroCommandMap() {
        Application application = new Application();
        // 创建用于构建宏命令的命令列表
        LinkedList<Revocable> commandList = new LinkedList<>();
        commandList.push(new AddFromHeadCommand(application, "head"));
        MacroCommand macroCommand = new MacroCommand("macro", commandList);
        application.addToMacroCommandMap(macroCommand);
        Map<String, MacroCommand> map = application.getMacroCommandMap();
        // 检查是否成功加入application的宏命令映射表
        assertEquals(macroCommand, map.get("macro"));
    }

    @Test
    void findMacroCommand() {
       Application application = new Application();
        // 创建用于构建宏命令的命令列表
       LinkedList<Revocable> commandList = new LinkedList<>();
       commandList.push(new DeleteFromTailCommand(application, 2));
       MacroCommand macroCommand = new MacroCommand("macro", commandList);
       application.addToMacroCommandMap(macroCommand);
       // 检查是否能成功根据宏命令名称获取对应宏命令，当宏命令不存在时是否成功返回null
       assertEquals(macroCommand, application.findMacroCommand("macro"));
       assertNull(application.findMacroCommand("another"));
    }

    @Test
    void getReversedModifyCommandHistory() {
        Application application = new Application();
        // 创造执行修改类命令的历史
        AddFromHeadCommand add = new AddFromHeadCommand(application, "hello");
        DeleteFromTailCommand delete = new DeleteFromTailCommand(application, 2);
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(add, showCommand);
        application.executeModifyCommand(delete, showCommand);
        // 检查指定命令数大于已执行命令数时能否成功获取反序历史列表
        LinkedList<Revocable> list = application.getReversedModifyCommandHistory(3);
        assertEquals(2, list.size());
        assertEquals("a \"hello\"", list.get(0).toString());
        assertEquals("D 2", list.get(1).toString());
    }
}
