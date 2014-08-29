package com.cse587.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMapper extends Mapper<Object, Text, Text, LongWritable>{

	private final static LongWritable one = new LongWritable(1);
	private Text word = new Text();
	private String[] stopWords = {"a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could",
			"did","do","does","else","ever","for","from","get","got","had","has","have","he","her","hers","him","his","how","i","if","in","into","is","it","its","just",
			"let","like","likely","may","me","might","most","must","my","no","nor","not","of","off","often","on","only","or","our","own","said","say","says","she","should",
			"since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to","too","twas","us","wants","was","we","were","what","when","where","which","while",
			"who","whom","why","will","with","would","yet","you","your","http","t","co","s","RT","a","b","c","d","e","f","g","h","j","k","l","m","n","o","p","q","r","t","u","v","w","x","y","z",
			"https","de","que","la","el","en","re","0","1","2","3","4","5","6","7","8","9","above","abroad","according","accordingly","actually","adj","afterwards","again","against","ago","ahead","ain't","allow","allows","alone",
			"along","alongside","already","although","always","amid","amidst","amongst","another","anybody","anyhow","anyone","anything","anyway",
			"anyways","anywhere","apart","appear","appreciate","appropriate","aren't","around","a's","aside","ask","asking","associated","available",
			"away","awfully","back","backward","backwards","became","become","becomes","becoming","before","beforehand","begin","behind",
			"being","believe","below","beside","besides","best","better","between","beyond","both","brief","came","cant","can't","caption",
			"cause","causes","certain","certainly","changes","clearly","c'mon","co.","com","come","comes","concerning","consequently",
			"consider","considering","contain","containing","contains","corresponding","couldn't","course","c's","currently","dare",
			"daren't","definitely","described","despite","didn't","different","directly","doesn't","doing","done","don't","down","downwards",
			"during","each","edu","eg","eight","eighty","either","elsewhere","end","ending","enough","entirely","especially","et","etc","even",
			"evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","fairly","far","farther","few",
			"fewer","fifth","first","five","followed","following","follows","forever","former","formerly","forth","forward","found","four","further",
			"furthermore","gets","getting","given","gives","go","goes","going","gone","gotten","greetings","hadn't","half","happens","hardly","hasn't",
			"haven't","having","he'd","he'll","hello","help","hence","here","hereafter","hereby","herein","here's","hereupon","herself","he's","hi",
			"himself","hither","hopefully","howbeit","however","hundred","i'd","ie","ignored","i'll","i'm","immediate","inasmuch","inc","inc.","indeed",
			"indicate","indicated","indicates","inner","inside","insofar","instead","inward","isn't","it'd","it'll","it's","itself","i've","keep","keeps",
			"kept","know","known","knows","last","lately","later","latter","latterly","least","less","lest","let's","liked","likewise","little","look",
			"looking","looks","low","lower","ltd","made","mainly","make","makes","many","maybe","mayn't","mean","meantime","meanwhile","merely","mightn't",
			"mine","minus","miss","more","moreover","mostly","mr","mrs","much","mustn't","myself","name","namely","nd","near","nearly","necessary","need",
			"needn't","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","nobody","non","none","nonetheless","noone",
			"no-one","normally","nothing","notwithstanding","novel","now","nowhere","obviously","oh","ok","okay","old","once","one","ones","one's",
			"onto","opposite","other","others","otherwise","ought","oughtn't","ours","ourselves","out","outside","over","overall","particular","particularly",
			"past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides","quite","qv","rather","rd","really",
			"reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round","same","saw","saying","second",
			"secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several",
			"shall","shan't","she'd","she'll","she's","shouldn't","six","somebody","someday","somehow","someone","something","sometime","sometimes",
			"somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","take","taken","taking","tell",
			"tends","th","thank","thanks","thanx","that'll","thats","that's","that've","theirs","themselves","thence","thereafter","thereby","there'd",
			"therefore","therein","there'll","there're","theres","there's","thereupon","there've","they'd","they'll","they're","they've","thing","things",
			"think","third","thirty","thorough","thoroughly","those","though","three","through","throughout","thru","thus","till","together","took","toward",
			"towards","tried","tries","truly","try","trying","t's","twice","two","un","under","underneath","undoing","unfortunately","unless","unlike","unlikely",
			"until","unto","up","upon","upwards","use","used","useful","uses","using","usually","value","various","versus","very","via","viz","vs","want","wasn't",
			"way","we'd","welcome","well","we'll","went","we're","weren't","we've","whatever","what'll","what's","what've","whence","whenever","whereafter","whereas",
			"whereby","wherein","where's","whereupon","wherever","whether","whichever","whilst","whither","who'd","whoever","whole","who'll","whomever","who's","whose",
			"willing","wish","within","without","wonder","won't","wouldn't","yes","you'd","you'll","you're","yours","yourself","yourselves","you've","zero"};
	private boolean isAStopWord = false;
	private boolean checkStopWordAfterSplit = false;
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String Line =  value.toString();
		String newLine = Line.replaceAll("[_$%^&\\*\\(\\)/+=?/|\\}{~,.;\\:><\'\"\\]\\[!-]+"," ");
		StringTokenizer itr = new StringTokenizer(newLine.toString());
		String next = null;
		while (itr.hasMoreTokens()) {
			next = itr.nextToken();
			isAStopWord = false;
			for(String check: stopWords)
			{
					if(next.equalsIgnoreCase(check))
					{
						isAStopWord = true;
						break;
					}
			}
			if(!isAStopWord)
			{
				if(next.startsWith("#"))
				{
					String obtainedVal[] = next.split("#");
					checkStopWordAfterSplit = false;
					for(int i=0;i<obtainedVal.length;i++)
					{
						if(!obtainedVal[i].equals(""))
						{
							for(String check: stopWords)
							{
									if(obtainedVal[i].equalsIgnoreCase(check))
									{
										checkStopWordAfterSplit = true;
										break;
									}
							}
							if(!checkStopWordAfterSplit)
							{
								next = "#"+obtainedVal[i];
								word.set(next);
								context.write(word, one);
							}
							
						}
					}
					
				}
				else if(next.startsWith("@"))
				{
					String obtainedVal[] = next.split("@");
					checkStopWordAfterSplit = false;
					for(int i=0;i<obtainedVal.length;i++)
					{
						if(!obtainedVal[i].equals(""))
						{
							for(String check: stopWords)
							{
									if(obtainedVal[i].equalsIgnoreCase(check))
									{
										checkStopWordAfterSplit = true;
										break;
									}
							}
							if(!checkStopWordAfterSplit)
							{
								next = "@"+obtainedVal[i];
								word.set(next);
								context.write(word, one);
							}
						}
					}
				}
				else
				{
					word.set(next);
					context.write(word, one);
				}
			}
		}
	}
}