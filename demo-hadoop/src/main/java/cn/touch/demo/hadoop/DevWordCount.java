package cn.touch.demo.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/8/22.
 */
public class DevWordCount {
    public static class WordCountMapper extends MapReduceBase implements Mapper<Object,Text,OutputCollector,Reporter> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, OutputCollector output, Reporter reporter) throws IOException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                output.collect(word, one);
            }

        }
    }

//    public static class WordCountReducer extends MapReduceBase implements Reducer {
//        private IntWritable result = new IntWritable();
//
//        public void reduce(Text key, Iterator values, OutputCollector output, Reporter reporter) throws IOException {
//            int sum = 0;
//            while (values.hasNext()) {
//                sum += values.next().get();
//            }
//            result.set(sum);
//            output.collect(key, result);
//        }
//    }

    public static void main(String[] args) throws Exception {
        String input = "hdfs://192.168.1.210:9000/user/hdfs/o_t_account";
        String output = "hdfs://192.168.1.210:9000/user/hdfs/o_t_account/result";

        JobConf conf = new JobConf(DevWordCount.class);
        conf.setJobName("cn.touch.demo.hadoop.DevWordCount");
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(WordCountMapper.class);
//        conf.setCombinerClass(WordCountReducer.class);
//        conf.setReducerClass(WordCountReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(input));
        FileOutputFormat.setOutputPath(conf, new Path(output));

        JobClient.runJob(conf);
        System.exit(0);
    }
}
