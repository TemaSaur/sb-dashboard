package dev.temasaur.client.tab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.temasaur.client.mixin.PlayerTabOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

public class TabReader {
  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");
  private static final Minecraft CLIENT = Minecraft.getInstance();

  private TabReader() {
  }

  public static List<String> getTabLines() {
    if (CLIENT.player == null) {
      LOGGER.debug("getTabLines() executed with player == null");
      return List.of();
    }

    Collection<PlayerInfo> playerList = CLIENT.player.connection.getOnlinePlayers();

    List<String> tabLines = new ArrayList<>(playerList.size());

    for (PlayerInfo player : playerList) {
      tabLines.add(getName(player));
    }

    return tabLines;
  }

  public static String getFooter() {
    Component footer = getTabOverlay(PlayerTabOverlayAccessor::getFooter);

    return footer == null ? null : footer.getString();
  }

  public static String getHeader() {
    Component header = getTabOverlay(PlayerTabOverlayAccessor::getHeader);

    return header == null ? null : header.getString();
  }

  private static String getName(PlayerInfo player) {
    Component displayName = player.getTabListDisplayName();
    if (displayName == null)
      return player.getProfile().name();
    else
      return displayName.getString();
  }

  private static Component getTabOverlay(Function<PlayerTabOverlayAccessor, Component> access) {
    PlayerTabOverlay tabOverlay = CLIENT.gui.hud.getTabList();
    if (tabOverlay == null)
      return null;

    return access.apply((PlayerTabOverlayAccessor) tabOverlay);
  }
}
