package dev.temasaur.client.state.providers;

import java.util.ArrayList;
import java.util.List;

import dev.temasaur.client.tab.TabReader;

public class JacobsStateProvider implements StateProvider {
  @Override
  public String key() {
    return "jacobs";
  }

  @Override
  public Object collect() {
    List<String> tabLines = new ArrayList<>();

    tabLines.add(TabReader.getFooter());
    tabLines.add(TabReader.getHeader());
    for (String line : TabReader.getTabLines()) {
      tabLines.add(line);
    }

    return tabLines;
  }
}
