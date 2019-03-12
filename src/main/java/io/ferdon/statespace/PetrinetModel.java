/*
 * File name: Transition.java
 * File Description:
 *      The Petrinet class for supporting parsing Json file into Petrinet object.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Dang Duy Nam
 */

package io.ferdon.statespace;

import java.util.Map;

public class PetrinetModel {
    int T;
    Map<String, String> placeToColor;
    Map<String, String> typeToColor;
    Map<String, String> placeToType;
    int[][] outPlace;
    int[][] inPlace;
    String[] Markings;

    String[] Guards;
    Object[][][] Expressions;
    Object[][][] Variables;
    Map<String, Object>[] Ports;

    public Map<String, String> getTypeToColor() {
        return typeToColor;
    }

    public void setTypeToColor(Map<String, String> typeToColor) {
        this.typeToColor = typeToColor;
    }

    public void setPlaceToType(Map<String, String> placeToType) {
        this.placeToType = placeToType;
    }

    public void setOutPlace(int[][] outPlace) {
        this.outPlace = outPlace;
    }

    public void setInPlace(int[][] inPlace) {
        this.inPlace = inPlace;
    }

    public void setMarkings(String[] markings) {
        this.Markings = markings;
    }

    public void setGuards(String[] guards) {
        this.Guards = guards;
    }

    public void setExpressions(Object[][][] expressions) {
        this.Expressions = expressions;
    }

    public void setVariables(Object[][][] variables) {
        this.Variables = variables;
    }

    public PetrinetModel(int t, Map<String, String> placeToColor, Map<String, String> typeToColor,
                         Map<String, String> placeToType, int[][] outPlace, int[][] inPlace, String[] markings,
                         String[] guards, Object[][][] expressions, Object[][][] variables, Map<String, Object>[] ports) {
        T = t;
        this.placeToColor = placeToColor;
        this.typeToColor = typeToColor;
        this.placeToType = placeToType;
        this.outPlace = outPlace;
        this.inPlace = inPlace;
        this.Markings = markings;
        this.Guards = guards;
        this.Expressions = expressions;
        this.Variables = variables;
        this.Ports = ports;
    }

    public PetrinetModel() {

    }

    public int getT() {
        return T;
    }

    public Map<String, String> getPlaceToType() {
        return placeToType;
    }

    public int[][] getOutPlace() {
        return outPlace;
    }

    public int[][] getInPlace() {
        return inPlace;
    }

    public String[] getMarkings() {
        return Markings;
    }

    public String[] getGuards() {
        return Guards;
    }

    public Object[][][] getExpressions() {
        return Expressions;
    }

    public Object[][][] getVariables() {
        return Variables;
    }

    public void setPlaceToColor(Map<String, String> placeToColor) {
        this.placeToColor = placeToColor;
    }

    public Map<String, String> getPlaceToColor() {
        return placeToColor;
    }

    public Map<String, Object>[] getPorts() {
        return Ports;
    }

    public void setPorts(Map<String, Object>[] ports) {
        this.Ports = ports;
    }

    public void setT(int t) {
        T = t;
    }
}
