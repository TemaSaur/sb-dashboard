package dev.temasaur.client.state.providers;

import dev.temasaur.client.tab.TabReader;

public class TabLinesStateProvider implements StateProvider {
  @Override
  public String key() {
    return "lines";
  }

  @Override
  public Object collect() {
    return TabReader.getTabLines();
  }
}
