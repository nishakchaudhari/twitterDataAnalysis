package com.cse587.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WordCountJob {

	private static final transient Logger LOG = LoggerFactory.getLogger(WordCountJob.class);

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();		

		LOG.info("HDFS Root Path: {}", conf.get("fs.defaultFS"));
		LOG.info("MR Framework: {}", conf.get("mapreduce.framework.name"));
		/* Set the Input/Output Paths on HDFS */
		String inputPath = "/input";
		String outputPath = "/output1";

		// FileOutputFormat wants to create the output directory '/output1' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
		
		//Defining Job1
		//Job to get the word count
		Job job1 = Job.getInstance(conf);
		job1.setJarByClass(WordCountJob.class);
		job1.setMapperClass(WordCountMapper.class);
		job1.setCombinerClass(WordCountReducer.class);
		job1.setReducerClass(WordCountReducer.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(LongWritable.class);
		FileInputFormat.addInputPath(job1, new Path(inputPath));
		FileOutputFormat.setOutputPath(job1, new Path(outputPath));
		
		//Wait for Job1 to complete
		job1.waitForCompletion(true); 
		
		//Setting the output file of 1st Job as input to the rest of the jobs
		inputPath = "/output1";
		
		//Setting the output path for job2
		outputPath = "/output2";
		
		// FileOutputFormat wants to create the output directory '/output2' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
		
		//Defining Job2
		//Job to get the trending word count
		Job job2 = Job.getInstance(conf);
		job2.setJarByClass(WordCountJob.class);
		job2.setMapperClass(TrendingWordCountMapper.class);
        job2.setReducerClass(TrendingWordCountReducer.class);
        job2.setInputFormatClass(TextInputFormat.class);
        job2.setMapOutputKeyClass(LongWritable.class);
        job2.setMapOutputValueClass(Text.class);
        job2.setSortComparatorClass(LongWritable.DecreasingComparator.class);
        job2.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job2, new Path(inputPath));
		FileOutputFormat.setOutputPath(job2, new Path(outputPath));	
		
		//Wait for Job2 to complete
		job2.waitForCompletion(true); 
		
		//Setting the output path for Job3
		outputPath = "/output3";
		
		// FileOutputFormat wants to create the output directory '/output3' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
		
		//Defining Job3
		//Job to get the # counts
		Job job3 = Job.getInstance(conf);
		job3.setJarByClass(WordCountJob.class);
		job3.setMapperClass(HashCountMapper.class);
		job3.setReducerClass(HashCountReducer.class);
		job3.setInputFormatClass(TextInputFormat.class);
		job3.setMapOutputKeyClass(LongWritable.class);
		job3.setMapOutputValueClass(Text.class);
		job3.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		job3.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job3, new Path(inputPath));
		FileOutputFormat.setOutputPath(job3, new Path(outputPath));	

		//Wait for Job3 to complete
		job3.waitForCompletion(true); 
		
		//Setting the output path for Job4
		outputPath = "/output4";
		
		// FileOutputFormat wants to create the output directory '/output4' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
		
		//Defining Job4
		//Job to get the @ counts
		Job job4 = Job.getInstance(conf);
		job4.setJarByClass(WordCountJob.class);
		job4.setMapperClass(AtCountMapper.class);
		job4.setReducerClass(AtCountReducer.class);
		job4.setInputFormatClass(TextInputFormat.class);
		job4.setMapOutputKeyClass(LongWritable.class);
		job4.setMapOutputValueClass(Text.class);
		job4.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		job4.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job4, new Path(inputPath));
		FileOutputFormat.setOutputPath(job4, new Path(outputPath));	
		
		System.exit(job4.waitForCompletion(true) ? 0 : 1);
	}
	
	/**
	 * Delete a folder on the HDFS. This is an example of how to interact
	 * with the HDFS using the Java API. You can also interact with it
	 * on the command line, using: hdfs dfs -rm -r /path/to/delete
	 * 
	 * @param conf a Hadoop Configuration object
	 * @param folderPath folder to delete
	 * @throws IOException
	 */
	private static void deleteFolder(Configuration conf, String folderPath ) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(folderPath);
		if(fs.exists(path)) {
			fs.delete(path,true);
		}
	}
}