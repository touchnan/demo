package cn.touch.demo.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/8/22.
 */
public class MaxTemperature {

    public static class MaxTemperatureMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final int MISSING = 9999;

        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String year = line.substring(15, 19);
            int airTemperautre;
            if (line.charAt(87) == '+') {
                airTemperautre = Integer.parseInt(line.substring(87, 92));
            } else {
                airTemperautre = Integer.parseInt(line.substring(92, 93));
            }
            String quality = line.substring(92, 93);
            if (airTemperautre != MISSING && quality.matches("[01459]")) {
                context.write(new Text(year), new IntWritable(airTemperautre));
            }
        }
    }

    public static class MaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int maxValue = Integer.MIN_VALUE;
            for (IntWritable value : values) {
                maxValue = Math.max(maxValue, value.get());
            }
            context.write(key, new IntWritable(maxValue));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length != 2) {
            System.err.println("Usage:MaxTemperature <input path> <output path>");
            System.exit(-1);
        }
//        Configuration conf = new Configuration();
//        Job job = Job.getInstance(conf, "Max temperature");
        Job job = new Job();
        job.setJobName("Max temperature");

        job.setJarByClass(MaxTemperature.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));//文件名，目录，符合特定文件模式的一系列文件。可多次调用来实现多路径输入
        FileOutputFormat.setOutputPath(job, new Path(args[1]));//只能有一个输出路径，指定输出文件的写入目录。在运行该作业前不应该存在，否则hadoop会报错并拒绝运行作业,,防止数据丢失（覆盖）

        job.setMapperClass(MaxTemperatureMapper.class);
//        job.setCombinerClass(MaxTemperatureReducer.class);//减少map和reduce之间的数据传输量
        job.setReducerClass(MaxTemperatureReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        /*- 如果Mapper和reducer的输出类型不一致，则需要设置mapper的输出类型*/
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
