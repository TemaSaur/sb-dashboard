package dev.temasaur.client.state.providers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.temasaur.client.tab.TabReader;

public class JacobsStateProvider implements StateProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");

  public static final Pattern CROP_TOP = Pattern.compile(".*?(\\w+)\\s*◆\\s*Top\\s*([\\d.]+)%");
  public static final Pattern MEDAL_AMOUNT = Pattern.compile("\\s*(\\w+)\\s+with\\s+([\\d,]+)");
  public static final Pattern BELOW = Pattern.compile("\\s*([\\d,]+)\\s+below\\s+(\\w+)");
  public static final Pattern OVER = Pattern.compile("\\s*([\\d,]+)\\s+over\\s+([\\w ]+)");

  @Override
  public String key() {
    return "jacobs";
  }

  @Override
  public Object collect() {
    List<String> lines = TabReader.getTabLines();

    Map<String, Object> state = new ConcurrentHashMap<>();

    for (String line : lines) {
      extractCropTop(line, state);
      extractMedalAmount(line, state);
      extractOver(line, state);
      extractBelow(line, state);
    }

    return state;
  }

  public static void extractCropTop(String line, Map<String, Object> state) {
    Matcher matcher = CROP_TOP.matcher(line);
    if (!matcher.find())
      return;
    state.put("crop", tryGroup(matcher, 1));
    state.put("top", tryDoubleGroup(matcher, 2));
  }

  public static void extractMedalAmount(String line, Map<String, Object> state) {
    Matcher matcher = MEDAL_AMOUNT.matcher(line);
    if (!matcher.find())
      return;
    state.put("medal", tryGroup(matcher, 1));
    state.put("amount", tryIntGroup(matcher, 2));
  }

  public static void extractBelow(String line, Map<String, Object> state) {
    Matcher matcher = BELOW.matcher(line);
    if (!matcher.find())
      return;
    state.put("below", tryIntGroup(matcher, 1));
  }

  public static void extractOver(String line, Map<String, Object> state) {
    Matcher matcher = OVER.matcher(line);
    if (!matcher.find())
      return;
    state.put("over", tryIntGroup(matcher, 1));
  }

  private static String tryGroup(Matcher matcher, int groupNumber) {
    // TODO: manually test try-catch
    try {
      return matcher.group(groupNumber);
    } catch (NullPointerException | IllegalStateException e) {
      return null;
    }
  }

  private static Integer tryIntGroup(Matcher matcher, int groupNumber) {
    // TODO: get rid of exsessive levels abstraction
    String str = tryGroup(matcher, groupNumber);
    if (str == null)
      return null;
    return Integer.valueOf(str.replace(",", ""));
  }

  private static Double tryDoubleGroup(Matcher matcher, int groupNumber) {
    // TODO: get rid of exsessive levels abstraction
    String str = tryGroup(matcher, groupNumber);
    if (str == null)
      return null;
    return Double.valueOf(str);
  }
}
