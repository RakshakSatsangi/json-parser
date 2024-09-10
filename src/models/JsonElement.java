package models;

/**
 * Base data type of JSON
 * Supported types:
 * Basic types: String, Boolean, Number
 * Advanced types: List, Nested objects
 */
public interface JsonElement {
    Object getVal();
}
