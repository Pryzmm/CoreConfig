package com.pryzmm.coreconfigapi.data;

public class ConfigValidity {

    public static boolean validateBooleanConfig(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public static boolean validateIntConfig(String value, int min, int max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            int intValue = Integer.parseInt(value);
            if (intValue >= min && intValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    public static boolean validateDoubleConfig(String value, double min, double max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            double doubleValue = Double.parseDouble(value);
            if (doubleValue >= min && doubleValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    public static boolean validateFloatConfig(String value, float min, float max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            float floatValue = Float.parseFloat(value);
            if (floatValue >= min && floatValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    public static boolean validateColorConfig(String value) {
        if (value == null) return false;
        try {
            Integer.parseInt(value, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
