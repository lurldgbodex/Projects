package tech.sgcor;

import java.util.List;

public class JsonParser {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java JsonParser <json_string>");
            System.exit(1);
        }

        String jsonString = args[0];

        Parser json = new Parser(jsonString);
        JsonObject parsedJson = json.parse();
        System.out.println("JsonObject: " + parsedJson);
        System.exit(0);
    }
}
