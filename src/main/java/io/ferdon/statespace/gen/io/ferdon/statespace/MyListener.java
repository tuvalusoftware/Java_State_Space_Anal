package io.ferdon.statespace.gen.io.ferdon.statespace;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MyListener extends mlBaseListener {
    private List<String> L = new ArrayList<>();
    private HashSet<String> operator = new HashSet<>(Arrays.asList("=", "<>", "<", ">", "<=", ">=", "andalso", "oralso", "not", "+", "-", "*", "/", "if", "else", "then", ")", "("));

    public void showPostfix() {
        for (String str : L) {
            System.out.print(str+ " ");
        }
        System.out.println();
    }
    public String postfix() {
        String ans = new String();
        for (String str : L) {
            str += " ";
            ans += str;
        }
        return ans;
    }
    boolean check(String str) {
        return operator.contains(str);
    }
    @Override public void exitIfelsesyntax(mlParser.IfelsesyntaxContext ctx) {
        L.add("ifelse");
    }

    @Override public void exitIfsyntax(mlParser.IfsyntaxContext ctx) {
        L.add("if");
    }

    @Override public void exitExpr(mlParser.ExprContext ctx) {
        int n = ctx.getChildCount();
        if (n != 1) {
            String str = ctx.getChild(1).getText();
            if (check(str))
                if (str.equals("="))
                    str = str + "=" ;
            L.add(str);
        }
    }
    @Override public void exitCondition(mlParser.ConditionContext ctx) {
        int n = ctx.getChildCount();
        if (n != 1) {
            String str = ctx.getChild(1).getText();
            if (str.equals("="))
                str = str + "=" ;
            L.add(str);
        }
    }

    @Override public void visitTerminal(TerminalNode node) {
        String str = node.getText();
        if (check(str) || str.equals("(") ||str.equals(")"))
            return;
        L.add(str);
    }
}