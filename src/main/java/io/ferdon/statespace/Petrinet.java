package io.ferdon.statespace;

import java.io.*;
import java.util.*;

import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.python.util.PythonInterpreter;

import com.google.common.collect.*;


/* This class initialize a petrinet object
 * Functions are sorted intp 4 types:
 * initializer/parser functions
 * get functions
 * set functions
 * Util functions
 */

/* What does this class do?
 * Store data of a petrinet
 * Compose jython script to find fireable transitions
 * Choose and execute a selected fireable transition and token
 * Update new data to petrinet
 */

/* Algorithm:
 * loop through transitions with T, for each transition t:
 * 		get input places, for each input place p:
 *			get marking of it
 *			add this marking to a list L
 *			if one of input place is empty then break, this transition is not fireable
 *		generate permutations base on the list L
 *		get variable binding, guard, expression (not used) then compose the fireable script
 *		execute fireable script and get the qualified transitions
 *
 * Read the qualified and pick a transition, token to execute
 * Compose execute script and run it
 * read the result and update the marking (remove from input, add to output)
 */

/* Driver code:
 * while(true):
 * 	get fireable
 * 	randomly select a transition and its qualified token
 * 	break if no firable transition OR no qualified token in a fireable transition
 * 	execute and update marking
 */

public class Petrinet implements Serializable {
    //to see an example of fireable script, print composeFireableScript()
    static String fireableScript = "import random\n" +
            "\n" +
            "#T => input places => all unique tokens of each input places => concat to create this list\n" +
            "input = %s\n" +
            "\n" +
            "#T => input places => inArc Variables string inArc[T][index P] => if in hashmap then add [T,index P] else create new key and add\n" +
            "variable = %s\n" +
            "#loop through keys in Variables and generate place holders\n" +
            "%s\n" +
            "\n" +
            "#guard for current transition\n" +
            "G = \"\"\"%s\"\"\"\n" +
            "\n" +
            "#expression on output arc\n" +
            "E = %s\n" +
            "\n" +
            "qualified = []\n" +
            "\n" +
            "#get qualified tokens\n" +
            "for token in input:\n" +
            "    #flag to check if all variable assignments are valid\n" +
            "    flag = True\n" +
            "    %s\n" +
            "    for key in variable:\n" +
            "        for index in variable[key]:\n" +
            "            p,i = index\n" +
            "            if globals()[key] == None:\n" +
            "                globals()[key] = token[p][i]\n" +
            "            else:\n" +
            "                if globals()[key] != token[p][i]:\n" +
            "                    flag = False\n" +
            "                    break\n" +
            "        if not flag:\n" +
            "            break\n" +
            "    #if all Variables are valid then check the guard\n" +
            "    if flag:\n" +
            "        #guard condition\n" +
            "        if eval(G):\n" +
            "            qualified.append(token)\n";


    //to see an example of execute script, print composeExecuteScript()
    static String executeScript = "import random\n" +
            "\n" +
            "#T => input places => all unique tokens of each input places => concat to create this list\n" +
            "qualified = %s\n" +
            "\n" +
            "#T => input places => inArc Variables string inArc[T][index P] => if in hashmap then add [T,index P] else create new key and add\n" +
            "variable = %s\n" +
            "#loop through keys in Variables and generate place holders\n" +
            "%s\n" +
            "\n" +
            "#expression on output arc\n" +
            "E = %s\n" +
            "\n" +
            "#assign variable again, just pick the first value of their apprearance\n" +
            "for key in variable:\n" +
            "    p,i = variable[key][0]\n" +
            "    globals()[key] = qualified[p][i]\n" +
            "\n" +
            "#execute expression and store result tokens\n" +
            "result = []\n" +
            "for e in E:\n" +
            "    exec('def Ex():\\n' + e)\n" +
            "    temp = Ex()\n" +
            "    if (Ex()!=None):\n" +
            "        result.append(temp)\n";


    /**
     * T: number of transitions
     * toColorSet: map a place to its data type
     * toInPlace: from a transition map to input places
     * toOutPlace: from a transition map to output places
     * toVariable: inPlace=>Transition forms an input arc index, on the arc we have variables. syntax: ...get(P).get(T)
     * toExpression: Transition=>outPlace forms an output arc index, on the arc we have expression. syntax: ...get(P).get(T)
     * toGuard: from transition map to its guard condition
     * toMarking: from a place map to its marking
     */
    private int T;
    transient PythonInterpreter pi;
    private int[] TP;
    private Map<Integer, String[]> colorSet = new HashMap<>();
    private Map<Integer, int[]> inPlaces = new HashMap<>();
    private Map<Integer, int[]> outPlaces = new HashMap<>();
    private Map<Pair<Integer, Integer>, String> variables = new HashMap<>();
    private Map<Integer,String> guards = new HashMap<>();
    private Map<Pair<Integer, Integer>, String> expressions = new HashMap<>();
    private Map<Integer,Multiset<List<String>>> markings = new HashMap<>();

