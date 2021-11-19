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
        assertEquals("text new", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromTailCommand command = new AddFromTailCommand(application, " new");
        command.execute();
        assertEquals("text new", application.getEditor().getText());
        command.undo();
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
        assertEquals("new text", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        AddFromHeadCommand command = new AddFromHeadCommand(application, "new ");
        command.execute();
        assertEquals("new text", application.getEditor().getText());
        command.undo();
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
        assertEquals("te", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromTailCommand command = new DeleteFromTailCommand(application, 2);
        command.execute();
        assertEquals("te", application.getEditor().getText());
        command.undo();
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
        assertEquals("xt", application.getEditor().getText());
    }

    @Test
    void undo() {
        Application application = new Application();
        application.resetEditorText("text");
        DeleteFromHeadCommand command = new DeleteFromHeadCommand(application, 2);
        command.execute();
        assertEquals("xt", application.getEditor().getText());
        command.undo();
        assertEquals("text", application.getEditor().getText());
    }
}

class MacroCommandTest {
    @Test
    void execute() {
        Application application = new Application();
        application.resetEditorText("text");
        LinkedList<Revocable> modifyCommands = new LinkedList<>();
        modifyCommands.add(new AddFromHeadCommand(application, "new "));
        modifyCommands.add(new DeleteFromHeadCommand(application, 1));
        MacroCommand command = new MacroCommand("macro", modifyCommands);
        command.execute();
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
        assertEquals("ew text", application.getEditor().getText());
        command.undo();
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
        assertEquals("Hello .", application.getEditor().getText());
        command.undo();
        assertEquals("Hello word.", application.getEditor().getText());
    }
}
