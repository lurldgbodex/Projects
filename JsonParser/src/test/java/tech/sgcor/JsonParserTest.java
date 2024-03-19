package tech.sgcor;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.jupiter.api.Test;
import tech.sgcor.exception.JsonNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonParserTest {

    @Test
    void testParseSimpleJson() {
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"isStudent\": false}";
        Parser parser = new Parser(jsonString);
        JsonObject jsonObject = parser.parse();

        assertThat(jsonObject.getProperty("name")).isEqualTo("John");
        assertThat(jsonObject.getProperty("age")).isEqualTo(30);
        assertThat(jsonObject.getProperty("isStudent")).isEqualTo(false);
    }

    @Test
    void testParseNestedJson() {
        String jsonString = "{\"name\": \"John\", \"address\": {\"city\": \"New York\", \"zip\": 10001}}";
        Parser parser = new Parser(jsonString);
        JsonObject jsonObject = parser.parse();
        JsonObject addressObject = (JsonObject) jsonObject.getProperty("address");

        assertThat(jsonObject.getProperty("name")).isEqualTo("John");
        assertThat(addressObject.getProperty("city")).isEqualTo("New York");
        assertThat(addressObject.getProperty("zip")).isEqualTo(10001);
    }

    @Test
    void testParseArrayJson() {
        String jsonString = "{\"name\": \"John\", \"hobbies\": [\"reading\", \"cooking\", \"gardening\"]}";
        Parser parser = new Parser(jsonString);
        JsonObject jsonObject = parser.parse();
        List<Object> hobbies = (ArrayList) (jsonObject.getProperty("hobbies"));

        assertThat(jsonObject.getProperty("name")).isEqualTo("John");
        assertThat(hobbies.size()).isEqualTo(3);
    }

}
