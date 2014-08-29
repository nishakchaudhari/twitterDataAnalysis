package CoOccurrence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class StripesReducer extends Reducer<Text,MapWritable,Text,DoubleWritable>{
	
	public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException 
	{
		int countOfKey1 = 0,countOfKey2=0;
		HashMap<Text, Integer> hMap = new HashMap<Text, Integer>();
		Text key2 = new Text();
		DoubleWritable frequency;
		Text keyPair;
		IntWritable tempCount = new IntWritable(0);
		System.out.println("#########The key here is: "+key);
		System.out.println("Crossed Point1");
		//Iterating over the values received
		for (MapWritable map : values) 
		{
			//iterating over the map retrieved from the values
			System.out.println("Map received: "+map.toString());
			Set<Writable> keys = map.keySet();
			System.out.println("keyset obtained="+keys.toString());
	        for (Writable keyVal : keys) 
	        {
	        	Text keyForMap = new Text((Text)(keyVal));
		    	System.out.println("keyForMap is : "+keyForMap);
		    	//Incrementing the count of key
				countOfKey1++;
				System.out.println("The count of key1 is now="+countOfKey1);
		    	if(hMap.containsKey(keyForMap))
				{
					System.out.println("Key2 already present!");
					tempCount = (IntWritable) map.get(keyForMap);
					countOfKey2 = hMap.get(keyForMap)+ tempCount.get();
					System.out.println("Value of countOfKey2 = "+countOfKey2);
					hMap.put(keyForMap, countOfKey2);
					System.out.println("Added count n put back in map");
					System.out.println("Map now looks like: "+hMap.toString());
				}
				else
				{
					System.out.println("Key2 not present!");
					tempCount = (IntWritable) map.get(keyForMap);
					hMap.put(new Text(keyForMap),tempCount.get());
					System.out.println("Added to map");
					System.out.println("Map now looks like: "+hMap.toString());
				}
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
