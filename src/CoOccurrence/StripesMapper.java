package CoOccurrence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StripesMapper extends Mapper<Object, Text, Text, MapWritable>{
	private final static IntWritable one = new IntWritable(1);
	MapWritable map = new MapWritable();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String Line =  value.toString();
		String newLine = Line.replaceAll("[_$%^&\\*\\(\\)/+=?/|\\}{~,.;\\:><\'\"\\]\\[!-]+"," ");
		StringTokenizer tokenizer = new StringTokenizer(newLine.toString());
		String next;
		int i =0;
		//TextPair count = new TextPair();
		Text keyVal;
		ArrayList<Text> hashTags = new ArrayList<Text>();
		while (tokenizer.hasMoreTokens()) 
		{
			next = tokenizer.nextToken();	
			if(next.startsWith("#"))
			{
				String obtainedVal[] = next.split("#");
				for(i=0;i<obtainedVal.length;i++)
				{
					if(!obtainedVal[i].equals(""))
					{
						next = "#"+obtainedVal[i];
						hashTags.add(new Text(next));
						
					}
				}
				
			}
		}
		for(i =0;i<hashTags.size();i++)
		{
			keyVal = new Text(hashTags.get(i));
			map.clear();
			for(int j=0;j<hashTags.size();j++)
			{				
				if(i!=j)
				{
					if(map.containsKey(hashTags.get(j)))
					{
						System.out.println(hashTags.get(j)+"already present");
						IntWritable tempCount =(IntWritable)map.get(hashTags.get(j));
						IntWritable count = new IntWritable(tempCount.get());
						count.set(count.get()+1);
						System.out.println(count.toString()+"is new count");
						map.put(hashTags.get(j), count);
						System.out.println("Map now looks like:"+map.toString());
					}
					else
					{
						map.put(new Text(hashTags.get(j)), new IntWritable(one.get()));
						System.out.println("Map now looks like:"+map.toString());
					}
					
				}
				
			}
			System.out.println("Emitting key="+keyVal+"Map="+map.toString());
			context.write(keyVal, map);
		}
		
	}

}
