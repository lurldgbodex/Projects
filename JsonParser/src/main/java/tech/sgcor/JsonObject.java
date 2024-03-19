package tech.sgcor;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private final Map<String, Object> properties;

    public JsonObject() {
        properties = new HashMap<>();
    }


    public void addProperties(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();

       sb.append("{");

       for (Map.Entry<String, Object> entry : properties.entrySet()) {
           sb.append("\"").append(entry.getKey()).append("\": ");
           Object value = entry.getValue();

           if (value instanceof String) {
               sb.append("\"").append(value).append("\"");
           } else {
               sb.append(value);
           }
           sb.append(", ");
       }

       if (!properties.isEmpty()) {
           sb.setLength(sb.length() - 2);
       }
       sb.append("}");
       return sb.toString();
    }
}
