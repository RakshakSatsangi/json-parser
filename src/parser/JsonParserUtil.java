package parser;

public class JsonParserUtil {
    public static boolean checkJsonObjectBegin(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.OBJECT_OPEN_BRACKET;
    }

    public static boolean checkJsonObjectKeyValueSeparator(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.OBJECT_KEY_VALUE_SEPARATOR;
    }

    public static boolean checkJsonObjectEnd(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.OBJECT_CLOSE_BRACKET;
    }

    public static boolean checkJsonListBegin(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.LIST_OPEN_BRACKET;
    }

    public static boolean checkJsonListEnd(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.LIST_CLOSE_BRACKET;
    }

    public static boolean checkCommaSeparator(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.COMMA_SEPARATOR;
    }

    public static boolean checkJsonStringBegin(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.STRING_TOKEN;
    }

    public static boolean checkJsonStringEnd(String jsonString, int cursor) {
        return checkJsonStringBegin(jsonString, cursor);
    }

    public static boolean checkJsonNumberNegativeToken(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.NUMBER_NEGATIVE_TOKEN;
    }

    public static boolean checkJsonNumberDigitToken(String jsonString, int cursor) {
        return Character.isDigit(jsonString.charAt(cursor));
    }

    private static boolean checkNumberLeadingZeros(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.NUMBER_ZERO && (cursor + 1 < jsonString.length()) && checkJsonNumberDigitToken(jsonString, cursor + 1);
    }
    public static boolean checkJsonNumberBegin(String jsonString, int cursor) {
        return (checkJsonNumberDigitToken(jsonString, cursor) && !checkNumberLeadingZeros(jsonString, cursor)) || checkJsonNumberNegativeToken(jsonString, cursor);
    }

    public static boolean checkJsonNumberDecimalToken(String jsonString, int cursor) {
        return jsonString.charAt(cursor) == JsonConstants.NUMBER_DECIMAL_TOKEN;
    }

    public static boolean checkJsonNumberPostDecimalBegin(String jsonString, int cursor) {
        return checkJsonNumberDigitToken(jsonString, cursor);
    }

    public static boolean checkJsonBooleanTrue(String jsonString, int cursor) {
        return jsonString.startsWith(JsonConstants.BOOLEAN_TRUE, cursor);
    }

    public static boolean checkJsonBooleanFalse(String jsonString, int cursor) {
        return jsonString.startsWith(JsonConstants.BOOLEAN_FALSE, cursor);
    }

    public static boolean checkJsonBooleanBegin(String jsonString, int cursor) {
        return checkJsonBooleanTrue(jsonString, cursor) || checkJsonBooleanFalse(jsonString, cursor);
    }
}
