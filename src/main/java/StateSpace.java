import com.google.common.collect.Multiset;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class StateSpace {
    Map<Integer, Map<Integer, Multiset<List<String>>>> node = new HashMap<>();
    Map<Integer, Set<Integer>> inArc = new HashMap<>();
    Map<Integer, Set<Integer>> outArc = new HashMap<>();
    //[src,dst] -> arc data
    Map<String, Integer> arcTransition = new HashMap<>();


    public StateSpace( Map<Integer, Map<Integer, Multiset<List<String>>>> node,  Map<Integer, Set<Integer>> inArc,
                       Map<Integer, Set<Integer>> outArc, Map<String, Integer> arcTransition){
        this.node = node;
        this.inArc = inArc;
        this.outArc = outArc;
        this.arcTransition = arcTransition;
    }

    String getGraphVizJson(){
        JSONObject obj = new JSONObject();
        JSONObject nodeObj = new JSONObject();
        JSONObject arcObj = new JSONObject();

        for (int key: node.keySet()){
            String s = "";
            for (int k: node.get(key).keySet()){
                s += node.get(key).get(k).size() + ", ";
            }
            nodeObj.put(key+"",key + "\\n" + s);
        }

        for (int key: outArc.keySet()){
            arcObj.put(key+"",outArc.get(key));
        }

        obj.put("node",nodeObj);
        obj.put("arc",arcObj);

        return obj.toString();
    }

    String getGraphXJson(){
        JSONObject obj = new JSONObject();
        JSONObject nodeObj = new JSONObject();
        JSONObject arcObj = new JSONObject();

        for (int key: node.keySet()){
            JSONObject marking = new JSONObject();
            for (int k: node.get(key).keySet()){
                marking.put(k+"",node.get(key).get(k).toString());
            }
            nodeObj.put(key+"",marking);
        }

        for (String key: arcTransition.keySet()){
            arcObj.put(key,arcTransition.get(key));
        }

        obj.put("node",nodeObj);
        obj.put("arc",arcObj);
        return obj.toString();
    }

    List<List<Integer>> allPathsBetween(int start, int end, List<Integer> inPath){

        List<Integer> path = new ArrayList<>();
        for (int i: inPath){
            path.add(i);
        }
        path.add(start);

        if (start == end){
            List<List<Integer>> temp = new ArrayList<>();
            temp.add(path);
            return temp;
        }

        if (!outArc.containsKey(start)){
            List<List<Integer>> temp = new ArrayList<>();
            return temp;
        }

        List<List<Integer>> result = new ArrayList<>();

        for (int n: outArc.get(start)){
            if (!path.contains(n)){
                List<List<Integer>> newPaths = allPathsBetween(n, end, path);
                for (List<Integer> p: newPaths){
                    result.add(p);
                }
            }
        }
        return result;
    }
    



    void print(String s){
        System.out.println(s);
    }

}
