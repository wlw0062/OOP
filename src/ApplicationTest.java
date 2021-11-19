import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    @Test
    void resetEditorText() {
        Application application = new Application();
        application.resetEditorText("new text");
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
        String[] result = application.listModifyCommand(3);
        String[] expect = { "D 2", "a \"head\"" };
        assertArrayEquals(expect, result);
    }

    @Test
    void executeNormalCommand() {
        Application application = new Application();
        SetTextCommand command = new SetTextCommand(application, "text");
        application.executeNormalCommand(command);
        assertEquals("text", application.getEditor().getText());
    }

    @Test
    void executeModifyCommand() {
        Application application = new Application();
        AddFromHeadCommand addFromHeadCommand = new AddFromHeadCommand(application, "head");
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(addFromHeadCommand, showCommand);
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
        assertEquals("head", application.getEditor().getText());
        assertEquals("A \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
        application.undoModifyCommand();
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
        assertEquals("", application.getEditor().getText());
        assertEquals(0, application.getModifyCommandHistory().size());
        assertEquals("a \"head\"", application.getUndoCommandHistory().get(0).toString());
        application.redoModifyCommand();
        assertEquals("head", application.getEditor().getText());
        assertEquals("a \"head\"", application.getModifyCommandHistory().get(0).toString());
        assertEquals(0, application.getUndoCommandHistory().size());
    }

    @Test
    void addToMacroCommandMap() {
        Application application = new Application();
        LinkedList<Revocable> commandList = new LinkedList<>();
        commandList.push(new AddFromHeadCommand(application, "head"));
        MacroCommand macroCommand = new MacroCommand("macro", commandList);
        application.addToMacroCommandMap(macroCommand);
        Map<String, MacroCommand> map = application.getMacroCommandMap();
        assertEquals(macroCommand, map.get("macro"));
    }

    @Test
    void findMacroCommand() {
       Application application = new Application();
       LinkedList<Revocable> commandList = new LinkedList<>();
       commandList.push(new DeleteFromTailCommand(application, 2));
       MacroCommand macroCommand = new MacroCommand("macro", commandList);
       application.addToMacroCommandMap(macroCommand);
       assertEquals(macroCommand, application.findMacroCommand("macro"));
       assertNull(application.findMacroCommand("another"));
    }

    @Test
    void getReversedModifyCommandHistory() {
        Application application = new Application();
        AddFromHeadCommand add = new AddFromHeadCommand(application, "hello");
        DeleteFromTailCommand delete = new DeleteFromTailCommand(application, 2);
        ShowCommand showCommand = new ShowCommand(application);
        application.executeModifyCommand(add, showCommand);
        application.executeModifyCommand(delete, showCommand);
        LinkedList<Revocable> list = application.getReversedModifyCommandHistory(3);
        assertEquals(2, list.size());
        assertEquals("a \"hello\"", list.get(0).toString());
        assertEquals("D 2", list.get(1).toString());
    }
}
