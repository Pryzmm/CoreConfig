package com.pryzmm.coreconfigapi.data;

public class ConfigValidity {

    /**
     * Mainly for internal use, but returns true if a string can be parsed as a boolean
     */
    public static boolean validateBooleanConfig(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as an integer
     * @apiNote This method throws if the minimum argument is greater than the maximum argument
     */
    public static boolean validateIntConfig(String value, int min, int max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            int intValue = Integer.parseInt(value);
            if (intValue >= min && intValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as a double
     * @apiNote This method throws if the minimum argument is greater than the maximum argument
     */
    public static boolean validateDoubleConfig(String value, double min, double max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            double doubleValue = Double.parseDouble(value);
            if (doubleValue >= min && doubleValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as a long
     * @apiNote This method throws if the minimum argument is greater than the maximum argument
     */
    public static boolean validateLongConfig(String value, double min, double max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            double longValue = Long.parseLong(value);
            if (longValue >= min && longValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as a float
     * @apiNote This method throws if the minimum argument is greater than the maximum argument
     */
    public static boolean validateFloatConfig(String value, float min, float max) {
        if (min > max) throw new IllegalArgumentException("Configuration error: Min cannot be greater than max");
        try {
            float floatValue = Float.parseFloat(value);
            if (floatValue >= min && floatValue <= max) return true;
        }
        catch (NumberFormatException e) { return false; }
        return false;
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as a long and is within bounds of an integer for colors
     */
    public static boolean validateColorConfig(String value) {
        if (value == null) return false;
        try {
            long parsed = Long.parseLong(value);
            return parsed >= Integer.MIN_VALUE && parsed <= 0xFFFFFFFFL;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Mainly for internal use, but returns true if a string can be parsed as an enum
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean validateEnumConfig(String value, Class<?> enumClass) {
        if (value == null) return false;
        try {
            Enum.valueOf((Class<Enum>) enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
