package Kmeans;

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

	int c1, c2, c3;
	private boolean firstTime =true;
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException
	{
		if(firstTime){
			int[] clusters = getMeans();
			c1 = clusters[0];
			c2 = clusters[1];
			c3 = clusters[2];

			firstTime = false;
		}
		if(!value.toString().equals("")){
			String newKey = "";
			String newValue = "";
			String[] temp = value.toString().split("\t");
			int c1Dist = Math.abs(Integer.parseInt(temp[0]) - c1); 
			int c2Dist = Math.abs(Integer.parseInt(temp[0]) - c2);
			int c3Dist = Math.abs(Integer.parseInt(temp[0]) - c3);
			if((c1Dist <= c2Dist) && (c1Dist<=c3Dist)){
				newKey += "1" ;
			}else if((c2Dist <= c1Dist)&& (c2Dist<= c3Dist)){
				newKey += "2" ;      		    			
			}else{
				newKey += "3" ;      		  
			}
			newValue = key+"\t"+value+"\t"+newKey;

			Key.set(newKey);
			Value.set(newValue);
			context.write(Key, Value);			
		}
	}
	private int[] getMeans(){
		int[] clusters = new int[3];
		try{
			for(int j=0;j<3;j++){
				Path path=new Path("/clusters"+(j+1)+".txt");
				FileSystem fs = FileSystem.get(new Configuration());
				BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(path)));
				String line;
				line=br.readLine();
				clusters[j] = Integer.parseInt(line);			
				br.close();
			}
		}catch(Exception e){
		}
		return clusters;
	}
}