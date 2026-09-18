package dev.temasaur.client.state.providers;

import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class PlayerStateProvider implements StateProvider {
  private final Minecraft client;

  public PlayerStateProvider() {
    client = Minecraft.getInstance();
  }

  @Override
  public String key() {
    return "playerState";
  }

  @Override
  public Object collect() {
    LocalPlayer player = client.player;
    return Map.of(
        "xpLevel", player.experienceLevel,
        "coordinates", List.of(player.getX(), player.getY(), player.getZ()),
        "active items count", player.getActiveItem().count());
  }
}
