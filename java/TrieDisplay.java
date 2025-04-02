import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.*;

public class TrieDisplay extends JPanel implements KeyListener
{
	private JFrame frame;
	private int size = 30, width = 1000, height = 600;
	private Trie trie;
	private String word;            // Word you are trying to spell printed in large font
	private char[] likelyChar;        // Used for single most likely character
	private TreeSet<WordNum> nextWords;
	private String firstThreeWords;
	private boolean wordsLoaded;    // Use this to make sure words are alll loaded before you start typing
	private Map<String, Boolean> isaword;		//keeps track of the color for words
	private String tabMostLikely;		//word to autofill
	private JButton buttonDescendants;
	private JButton buttonIncredibles;
	private JButton buttonPrincess;
	private JButton buttonBack;
	private JButton buttonGame;
	private boolean startUI;
	private boolean descendantsUI;
	private boolean princesssUI;
	private boolean incrediblesUI;
	private boolean gameUI;
	private boolean newGame = true;
	private String wordToGuess = "-";
	private ArrayList<String> incorrectGuesses = new ArrayList<String>();
	private StringBuilder currentWord = new StringBuilder("");
	private int lives = 9;
	private File file;
	private URL url;
	private Clip clip;

	public TrieDisplay()
	{
		frame = new JFrame("Trie Next");
		frame.setSize(1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);

		this.setLayout(null);

		word = "";
		likelyChar = new char[3];
		firstThreeWords = "";
		wordsLoaded = false;
		trie = new Trie();
		startUI = true;
		descendantsUI = false;
		princesssUI = false;
		incrediblesUI = false;
		isaword = new HashMap<>();

//sets the buttons up
		buttonDescendants = new JButton("Descendants");
		buttonDescendants.setBounds(130, 350, 160, 50); // Position in JPanel
		buttonDescendants.setBackground(new Color(142, 124, 195));
		buttonDescendants.setForeground(new Color(147, 196, 125));
		buttonDescendants.setFont(new Font("Segoe UI", Font.BOLD, 20));

		buttonIncredibles = new JButton("Incredibles");
		buttonIncredibles.setBounds(130*2 + 160, 350, 160, 50); // Position in JPanel
		buttonIncredibles.setBackground(new Color(224, 102, 102));
		buttonIncredibles.setForeground(new Color(241, 194, 50));
		buttonIncredibles.setFont(new Font("Segoe UI", Font.BOLD, 20));

		buttonPrincess = new JButton("Princesses");
		buttonPrincess.setBounds(130*3 + 160*2, 350, 160, 50); // Position in JPanel
		buttonPrincess.setBackground(new Color(255, 186, 226));
		buttonPrincess.setForeground(new Color(142, 124, 195));
		buttonPrincess.setFont(new Font("Segoe UI", Font.BOLD, 20));

		buttonGame = new JButton("Hangman");
		buttonGame.setBounds(130*2 + 160, 450, 160, 50);
		buttonGame.setBackground(new Color(255, 251, 130));
		buttonGame.setForeground(new Color(0, 81, 255));
		buttonGame.setFont(new Font("Segoe UI", Font.BOLD, 20));

		buttonBack = new JButton("Go Back");
		buttonBack.setBounds(40, 450, 150, 50); // Position in JPanel moved down
		buttonBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
		buttonBack.setVisible(false);

		buttonDescendants.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				word = ""; // clears word, likelyChar, firstThreeWords
				likelyChar = new char[3];
				firstThreeWords = "";
				trie = new Trie(); // resets Trie
				readFileToTrie("..\\res\\descendants_script.txt"); // sends descendants script to readFileToTrie
				wordsLoaded = true;

				startUI = false; // hides everything but the descendants UI
				gameUI = false;
				descendantsUI = true;
				princesssUI = false;
				incrediblesUI = false;

				buttonDescendants.setVisible(false);
				buttonIncredibles.setVisible(false);
				buttonPrincess.setVisible(false);
				buttonBack.setVisible(true);
				buttonGame.setVisible(false);
				buttonBack.setBackground(new Color(147, 196, 125));
				buttonBack.setForeground(new Color(142, 124, 195));

				try
				{
					playMusic("..\\res\\descendants.wav"); // plays descendants music
				}
				catch (LineUnavailableException ex)
				{
					throw new RuntimeException(ex);
				}

				repaint();
				TrieDisplay.this.requestFocusInWindow();
			}
		});

		buttonIncredibles.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				word = ""; // clears word, likelyChar, firstThreeWords
				likelyChar = new char[3];
				firstThreeWords = "";
				trie = new Trie(); // resets Trie
				readFileToTrie("..\\res\\incredibles_scripts.txt");  // sends incredibles script to readFileToTrie
				wordsLoaded = true;

				startUI = false;  // hides everything but the incredibles UI
				gameUI = false;
				descendantsUI = false;
				princesssUI = false;
				incrediblesUI = true;

				buttonDescendants.setVisible(false);
				buttonIncredibles.setVisible(false);
				buttonPrincess.setVisible(false);
				buttonBack.setVisible(true);
				buttonGame.setVisible(false);
				buttonBack.setBackground(new Color(241, 194, 50));
				buttonBack.setForeground(new Color(224, 102, 102));

				try
				{
					playMusic("..\\res\\incredibles.wav"); // plays incredibles music
				}
				catch (LineUnavailableException ex)
				{
					throw new RuntimeException(ex);
				}
				repaint();
				TrieDisplay.this.requestFocusInWindow();
			}
		});

		buttonPrincess.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				word = ""; // clears word, likelyChar, firstThreeWords
				likelyChar = new char[3];
				firstThreeWords = "";
				trie = new Trie(); // resets Trie
				readFileToTrie("..\\res\\princess_scripts.txt");  // sends princesses script to readFileToTrie
				wordsLoaded = true;

				startUI = false;  // hides everything but the princesses UI
				gameUI = false;
				descendantsUI = false;
				princesssUI = true;
				incrediblesUI = false;

				buttonDescendants.setVisible(false);
				buttonIncredibles.setVisible(false);
				buttonPrincess.setVisible(false);
				buttonBack.setVisible(true);
				buttonGame.setVisible(false);
				buttonBack.setBackground(new Color(142, 124, 195));
				buttonBack.setForeground(new Color(255, 186, 226));

				try
				{
					playMusic("..\\res\\rapunzel.wav");  // plays princesses music
				}
				catch (LineUnavailableException ex)
				{
					throw new RuntimeException(ex);
				}
				repaint();
				TrieDisplay.this.requestFocusInWindow();
			}
		});


		buttonBack.addActionListener(new ActionListener() //resets back to start frame
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				word = ""; // clears word, likelyChar, firstThreeWords
				likelyChar = new char[3];
				firstThreeWords = "";
				trie = new Trie(); // resets Trie
				wordsLoaded = false;

				startUI = true;  // hides everything but the start UI
				gameUI = false;
				descendantsUI = false;
				princesssUI = false;
				incrediblesUI = false;

				buttonDescendants.setVisible(true);
				buttonIncredibles.setVisible(true);
				buttonPrincess.setVisible(true);
				buttonBack.setVisible(false);
				buttonGame.setVisible(true);

				repaint();
				TrieDisplay.this.requestFocusInWindow();
			}
		});

		buttonGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				word = ""; // clears word, likelyChar, firstThreeWords
				likelyChar = new char[3];
				firstThreeWords = "";
				trie = new Trie(); // resets frame

				readFileToTrie("..\\res\\princess_scripts.txt");  // sends all scripts to readFileToTrie
				readFileToTrie("..\\res\\descendants_script.txt");
				readFileToTrie("..\\res\\incredibles_scripts.txt");
				wordsLoaded = true;

				startUI = false;  // hides everything but the game UI
				gameUI = true;
				descendantsUI = false;
				princesssUI = false;
				incrediblesUI = false;

				buttonDescendants.setVisible(false);
				buttonIncredibles.setVisible(false);
				buttonPrincess.setVisible(false);
				buttonBack.setVisible(true);
				buttonGame.setVisible(false);
				buttonBack.setBackground(null);
				buttonBack.setForeground(null);

				repaint();
				TrieDisplay.this.requestFocusInWindow();
			}
		});

		this.add(buttonDescendants);
		this.add(buttonIncredibles);
		this.add(buttonPrincess);
		this.add(buttonBack);
		this.add(buttonGame);

		frame.setVisible(true);
		this.requestFocusInWindow();
		setFocusTraversalKeysEnabled(false);			//to be able to use tab
	}

	// All Graphics handled in this method.  Don't do calculations here
	public void paintComponent(Graphics g)
	{
		setFocusTraversalKeysEnabled(false);
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		Color backgroundColor = new Color(164, 194, 244); // Default to startUI
		Color textColor = Color.WHITE; // Default text color

		if (descendantsUI)
		{
			backgroundColor = new Color(142, 124, 195);
			textColor = new Color(147, 196, 125);
		}
		else if (princesssUI)
		{
			backgroundColor = new Color(255, 186, 226);
			textColor = new Color(142, 124, 195);
		}
		else if (incrediblesUI)
		{
			backgroundColor = new Color(224, 102, 102);
			textColor = new Color(241, 194, 50);
		}
		else if (gameUI)
		{
			backgroundColor = new Color(253, 255, 175);
			textColor = Color.BLUE;
		}

		g2.setColor(backgroundColor);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		g2.setFont(new Font("Segoe UI", Font.BOLD, 40));

		g2.setColor(textColor);

		if (wordsLoaded && !gameUI)		//in the predictve word screens
		{
			g2.drawString("Start Typing:", 40, 100);

			g2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
			String[] arr = word.split(" ");

			int x = 40;
			int y = 160;
			FontMetrics metrics = g2.getFontMetrics(new Font("Segoe UI", Font.BOLD, 24));

			for (int i = 0; i < arr.length; i++)
			{		//go through all words typed to display them well
				int wordWidth = metrics.stringWidth(arr[i]);

				if (isaword.containsKey(arr[i]))
					if (isaword.get(arr[i]))
						g2.setColor(Color.GREEN); // Word is valid
					else g2.setColor(Color.RED);   // Word is invalid
				else
					if (trie.contains(arr[i]))
					{
						g2.setColor(Color.GREEN);
						isaword.put(arr[i], true); // Mark as valid
					}
					else if (likelyChar[0] == '_')
					{
						g2.setColor(Color.RED);
						isaword.put(arr[i], false); // Mark as invalid
					}
					else g2.setColor(Color.WHITE);

				if (x + wordWidth <= 900)
				{
					g2.drawString(arr[i], x, y);
					x += wordWidth + 10;
				}
				else
				{
					x = 40;
					y += 25;
					g2.drawString(arr[i], x, y);
					x += wordWidth + 10;
				}
			}

			if (startUI) //homepage ui
				g2.setColor(Color.WHITE);

			if (descendantsUI) //decendants ui
				g2.setColor(new Color(147, 196, 125));

			if (princesssUI) //princess ui
				g2.setColor(new Color(142, 124, 195));

			if (incrediblesUI) //incredibles ui
				g2.setColor(new Color(241, 194, 50));

			g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
//draw likely next word + char, allowing for multiple lines
			if (y <= 150)
			{
				g2.drawString("Likely next char: " + likelyChar[0] + ", " + likelyChar[1] + ", " + likelyChar[2] , 40, 210);
				g2.drawString("Likely next words: " + firstThreeWords, 40, 235);
			}
			else
			{
				g2.drawString("Likely next char: " + likelyChar[0] + ", " + likelyChar[1] + ", " + likelyChar[2], 40, y + 45);
				g2.drawString("Likely next words: " + firstThreeWords, 40, y + 70);
			}

			g2.setColor(Color.MAGENTA);
		}
		else if(startUI) 		//in the start UI
		{
			stopMusic();
			g2.setFont(new Font("Segoe UI", Font.BOLD, 60));
			FontMetrics metrics = g2.getFontMetrics();
			int strWidth = metrics.stringWidth("Disney PWA");
			g2.drawString("Disney PWA", (1000 - strWidth) / 2, 120);

			g2.setFont(new Font("Segoe UI", Font.PLAIN, 21));
			metrics = g2.getFontMetrics();
			strWidth = metrics.stringWidth("Created by:");
			g2.drawString("Created by:", (1000 - strWidth) / 2, 190);

			strWidth = metrics.stringWidth("Aahana, Srinidhi, Nandhikha");
			g2.drawString("Aahana, Srinidhi, Nandhikha", (1000 - strWidth) / 2, 220);

			g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
			metrics = g2.getFontMetrics();
			strWidth = metrics.stringWidth("Select a Franchise:");
			g2.drawString("Select a Franchise:", (1000 - strWidth) / 2, 290);
		}
		else if(gameUI)		//in the gameUI
		{
			stopMusic();
			g2.setFont(new Font("Segoe UI", Font.BOLD, 60));
			FontMetrics metrics = g2.getFontMetrics();
			int strWidth = metrics.stringWidth("Guess the Word!");
			g2.drawString("Guess the Word!", (1000 - strWidth) / 2, 120);

			g2.setFont(new Font("Segoe UI", Font.PLAIN, 21));
			metrics = g2.getFontMetrics();
			strWidth = metrics.stringWidth("Type to guess a random word from all the franchises");
			g2.drawString("Type to guess a random word from all the franchises", (1000 - strWidth) / 2, 190);

			g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
			g2.drawString("Start Typing:", 40, 250);
			g2.setStroke(new BasicStroke(3));

	//draw hangman and stand
			int offsetX = 550; // Move right
			int offsetY = 200; // Move down

			g2.drawLine(50 + offsetX, 300 + offsetY, 200 + offsetX, 300 + offsetY); // Base
			g2.drawLine(125 + offsetX, 300 + offsetY, 125 + offsetX, 50 + offsetY); // Vertical pole
			g2.drawLine(125 + offsetX, 50 + offsetY, 250 + offsetX, 50 + offsetY);  // Top beam
			g2.drawLine(250 + offsetX, 50 + offsetY, 250 + offsetX, 80 + offsetY);  // Rope

			if (9 - lives >= 1) // Head
				g2.drawOval(225 + offsetX, 80 + offsetY, 50, 50);
			if (9 - lives >= 2) // Body
				g2.drawLine(250 + offsetX, 130 + offsetY, 250 + offsetX, 200 + offsetY);
			if (9 - lives >= 3) // Left arm
				g2.drawLine(250 + offsetX, 150 + offsetY, 220 + offsetX, 180 + offsetY);
			if (9 - lives >= 4) // Right arm
				g2.drawLine(250 + offsetX, 150 + offsetY, 280 + offsetX, 180 + offsetY);
			if (9 - lives >= 5) // Left leg
				g2.drawLine(250 + offsetX, 200 + offsetY, 230 + offsetX, 250 + offsetY);
			if (9 - lives >= 6) // Right leg
				g2.drawLine(250 + offsetX, 200 + offsetY, 270 + offsetX, 250 + offsetY);
			if (9 - lives >= 7) // Left hand
				g2.fillOval(210 + offsetX, 175 + offsetY, 10, 10);
			if (9 - lives >= 8) // Right hand
				g2.fillOval(280 + offsetX, 175 + offsetY, 10, 10);
			if (9 - lives >= 9)		//draw face + some end of game stuff
			{
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				g2.fillOval(240 + offsetX, 95 + offsetY, 5, 5); // Left eye
				g2.fillOval(255 + offsetX, 95 + offsetY, 5, 5); // Right eye
				g2.drawArc(240 + offsetX, 105 + offsetY, 20, 10, 0, -180); // Frown
				g2.drawString("The Word Was: " + wordToGuess, 40, 350);
				g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
				g2.drawString("So sad:( Enter to play again!", 40, 380);
			}

			if(newGame) //resets after each new game
			{
				g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
				newGame();
				newGame = false;
			}
			g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
			if(currentWord.toString().equals(wordToGuess))
				g2.drawString("Good Job! Enter to play again!", 40, 380);

			String spacedWord = currentWord.toString().replaceAll("", " ").trim();
			g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
			g2.drawString(spacedWord, 250, 300);
			g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			g2.drawString("Incorrect guesses: " + incorrectGuesses, 40, 400);
			g2.drawString("Lives Left: " + lives, 40, 425);
		}
	}

	@Override
	public void processKeyEvent(KeyEvent e) //separate method needed to call methods to tab
	{
		if (e.getID() == KeyEvent.KEY_RELEASED)	//on release so happens only once each time
			if (e.getKeyCode() == KeyEvent.VK_TAB)
			{
				if(nextWords != null && !nextWords.isEmpty()) //if statement to autofill a word
				{
					String[] words = word.split(" ");
					String currWord = words[words.length-1];		//latest word you typed

					int rand = (int)(Math.random()*(nextWords.size()));		//get a random word that is possible

					Iterator<WordNum> iterator = nextWords.iterator();
					WordNum selectedWord = null;
					for (int i = 0; i <= rand; i++)
						selectedWord = iterator.next();
					String auto = selectedWord.getWord();

					if (auto.startsWith(currWord))		//add only the part of the word that isn’t typed
						words[words.length - 1] = auto;
					word = String.join(" ", words);
				}
				repaint();
				return;
			}
			else keyPressed(e);		//for everything else other than tab, use keyPressed
	}

	public void newGame() //starts a new game
	{
		wordToGuess = trie.getRandomWord();
		incorrectGuesses.clear();
		currentWord = new StringBuilder("");
		lives = 9;
		for (int i = 0; i < wordToGuess.length(); i++)		//makes currentWord have blanks
			currentWord.append("_");
	}

	private void handleGuess(char guessedLetter) //checks if the guess exists as part of the word.
	{
		boolean correctGuess = false;		//if guess is correct of not
		for (int i = 0; i < wordToGuess.length(); i++)		//if the guessed letter is in the word, replace that character in the currentWord stringbuilder to display it
			if (wordToGuess.charAt(i) == guessedLetter) //if the guessed letter is a part of the word, set true
			{
				currentWord.setCharAt(i, guessedLetter);
				correctGuess = true;
			}

		if (!correctGuess && !incorrectGuesses.contains(String.valueOf(guessedLetter)))	//if incorrectguess for the first time, lose a life
		{
			incorrectGuesses.add(String.valueOf(guessedLetter));
			lives--;
		}

		if(currentWord.toString().equals(wordToGuess))	//display currentword at the end
			currentWord = new StringBuilder(wordToGuess);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE)
			System.exit(0);

		if(gameUI) //loads the game if the game ui is true
		{
			if (Character.isLetter(keyCode))		//in the game, registers typing for letters
				handleGuess(Character.toLowerCase((char)keyCode));
			if(keyCode == KeyEvent.VK_ENTER)		//resets game
			{
				newGame = true;
				lives = 9;
				repaint();
			}
		}
		else		//for the predictive word part
		{
			if (keyCode == KeyEvent.VK_BACK_SPACE && word.length() > 0)	//deletes letters
				word = word.substring(0, word.length() - 1);
			if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z)	//types characters
				word += (char) keyCode; // Avoids duplication
			if (keyCode == KeyEvent.VK_QUOTE)	//allows quotes
				word += "'";
			if (keyCode == KeyEvent.VK_SPACE)	//allows spaces for multiple words
				word += " ";

			word = word.toLowerCase();

			String[] words = word.split(" ");
			if (words.length > 0 && words[words.length - 1].length() > 0)	//if there is something typed
			{
				likelyChar = trie.mostLikelyNextChar(words[words.length - 1]);		//gets mostLikely char
				nextWords = trie.getWords(words[words.length - 1]);	//gets mostLikelyWords
				int count = 0;
				firstThreeWords = "";

				for (WordNum currWord : nextWords)	//formats first three most likely words
					if (count < 3)
					{
						firstThreeWords += currWord.getWord() + " = " +  currWord.getCount() + " times; ";
						count++;
					}
					else break;

//formats first three most likely words
				String[] temp = firstThreeWords.split("; ");
				firstThreeWords = "";

				for (int i = 0; i < temp.length - 1; i++)
					firstThreeWords += temp[i] + "; ";
				firstThreeWords += temp[temp.length - 1];
			}
			else likelyChar[0] = '_';
		}

		repaint();
	}

//reads data file
	public void readFileToTrie(String fileName)
	{
		String text = "";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null)
			{
				line = line.toLowerCase();		//makes it all lowercase
				if(line.equals("e") || line.equals("l") || line.equals("k") || line.equals("d") || line.equals("m") || line.equals("v") || line.equals("c") || line.equals("b"))
					//some individual letters were registering as correct, this line makes sure that doesnt happen
					continue;
				else trie.insert(line);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error reading file: " + e.getMessage());
		}
	}

//gets music file and plays the music with an infinite loop
	public void playMusic(String filePath) throws LineUnavailableException
	{
		file = new File(filePath);

		try
		{
			url = file.toURI().toURL(); // Convert file to URL
		}
		catch (MalformedURLException e)
		{
			throw new RuntimeException(e);
		}

		try
		{
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		}
		catch (UnsupportedAudioFileException | IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void stopMusic()		//stops music
	{
		if (clip != null && clip.isRunning())
			clip.stop();
	}


	/*** empty methods needed for interfaces ***/
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void actionPerformed(ActionEvent e) {}

	public static void main(String[] args)
	{
		new TrieDisplay();
	}
}