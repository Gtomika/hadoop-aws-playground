package com.gaspar.hadoop.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.StringTokenizer;

/*
The InputFormat determines what kind of key-value pairs do the mapper receive.
By default, it is the TextInputFormat which uses LongWritable for key (line number) and Text for the value (the line).
 */
public class WordCountMapper extends MapReduceBase implements Mapper<LongWritable, Text,Text, IntWritable> {

    private final static IntWritable ONE = new IntWritable(1);

    public void map(
            LongWritable key,
            Text value,
            OutputCollector<Text,IntWritable> output,
            Reporter reporter
    ) throws IOException {
        String line = value.toString();
        //splits by whitespaces
        StringTokenizer tokenizer = new StringTokenizer(line);
        while (tokenizer.hasMoreTokens()){
            Text word = new Text(tokenizer.nextToken());
            output.collect(word, ONE);
        }
    }

}