    //state space data
    transient StateSpace ss;

    public PythonInterpreter getPi() {
        return pi;
    }

    public void setPi(PythonInterpreter pi) {
        this.pi = pi;
    }

    /**
     * Constructor
     */
    public Petrinet(int T, Map<String, String> placeToColor, int[][] outPlace, int[][] inPlace, String[] markings,
                    String[] guards, Object[][][] expressions, Object[][][] variables) {

        this.pi = new PythonInterpreter();
        this.T = T;
        this.colorSet = parseColorSet(placeToColor);
        this.TP = parseTP(inPlace, outPlace);
        this.inPlaces = parsePlace(inPlace);
        this.outPlaces = parsePlace(outPlace);
        this.markings = parseMarking(markings);
        this.variables = parseExpression(variables);
        this.guards = parseGuard(guards);
        this.expressions = parseExpression(expressions);
    }

    public Petrinet(PetrinetModel model){
        this.T = model.T;
        this.TP = parseTP(model.inPlace, model.outPlace);
        this.pi = new PythonInterpreter();
        this.colorSet = parseColorSet(model.placeToColor);
        this.inPlaces = parsePlace(model.inPlace);
        this.outPlaces = parsePlace(model.outPlace);
        this.markings = parseMarking(model.Markings);
        this.variables = parseExpression(model.Variables);
        this.guards = parseGuard(model.Guards);
        this.expressions = parseExpression(model.Expressions);
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    public int[] getTP() {
        return TP;
    }

    public void setTP(int[] TP) {
        this.TP = TP;
    }

    public Map<Integer, String[]> getColorSet() {
        return colorSet;
    }

    public void setColorSet(Map<Integer, String[]> colorSet) {
        this.colorSet = colorSet;
    }

    public Map<Integer, int[]> getInPlaces() {
        return inPlaces;
    }

    public void setInPlaces(Map<Integer, int[]> inPlaces) {
        this.inPlaces = inPlaces;
    }

    public Map<Integer, int[]> getOutPlaces() {
        return outPlaces;
    }

    public void setOutPlaces(Map<Integer, int[]> outPlaces) {
        this.outPlaces = outPlaces;
    }

    public Map<Pair<Integer, Integer>, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<Pair<Integer, Integer>, String> variables) {
        this.variables = variables;
    }

    public Map<Integer, String> getGuards() {
        return guards;
    }

    public void setGuards(Map<Integer, String> guards) {
        this.guards = guards;
    }

    public Map<Pair<Integer, Integer>, String> getExpressions() {
        return expressions;
    }

    public void setExpressions(Map<Pair<Integer, Integer>, String> expressions) {
        this.expressions = expressions;
    }

    public Map<Integer, Multiset<List<String>>> getMarkings() {
        return markings;
    }

    public void setMarkings(Map<Integer, Multiset<List<String>>> markings) {
        this.markings = markings;
    }

    private Map<Integer, String[]> parseColorSet(Map<String, String> placeToColor) {
        Map<Integer, String[]> result = new HashMap<>();
        for (String key : placeToColor.keySet()) {
            String[] c = placeToColor.get(key).split("\\*");
            result.put(Integer.parseInt(key), c);
        }
        return result;
    }

