import java.io.*;
import java.util.*;

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



public class Petrinet implements Serializable{
    //to see an example of fireable script, print composeFireableScript()
    static String fireableScript = "import random\n" +
            "\n" +
            "#T => input places => all unique tokens of each input places => concat to create this list\n" +
            "input = %s\n" +
            "\n" +
            "#T => input places => inArc variables string inArc[T][index P] => if in hashmap then add [T,index P] else create new key and add\n" +
            "variable = %s\n" +
            "#loop through keys in variables and generate place holders\n" +
            "%s\n" +
            "\n" +
            "#guard for current transition\n" +
            "G = '%s'\n" +
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
            "    #if all variables are valid then check the guard\n" +
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
            "#T => input places => inArc variables string inArc[T][index P] => if in hashmap then add [T,index P] else create new key and add\n" +
            "variable = %s\n" +
            "#loop through keys in variables and generate place holders\n" +
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
            "    result.append(Ex())\n";

    //these 2 are used in stringToList()
    int index = 1;
    String temp = "";
    /*data structure
     * T: number of transitions
     * toColorSet: map a place to its data type
     * toInPlace: from a transition map to input places
     * toOutPlace: from a transition map to output places
     * toVariable: inPlace=>Transition forms an input arc index, on the arc we have variables. syntax: ...get(P).get(T)
     * toExpression: Transition=>outPlace forms an output arc index, on the arc we have expression. syntax: ...get(P).get(T)
     * toGuard: from transition map to its guard condition
     * toMarking: from a place map to its marking
     */
    int T;
    transient PythonInterpreter pi;
    int[] TP;
    Map<Integer, String[]> toColorSet = new HashMap<>();
    Map<Integer, int[]> toInPlace = new HashMap<>();
    Map<Integer, int[]> toOutPlace = new HashMap<>();
    Map<String, String> toVariable = new HashMap<>();
    Map<Integer,String> toGuard = new HashMap<>();
    Map<String, String> toExpression = new HashMap<>();
    Map<Integer,Multiset<List<String>>> toMarking = new HashMap<>();

    //state space data
    StateSpace ss;



    //constructor
    public Petrinet(int T, String[] color, int[] TP, String[] M, String[] V, String[] G, String[] E) {
        this.T = T;
        this.TP = TP;
        this.pi = new PythonInterpreter();
        this.toColorSet = parseColorSet(color);
        List<Map<Integer,int[]>> InOutPlace = parseTP(TP);
        this.toInPlace = InOutPlace.get(0);
        this.toOutPlace = InOutPlace.get(1);
        this.toMarking = parseMarking(M);
        this.toVariable = parseVariable(V);
        this.toGuard = parseGuard(G);
        this.toExpression = parseExpression(E);
    }

    /*set functions
     *
     *
     *
     */

    //send script to jython and read current marking - result marking of this execution
    void executeTransition(Map<Integer,List<List<List<Object>>>> qualified,int selectTransition, int selectToken) {
        String script = composeExecuteScript(qualified,selectTransition,selectToken);
        pi.exec(script);

        //tokens from input places to remove, tokens to add to output places
        List<List<String>> add = formatToken((List<List<Object>>) pi.get("result"));
        List<List<String>> remove = formatToken(qualified.get(selectTransition).get(selectToken));

        //loop through input places to remove
        int[] inPlace = toInPlace.get(selectTransition);
        for (int i=0; i<remove.size(); i++) {
            toMarking.get(inPlace[i]).remove(remove.get(i));
        }
        //loop through out places to add
        int[] outPlace = toOutPlace.get(selectTransition);
        for (int i=0; i<add.size(); i++) {
            toMarking.get(outPlace[i]).add(add.get(i));
        }
    }

