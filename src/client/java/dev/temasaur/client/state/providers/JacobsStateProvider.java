package dev.temasaur.client.state.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.temasaur.client.tab.TabReader;

public class JacobsStateProvider implements StateProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");

  @Override
  public String key() {
    return "jacobs";
  }

  @Override
  public Object collect() {

    Pattern CROP_TOP = Pattern.compile(".*?(\\w+)\\s*◆\\s*Top\\s*([\\d.]+)%");
    Pattern MEDAL_AMOUNT = Pattern.compile("\\s*(\\w+)\\s+with\\s+([\\d,]+)");
    Pattern BELOW = Pattern.compile("\\s*([\\d,]+)\\s+below\\s+(\\w+)");
    Pattern OVER = Pattern.compile("\\s*([\\d,]+)\\s+over\\s+([\\w ]+)");

    ArrayList<String> result = new ArrayList<>(4);

    result.add(getMatch(CROP_TOP));
    result.add(getMatch(MEDAL_AMOUNT));
    result.add(getMatch(BELOW));
    result.add(getMatch(OVER));

    return result;
  }

  private String getMatch(Pattern pattern) {
    try {
      List<Matcher> matchers = TabReader.getTabLinesRegex(pattern);

      if (matchers == null || matchers.isEmpty())
        return null;

      return matchers.get(0).group().replace(",", "");
    } catch (Exception e) {
      LOGGER.warn("JacobsStateProvider::getMatch() failed with " + e.toString());
      return null;
    }
  }
}
