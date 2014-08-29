package CoOccurrence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PairsMapper extends Mapper<Object, Text, Text, TextPair>{

	private final static LongWritable one = new LongWritable(1);
	
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String Line =  value.toString();
		String newLine = Line.replaceAll("[_$%^&\\*\\(\\)/+=?/|\\}{~,.;\\:><\'\"\\]\\[!-]+"," ");
		StringTokenizer tokenizer = new StringTokenizer(newLine.toString());
		String next;
		int i =0;
		TextPair count = new TextPair();
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
			for(int j=0;j<hashTags.size();j++)
			{				
				if(i!=j)
				{
					keyVal = new Text(hashTags.get(i));
					count = new TextPair(hashTags.get(j).toString(),one.toString());
					System.out.println("Emitting key: "+keyVal.toString()+"count: "+count);
					context.write(keyVal, count);
				}
				
			}
		}
		
	}
}