package parser;

import exceptions.JsonException;
import models.JsonBoolean;
import models.JsonElement;
import models.JsonList;
import models.JsonNumber;
import models.JsonObject;
import models.JsonString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Input: String
 * Output: JsonElement
 *
 * {
 *   "a": "b",
 *   "c": [
 *     1,
 *     [
 *       2,
 *       3
 *     ],
 *     "hello",
 *     "world"
 *   ],
 *   "d": [
 *     {
 *       "e": "f",
 *       "g": true
 *     },
 *     101
 *   ],
 *   "h": {
 *     "i": {
 *       "j": {
 *         "k": 11
 *       }
 *     }
 *   }
 * }
 *
 * Todo: Define exception for each ∂ata type parser, pretty print multiple exceptions stacktrace
 * Todo: Write unit tests
 */

public class JsonParser {
    int cursor;
    String jsonString;
    public JsonElement parse(String jsonString) {
        this.cursor = 0;
        this.jsonString = jsonString;
        // skip whitespace in the beginning
        skipWhitespace();

        JsonElement result;
        try {
            result = parseElement();
        } catch (JsonException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonException(e);
        }

        // skip whitespace in the end
        skipWhitespace();

        if (cursor == jsonString.length()) {
            return result;
        } else {
            throw new JsonException("Invalid literal after a valid json element");
        }
    }
    public JsonElement parseElement() {
        if (JsonParserUtil.checkJsonObjectBegin(jsonString, cursor)) {
            // object
            return parseObject();
        } else if (JsonParserUtil.checkJsonListBegin(jsonString, cursor)) {
            // list
            return parseList();

        } else if (JsonParserUtil.checkJsonStringBegin(jsonString, cursor)) {
            // string
            return parseString();

        } else if (JsonParserUtil.checkJsonNumberBegin(jsonString, cursor)) {
            // number
            return parseNumber();

        } else if (JsonParserUtil.checkJsonBooleanBegin(jsonString, cursor)) {
            // boolean
            return parseBoolean();
        } else {
            // exception
            throw new JsonException("Invalid json element");
        }
    }

    private void skipWhitespace() {
        while(cursor < jsonString.length() && Character.isWhitespace(jsonString.charAt(cursor))) {
            cursor++;
        }
    }

    private JsonObject parseObject() {
        assert JsonParserUtil.checkJsonObjectBegin(jsonString, cursor);
        cursor++; // {

        // skip whitespace
        skipWhitespace();

        Map<String, JsonElement> elementMap = new HashMap<>();
        while (!JsonParserUtil.checkJsonObjectEnd(jsonString, cursor)) {
            // skip whitespace
            skipWhitespace();

            // key = parseString
            String key = parseString().getVal();

            // skip whitespace
            skipWhitespace();

            // skip colon
            if (!JsonParserUtil.checkJsonObjectKeyValueSeparator(jsonString, cursor)) {
                throw new JsonException("Invalid key value separator");
            }
            cursor++; // :

            // skip whitespace
            skipWhitespace();

            // parseElement
            JsonElement value = parseElement();
            elementMap.put(key, value);

            // skip whitespace
            skipWhitespace();

            // read comma or }
            // if comma loop again to Step1
            // if } return
            if (JsonParserUtil.checkCommaSeparator(jsonString, cursor)) {
                cursor++; // ,
            } else if (!JsonParserUtil.checkJsonObjectEnd(jsonString, cursor)) {
                throw new JsonException("Invalid literal after key-value pair");
            }
        }

        assert JsonParserUtil.checkJsonObjectEnd(jsonString, cursor);
        cursor++; // }

        return new JsonObject(elementMap);
    }

    private JsonList parseList() {
        List<JsonElement> elements = new ArrayList<>();
        assert JsonParserUtil.checkJsonListBegin(jsonString, cursor);
        cursor++; // [

        // skip whitespace
        skipWhitespace();

        while (!JsonParserUtil.checkJsonListEnd(jsonString, cursor)) {
            // skip whitespace
            skipWhitespace();

            // parseElement
            JsonElement element = parseElement();
            elements.add(element);

            // skip whitespace
            skipWhitespace();

            // read comma or ]
            // if comma loop again to Step1
            // if ] return
            if (JsonParserUtil.checkCommaSeparator(jsonString, cursor)) {
                cursor++; // ,
            } else if (!JsonParserUtil.checkJsonListEnd(jsonString, cursor)) {
                throw new JsonException("Invalid literal after list element");
            }
        }

        assert JsonParserUtil.checkJsonListEnd(jsonString, cursor);
        cursor++; // ]

        return new JsonList(elements);
    }

