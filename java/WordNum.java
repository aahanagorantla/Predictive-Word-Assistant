public class WordNum implements Comparable<WordNum>
{
	String word; int count;

	public WordNum(String word, int count)
	{
		this.word = word;
		this.count = count;
	}

	public int getCount()
	{
		return count;
	}

	public void incrementCount()
	{
		count++;
	}

	public String getWord()
	{
		return word;
	}

	@Override
	public String toString()
	{
		return "word: "+word+"; count: "+count+" times";
	}

	@Override
	public int compareTo(WordNum w)
	{
		return Integer.compare(w.count, count); //Integer.compare(count, w.count);
	}
}