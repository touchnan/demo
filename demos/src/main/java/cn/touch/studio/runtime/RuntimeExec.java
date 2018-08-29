package cn.touch.studio.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chengqiang.han on 2018/8/29.
 *
 * https://blog.csdn.net/c315838651/article/details/72085739
 */
public class RuntimeExec {
    public static void main(String[] args) throws InterruptedException, IOException {

    }

    public static void func() throws IOException {
        Runtime.getRuntime().exec("dir");
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().availableProcessors();
    }

    public static void normal() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("javac SortGvf.java");
        if (process.waitFor() !=0)
            System.exit(1);
        InputStream is  =  process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     * 如果要向子进程传递参数或者输入信息，则应该用OutputStream。但是不推荐用java 1.0引入的Process，
     * 而是用java 5.0的ProcessBuilder替代。
     */
    public static void processBuilder() throws InterruptedException, IOException {
        List<String> cmdList = Arrays.asList("javac", "SortGvf.java");
        ProcessBuilder pb = new ProcessBuilder(cmdList);
        Process  process = pb.start();
        if (process.waitFor() != 0) {
            System.exit(1);
        }
        cmdList = Arrays.asList("java", "SortGvf");
        pb = new ProcessBuilder(cmdList);
        process = pb.start();
        InputStream is  =  process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }

    /**
     * 在用Runtime.getRuntime().exec()或ProcessBuilder(array).start()创建子进程Process之后，
     * 一定要及时取走子进程的输出信息和错误信息，
     * 否则输出信息流和错误信息流很可能因为信息太多导致被填满，最终导致子进程阻塞住，然后执行不下去。
     * @param process
     * @throws InterruptedException
     * @throws IOException
     */
    public static void proess_classic(Process process) throws InterruptedException, IOException {
        InputStream is  =  process.getInputStream();
        int data = 0;
        while ((data = is.read())!=-1) {//取走子进程输出流信息
            System.out.println("data = " + data);
        }
        InputStream isErr = process.getErrorStream();//取走子进程错误流信息
        data = 0;
        while ((data = isErr.read())!=-1) {//取走子进程错误流信息
            System.out.println("data = " + data);
        }

    }
}