    void generateStateSpace() throws IOException, ClassNotFoundException{
        Map<Integer,Multiset<List<String>>> ref = toMarking;
        Queue<Map<Integer,Multiset<List<String>>>> queue = new LinkedList<>();
        Queue<Integer> index = new LinkedList<>();
        //state space result
        Map<Integer,Map<Integer,Multiset<List<String>>>> node = new HashMap<>();
        Map<Integer,Set<Integer>> inArc = new HashMap<>();
        Map<Integer,Set<Integer>> outArc = new HashMap<>();
        Map<String, Integer> arcTransition = new HashMap<>();

        //init
        int id = 0;
        queue.add(ref);
        index.add(0);
        node.put(0,ref);

        while(queue.size()>0){
            toMarking = queue.remove();
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

                    //loop through input places to remove
                    int[] inPlace = toInPlace.get(T);
                    for (int j=0; j<remove.size(); j++) {
                        ref.get(inPlace[j]).remove(remove.get(j));
                    }
                    //loop through out places to add
                    int[] outPlace = toOutPlace.get(T);
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
                        //in arc, new key for sure
                        Set<Integer> temp = new HashSet<>();
                        temp.add(parentID);
                        inArc.put(id,temp);

                        //out arc
                        if (outArc.containsKey(parentID)){
                            outArc.get(parentID).add(id);
                        }
                        else{
                            temp = new HashSet<>();
                            temp.add(id);
                            outArc.put(parentID,temp);
                        }

                        //add new arc transition
                        arcTransition.put("[" + parentID + ", " + id +"]",T);
                    }
                    //if exist then only update new arc
                    else {
                        //in arc, already exist for sure
                        inArc.get(existID).add(parentID);

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
        ss = new StateSpace(node,inArc,outArc,arcTransition);
    }


    /*custom get function
     *
     *
     *
     */
    String getGraphXSchema(){
        JSONObject obj = new JSONObject();
        obj.put("id",1000);
        for (int p: toColorSet.keySet()){
            JSONArray arr = new JSONArray();
            JSONObject token = new JSONObject();
            for (int i=0; i<toColorSet.get(p).length; i++){
                switch(toColorSet.get(p)[i]){
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
                }
            }
            arr.put(token);
            obj.put("P"+p,arr);
        }
        return obj.toString();
    }

    String getMarking() {
        JSONObject obj = new JSONObject();
        for (int i=0; i<toMarking.size(); i++) {
            List<String> temp = new ArrayList<>();
            for (List<String> l: toMarking.get(i).elementSet()) {
                temp.add(toMarking.get(i).count(l) + ": " + l.toString());
            }
            obj.put(Integer.toString(i), temp);
        }
        return obj.toString();
    }

    Map<Integer,Multiset<List<String>>> copyMarking() throws IOException,ClassNotFoundException{
        // Serialize to byte[]
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new ObjectOutputStream(os).writeObject(this.toMarking);
        byte[] buf = os.toByteArray();

        //deserialize to object
        ByteArrayInputStream is = new ByteArrayInputStream(buf);
        return (Map<Integer,Multiset<List<String>>>) new ObjectInputStream(is).readObject();
    }


    //format token to string from jython script
    List<List<String>> formatToken(List<List<Object>> token){
        List<List<String>> result = new ArrayList<>();
        for (List<Object> a: token) {
            List<String> temp = new ArrayList<>();
            //edge case with unit token
            if (a.size() == 0) {
                result.add(Arrays.asList(""));
            }
            else {
                for (Object b: a) {
                    if (b instanceof java.lang.Integer) {
                        temp.add(Integer.toString((int) b));
                    }
                    else if (b instanceof java.lang.Double) {
                        temp.add(Double.toString((double) b));
                    }
                    else if (b instanceof java.lang.Boolean){
                        if (b.toString() == "true") {
                            temp.add("True");
                        }
                        else {
                            temp.add("False");
                        }
                    }
                    else {
                        temp.add("'" + b.toString() + "'");
                    }
                }
                result.add(temp);
            }
        }
        return result;
    }

    //calculate fireable transition of current marking
    Map<Integer,List<List<List<Object>>>> getFireable(){
        Map<Integer,List<List<List<Object>>>>result = new HashMap<>();
        for (int t=0; t<T; t++) {
            boolean flag = true;
            for (int p: toInPlace.get(t)) {
                if (toMarking.get(p).size() == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                String script = composeFireableScript(t);
                pi.exec(script);
                List<List<List<Object>>> qualified = (List<List<List<Object>>>) pi.get("qualified");
                if (qualified.size()>0) {
                    result.put(t, qualified);
                }
            }
        }
        return result;
    }

    //compose script to execute selected transition, token
    String composeExecuteScript(Map<Integer,List<List<List<Object>>>> qualified,int selectTransition, int selectToken) {
        String selection = qualified.get(selectTransition).get(selectToken).toString();
        Map<String,List<List<String>>> obj = getCurrentVariableBinding(selectTransition);
        String binding = formatVariableBinding(obj);
        String varname = formatVariableName(obj,0);
        //get expression
        String expression = getCurrentExpression(selectTransition);
        return String.format(executeScript, selection, binding, varname, expression);
    }


    //compose script to find fireable transition
    String composeFireableScript(int T) {
        String token = getBindingValue(T).toString();
        //get binding variable
        Map<String,List<List<String>>> obj = getCurrentVariableBinding(T);
        String binding = formatVariableBinding(obj);
        String varname1 = formatVariableName(obj,0);
        String varname2 = formatVariableName(obj,1);
        //get guard
        String guard = toGuard.get(T);
        //get expression
        String expression = getCurrentExpression(T);

        //compose the jython script to calculate fireable transition
        //can change the approach to compile=>put in variable to improve performance
        return String.format(fireableScript, token, binding, varname1, guard, expression,varname2);
    }

    //from transition map to expressions on output arc
    String getCurrentExpression(int T) {
        String result = "";
        for (int p: this.toOutPlace.get(T)) {
            String arc = "[" + T + ", " + p + "]";
            result += "\"\"\"" + toExpression.get(arc) + "\"\"\",";
        }
        return "[" + result.substring(0,result.length()-1) + "]";
    }

    //get variable appearance position on input arc, with key as string of [P,T]
    Map<String,List<List<String>>> getCurrentVariableBinding(int T){
        Map<String,List<List<String>>> binding = new HashMap<>();
        int[] inP = this.toInPlace.get(T);
        for (int i=0; i<inP.length; i++) {
            String arc = "[" + inP[i] + ", " + T + "]";
            if (toVariable.containsKey(arc)) {
                String[] var = toVariable.get(arc).split(",");
                for (int j=0; j<var.length; j++) {
                    if (!binding.containsKey(var[j])) {
                        List<List<String>> index = new ArrayList<>();
                        index.add(Arrays.asList(Integer.toString(i),Integer.toString(j)));
                        binding.put(var[j],index);
                    }
                    else {
                        binding.get(var[j]).add(Arrays.asList(Integer.toString(i),Integer.toString(j)));
                    }
                }
            }
        }
        return binding;
    }

    //data type of each Place
    String getBindingType(int T) {
        List<String> type = new ArrayList<>();
        for (int inP: this.toInPlace.get(T)) {
            //get type of fused token
            String s = Arrays.toString(toColorSet.get(inP));
            type.add(s);
        }
        return type.toString();
    }

    //get all permutations between input places tokens
    List<List<Object>> getBindingValue(int T) {
        List<List<Object>> token = new ArrayList<>();
        List<List<Object>> value = new ArrayList<>();
        for (int inP: this.toInPlace.get(T)) {
            List<Object> Pi = new ArrayList<>();
            for (List<String> o: toMarking.get(inP).elementSet()) {
                Pi.add(o);
            }
            token.add(Pi);
        }
        ///send type and all tokens to guard
        for (List<Object> z: Lists.cartesianProduct(token)) {
            value.add(z);
        }
        return value;
    }

    /*parsing functions
     *
     *
     *
     */

    //P <= T, foo.get(P).get(T) = variable
    static Map<String, String> parseVariable(String[] V) {
        Map<String, String> toVariable = new HashMap<>();
        for (int i=0; i<V.length; i++) {
            int position = countColonUtil(V[i]);
            String index = Arrays.toString(V[i].substring(0,position).split(","));
            String code = V[i].substring(position+1);
            toVariable.put(index, code);
        }
        return toVariable;
    }

    //
    static Map<String,String> parseExpression(String[] E){
        Map<String, String> toExpression = new HashMap<>();
        for (int i=0; i<E.length; i++) {
            int position = countColonUtil(E[i]);
            String index = Arrays.toString(E[i].substring(0,position).split(","));
            String code = E[i].substring(position+1);
            toExpression.put(index, code);
        }
        return toExpression;
    }

    Map<Integer, String[]> parseColorSet(String[] color){
        Map<Integer, String[]> toColorSet = new HashMap<Integer, String[]>();
        for (int i=0; i<color.length; i++) {
            String[] c = color[i].split("\\*");
            toColorSet.put(i, c);
        }
        return toColorSet;
    }

    List<Map<Integer,int[]>> parseTP(int[] TP) {
        int T = 0;
        int i = 0;
        int in_len = 0;
        int out_len = 0;
        Map<Integer, int[]> toInPlace = new HashMap<Integer, int[]>();
        Map<Integer, int[]> toOutPlace = new HashMap<Integer, int[]>();
        List<Integer> temp = new ArrayList<>();

        while(i < TP.length){
            in_len = TP[i];
            out_len = TP[i+1];
            i += 2;

            temp.clear();

            for (int j=0; j<in_len; j++){
                temp.add(TP[i+j]);
            }
            toInPlace.put(T,temp.stream().mapToInt(Integer::intValue).toArray());
            i += in_len;

            temp.clear();
            for (int j=0; j<out_len; j++){
                temp.add(TP[i+j]);
            }
            toOutPlace.put(T,temp.stream().mapToInt(Integer::intValue).toArray());
            i += out_len;
            T += 1;
        }
        return Arrays.asList(toInPlace,toOutPlace);

    }

    Map<Integer,String> parseGuard(String[] G) {
        Map<Integer,String> toGuard = new HashMap<Integer, String>();
        for (int i=0; i<G.length; i++) {
            if (G[i].length() > 0) {
                toGuard.put(i, G[i]);
            }
            else {
                toGuard.put(i, "True");
            }
        }
        return toGuard;
    }

    Map<Integer,Multiset<List<String>>> parseMarking(String[] M){
        Map<Integer,Multiset<List<String>>> marking = new HashMap<Integer, Multiset<List<String>>>();
        for (int i=0; i<M.length; i++) {
            marking.put(i, stringToMultiset(M[i]));
        }
        return marking;
    }



    /*Util functions
     *
     *
     *
     */
    String formatVariableName(Map<String,List<List<String>>> binding,int level) {
        String result = "";
        String indent = "";
        if (binding.isEmpty()) return "";

        for (int i=0; i<level; i++) {
            indent += "    ";
        }

        for (String key: binding.keySet()) {
            result += key + " = None\n" + indent;
        }
        return result;
    }

    String formatVariableBinding(Map<String,List<List<String>>> binding) {
        String result = "";
        if  (binding.isEmpty()) return "{}";
        for (String key: binding.keySet()) {
            result += "'" + key + "'" + ":";
            result += binding.get(key).toString() + ",";
        }
        result = result.substring(0, result.length()-1);
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

    static Multiset<List<String>> stringToMultiset(String s){
        Multiset<List<String>> MP = HashMultiset.create();
        if (s.length() == 0) {
            return MP;
        }

        s = s.replaceAll(" ", "");
        s = s.replaceAll("],","]:");
        String[] e = s.split(":");
        for (int j=0; j<e.length; j++) {
            int pos = e[j].indexOf('x');
            int n = 1;
            if (pos>0) {
                n = Integer.parseInt(e[j].substring(0, pos));
            }

            List<String> data = Arrays.asList(e[j].substring(pos+2, e[j].length()-1).split(","));
            MP.add(data, n);
        }
        return MP;
    }


    static int countColonUtil(String s) {
        int count = 0;
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) == ',')
                count += 1;
            if (count==2)
                return i;
        }
        return -1;
    }

    static void print(String s) {
        System.out.println(s);
    }

}