    void  generateStateSpace() throws IOException, ClassNotFoundException{
        Map<Integer,Multiset<List<String>>> ref = markings;
        Queue<Map<Integer,Multiset<List<String>>>> queue = new LinkedList<>();
        Queue<Integer> index = new LinkedList<>();
        //state space result
        Map<Integer,Map<Integer,Multiset<List<String>>>> node = new HashMap<>();
        Map<Integer,Set<Integer>> outArc = new HashMap<>();
        Map<String, Integer> arcTransition = new HashMap<>();

        //init
        int id = 0;
        queue.add(ref);
        index.add(0);
        node.put(0,ref);

        while(queue.size()>0){
            markings = queue.remove();
            int parentID = index.remove();
            Map<Integer,List<List<List<Object>>>> qualified = getFireable();
            for (int T: qualified.keySet()){
                for (int i=0; i<qualified.get(T).size(); i++){
                    ref = copyMarking();
                    boolean exist = false;

                    String script = composeExecuteScript(qualified,T,i);
                    pi.exec(script);

                    //tokens from input places to remove, tokens to add to output places
                    List<List<String>> add = formatToken((List<List<Object>>) pi.get("result"));
                    List<List<String>> remove = formatToken(qualified.get(T).get(i));

                    if(add.isEmpty()) break;

                    //loop through input places to remove
                    int[] inPlace = inPlaces.get(T);
                    for (int j=0; j<remove.size(); j++) {
                        ref.get(inPlace[j]).remove(remove.get(j));
                    }
                    //loop through out places to add
                    int[] outPlace = outPlaces.get(T);
                    for (int j=0; j<add.size(); j++) {
                        ref.get(outPlace[j]).add(add.get(j));
                    }

                    //now ref is a child marking
                    //check if marking is fresh
                    int existID = 0;
                    for (int key: node.keySet()){
                        if (node.get(key).equals(ref)){
                            exist = true;
                            existID = key;
                            break;
                        }
                    }

                    //add this state to queue & index, update both node and arc
                    if (!exist){
                        //new state so we increase id
                        id += 1;
                        queue.add(ref);
                        index.add(id);

                        //add to state space
                        node.put(id,ref);

                        //update arcs
                        if (outArc.containsKey(parentID)){
                            outArc.get(parentID).add(id);
                        }
                        else{
                            Set<Integer> temp = new HashSet<>();
                            temp.add(id);
                            outArc.put(parentID,temp);
                        }

                        //add new arc transition
                        arcTransition.put("[" + parentID + ", " + id +"]",T);
                    }
                    //if exist then only update new arc
                    else {
                        //out arc
                        if (outArc.containsKey(parentID)) {
                            outArc.get(parentID).add(existID);
                        } else {
                            Set<Integer> temp = new HashSet<>();
                            temp.add(existID);
                            outArc.put(parentID, temp);
                        }

                        //add new arc transition
                        arcTransition.put("[" + parentID + ", " + existID +"]",T);
                    }

                }
            }
        }
        //init state space object in petrinet
        ss = new StateSpace(TP,T,markings.size(),node,outArc,arcTransition);
    }


    /*custom get function
     *
     *
     *
     */
    JSONObject getGraphXSchema(){
        JSONObject obj = new JSONObject();
        obj.put("id",1000);
        for (int p: colorSet.keySet()){
            JSONArray arr = new JSONArray();
            JSONObject token = new JSONObject();
            for (int i=0; i<colorSet.get(p).length; i++){
                switch(colorSet.get(p)[i]){
                    case "STRING":
                        token.put("m" + i,"string_holder");
                        break;
                    case "INT":
                        token.put("m" + i,5);
                        break;
                    case "BOOL":
                        token.put("m" + i,true);
                        break;
                    case "DOUBLE":
                        token.put("m" + i,3.14);
                        break;
                    //treat UNIT token as  place with single integer token
                    case "UNIT":
                        token.put("m" + i,5);
                        break;
                }
            }
            arr.put(token);
            obj.put("P"+p,arr);
        }
        return obj;
    }

    Map<Integer,Multiset<List<String>>> copyMarking() throws IOException,ClassNotFoundException{
        // Serialize to byte[]
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new ObjectOutputStream(os).writeObject(this.markings);
        byte[] buf = os.toByteArray();

        //deserialize to object
        ByteArrayInputStream is = new ByteArrayInputStream(buf);
        return (Map<Integer,Multiset<List<String>>>) new ObjectInputStream(is).readObject();
    }

