package dev.temasaur.client.state.providers;

public interface StateProvider {
  String key();

  Object collect();
}
