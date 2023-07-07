package com.gaspar.hadoop.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class WordCountRunner {

    public static void main(String[] args) throws IOException {
        JobConf conf = new JobConf(WordCountRunner.class);
        conf.setJobName("WordCount");

        //this input format is for plain text files. Key: line number (LongWritable), value: line (Text)
        conf.setInputFormat(TextInputFormat.class);
        FileInputFormat.setInputPaths(conf, toS3Path(args[0]));

        conf.setMapperClass(WordCountMapper.class);

        conf.setCombinerClass(WordCountReducer.class);
        conf.setReducerClass(WordCountReducer.class);

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setOutputFormat(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(conf, toS3Path(args[1]));

        JobClient.runJob(conf);
    }

    //REPLACE WITH YOUR OWN BUCKET NAME
    private static final String BUCKET_NAME = "tamas-gaspar-hadoop-playground";

    /**
     * Converts a relative path such as '/input' to an S3 path.
     * AWS EMR supports the S3 as a file system natively. EC2 instance profile should
     * include the permissions to access this bucket.
     * @param relativePath Should start with /
     */
    private static Path toS3Path(String relativePath) {
        return new Path("s3://" + BUCKET_NAME + relativePath);
    }

}