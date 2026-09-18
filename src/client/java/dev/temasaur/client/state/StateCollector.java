package dev.temasaur.client.state;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.temasaur.client.state.providers.PlayerStateProvider;
import dev.temasaur.client.state.providers.StateProvider;

public class StateCollector {
  private final static List<StateProvider> PROVIDERS = List.of(
      new PlayerStateProvider());

  public static DashboardState collect() {
    Map<String, Object> data = new LinkedHashMap<>();
    for (StateProvider provider : PROVIDERS) {
      Object value = provider.collect();
      if (value != null) {
        data.put(provider.key(), value);
      }
    }
    return new DashboardState(System.currentTimeMillis(), data);
  }
}