    private int[] parseTP(int[][] inPlace, int[][] outPlace) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < T; i++) {
            result.add(inPlace[i].length);
            result.add(outPlace[i].length);
            for (int j = 0; j < inPlace[i].length; j++) {
                result.add(inPlace[i][j]);
            }
            for (int j = 0; j < outPlace[i].length; j++) {
                result.add(outPlace[i][j]);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    private Map<Integer, int[]> parsePlace(int[][] places) {
        Map<Integer, int[]> result = new HashMap<>();

        for (int i = 0; i < places.length; i++) {
            result.put(i, places[i]);
        }

        return result;
    }

    private Map<Integer, Multiset<List<String>>> parseMarking(String[] markings) {
        Map<Integer, Multiset<List<String>>> result = new HashMap<>();
        for (int i = 0; i < markings.length; i++) {
            result.put(i, stringToMultiset(markings[i]));
        }

        return result;
    }

    private Map<Pair<Integer, Integer>, String> parseExpression(Object[][][] expressions) {
        Map<Pair<Integer, Integer>, String> result = new HashMap<>();

        for (int transitionId = 0; transitionId < expressions.length; transitionId++) {
            for (int j = 0; j < expressions[transitionId].length; j++) {
                int inPlaceId = (Integer) expressions[transitionId][j][0];
                Pair<Integer, Integer> key = new Pair<>(transitionId, inPlaceId);
                //only parse expression and variable if text != ""
                if (!expressions[transitionId][j][1].equals("")){
                    result.put(key, (String) expressions[transitionId][j][1]);
                }
            }
        }
        return result;
    }

    private Map<Integer, String> parseGuard(String[] guards) {
        Map<Integer, String> result = new HashMap<>();
        for (int transitionId = 0; transitionId < guards.length; transitionId++) {
            if (guards[transitionId].equals("")) {
                result.put(transitionId,"True");
            }
            else {
                result.put(transitionId,guards[transitionId]);
            }
        }

        return result;
    }

    /*set functions
     *
     *
     *
     */
    //send script to jython and read current marking - result marking of this execution
    void executeTransition(Map<Integer, List<List<List<Object>>>> qualified, int selectTransition, int selectToken) {
        String script = composeExecuteScript(qualified, selectTransition, selectToken);
        pi.exec(script);

        //tokens from input places to remove, tokens to add to output places
        List<List<String>> add = formatToken((List<List<Object>>) pi.get("result"));
        List<List<String>> remove = formatToken(qualified.get(selectTransition).get(selectToken));

        //loop through input places to remove
        int[] inPlace = inPlaces.get(selectTransition);
        for (int i = 0; i < remove.size(); i++) {
            markings.get(inPlace[i]).remove(remove.get(i));
        }
        //loop through out places to add
        int[] outPlace = outPlaces.get(selectTransition);
        for (int i = 0; i < add.size(); i++) {
            markings.get(outPlace[i]).add(add.get(i));
        }
    }

    /*get functions
     *
     *
     *
     */

    String getStringMarking() {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < markings.size(); i++) {
            JSONObject temp = new JSONObject();
            for (List<String> l : markings.get(i).elementSet()) {
                temp.put(l.toString(), Integer.toString(markings.get(i).count(l)));
            }
            obj.put(Integer.toString(i), temp);
        }
        return obj.toString();
    }

    //format token to string from jython script
    List<List<String>> formatToken(List<List<Object>> token) {
        List<List<String>> result = new ArrayList<>();
        for (List<Object> a : token) {
            List<String> temp = new ArrayList<>();
            //edge case with unit token
            if (a.size() == 0) {
                result.add(Arrays.asList(""));
            } else {
                for (Object b : a) {
                    if (b instanceof java.lang.Integer) {
                        temp.add(Integer.toString((int) b));
                    } else if (b instanceof java.lang.Double) {
                        temp.add(Double.toString((double) b));
                    } else if (b instanceof java.lang.Boolean) {
                        if (b.toString() == "true") {
                            temp.add("True");
                        } else {
                            temp.add("False");
                        }
                    } else {
                        temp.add("'" + b.toString() + "'");
                    }
                }
                result.add(temp);
            }
        }
        return result;
    }

    //calculate fireable transition of current marking
    Map<Integer, List<List<List<Object>>>> getFireable() {
        Map<Integer, List<List<List<Object>>>> result = new HashMap<>();
        for (int t = 0; t < T; t++) {
            boolean flag = true;
            for (int p : inPlaces.get(t)) {
                if (markings.get(p).size() == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                String script = composeFireableScript(t);
                pi.exec(script);
                List<List<List<Object>>> qualified = (List<List<List<Object>>>) pi.get("qualified");
                if (qualified.size() > 0) {
                    result.put(t, qualified);
                }
            }
        }
        return result;
    }

    //compose script to execute selected transition, token
    String composeExecuteScript(Map<Integer, List<List<List<Object>>>> qualified, int selectTransition, int selectToken) {
        String selection = qualified.get(selectTransition).get(selectToken).toString();
        Map<String, List<List<String>>> obj = getCurrentVariableBinding(selectTransition);
        String binding = formatVariableBinding(obj);
        String varname = formatVariableName(obj, 0);
        //get expression
        String expression = getCurrentExpression(selectTransition);
        return String.format(executeScript, selection, binding, varname, expression);
    }


    //compose script to find fireable transition
    String composeFireableScript(int T) {
        String token = getBindingValue(T).toString();
        //get binding variable
        Map<String, List<List<String>>> obj = getCurrentVariableBinding(T);
        String binding = formatVariableBinding(obj);
        String varname1 = formatVariableName(obj, 0);
        String varname2 = formatVariableName(obj, 1);
        //get guard
        String guard = guards.get(T);
        //get expression
        String expression = getCurrentExpression(T);

        //compose the jython script to calculate fireable transition
        //can change the approach to compile=>put in variable to improve performance
        return String.format(fireableScript, token, binding, varname1, guard, expression, varname2);
    }

    //from transition map to Expressions on output arc
    String getCurrentExpression(int T) {
        String result = "";
        for (int p : this.outPlaces.get(T)) {
            Pair<Integer, Integer> arcKey = new Pair<>(T, p);
            result += "\"\"\"" + expressions.get(arcKey) + "\"\"\",";
        }
        return "[" + result.substring(0, result.length() - 1) + "]";
    }

    //get variable appearance position on input arc, with key as string of [P,T]
    Map<String, List<List<String>>> getCurrentVariableBinding(int T) {
        Map<String, List<List<String>>> binding = new HashMap<>();
        int[] inP = this.inPlaces.get(T);
        for (int i = 0; i < inP.length; i++) {

            Pair<Integer, Integer> arcKey = new Pair<>(T, inP[i]);

            if (variables.containsKey(arcKey)) {
                String[] var = variables.get(arcKey).split(",");

                for (int j = 0; j < var.length; j++) {
                    if (!binding.containsKey(var[j])) {
                        List<List<String>> index = new ArrayList<>();
                        index.add(Arrays.asList(Integer.toString(i), Integer.toString(j)));
                        binding.put(var[j], index);
                    } else {
                        binding.get(var[j]).add(Arrays.asList(Integer.toString(i), Integer.toString(j)));
                    }
                }
            }
        }
        return binding;
    }

    //data type of each Place
    String getBindingType(int T) {
        List<String> type = new ArrayList<>();
        for (int inP : this.inPlaces.get(T)) {
            //get type of fused token
            String s = Arrays.toString(colorSet.get(inP));
            type.add(s);
        }
        return type.toString();
    }

    //get all permutations between input places tokens
    List<List<Object>> getBindingValue(int T) {
        List<List<Object>> token = new ArrayList<>();
        List<List<Object>> value = new ArrayList<>();
        for (int inP : this.inPlaces.get(T)) {
            List<Object> Pi = new ArrayList<>();
            for (List<String> o : markings.get(inP).elementSet()) {
                Pi.add(o);
            }
            token.add(Pi);
        }
        ///send type and all tokens to guard
        for (List<Object> z : Lists.cartesianProduct(token)) {
            value.add(z);
        }
        return value;
    }


    /*Util functions
     *
     *
     *
     */
    String formatVariableName(Map<String, List<List<String>>> binding, int level) {
        String result = "";
        String indent = "";
        if (binding.isEmpty()) return "";

        for (int i = 0; i < level; i++) {
            indent += "    ";
        }

        for (String key : binding.keySet()) {
            if (!key.equals("")) {
                result += key + " = None\n" + indent;
            }
        }
        return result;
    }

    String formatVariableBinding(Map<String, List<List<String>>> binding) {
        //empty then return
        if (binding.isEmpty()) return "{}";
        String result = "";
        for (String key : binding.keySet()) {
            if (!key.equals("")) {
                result += "'" + key + "'" + ":";
                result += binding.get(key).toString() + ",";
            }
        }
        result = result.substring(0, result.length() - 1);
        result = "{" + result + "}";
        return result;
    }

    //	List<String> flatten(List<Object> z) {
    //		List<String> result = new ArrayList<>();
    //		LinkedList<Object> stack = new LinkedList<>(z);
    //		while (!stack.isEmpty()) {
    //			Object e = stack.pop();
    //			if (e instanceof List<?>)
    //				stack.addAll(0, (List<?>)e);
    //			else
    //				result.add((String) e);
    //		}
    //		return result;
    //	}

    static Multiset<List<String>> stringToMultiset(String s) {
        Multiset<List<String>> MP = HashMultiset.create();
        if (s.length() == 0) {
            return MP;
        }

        s = s.replaceAll(" ", "");
        s = s.replaceAll("],", "]:");
        String[] e = s.split(":");
        for (int j = 0; j < e.length; j++) {
            int pos = e[j].indexOf('x');
            int n = 1;
            if (pos > 0) {
                n = Integer.parseInt(e[j].substring(0, pos));
            }

            List<String> data = Arrays.asList(e[j].substring(pos + 2, e[j].length() - 1).split(","));
            MP.add(data, n);
        }
        return MP;
    }

    static void println(String s) {
        System.out.println(s);
    }

}
