package Kmeans;

import org.apache.hadoop.conf.Configuration;
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
	String[] oldCentroids;
	boolean firstTime = true;
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		long Sum = 0;
		long Count = 0;
		String oldKey = key.toString(); 
	
		Path path = new Path("/users"+oldKey+".txt");
		FileSystem fs = FileSystem.get(new Configuration());
		BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.create(path,true)));
		// TO append data to a file, use fs.append(Path f)
		
		for (Text val : values) {
			String translations = "";
			String[] temp = val.toString().split("\t");
			Sum += Integer.parseInt(temp[1]);
			Count++;
			key.set(temp[0]);
			translations += temp[1]+"\t"+temp[2] ;
			result.set(translations);
			context.write(key, result);
			br.write(temp[0]+"\t"+temp[1]);
			br.newLine();
		}		
		long mean = Sum/Count;
		writeMeanToFile(mean,context,oldKey);
		writeCountToFile(Count,oldKey);
		br.close();
	}
	private void writeMeanToFile(long mean,Context context,String key){
		try{
			String data = readFile(key);	
			Path path=new Path("/clusters"+key+".txt");
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.create(path,true)));
			// TO append data to a file, use fs.append(Path f)
			br.write(String.valueOf(mean));
			br.close();
			compareAndIncrement(data,mean,context);

		}catch(Exception e){
			System.out.println("File not found");
		}
	}
	private void writeCountToFile(long count,String key){
		try{
			String data = readFile(key);	
			Path path=new Path("/count"+key+".txt");
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.create(path,true)));
			// TO append data to a file, use fs.append(Path f)
			br.write(String.valueOf(count));
			br.close();			
		}catch(Exception e){
			System.out.println("File not found");
		}
	}
	private void compareAndIncrement(String data,long mean,Context context){
		if(Integer.parseInt(data)!= mean){
			context.getCounter(stopCounter.counter.numberOfIterations).increment(1);
		}
	}
	private String readFile(String key){
		String data = "";
		try{
			Path path=new Path("/clusters"+key+".txt");
			FileSystem fs = FileSystem.get(new Configuration());
			BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));
			data=br.readLine();
			br.close();
		}catch(Exception e){
		}
		return data;
	}
}