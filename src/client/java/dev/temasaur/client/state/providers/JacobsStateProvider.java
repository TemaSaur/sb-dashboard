package dev.temasaur.client.state.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.temasaur.client.mixin.PlayerTabOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

public class JacobsStateProvider implements StateProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");

  private final Minecraft client;

  public JacobsStateProvider() {
    client = Minecraft.getInstance();
  }

  @Override
  public String key() {
    return "jacobs";
  }

  @Override
  public Object collect() {
    List<String> tabLines = new ArrayList<>();
    if (client.player == null)
      return null;

    Collection<PlayerInfo> a = client.player.connection.getOnlinePlayers();
    PlayerTabOverlay tabOverlay = client.gui.hud.getTabList();

    Component footer = ((PlayerTabOverlayAccessor) tabOverlay).getFooter();
    Component header = ((PlayerTabOverlayAccessor) tabOverlay).getHeader();

    if (footer != null)
      tabLines.add(footer.getString());

    if (header != null)
      tabLines.add(header.getString());

    for (PlayerInfo player : a) {
      try {
        Component dn = player.getTabListDisplayName();
        if (dn == null)
          tabLines.add(player.getProfile().name());
        else
          tabLines.add(dn.getString());
      } catch (Exception e) {
        LOGGER.warn(e.toString());
      }
    }
    return tabLines;
  }
}
