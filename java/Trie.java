import java.util.*;
public class Trie
{
	private TrieNode root;

	public Trie()
	{
		root = new TrieNode();
	}

	public void insert(String word)
	{
		TrieNode curr = root;
		for (char c : word.toCharArray())
		{
			Map<Character, TrieNode> kids = curr.getChildren();
			if (kids.containsKey(c))
			{
				curr = kids.get(c);
				curr.incrementCount();
			}
			else
			{
				kids.put(c, new TrieNode());
				curr = kids.get(c);
			}
		}
		curr.setEndOfWord();
	}

	public boolean contains(String word)
	{
		TrieNode curr = root;
		for (char c : word.toCharArray())
		{
			Map<Character, TrieNode> kids = curr.getChildren();

			if (kids.containsKey(c))
				curr = kids.get(c);
			else return false;
		}
		return curr.isEndOfWord();
	}

	public String getRandomWord() //gets a random word based on the list valid words
	{
		List<String> validWords = new ArrayList<>();
		randomwordhelper(root, "", validWords, 4); //calls randomwordhelper to generate the list
		if (validWords.isEmpty())
			return null;

		Random rand = new Random();
		return validWords.get(rand.nextInt(validWords.size()));
	}

	private void randomwordhelper(TrieNode node, String prefix, List<String> words, int minLength) //if the word meets all the requirements, it will be added to a list
	{
		if (node == null) return;

		if (node.isEndOfWord() && prefix.length() >= minLength && !prefix.contains("'"))		//requirements
			words.add(prefix);

		for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet())		//recursive to all children to complete the prefix
			randomwordhelper(entry.getValue(), prefix + entry.getKey(), words, minLength);
	}


	public char[] mostLikelyNextChar(String str)
	{
		char[] mostLikely = {'_', '_', '_'};
		TrieNode curr = goToNode(str);
		if (curr == null)
			return mostLikely;

		List<Map.Entry<Character, TrieNode>> sortedEntries = new ArrayList<>(curr.getChildren().entrySet());
		sortedEntries.sort((a, b) -> Integer.compare(b.getValue().getCount(), a.getValue().getCount()));  //sorts chars and trienodes based on count


		for (int i = 0; i < 3; i++)
			if (i < sortedEntries.size())
				mostLikely[i] = sortedEntries.get(i).getKey();
			else
				mostLikely[i] = '_';
		return mostLikely;
	}


	private TrieNode goToNode(String str)
	{
		TrieNode curr = root;
		for (char c : str.toCharArray())
		{
			Map<Character, TrieNode> kids = curr.getChildren();
			if (!kids.containsKey(c))
				return null;
			curr = kids.get(c);
		}
		return curr;
	}

	public TreeSet<WordNum> getWords(String str) // returns TreeSet<WordNum> of most likely words based given string
	{
		TrieNode curr = goToNode(str);
		TreeSet<WordNum> map = new TreeSet<WordNum>();
		StringBuilder sb = new StringBuilder(str);

		collectWords(curr, map, sb);

		return map;
	}

	private void collectWords(TrieNode node, TreeSet<WordNum> ds, StringBuilder prefix)
	{
		if(node != null && node.wordEndCount > 0)
			ds.add(new WordNum(prefix.toString(), node.wordEndCount));

		if(node != null)
		{
			Map<Character, TrieNode> kids = node.getChildren();

			for (Map.Entry<Character, TrieNode> entry : kids.entrySet())
			{
				collectWords(entry.getValue(), ds, prefix.append(entry.getKey()));		//collect all the words for all the prefixes
				prefix.setLength(prefix.length()-1);		//shorten the prefix to take a different path
			}
		}
	}

//internal class
	class TrieNode
	{
		int count; //counts num of times path is taken
		int wordEndCount; //counts num of times full word appears
		Map<Character, TrieNode> children;
		public TrieNode()
		{
			count = 1;
			wordEndCount = 0;
			children = new HashMap<Character, TrieNode>();
		}

		private Map<Character, TrieNode> getChildren()
		{
			return children;
		}

		private void incrementCount()
		{
			count++;
		}

		private int getCount()
		{
			return count;
		}

		private boolean isEndOfWord()
		{
			return (wordEndCount > 0);
		}

		private void setEndOfWord()
		{
			this.wordEndCount++;
		}

		public String toString()
		{
			return "(" + count + "," + wordEndCount + ")";
		}
	}
}