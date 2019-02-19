// Generated from /Users/apple/Github/Java_State_Space_Analysis/src/main/java/io/ferdon/statespace/ml.g4 by ANTLR 4.7.2
package io.ferdon.statespace.generator;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link mlParser}.
 */
public interface mlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link mlParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(mlParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(mlParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#ifelsesyntax}.
	 * @param ctx the parse tree
	 */
	void enterIfelsesyntax(mlParser.IfelsesyntaxContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#ifelsesyntax}.
	 * @param ctx the parse tree
	 */
	void exitIfelsesyntax(mlParser.IfelsesyntaxContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(mlParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(mlParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(mlParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(mlParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#multipleToken}.
	 * @param ctx the parse tree
	 */
	void enterMultipleToken(mlParser.MultipleTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#multipleToken}.
	 * @param ctx the parse tree
	 */
	void exitMultipleToken(mlParser.MultipleTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#numberToken}.
	 * @param ctx the parse tree
	 */
	void enterNumberToken(mlParser.NumberTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#numberToken}.
	 * @param ctx the parse tree
	 */
	void exitNumberToken(mlParser.NumberTokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link mlParser#token}.
	 * @param ctx the parse tree
	 */
	void enterToken(mlParser.TokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link mlParser#token}.
	 * @param ctx the parse tree
	 */
	void exitToken(mlParser.TokenContext ctx);
}