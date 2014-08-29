package CoOccurrence;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PairsPartitioner extends Partitioner<Text,TextPair> 
{
	@Override
	public int getPartition(Text key, TextPair value, int numPartitions) {
		return Math.abs(key.hashCode() % numPartitions);
	}

}