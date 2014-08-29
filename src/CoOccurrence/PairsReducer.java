package CoOccurrence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PairsReducer extends Reducer<Text,TextPair,Text,DoubleWritable> {

	public void reduce(Text key, Iterable<TextPair> values, Context context) throws IOException, InterruptedException {
		int countOfKey1 = 0,countOfKey2=0;
		HashMap<Text, Integer> hMap = new HashMap<Text, Integer>();
		Text key2 = new Text();
		Text value;
		DoubleWritable frequency;
		Text keyPair;
		int tempCount =0;
		String txtVal;
		System.out.println("#########The key here is: "+key);
		System.out.println("Crossed Point1");
		for (TextPair val : values) {
			System.out.println("Pair received: "+val.toString());
			key2 = val.getFirstText();
			System.out.println("Key2:"+key2+"to check if anything appended");
			value = val.getSecondText();
			//System.out.println("Value of Key2 ="+value+"to check if anything appended");
			txtVal = value.toString();
			//System.out.println("Text Value of Key2 ="+txtVal+"to check if anything appended");
			tempCount = Integer.parseInt(txtVal);
			System.out.println("Value of Key2 obtained from current keypair = "+tempCount);
			countOfKey1 = countOfKey1 +tempCount ;
			System.out.println("Total of Key2 obtained from current keypair = "+countOfKey1);
			if(hMap.containsKey(key2))
			{
				System.out.println("Key2 already present!");
				value = val.getSecondText();
				txtVal = value.toString();
				tempCount = Integer.parseInt(txtVal);
				countOfKey2 = hMap.get(key2)+ tempCount;
				System.out.println("Value of countOfKey2 = "+countOfKey2);
				hMap.put(key2, countOfKey2);
				System.out.println("Added count n put back in map");
				System.out.println("Map now looks like: "+hMap.toString());
			}
			else
			{
				System.out.println("Key2 not present!");
				value = val.getSecondText();
				txtVal = value.toString();
				tempCount = Integer.parseInt(txtVal);
				hMap.put(new Text(key2),tempCount);
				System.out.println("Added to map");
				System.out.println("Map now looks like: "+hMap.toString());
			}
		}
		System.out.println("Crossed Point2\nFinal results");
		Iterator<Entry<Text, Integer>> it = hMap.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Text, Integer> mp = (Map.Entry<Text, Integer>)it.next();
			key2 = (Text)mp.getKey();
			countOfKey2 = (Integer)mp.getValue();
			keyPair = new Text("( "+key+" "+key2+" )");
			double tempFrequency = (double)countOfKey2/countOfKey1;
			System.out.println("The tempFrequency: "+tempFrequency);
			frequency = new DoubleWritable(tempFrequency);
			System.out.println("The keyPair: "+keyPair);
			System.out.println("The frequency: "+frequency.toString());
			context.write(keyPair, frequency);
		}
	}
}
