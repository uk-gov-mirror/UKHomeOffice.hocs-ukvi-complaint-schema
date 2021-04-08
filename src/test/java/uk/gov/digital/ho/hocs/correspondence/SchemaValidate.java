package uk.gov.digital.ho.hocs.correspondence;

import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import com.jayway.jsonpath.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SchemaValidate {

    private ReadContext ctx;

    @Before
    public void loadSchema () throws Exception {

        Path path = Paths.get("src/main/resources/cmsSchema.json");
        String json = new String(Files.readAllBytes(path));
         ctx = JsonPath.parse(json);
    }

    @Test
    public void allStringsHaveMaxLengths () throws Exception {

        JSONArray definitionsResult = ctx.read("$..definitions");
        LinkedHashMap<?, ?> definitionsList = (LinkedHashMap<?, ?>) definitionsResult.get(0);

        for (Object key : definitionsList.keySet()){
            JSONArray definitionProperties = ctx.read(String.format("$.definitions.%s..properties", key.toString()));

            for (Object propertySet : definitionProperties) {
                checkMaxLengthExists(propertySet, key.toString());
            }

        }
        assertTrue(true);
    }

    private void checkMaxLengthExists(Object nodeObject, String definitionKey){
        if (nodeObject instanceof LinkedHashMap<?, ?>) {

            List<LinkedHashMap<?, ?>> nestedObjects = new ArrayList<>();
            Map<?, ?> node = (LinkedHashMap<?, ?>) nodeObject;

            if (
                node.containsKey("type") &&
                node.get("type").equals("string") &&
                !node.containsKey("enum") &&
                !node.containsKey("maxLength")
            ) {
                fail(String.format("A string value in %s is missing a maxLength property.", definitionKey));
            }

            for (Object value : node.values()) {
                if (value instanceof LinkedHashMap<?, ?>) {
                    nestedObjects.add((LinkedHashMap<?, ?>) value);
                }
            }

            for (Object nestedObject : nestedObjects) {
                checkMaxLengthExists(nestedObject, definitionKey);
            }

        } else {
            fail(String.format("JSON Element was not a linkedhashmap object inside %s.", definitionKey));
        }
        assertTrue(true);
    }
}
