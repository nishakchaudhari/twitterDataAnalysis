package CoOccurrence;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextPair implements WritableComparable<TextPair>
{
	private Text firstText;
	private Text secondText;
	
	public TextPair()
	{
		this.firstText = new Text();
		this.secondText = new Text();
	}
	
	public TextPair(Text firstText, Text secondText)
	{
		this.firstText = firstText;
		this.secondText = secondText;
	}
	
	public TextPair(String firstString, String secondString)
	{
		this.firstText = new Text(firstString);
		this.secondText = new Text(secondString);
	}
	
	public Text getFirstText() {
		return firstText;
	}

	public void setFirstText(Text firstText) {
		this.firstText = firstText;
	}

	public Text getSecondText() {
		return secondText;
	}

	public void setSecondText(Text secondText) {
		this.secondText = secondText;
	}
	
	public void set(Text firstText, Text secondText)
	{
		this.firstText = firstText;
		this.secondText = secondText;
	}
	
	public TextPair reverse()
	{
		return new TextPair(this.getSecondText(),this.getFirstText());
	}
	
	@Override
	public String toString()
	{
		return (firstText.toString()+" "+secondText.toString());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		firstText.readFields(in);
		secondText.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		firstText.write(out);
		secondText.write(out);
	}

	@Override
	public int compareTo(TextPair o) {
		// TODO Auto-generated method stub
		int value = firstText.compareTo(o.getFirstText());
		if(value!=0)
			return value;
		return secondText.compareTo(o.getSecondText());
	}

}