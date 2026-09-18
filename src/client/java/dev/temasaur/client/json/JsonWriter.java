package dev.temasaur.client.json;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class JsonWriter {
  public static String write(Object obj) {
    StringBuilder sb = new StringBuilder();
    writeValue(sb, obj);
    return sb.toString();
  }

  private static void writeValue(StringBuilder sb, Object obj) {
    if (obj == null)
      sb.append("null");

    else if (obj instanceof Boolean)
      sb.append(obj);

    else if (obj instanceof Number)
      writeNumber(sb, (Number) obj);

    else if (obj instanceof String)
      writeString(sb, (String) obj);

    else if (obj instanceof Map<?, ?>)
      writeMap(sb, (Map<?, ?>) obj);

    else if (obj instanceof List<?>)
      writeList(sb, (List<?>) obj);

    else
      throw new IllegalArgumentException("Cannot serialize " + obj.getClass());
  }

  private static void writeNumber(StringBuilder sb, Number number) {
    if (number instanceof Double d) {
      writeDouble(sb, d);
      return;
    }
    if (number instanceof Float f) {
      writeFloat(sb, f);
      return;
    }

    sb.append(number.toString());
  }

  private static void writeDouble(StringBuilder sb, double value) {
    if (Double.isNaN(value)) {
      sb.append("\"NaN\"");
    } else if (value == Double.POSITIVE_INFINITY) {
      sb.append("\"+Inf\"");
    } else if (value == Double.NEGATIVE_INFINITY) {
      sb.append("\"-Inf\"");
    } else {
      sb.append(value);
    }
  }

  private static void writeFloat(StringBuilder sb, float value) {
    if (Float.isNaN(value)) {
      sb.append("\"NaN\"");
    } else if (value == Float.POSITIVE_INFINITY) {
      sb.append("\"+Inf\"");
    } else if (value == Float.NEGATIVE_INFINITY) {
      sb.append("\"-Inf\"");
    } else {
      sb.append(value);
    }
  }

  private static void writeString(StringBuilder sb, String str) {
    sb.append('"');

    char c;
    for (int i = 0; i < str.length(); ++i) {
      c = str.charAt(i);

      switch (c) {
        case '"' -> sb.append("\\\"");
        case '\\' -> sb.append("\\\\");
        case '\n' -> sb.append("\\n");
        case '\r' -> sb.append("\\r");
        case '\t' -> sb.append("\\t");
        case '\b' -> sb.append("\\b");
        case '\f' -> sb.append("\\f");
        default -> sb.append(c);
      }
    }

    sb.append('"');
  }

  private static void writeMap(StringBuilder sb, Map<?, ?> map) {
    // validate keys
    for (Object key : map.keySet()) {
      if (!(key instanceof String))
        throw new IllegalArgumentException(
            "JSON object keys must be strings. Got"
                + (key == null ? "null" : key.getClass().getName()));
    }

    sb.append('{');

    // sort keys for consistency
    Map<?, ?> sorted = new TreeMap<>(map);

    boolean first = true;
    for (Map.Entry<?, ?> entry : sorted.entrySet()) {
      if (!first)
        sb.append(',');
      first = false;

      writeValue(sb, entry.getKey());

      sb.append(':');
      writeValue(sb, entry.getValue());
    }

    sb.append('}');
  }

  private static void writeList(StringBuilder sb, List<?> list) {
    sb.append('[');

    boolean first = true;
    for (Object obj : list) {
      if (!first)
        sb.append(',');
      first = false;

      writeValue(sb, obj);
    }

    sb.append(']');
  }
}
