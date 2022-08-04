package cn.touch.demo.vertx.demo_vertx;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import name.fraser.neil.plaintext.diff_match_patch;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/8/4.
 *
 * https://blog.csdn.net/qq_33697094/article/details/121681707
 */
public class TestDiff {

    public static String path = "src/test/java/cn/touch/demo/vertx/demo_vertx/jdiff/";
    public static String s_file = path + "test1.txt";
    public static String d_file = path + "test2.txt";
    public static String m_file = path + "test3.txt";

    public static void main(String[] args) throws Exception{


//        System.out.println(new File(s_file).getAbsoluteFile());
//        diff();
//        udiff();
//        patch();
//        compare();

        google_diff();
    }

    public static void google_diff() throws IOException {
        //https://github.com/google/diff-match-patch/tree/master/java

        diff_match_patch patch = new diff_match_patch();
//        LinkedList<diff_match_patch.Diff> diffs = patch.diff_main(Files.readString(new File(s_file).toPath()), Files.readString(new File(d_file).toPath()), true);
//        patch.diff_prettyHtml(diffs);
//        patch.diff_cleanupMerge(diffs);
//        System.out.println(patch.diff_prettyHtml(diffs));

        LinkedList<diff_match_patch.Diff> diffs = patch.diff_main("wo men dou you aa jia", "wo men dou you yi ge jia", false);
        System.out.println(patch.diff_prettyHtml(diffs));
    }

    public static void diff() throws IOException {
        //原始文件
        List<String> original = Files.readAllLines(new File(s_file).toPath());
        //对比文件
        List<String> revised = Files.readAllLines(new File(d_file).toPath());

        //两文件的不同点
        Patch<String> patch = DiffUtils.diff(original, revised);
        patch.getDeltas().forEach(System.out::println);
//        for (AbstractDelta<String> delta : patch.getDeltas()) {
//            System.out.println(delta);
//        }

    }

    public static void udiff() throws IOException {
        //原始文件
        List<String> original = Files.readAllLines(new File(s_file).toPath());
        //对比文件
        List<String> revised = Files.readAllLines(new File(d_file).toPath());

        //两文件的不同点
        Patch<String> patch = DiffUtils.diff(original, revised);

        //生成统一的差异格式
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("test1.txt", "test2.txt", original, patch, 0);
        unifiedDiff.forEach(System.out::println);

    }

    //打补丁，把旧文件内容变为新文件内容
    public static void patch() throws PatchFailedException, IOException {

        //原始文件
        List<String> original = Files.readAllLines(new File(s_file).toPath());
        //对比文件
        List<String> revised = Files.readAllLines(new File(d_file).toPath());

        //两文件的不同点
        Patch<String> patch = DiffUtils.diff(original, revised);

        //生成统一的差异格式
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("test1.txt", "test2.txt", original, patch, 0);

        //从文件或此处从内存导入统一差异格式到补丁
        Patch<String> importedPatch = UnifiedDiffUtils.parseUnifiedDiff(unifiedDiff);

        List<String> test3 = Files.readAllLines(new File(s_file).toPath());

        //将差异运用到其他文件打补丁，即将不同点运用到其他文件（相当于git的冲突合并）
        List<String> patchedText = DiffUtils.patch(test3, importedPatch);
        for (String patchedTextPow : patchedText) {
            System.out.println(patchedTextPow);
        }

    }

    //比较： 对比两文件的不同点并按行显示不同
    public static void compare() throws IOException {
        //原始文件
        List<String> text1= Files.readAllLines(new File(s_file).toPath());
        //对比文件
        List<String> text2=Files.readAllLines(new File(d_file).toPath());

        //行比较器，原文件删除的内容用"~"包裹，对比文件新增的内容用"**"包裹
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "~")
                .newTag(f -> "**")
                .build();
        //通过行比较器对比得到每一行的不同
        List<DiffRow> rows = generator.generateDiffRows(text1, text2);

        //输出每一行的原始文件和对比文件，每一行的原始文件和对比文件通过 "|"分割
        for (DiffRow row : rows) {
            System.out.println(row.getOldLine() + "|" + row.getNewLine());
        }
    }
}
