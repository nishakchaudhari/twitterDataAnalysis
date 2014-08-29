package ShortestPath;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA. User: pascal Date: 16-07-13 Time: 12:07
 */
public class WordMapper extends Mapper<Text, Text, Text, Text> {

	private Text Key = new Text();
	private Text Value = new Text();

	public void map(Text key, Text value, Context context) throws IOException, InterruptedException
	{	
		String newKey;
		String[] temp = value.toString().split(" ");
		int distance = Integer.parseInt(temp[0]);
		String[] adjNodes = temp[1].split(":");
		for(int i=0;i<adjNodes.length;i++){
			//String[] temp1 = adjNodes[i].toString().split(":");
			if(adjNodes[i] != ""){
				newKey = adjNodes[i];
				int newDistance = 1 + distance;
				Key.set(newKey);
				Value.set(String.valueOf(newDistance));
				context.write(Key, Value);			
			}
		}
		//Key.set(key);		
		context.write(key, value);				
	}	
}