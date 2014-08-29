package CoOccurrence;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CoOccurrenceJob {

	private static final transient Logger LOG = LoggerFactory.getLogger(CoOccurrenceJob.class);

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();		

		LOG.info("HDFS Root Path: {}", conf.get("fs.defaultFS"));
		LOG.info("MR Framework: {}", conf.get("mapreduce.framework.name"));
		/* Set the Input/Output Paths on HDFS */
		String inputPath = "/input";
		String outputPath = "/outputPairs";

		// FileOutputFormat wants to create the output directory '/output1' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
		
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(CoOccurrenceJob.class);
		job.setMapperClass(PairsMapper.class);
		job.setReducerClass(PairsReducer.class);
		job.setNumReduceTasks(3);
		job.setPartitionerClass(PairsPartitioner.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(TextPair.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(inputPath));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));	
		job.waitForCompletion(true);
		
		outputPath = "/outputStripes";
		
		// FileOutputFormat wants to create the output directory '/output1' itself..If it exists, delete it
		deleteFolder(conf,outputPath);
				
		Job job2 = Job.getInstance(conf);
		job2.setJarByClass(CoOccurrenceJob.class);
		job2.setMapperClass(StripesMapper.class);
		job2.setReducerClass(StripesReducer.class);
		job2.setNumReduceTasks(3);
		job2.setPartitionerClass(StripesPartitioner.class);
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(MapWritable.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job2, new Path(inputPath));
		FileOutputFormat.setOutputPath(job2, new Path(outputPath));	
		
		System.exit(job2.waitForCompletion(true) ? 0 : 1);
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