    private JsonString parseString() {
        // todo: nested " or escape characters
        assert JsonParserUtil.checkJsonStringBegin(jsonString, cursor);
        cursor++;

        // read till we find "
        StringBuilder result = new StringBuilder();
        while(!JsonParserUtil.checkJsonStringEnd(jsonString, cursor)) {
            char charElement = jsonString.charAt(cursor);
            result.append(charElement);
            cursor++;
        }

        assert JsonParserUtil.checkJsonStringEnd(jsonString, cursor);
        cursor++;

        return new JsonString(result.toString());
    }

    private JsonNumber parseNumber() {
        // todo: handling of complex nos like 1000L or 0.4F or e01
        boolean isNegative = false;
        boolean isDecimal = false;

        // check if negative number
        if (JsonParserUtil.checkJsonNumberNegativeToken(jsonString, cursor)) {
            cursor++;
            isNegative = true;
        }

        StringBuilder result = new StringBuilder();

        // read digits before decimal
        while(JsonParserUtil.checkJsonNumberDigitToken(jsonString, cursor)) {
            char digit = jsonString.charAt(cursor);
            result.append(digit);
            cursor++;
        }

        // read decimal point
        if (JsonParserUtil.checkJsonNumberDecimalToken(jsonString, cursor)) {
            char decimalToken = jsonString.charAt(cursor);
            result.append(decimalToken);
            cursor++;
            isDecimal = true;
        }

        if (isDecimal) {
            // at least one digit after decimal
            if (!JsonParserUtil.checkJsonNumberPostDecimalBegin(jsonString, cursor)) {
                throw new JsonException("Invalid literal after decimal point");
            }

            // read digits after decimal
            while(JsonParserUtil.checkJsonNumberDigitToken(jsonString, cursor)) {
                char digit = jsonString.charAt(cursor);
                result.append(digit);
                cursor++;
            }
        }

        // Construct number based on isNegative and isDecimal flags
        int multiplier = JsonConstants.NUMBER_POSITIVE_MULTIPLIER;
        if (isNegative) {
            multiplier = JsonConstants.NUMBER_NEGATIVE_MULTIPLIER;
        }

        Number number;
        if (isDecimal) {
            number = multiplier * Float.parseFloat(result.toString());
        } else {
            number = multiplier * Integer.parseInt(result.toString());
        }

        return new JsonNumber(number);
    }

    private JsonBoolean parseBoolean() {
        // either read true or false
        if (JsonParserUtil.checkJsonBooleanTrue(jsonString, cursor)) {
            cursor += JsonConstants.BOOLEAN_TRUE.length();
            return new JsonBoolean(true);
        } else if (JsonParserUtil.checkJsonBooleanFalse(jsonString, cursor)) {
            cursor += JsonConstants.BOOLEAN_FALSE.length();
            return new JsonBoolean(false);
        } else {
            throw new JsonException("Invalid literal while parsing boolean");
        }

    }

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        String jsonString = " {\n" +
                "  \"a\": \"b\",\n" +
                "  \"c\": [\n" +
                "    1,\n" +
                "    [\n" +
                "      2,\n" +
                "      3\n" +
                "    ],\n" +
                "    \"hello\",\n" +
                "    \"world\"\n" +
                "  ],\n" +
                "  \"d\": [\n" +
                "    {\n" +
                "      \"e\": \"f\",\n" +
                "      \"g\": true\n" +
                "    },\n" +
                "    101\n" +
                "  ],\n" +
                "  \"h\": {\n" +
                "    \"i\": {\n" +
                "      \"j\": {\n" +
                "        \"k\": 11\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "} ";
        JsonElement result = jsonParser.parse(jsonString);
        System.out.println(result);
    }
}
