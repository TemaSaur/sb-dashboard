package dev.temasaur.client.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonWriterTest {
  @Test
  void shouldPass() {
    assertTrue(true);
  }

  @Test
  void shouldSerializeNull() {
    assertEquals("null", JsonWriter.write(null));
  }

  @Test
  void shouldSerializeBooleans() {
    assertEquals("true", JsonWriter.write(true));
    assertEquals("false", JsonWriter.write(false));
  }

  @Test
  void shouldSerializeIntegers() {
    assertEquals("2", JsonWriter.write(2));
    assertEquals("0", JsonWriter.write(0));
    assertEquals("-1", JsonWriter.write(-1));
  }

  @Test
  void shouldSerializeDoubles() {
    assertEquals("1.0", JsonWriter.write(1.0));
    assertEquals("1.2", JsonWriter.write(1.2));
    assertEquals("0.0", JsonWriter.write(0.0));
    assertEquals("-1.0", JsonWriter.write(-1.0));
    assertEquals("1.0E7", JsonWriter.write(1e7));
  }

  @Test
  void shouldSerializeWeirdNumbers() {
    assertEquals("\"NaN\"", JsonWriter.write(Double.NaN));
    assertEquals("\"-Inf\"", JsonWriter.write(Double.NEGATIVE_INFINITY));
    assertEquals("\"+Inf\"", JsonWriter.write(Double.POSITIVE_INFINITY));
  }

  @Test
  void shouldSerializeStrings() {
    assertEquals("\"\"", JsonWriter.write(""));
    assertEquals("\".\"", JsonWriter.write("."));
    assertEquals("\"passed\"", JsonWriter.write("passed"));
  }

  @Test
  void shouldEscapeStrings() {
    assertEquals("\"\\\"\"", JsonWriter.write("\""));
    assertEquals("\"\\\\\"", JsonWriter.write("\\"));
    assertEquals("\"\\\"\\\\\"", JsonWriter.write("\"\\"));
    assertEquals("\"text with so called \\\"quotes\\\", innit\"",
        JsonWriter.write("text with so called \"quotes\", innit"));

    assertEquals("\"multi\\nline\"", JsonWriter.write("multi\nline"));
    assertEquals("\"first\\tsecond\"", JsonWriter.write("first\tsecond"));
    assertEquals("\"a\\rb\"", JsonWriter.write("a\rb"));
    assertEquals("\"a\\bb\"", JsonWriter.write("a\bb"));
    assertEquals("\"a\\fb\"", JsonWriter.write("a\fb"));
  }

  @Test
  void shouldSerializeMaps() {
    assertEquals("{}", JsonWriter.write(Map.of()));
    assertEquals("{\"status\":\"ok\"}", JsonWriter.write(Map.of("status", "ok")));
    assertEquals("{\"answer\":42}", JsonWriter.write(Map.of("answer", 42)));
    assertEquals("{\"first\":42,\"second\":27,\"sum\":69}",
        JsonWriter.write(Map.of("first", 42, "second", 27, "sum", 69)));
  }

  @Test
  void shouldSerializeLists() {
    assertEquals("[]", JsonWriter.write(List.of()));
    assertEquals("[1,2,3]", JsonWriter.write(List.of(1, 2, 3)));
    assertEquals("[1,\"b\"]", JsonWriter.write(List.of(1, "b")));
  }

  @Test
  void shouldSerializeComplexStructures() {
    assertEquals("{\"key\":{}}", JsonWriter.write(Map.of("key", Map.of())));
    assertEquals("{\"object\":{\"status\":\"ok\"}}", JsonWriter.write(Map.of("object", Map.of("status", "ok"))));
    assertEquals("[[]]", JsonWriter.write(List.of(List.of())));
    assertEquals("[{}]", JsonWriter.write(List.of(Map.of())));
    assertEquals("[{\"object\":{\"status\":\"ok\"}}]",
        JsonWriter.write(List.of(Map.of("object", Map.of("status", "ok")))));
  }

  @Test
  void shouldThrowWhenMapHasInvalidKeys() {
    assertThrows(IllegalArgumentException.class, () -> {
      JsonWriter.write(Map.of(Map.of(), "error"));
    });
  }

  @Test
  void shouldThrowWithUnknownTypes() {
    assertThrows(IllegalArgumentException.class, () -> {
      JsonWriter.write(JsonWriter.class);
    });
  }

  @Test
  void shouldSerializeNestedLists() {
    assertEquals("[[1,2],[3]]", JsonWriter.write(List.of(List.of(1, 2), List.of(3))));
  }

  @Test
  void shouldSerializeMapWithListValues() {
    Map<String, Object> input = new LinkedHashMap<>();
    input.put("items", List.of("a", "b"));
    assertEquals("{\"items\":[\"a\",\"b\"]}", JsonWriter.write(input));
  }

  @Test
  void shouldEscapeStringsInsideMaps() {
    assertEquals("{\"msg\":\"say \\\"hi\\\"\"}",
        JsonWriter.write(Map.of("msg", "say \"hi\"")));
  }
}
