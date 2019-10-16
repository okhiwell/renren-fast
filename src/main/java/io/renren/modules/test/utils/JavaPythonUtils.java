package io.renren.modules.test.utils;

import org.junit.Test;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author CT
 * @date 2019/7/113:17
 * @describe JavaPythonUtils用于在java中调用python进行部分功能的实现
 */

public class JavaPythonUtils {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("add.py");
        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
        PyFunction pyFunction = interpreter.get("add", PyFunction.class);
        int a = 5, b = 10;
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println("the anwser is: " + pyobj);
    }



    @Test
    public void testPythonScript(){
//        String[] args = new String[] { "python", "add.py", String.valueOf(5), String.valueOf(10) };
        String[] args = new String[] { "pwd" };
        RunPythonScript(args);
    }


    // 目前系统仍存在问题
    public static void RunPythonScript(String[] args) {
        Process proc;
        try {
            String cmds = "";
            for(String arg:args){
                cmds += " "+arg+" ";
            }
            proc = Runtime.getRuntime().exec(cmds);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
