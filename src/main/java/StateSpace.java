import com.google.common.collect.Multiset;
import org.json.JSONObject;

import java.util.*;

public class StateSpace {
    Map<Integer, Map<Integer, Multiset<List<String>>>> node = new HashMap<>();
    Map<Integer, Set<Integer>> inArc = new HashMap<>();
    Map<Integer, Set<Integer>> outArc = new HashMap<>();


    public StateSpace( Map<Integer, Map<Integer, Multiset<List<String>>>> node,  Map<Integer, Set<Integer>> inArc, Map<Integer, Set<Integer>> outArc){
        this.node = node;
        this.inArc = inArc;
        this.outArc = outArc;
    }

    String getStateSpaceJson(){
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

    List<Integer> fipathBetween(int fromNode, int toNode){
        List<Integer> result = new ArrayList<>();

        return result;
    }

}
