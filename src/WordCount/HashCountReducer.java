package com.cse587.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HashCountReducer 
extends Reducer<LongWritable,Text,Text,Text> {

	public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		for (Text val : values) {
		context.write(new Text(val.toString()),new Text(key.toString()));
		}
	}
}
