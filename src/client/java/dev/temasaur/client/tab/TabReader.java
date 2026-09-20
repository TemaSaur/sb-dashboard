package dev.temasaur.client.tab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import dev.temasaur.client.mixin.PlayerTabOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

public class TabReader {
  private static final Minecraft client = Minecraft.getInstance();

  private TabReader() {
  }

  public static List<String> getTabLines() {
    if (client.player == null)
      return null;

    List<String> tabLines = new ArrayList<>();

    Collection<PlayerInfo> playerList = client.player.connection.getOnlinePlayers();

    for (PlayerInfo player : playerList) {
      Component dn = player.getTabListDisplayName();
      if (dn == null)
        tabLines.add(player.getProfile().name());
      else
        tabLines.add(dn.getString());
    }

    return tabLines;
  }

  public static String getFooter() {
    Component footer = getTabOverlay(PlayerTabOverlayAccessor::getFooter);

    if (footer != null)
      return footer.getString();

    return null;

  }

  public static String getHeader() {
    Component header = getTabOverlay(PlayerTabOverlayAccessor::getHeader);

    if (header != null)
      return header.getString();

    return null;
  }

  private static Component getTabOverlay(Function<PlayerTabOverlayAccessor, Component> access) {
    if (client.player == null)
      return null;

    PlayerTabOverlay tabOverlay = client.gui.hud.getTabList();
    if (tabOverlay == null)
      return null;

    return access.apply((PlayerTabOverlayAccessor) tabOverlay);
  }
}
