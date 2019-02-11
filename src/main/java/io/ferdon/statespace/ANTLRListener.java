package io.ferdon.statespace;

import io.ferdon.statespace.generator.mlBaseListener;
import io.ferdon.statespace.generator.mlParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public class ANTLRListener extends mlBaseListener {
    StringBuffer postfix = new StringBuffer();
    private HashSet<String> operator = new HashSet<>(Arrays.asList("=", "<>", "<", ">", "<=", ">=", "andalso", "oralso",
            "not", "+", "-", "*", "/", "if", "else", "then", ")", "(", "^^"));
    private HashMap<String, String> translate = new HashMap<String, String>() {{
        put("andalso", "and");
        put("oralso", "or");
        put("=", "==");
        put("^^", "concat");
    }};

    public void showPostfix() {
        System.out.println(getPostfix());
    }

    public String getPostfix() {
        return postfix.toString();
    }

    private boolean isOperator(String str) {
        return operator.contains(str);
    }

    private void antlrPostfix(ParserRuleContext ruleContext) {
        int n = ruleContext.getChildCount();
        if (n != 1) {
            String str = ruleContext.getChild(1).getText();
            if (!isOperator(str))
                return;
            str = convertOp(str);
            postfix.append(str + " ");
        }
    }

    private String convertOp(String op) {
        if (translate.containsKey(op))
            op = translate.get(op);
        return op;
    }

    @Override
    public void exitIfelsesyntax(mlParser.IfelsesyntaxContext ctx) {
        postfix.append("ifelse ");
    }

    @Override
    public void enterToken(mlParser.TokenContext ctx) {
        postfix.append("[ ");
    }

    @Override
    public void exitToken(mlParser.TokenContext ctx) {
        postfix.append("] ");
    }

    @Override
    public void exitExpr(mlParser.ExprContext ctx) {
        antlrPostfix(ctx);
    }

    @Override
    public void exitCondition(mlParser.ConditionContext ctx) {
        antlrPostfix(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String str = node.getText();
        if (isOperator(str))
            return;
        postfix.append(str + " ");
    }
}