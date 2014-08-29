package CoOccurrence;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class StripesPartitioner extends Partitioner<Text,MapWritable> {

	@Override
	public int getPartition(Text key, MapWritable value, int numPartitions) {
		return Math.abs(key.hashCode() % numPartitions);
	}
}




