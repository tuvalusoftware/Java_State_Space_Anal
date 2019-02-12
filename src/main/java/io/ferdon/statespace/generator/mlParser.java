// Generated from /Users/apple/Github/Java_State_Space_Analysis/src/main/java/io/ferdon/statespace/ml.g4 by ANTLR 4.7.2
package io.ferdon.statespace.generator;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class mlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, IF=8, ELSE=9, 
		THEN=10, GT=11, LESS=12, EQUAL=13, GTandEQUAL=14, EQUALandLESS=15, NotEQUAL=16, 
		Concat=17, AND=18, OR=19, NOT=20, ID=21, INT=22, REAL=23, STRING=24, NEWLINE=25, 
		WS=26;
	public static final int
		RULE_prog = 0, RULE_ifelsesyntax = 1, RULE_expr = 2, RULE_condition = 3, 
		RULE_token = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "ifelsesyntax", "expr", "condition", "token"
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

	@Override
	public String getGrammarFileName() { return "ml.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public mlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public IfelsesyntaxContext ifelsesyntax() {
			return getRuleContext(IfelsesyntaxContext.class,0);
		}
		public TokenContext token() {
			return getRuleContext(TokenContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			setState(14);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(10);
				ifelsesyntax();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(11);
				token();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(12);
				condition(0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(13);
				expr(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfelsesyntaxContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(mlParser.IF, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(mlParser.THEN, 0); }
		public TerminalNode ELSE() { return getToken(mlParser.ELSE, 0); }
		public List<IfelsesyntaxContext> ifelsesyntax() {
			return getRuleContexts(IfelsesyntaxContext.class);
		}
		public IfelsesyntaxContext ifelsesyntax(int i) {
			return getRuleContext(IfelsesyntaxContext.class,i);
		}
		public List<TokenContext> token() {
			return getRuleContexts(TokenContext.class);
		}
		public TokenContext token(int i) {
			return getRuleContext(TokenContext.class,i);
		}
		public IfelsesyntaxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifelsesyntax; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterIfelsesyntax(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitIfelsesyntax(this);
		}
	}

	public final IfelsesyntaxContext ifelsesyntax() throws RecognitionException {
		IfelsesyntaxContext _localctx = new IfelsesyntaxContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ifelsesyntax);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			match(IF);
			setState(17);
			condition(0);
			setState(18);
			match(THEN);
			setState(21);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				{
				setState(19);
				ifelsesyntax();
				}
				break;
			case T__4:
			case ELSE:
				{
				setState(20);
				token();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(23);
			match(ELSE);
			setState(26);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				{
				setState(24);
				ifelsesyntax();
				}
				break;
			case EOF:
			case T__4:
			case ELSE:
				{
				setState(25);
				token();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(mlParser.INT, 0); }
		public TerminalNode REAL() { return getToken(mlParser.REAL, 0); }
		public TerminalNode STRING() { return getToken(mlParser.STRING, 0); }
		public TerminalNode ID() { return getToken(mlParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode Concat() { return getToken(mlParser.Concat, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				{
				setState(29);
				match(INT);
				}
				break;
			case REAL:
				{
				setState(30);
				match(REAL);
				}
				break;
			case STRING:
				{
				setState(31);
				match(STRING);
				}
				break;
			case ID:
				{
				setState(32);
				match(ID);
				}
				break;
			case T__4:
				{
				setState(33);
				match(T__4);
				setState(34);
				expr(0);
				setState(35);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(50);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(48);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(39);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(40);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__1) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(41);
						expr(9);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(42);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(43);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__3) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(44);
						expr(8);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(45);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(46);
						match(Concat);
						setState(47);
						expr(7);
						}
						break;
					}
					} 
				}
				setState(52);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode GT() { return getToken(mlParser.GT, 0); }
		public TerminalNode LESS() { return getToken(mlParser.LESS, 0); }
		public TerminalNode EQUAL() { return getToken(mlParser.EQUAL, 0); }
		public TerminalNode GTandEQUAL() { return getToken(mlParser.GTandEQUAL, 0); }
		public TerminalNode EQUALandLESS() { return getToken(mlParser.EQUALandLESS, 0); }
		public TerminalNode NotEQUAL() { return getToken(mlParser.NotEQUAL, 0); }
		public TerminalNode NOT() { return getToken(mlParser.NOT, 0); }
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode AND() { return getToken(mlParser.AND, 0); }
		public TerminalNode OR() { return getToken(mlParser.OR, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		return condition(0);
	}

	private ConditionContext condition(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionContext _localctx = new ConditionContext(_ctx, _parentState);
		ConditionContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_condition, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				}
				break;
			case 2:
				{
				setState(54);
				expr(0);
				setState(55);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LESS) | (1L << EQUAL) | (1L << GTandEQUAL) | (1L << EQUALandLESS) | (1L << NotEQUAL))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(56);
				expr(0);
				}
				break;
			case 3:
				{
				setState(58);
				match(NOT);
				setState(59);
				condition(2);
				}
				break;
			case 4:
				{
				setState(60);
				match(T__4);
				setState(61);
				condition(0);
				setState(62);
				match(T__5);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(71);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ConditionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_condition);
					setState(66);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(67);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(68);
					condition(4);
					}
					} 
				}
				setState(73);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TokenContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_token; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitToken(this);
		}
	}

	public final TokenContext token() throws RecognitionException {
		TokenContext _localctx = new TokenContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_token);
		int _la;
		try {
			setState(88);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(75);
				match(T__4);
				setState(76);
				expr(0);
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__6) {
					{
					{
					setState(77);
					match(T__6);
					setState(78);
					expr(0);
					}
					}
					setState(83);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(84);
				match(T__5);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				match(T__4);
				setState(87);
				match(T__5);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 3:
			return condition_sempred((ConditionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		}
		return true;
	}
	private boolean condition_sempred(ConditionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34]\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\5\2\21\n\2\3\3\3\3\3\3\3\3"+
		"\3\3\5\3\30\n\3\3\3\3\3\3\3\5\3\35\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\5\4(\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\63\n\4\f\4\16\4"+
		"\66\13\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5C\n\5\3\5\3\5"+
		"\3\5\7\5H\n\5\f\5\16\5K\13\5\3\6\3\6\3\6\3\6\3\6\7\6R\n\6\f\6\16\6U\13"+
		"\6\3\6\3\6\3\6\3\6\5\6[\n\6\3\6\2\4\6\b\7\2\4\6\b\n\2\6\3\2\3\4\3\2\5"+
		"\6\3\2\r\22\3\2\24\25\2j\2\20\3\2\2\2\4\22\3\2\2\2\6\'\3\2\2\2\bB\3\2"+
		"\2\2\nZ\3\2\2\2\f\21\5\4\3\2\r\21\5\n\6\2\16\21\5\b\5\2\17\21\5\6\4\2"+
		"\20\f\3\2\2\2\20\r\3\2\2\2\20\16\3\2\2\2\20\17\3\2\2\2\21\3\3\2\2\2\22"+
		"\23\7\n\2\2\23\24\5\b\5\2\24\27\7\f\2\2\25\30\5\4\3\2\26\30\5\n\6\2\27"+
		"\25\3\2\2\2\27\26\3\2\2\2\30\31\3\2\2\2\31\34\7\13\2\2\32\35\5\4\3\2\33"+
		"\35\5\n\6\2\34\32\3\2\2\2\34\33\3\2\2\2\35\5\3\2\2\2\36\37\b\4\1\2\37"+
		"(\7\30\2\2 (\7\31\2\2!(\7\32\2\2\"(\7\27\2\2#$\7\7\2\2$%\5\6\4\2%&\7\b"+
		"\2\2&(\3\2\2\2\'\36\3\2\2\2\' \3\2\2\2\'!\3\2\2\2\'\"\3\2\2\2\'#\3\2\2"+
		"\2(\64\3\2\2\2)*\f\n\2\2*+\t\2\2\2+\63\5\6\4\13,-\f\t\2\2-.\t\3\2\2.\63"+
		"\5\6\4\n/\60\f\b\2\2\60\61\7\23\2\2\61\63\5\6\4\t\62)\3\2\2\2\62,\3\2"+
		"\2\2\62/\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\7\3\2\2"+
		"\2\66\64\3\2\2\2\67C\b\5\1\289\5\6\4\29:\t\4\2\2:;\5\6\4\2;C\3\2\2\2<"+
		"=\7\26\2\2=C\5\b\5\4>?\7\7\2\2?@\5\b\5\2@A\7\b\2\2AC\3\2\2\2B\67\3\2\2"+
		"\2B8\3\2\2\2B<\3\2\2\2B>\3\2\2\2CI\3\2\2\2DE\f\5\2\2EF\t\5\2\2FH\5\b\5"+
		"\6GD\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2J\t\3\2\2\2KI\3\2\2\2L[\3\2"+
		"\2\2MN\7\7\2\2NS\5\6\4\2OP\7\t\2\2PR\5\6\4\2QO\3\2\2\2RU\3\2\2\2SQ\3\2"+
		"\2\2ST\3\2\2\2TV\3\2\2\2US\3\2\2\2VW\7\b\2\2W[\3\2\2\2XY\7\7\2\2Y[\7\b"+
		"\2\2ZL\3\2\2\2ZM\3\2\2\2ZX\3\2\2\2[\13\3\2\2\2\f\20\27\34\'\62\64BISZ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}