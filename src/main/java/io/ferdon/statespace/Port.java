package io.ferdon.statespace;

import java.util.Map;

public class Port {
    private String name;
    private String description;
    private Integer identifier;
    private String type;
    private String portType;
    private String elementType;
    private String rules;
    private String restriction;

    Port(Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.description = (String) data.get("description");
        this.identifier = (Integer) data.get("identifier");
        this.type = (String) data.get("type");
        this.portType = (String) data.get("portType");
        this.elementType = (String) data.get("elementType");
        this.rules = (String) data.get("rules");
        this.restriction = (String) data.get("restriction");
    }

    Node getElementID(Map<Integer, Place> places, Map<Integer, Transition> transitions) {
        if (elementType.equals("place")) {
            return places.get(identifier);
        } else {
            return transitions.get(identifier);
        }
    }
}
