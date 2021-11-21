# OOP使用文档

## 1. 目录结构说明
&emsp;&emsp;files文件夹用于存放外部文件，包括自动测试用例需要用到的词表文件，手动测试用例各部分的用例文档和词表文件，lab1的说明文档。

## 2. 使用说明
&emsp;&emsp;Invoker类中的main函数是整个程序的入口，运行后从命令行输入命令即可，
&emsp;&emsp;如需修改词表文件路径，从Common类中的常量ENG_FILE_PATH和FRA_FILE_PATH修改。

## 3. 测试文件
&emsp;&emsp;关于自动化测试用例，首先为每个类用junit5编写了其单元测试，基本覆盖了类中的所有行，
&emsp;&emsp;同时在InvokerTest类中，设计了测试用例1-4（测试函数名testcase1-4），分别针对各个命令的组合、特殊情况等进行了测试，覆盖了所有有效的命令。
