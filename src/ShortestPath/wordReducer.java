package ShortestPath;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class wordReducer extends Reducer<Text, Text, Text, Text> {

	private Text result = new Text();
	boolean firstTime = true;
	
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String translations = "";
		ArrayList<Integer> distances = new ArrayList<Integer>(); 
		String adjList = "";
		int originalDistance = 0;
		for (Text val : values) {
			String[] temp = val.toString().split(" ");		
			distances.add(Integer.parseInt(temp[0]));
			if(temp.length > 1){
				adjList = temp[1];
				originalDistance = Integer.parseInt(temp[0]);
			}			
		}
		int minDistance = Collections.min(distances);
		if(minDistance != originalDistance){
			context.getCounter(stopCounter.counter.numberOfIterations).increment(1);
		}
		translations += String.valueOf(minDistance)+" "+adjList ;
		result.set(translations);
		context.write(key, result);
	}
}