package dev.temasaur.client.state.providers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class PlayerStateProvider implements StateProvider {
  private final Minecraft client;

  public PlayerStateProvider() {
    client = Minecraft.getInstance();
  }

  @Override
  public String key() {
    return "xpLevel";
  }

  @Override
  public Object collect() {
    LocalPlayer player = client.player;
    return player.experienceLevel;
  }
}
