import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InvokerTest {
    @Test
    void executeCommand() {
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        // 执行修改类命令，需要回显修改后文本，以从头部添加命令为例
        invoker.executeCommand("a \"head\"");
        assertEquals("head", application.getEditor().getText());
        assertEquals("head", outContent.toString().trim());
        // 执行非修改类命令，以undo命令为例
        invoker.executeCommand("u");
        assertEquals("", application.getEditor().getText());
        System.setOut(oldOut);
    }

    @Test
    void parseCommandString() {
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        Command command = invoker.parseCommandString("-t \"init\"");
        assertTrue(command instanceof SetTextCommand);
        command = invoker.parseCommandString("s");
        assertTrue(command instanceof ShowCommand);
        command = invoker.parseCommandString("A \"tail\"");
        assertTrue(command instanceof AddFromTailCommand);
        command = invoker.parseCommandString("a \"head\"");
        assertTrue(command instanceof AddFromHeadCommand);
        command = invoker.parseCommandString("D 2");
        assertTrue(command instanceof DeleteFromTailCommand);
        command = invoker.parseCommandString("d 2");
        assertTrue(command instanceof DeleteFromHeadCommand);
        command = invoker.parseCommandString("l 1");
        assertTrue(command instanceof ListModifyCommand);
        command = invoker.parseCommandString("u");
        assertTrue(command instanceof UndoCommand);
        command = invoker.parseCommandString("r");
        assertTrue(command instanceof RedoCommand);
        command = invoker.parseCommandString("m 2 macro");
        assertTrue(command instanceof DefineMacroCommand);
        // 要先定义宏命令
        invoker.executeCommand("m 2 macro");
        command = invoker.parseCommandString("$macro");
        assertTrue(command instanceof MacroCommand);
        command = invoker.parseCommandString("lang fra");
        assertTrue(command instanceof SetLanguageCommand);
        command = invoker.parseCommandString("content xml");
        assertTrue(command instanceof SetFormatCommand);
        command = invoker.parseCommandString("spell");
        assertTrue(command instanceof CheckSpellCommand);
        command = invoker.parseCommandString("spell-a");
        assertTrue(command instanceof CheckSpellAndMarkCommand);
        command = invoker.parseCommandString("spell-m");
        assertTrue(command instanceof CheckSpellAndDeleteCommand);
        command = invoker.parseCommandString("invalid");
        assertNull(command);
    }

    @Test
    void testcase1() {
        // 设置输出流
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 初始化对象
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        // 测试范围：1.从头尾增加删除字符串的4个命令
        // 测试用例：1.编辑字符串为空时添加内容（1.从头部添加 2.从尾部添加）
        //         2.编辑字符串不为空时从头、尾添加
        //         3.从头、尾删除小于字符串总长度的内容
        //         4.从头、尾删除大于字符串总长度的内容，结果为将字符串清空
         String[] commands = {
                 "A \"b\"", // case1.1
                 "A \"ug\"", "a \"de\"", // case 2
                 "D 3", "d 1", // case 3
                 "D 2", "a \"abc\"", "d 4" // case 4
         };
         String[] results = {
                 "b", // case1.1
                 "bug", "debug", // case 2
                 "de", "e", // case 3
                 "", "abc", "" // case 4
         };
         for (String command : commands) {
             invoker.executeCommand(command);
         }
         // 检验输出结果是否正确
         String[] outputs = outContent.toString().split("\n");
         assertEquals(results.length, outputs.length);
         for (int i = 0; i < results.length; i++) {
             assertEquals(results[i], outputs[i].trim());
         }
         // 恢复原来的输出流
         System.setOut(oldOut);
    }

    @Test
    void testcase2() {
        // 设置输出流
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 初始化对象
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        // 测试范围：1.设置初始字符串的1个命令
        //         2.展示字符串的1个命令
        //         3.进行undo、redo操作的2个命令
        //         4.列出执行命令（修改类）历史的1个命令
        //         5.宏命令定义和执行的2个命令
        // 测试用例：1.在原字符串为空、不为空时设置编辑内容，字符串更改为制定内容
        //         2.列出超过、不超过执行的修改类命令历史总长度的记录，超过时展示所有历史
        //         3.在有、无可以撤销的命令时执行undo，有则撤销修改并删除对应历史，无则不做改变
        //         4.在有、无可以重做的命令时执行redo，有则恢复修改并恢复对应历史，无则不做改变
        //         （无的情况包括：1.undo后执行了其他修改类命令 2.redo了所有undo过的命令）
        //         5.定义重名、不重名宏命令，长度超过、不超过命令历史，重名时覆盖原来的命令，超过长度时定义已有历史为宏命令
        //         6.执行存在、不存在对应名称的宏命令，撤销、重做宏命令
        String[] commands = {
                "-t \"init\"", "s", "-t \"abc\"", "s", // case 1
                "D 1", "A \"de\"", "l 1", "l 5", // case 2
                "u", "s", "u", "s", "u", "s", "l 1", // case 3
                "r", "s", "r", "s", "r", "s", "l 5", "m 2 macro1", "m 5 macro2",// case 4.1 & 5
                "u", "s", "A \"def\"", "r", "s", "l 5", // case 4.2
                "m 2 macro1",  // case 5
                "$macro1", "u", "s", "r", "s", "$macro2", "l 5" // case 6
        };
        String[] results = {
                "init", "abc", // case 1
                "ab", "abde", "1 A \"de\"", "1 A \"de\"", "2 D 1", // case 2
                "ab", "abc", "abc", // case 3
                "ab", "abde", "abde", "1 A \"de\"", "2 D 1", // case 4.1
                "ab", "abdef", "abdef", "1 A \"def\"" , "2 D 1", // case 4.2
                "abdedef", "abdef", "abdedef", "abdedede", "1 macro2", "2 macro1", "3 A \"def\"", "4 D 1" // case 6
        };
        for (String command : commands) {
            invoker.executeCommand(command);
        }
        // 检验输出结果是否正确
        String[] outputs = outContent.toString().split("\n");
        assertEquals(results.length, outputs.length);
        for (int i = 0; i < results.length; i++) {
            assertEquals(results[i], outputs[i].trim());
        }
        // 恢复原来的输出流
        System.setOut(oldOut);
    }

    @Test
    void testcase3() {
        // 设置输出流
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 初始化对象
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        // 测试范围：1.切换文本语言及格式的2个命令
        //         2.执行拼写检查，列出错误单词的1条命令
        // 测试用例：1.切换语言（1.无效时维持原语言 2.语言有效时切换）
        //         2.切换格式（1.无效时维持原格式 2.格式有效时切换）
        //         3.在各语言、格式排列组合的情况下执行拼写检查，列出所有错误单词
        //        （1.txt&eng 2.txt&fra 3.xml&fra 4.xml&eng）
        String[] commands = {
                "A \"Hello world. Bonjour le monde.\"", "spell", // case 3.1
                "lang ita", "spell", // case 1.1
                "lang fra", "spell", // case 1.2 & 3.2
                "d 100", "A \"<tag>Hello</tag> le monde.\"", // 修改文本内容
                "content xml", "spell", // case 2.2 & 3.3
                "content rdm", "spell", // case 2.1
                "lang eng", "spell", // case 3.4
        };
        String[] results = {
                "Hello world. Bonjour le monde.", "Bonjour", "le", "monde", // case 3.1
                "Bonjour", "le", "monde", // case 1.1
                "Hello", "world", // case 1.2 & 3.2
                "", "<tag>Hello</tag> le monde.", // 修改文本内容
                "Hello", // case 2.2 & 3.3
                "Hello", // case 2.1
                "le", "monde" // case 3.4
        };
        for (String command : commands) {
            invoker.executeCommand(command);
        }
        // 检验输出结果是否正确
        String[] outputs = outContent.toString().split("\n");
        assertEquals(results.length, outputs.length);
        for (int i = 0; i < results.length; i++) {
            assertEquals(results[i], outputs[i].trim());
        }
        // 恢复原来的输出流
        System.setOut(oldOut);
    }

    @Test
    void testcase4() {
        // 设置输出流
        final PrintStream oldOut = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // 初始化对象
        Application application = new Application();
        Invoker invoker = new Invoker(application);
        // 测试范围：1.检查拼写，标记错误部分的1个命令
        //         2.检查拼写，删除错误部分的1个命令
        // 测试用例：1.两种格式、两种语言排列组合情况下，能正确标记出拼写错误部分（但不修改字符串），能正确删除拼写错误部分（修改字符串，可撤销、重做）
        //         （1.txt&eng 2.txt&fra 3.xml&fra 4.xml&eng）
        String[] commands = {
                "A \"Hello world. Bonjour le monde.\"", "spell-a", "s", "spell-m", "s", "u", "s", // case 1.1
                "lang fra", "spell-a", "s", "spell-m", "s", "u", "s", "r", "s", // case 1.2
                "d 100", "A \"<tag>Hello</tag> le monde.\"", // 修改文本内容
                "content xml", "spell-a", "s", "spell-m", "s", "u", "s", // case 1.3
                "lang eng", "spell-a", "s", "spell-m", "s", "u", "s", "r", "s", // case 1.4
        };
        String[] results = {
                "Hello world. Bonjour le monde.", "Hello world. *[Bonjour] *[le] *[monde].", "Hello world. Bonjour le monde.", "Hello world.   .", "Hello world.   .", "Hello world. Bonjour le monde.", // case 1.1
                "*[Hello] *[world]. Bonjour le monde.", "Hello world. Bonjour le monde.", " . Bonjour le monde.", " . Bonjour le monde.", "Hello world. Bonjour le monde.", " . Bonjour le monde.", // case 1.2
                "", "<tag>Hello</tag> le monde.", // 修改文本内容
                "<tag>*[Hello]</tag> le monde.", "<tag>Hello</tag> le monde.", "<tag></tag> le monde.", "<tag></tag> le monde.", "<tag>Hello</tag> le monde.", // case 1.3
                "<tag>Hello</tag> *[le] *[monde].", "<tag>Hello</tag> le monde.", "<tag>Hello</tag>  .", "<tag>Hello</tag>  .", "<tag>Hello</tag> le monde.", "<tag>Hello</tag>  ." // case 1.4
        };
        for (String command : commands) {
            invoker.executeCommand(command);
        }
        // 检验输出结果是否正确
        String[] outputs = outContent.toString().split("\n");
        assertEquals(results.length, outputs.length);
        for (int i = 0; i < results.length; i++) {
            assertEquals(results[i].trim(), outputs[i].trim());
        }
        // 恢复原来的输出流
        System.setOut(oldOut);
    }
}
