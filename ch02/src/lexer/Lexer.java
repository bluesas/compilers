package lexer;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer{

	private Hashtable<String, Token> words = new Hashtable();

	private int line = 1;
	private int peek;

	public Lexer() {
		reverse(new Word(Tag.TRUE, "true"));
		reverse(new Word(Tag.FALSE, "false"));
	}

	public Token scan() throws IOException {
		peek = getNextInput();
		skipWhitespace();
		if (Character.isDigit(peek)) {
			return getNumber();
		}
		if (Character.isLetter(peek)) {
			return getWord();
		}
		Token t = new Token(peek);
		peek = ' ';
		return t;
	}
		
	private void reverse(Word w) {
		words.put(w.lexeme, w);
	}

	private void skipWhitespace() {
		
		for(;;peek = getNextInput()) {
			if ( Character.isWhitespace(peek) ) {
				if ( peek == '\n' ) {
					line++;
				}
			} else {
				break;
			}
		}

	}

	private Num getNumber() {
		int v = 0;
		do {
			v = v * 10 + Character.digit(peek, 10);
			peek = getNextInput();
		} while (Character.isDigit(peek));
		return new Num(v);
	}

	public int getLineNo() {
		return line;
	}

	private Word getWord() {
		StringBuffer b = new StringBuffer();
		do {
			b.append((char)peek);
			peek = getNextInput();
		} while ( Character.isLetterOrDigit(peek) );

		String s = b.toString();
		Word w = (Word) words.get(s);
		if ( w != null ) {
			return w;
		}

		w = new Word(Tag.ID, s);
		words.put(s, w);
		return w;
	}

	private char getNextInput(){
		try {
			return (char) System.in.read();
		} catch (Exception e) {
			return ' ';
		}
	}
}
