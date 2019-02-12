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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, IF=9, 
		ELSE=10, THEN=11, GT=12, LESS=13, EQUAL=14, GTandEQUAL=15, EQUALandLESS=16, 
		NotEQUAL=17, Concat=18, AND=19, OR=20, NOT=21, ID=22, INT=23, REAL=24, 
		STRING=25, NEWLINE=26, WS=27;
	public static final int
		RULE_prog = 0, RULE_ifelsesyntax = 1, RULE_expr = 2, RULE_condition = 3, 
		RULE_multipleToken = 4, RULE_numberToken = 5, RULE_token = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "ifelsesyntax", "expr", "condition", "multipleToken", "numberToken", 
			"token"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'*'", "'/'", "'+'", "'-'", "'('", "')'", "'`'", "','", "'if'", 
			"'else'", "'then'", "'>'", "'<'", "'='", "'>='", "'<='", "'<>'", "'^'", 
			"'andalso'", "'oralso'", "'not'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "IF", "ELSE", "THEN", 
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
		public MultipleTokenContext multipleToken() {
			return getRuleContext(MultipleTokenContext.class,0);
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
			setState(19);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(14);
				ifelsesyntax();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(15);
				token();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(16);
				multipleToken();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(17);
				condition(0);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(18);
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
			setState(21);
			match(IF);
			setState(22);
			condition(0);
			setState(23);
			match(THEN);
			setState(26);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				{
				setState(24);
				ifelsesyntax();
				}
				break;
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
			setState(28);
			match(ELSE);
			setState(31);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IF:
				{
				setState(29);
				ifelsesyntax();
				}
				break;
			case EOF:
			case T__4:
			case ELSE:
				{
				setState(30);
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
			setState(42);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				{
				setState(34);
				match(INT);
				}
				break;
			case REAL:
				{
				setState(35);
				match(REAL);
				}
				break;
			case STRING:
				{
				setState(36);
				match(STRING);
				}
				break;
			case ID:
				{
				setState(37);
				match(ID);
				}
				break;
			case T__4:
				{
				setState(38);
				match(T__4);
				setState(39);
				expr(0);
				setState(40);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(55);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(53);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(44);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(45);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__1) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(46);
						expr(9);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(47);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(48);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__3) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(49);
						expr(8);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(50);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(51);
						match(Concat);
						setState(52);
						expr(7);
						}
						break;
					}
					} 
				}
				setState(57);
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
			setState(69);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				}
				break;
			case 2:
				{
				setState(59);
				expr(0);
				setState(60);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LESS) | (1L << EQUAL) | (1L << GTandEQUAL) | (1L << EQUALandLESS) | (1L << NotEQUAL))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(61);
				expr(0);
				}
				break;
			case 3:
				{
				setState(63);
				match(NOT);
				setState(64);
				condition(2);
				}
				break;
			case 4:
				{
				setState(65);
				match(T__4);
				setState(66);
				condition(0);
				setState(67);
				match(T__5);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(76);
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
					setState(71);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(72);
					_la = _input.LA(1);
					if ( !(_la==AND || _la==OR) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(73);
					condition(4);
					}
					} 
				}
				setState(78);
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

	public static class MultipleTokenContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(mlParser.INT, 0); }
		public TokenContext token() {
			return getRuleContext(TokenContext.class,0);
		}
		public MultipleTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multipleToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterMultipleToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitMultipleToken(this);
		}
	}

	public final MultipleTokenContext multipleToken() throws RecognitionException {
		MultipleTokenContext _localctx = new MultipleTokenContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_multipleToken);
		try {
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EOF:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				match(INT);
				setState(81);
				match(T__6);
				setState(82);
				token();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class NumberTokenContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(mlParser.INT, 0); }
		public NumberTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberToken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).enterNumberToken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof mlListener ) ((mlListener)listener).exitNumberToken(this);
		}
	}

	public final NumberTokenContext numberToken() throws RecognitionException {
		NumberTokenContext _localctx = new NumberTokenContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_numberToken);
		try {
			setState(87);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EOF:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(86);
				match(INT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
		enterRule(_localctx, 12, RULE_token);
		int _la;
		try {
			setState(103);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(90);
				match(T__4);
				setState(91);
				expr(0);
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__7) {
					{
					{
					setState(92);
					match(T__7);
					setState(93);
					expr(0);
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(99);
				match(T__5);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(T__4);
				setState(102);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\35l\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\3\2\3\2\3\2\5\2\26"+
		"\n\2\3\3\3\3\3\3\3\3\3\3\5\3\35\n\3\3\3\3\3\3\3\5\3\"\n\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\5\4-\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\7\48\n\4\f\4\16\4;\13\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5"+
		"\5H\n\5\3\5\3\5\3\5\7\5M\n\5\f\5\16\5P\13\5\3\6\3\6\3\6\3\6\5\6V\n\6\3"+
		"\7\3\7\5\7Z\n\7\3\b\3\b\3\b\3\b\3\b\7\ba\n\b\f\b\16\bd\13\b\3\b\3\b\3"+
		"\b\3\b\5\bj\n\b\3\b\2\4\6\b\t\2\4\6\b\n\f\16\2\6\3\2\3\4\3\2\5\6\3\2\16"+
		"\23\3\2\25\26\2z\2\25\3\2\2\2\4\27\3\2\2\2\6,\3\2\2\2\bG\3\2\2\2\nU\3"+
		"\2\2\2\fY\3\2\2\2\16i\3\2\2\2\20\26\5\4\3\2\21\26\5\16\b\2\22\26\5\n\6"+
		"\2\23\26\5\b\5\2\24\26\5\6\4\2\25\20\3\2\2\2\25\21\3\2\2\2\25\22\3\2\2"+
		"\2\25\23\3\2\2\2\25\24\3\2\2\2\26\3\3\2\2\2\27\30\7\13\2\2\30\31\5\b\5"+
		"\2\31\34\7\r\2\2\32\35\5\4\3\2\33\35\5\16\b\2\34\32\3\2\2\2\34\33\3\2"+
		"\2\2\35\36\3\2\2\2\36!\7\f\2\2\37\"\5\4\3\2 \"\5\16\b\2!\37\3\2\2\2! "+
		"\3\2\2\2\"\5\3\2\2\2#$\b\4\1\2$-\7\31\2\2%-\7\32\2\2&-\7\33\2\2\'-\7\30"+
		"\2\2()\7\7\2\2)*\5\6\4\2*+\7\b\2\2+-\3\2\2\2,#\3\2\2\2,%\3\2\2\2,&\3\2"+
		"\2\2,\'\3\2\2\2,(\3\2\2\2-9\3\2\2\2./\f\n\2\2/\60\t\2\2\2\608\5\6\4\13"+
		"\61\62\f\t\2\2\62\63\t\3\2\2\638\5\6\4\n\64\65\f\b\2\2\65\66\7\24\2\2"+
		"\668\5\6\4\t\67.\3\2\2\2\67\61\3\2\2\2\67\64\3\2\2\28;\3\2\2\29\67\3\2"+
		"\2\29:\3\2\2\2:\7\3\2\2\2;9\3\2\2\2<H\b\5\1\2=>\5\6\4\2>?\t\4\2\2?@\5"+
		"\6\4\2@H\3\2\2\2AB\7\27\2\2BH\5\b\5\4CD\7\7\2\2DE\5\b\5\2EF\7\b\2\2FH"+
		"\3\2\2\2G<\3\2\2\2G=\3\2\2\2GA\3\2\2\2GC\3\2\2\2HN\3\2\2\2IJ\f\5\2\2J"+
		"K\t\5\2\2KM\5\b\5\6LI\3\2\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2\2O\t\3\2\2\2"+
		"PN\3\2\2\2QV\3\2\2\2RS\7\31\2\2ST\7\t\2\2TV\5\16\b\2UQ\3\2\2\2UR\3\2\2"+
		"\2V\13\3\2\2\2WZ\3\2\2\2XZ\7\31\2\2YW\3\2\2\2YX\3\2\2\2Z\r\3\2\2\2[j\3"+
		"\2\2\2\\]\7\7\2\2]b\5\6\4\2^_\7\n\2\2_a\5\6\4\2`^\3\2\2\2ad\3\2\2\2b`"+
		"\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2ef\7\b\2\2fj\3\2\2\2gh\7\7\2\2h"+
		"j\7\b\2\2i[\3\2\2\2i\\\3\2\2\2ig\3\2\2\2j\17\3\2\2\2\16\25\34!,\679GN"+
		"UYbi";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}