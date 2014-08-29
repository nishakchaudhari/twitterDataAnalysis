package com.cse587.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TrendingWordCountMapper extends Mapper<LongWritable, Text, LongWritable,Text>{

	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		String next = null;
		while (itr.hasMoreTokens()) {
			next = itr.nextToken();
			Text word = new Text(next);
			context.write(new LongWritable(Long.parseLong(itr.nextToken().toString())),word);
		}
	}
}