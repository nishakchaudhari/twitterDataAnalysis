package Kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Created with IntelliJ IDEA.
 * User: pascal
 * Date: 16-07-13
 * Time: 12:07
 */
public class Driver {

	public static void main(String[] args) throws Exception
	{
		boolean result =false;
		boolean firstTime = true;
		String input ="";
		String output="" ;
		int i =0;
		while(true){
			Configuration conf = new Configuration();
			if(firstTime){
				input = args[0];
				output = args[1]+i;
				firstTime = false;
			}else{
				input = output+"/"+"part-r-00000";
				output = args[1]+i;
			}

			@SuppressWarnings("deprecation")
			Job job = new Job(conf, "k-means");
			job.setJarByClass(Driver.class);
			job.setMapperClass(WordMapper.class);
			job.setReducerClass(wordReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			job.setInputFormatClass(KeyValueTextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			FileInputFormat.addInputPath(job, new Path(input)); 
			FileOutputFormat.setOutputPath(job, new Path(output));
			result = job.waitForCompletion(true);
			Counters jobCntrs = job.getCounters();            
			long terminationValue = jobCntrs.findCounter(stopCounter.counter.numberOfIterations).getValue();
            jobCntrs.findCounter(stopCounter.counter.numberOfIterations).setValue(0);
            if(terminationValue == 0)
            	break;
            i++;
		}
		System.exit(result ? 0 : 1);
	}	
}