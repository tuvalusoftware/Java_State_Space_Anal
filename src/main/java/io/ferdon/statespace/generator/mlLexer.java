// Generated from /Users/apple/Github/Java_State_Space_Analysis/src/main/java/io/ferdon/statespace/ml.g4 by ANTLR 4.7.2
package io.ferdon.statespace.generator;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class mlLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, IF=8, ELSE=9, 
		THEN=10, GT=11, LESS=12, EQUAL=13, GTandEQUAL=14, EQUALandLESS=15, NotEQUAL=16, 
		Concat=17, AND=18, OR=19, NOT=20, ID=21, INT=22, REAL=23, STRING=24, NEWLINE=25, 
		WS=26;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "IF", "ELSE", 
			"THEN", "GT", "LESS", "EQUAL", "GTandEQUAL", "EQUALandLESS", "NotEQUAL", 
			"Concat", "AND", "OR", "NOT", "ID", "INT", "REAL", "STRING", "NEWLINE", 
			"WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "'/'", "'+'", "'-'", "'('", "')'", "','", "'if'", "'else'", 
			"'then'", "'>'", "'<'", "'='", "'>='", "'<='", "'<>'", "'^^'", "'andalso'", 
			"'oralso'", "'not'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "IF", "ELSE", "THEN", 
			"GT", "LESS", "EQUAL", "GTandEQUAL", "EQUALandLESS", "NotEQUAL", "Concat", 
			"AND", "OR", "NOT", "ID", "INT", "REAL", "STRING", "NEWLINE", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public mlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ml.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\34\u00a1\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\6\26y\n\26\r\26\16\26z\3"+
		"\27\6\27~\n\27\r\27\16\27\177\3\30\7\30\u0083\n\30\f\30\16\30\u0086\13"+
		"\30\3\30\3\30\6\30\u008a\n\30\r\30\16\30\u008b\3\31\3\31\6\31\u0090\n"+
		"\31\r\31\16\31\u0091\3\31\3\31\3\32\5\32\u0097\n\32\3\32\3\32\3\33\6\33"+
		"\u009c\n\33\r\33\16\33\u009d\3\33\3\33\2\2\34\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26"+
		"+\27-\30/\31\61\32\63\33\65\34\3\2\6\4\2C\\c|\3\2\62;\5\2\62;C\\c|\5\2"+
		"\13\13\"\"^^\2\u00a7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\3\67\3\2"+
		"\2\2\59\3\2\2\2\7;\3\2\2\2\t=\3\2\2\2\13?\3\2\2\2\rA\3\2\2\2\17C\3\2\2"+
		"\2\21E\3\2\2\2\23H\3\2\2\2\25M\3\2\2\2\27R\3\2\2\2\31T\3\2\2\2\33V\3\2"+
		"\2\2\35X\3\2\2\2\37[\3\2\2\2!^\3\2\2\2#a\3\2\2\2%d\3\2\2\2\'l\3\2\2\2"+
		")s\3\2\2\2+x\3\2\2\2-}\3\2\2\2/\u0084\3\2\2\2\61\u008d\3\2\2\2\63\u0096"+
		"\3\2\2\2\65\u009b\3\2\2\2\678\7,\2\28\4\3\2\2\29:\7\61\2\2:\6\3\2\2\2"+
		";<\7-\2\2<\b\3\2\2\2=>\7/\2\2>\n\3\2\2\2?@\7*\2\2@\f\3\2\2\2AB\7+\2\2"+
		"B\16\3\2\2\2CD\7.\2\2D\20\3\2\2\2EF\7k\2\2FG\7h\2\2G\22\3\2\2\2HI\7g\2"+
		"\2IJ\7n\2\2JK\7u\2\2KL\7g\2\2L\24\3\2\2\2MN\7v\2\2NO\7j\2\2OP\7g\2\2P"+
		"Q\7p\2\2Q\26\3\2\2\2RS\7@\2\2S\30\3\2\2\2TU\7>\2\2U\32\3\2\2\2VW\7?\2"+
		"\2W\34\3\2\2\2XY\7@\2\2YZ\7?\2\2Z\36\3\2\2\2[\\\7>\2\2\\]\7?\2\2] \3\2"+
		"\2\2^_\7>\2\2_`\7@\2\2`\"\3\2\2\2ab\7`\2\2bc\7`\2\2c$\3\2\2\2de\7c\2\2"+
		"ef\7p\2\2fg\7f\2\2gh\7c\2\2hi\7n\2\2ij\7u\2\2jk\7q\2\2k&\3\2\2\2lm\7q"+
		"\2\2mn\7t\2\2no\7c\2\2op\7n\2\2pq\7u\2\2qr\7q\2\2r(\3\2\2\2st\7p\2\2t"+
		"u\7q\2\2uv\7v\2\2v*\3\2\2\2wy\t\2\2\2xw\3\2\2\2yz\3\2\2\2zx\3\2\2\2z{"+
		"\3\2\2\2{,\3\2\2\2|~\t\3\2\2}|\3\2\2\2~\177\3\2\2\2\177}\3\2\2\2\177\u0080"+
		"\3\2\2\2\u0080.\3\2\2\2\u0081\u0083\t\3\2\2\u0082\u0081\3\2\2\2\u0083"+
		"\u0086\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2"+
		"\2\2\u0086\u0084\3\2\2\2\u0087\u0089\7\60\2\2\u0088\u008a\t\3\2\2\u0089"+
		"\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2"+
		"\2\2\u008c\60\3\2\2\2\u008d\u008f\7$\2\2\u008e\u0090\t\4\2\2\u008f\u008e"+
		"\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\u0093\3\2\2\2\u0093\u0094\7$\2\2\u0094\62\3\2\2\2\u0095\u0097\7t\2\2"+
		"\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099"+
		"\7\f\2\2\u0099\64\3\2\2\2\u009a\u009c\t\5\2\2\u009b\u009a\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2"+
		"\2\2\u009f\u00a0\b\33\2\2\u00a0\66\3\2\2\2\n\2z\177\u0084\u008b\u0091"+
		"\u0096\u009d\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}