package dev.temasaur.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.temasaur.client.http.DashboardServer;
import net.fabricmc.api.ClientModInitializer;

public class SbDashboardClient implements ClientModInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");
  private static DashboardServer server;

  @Override
  public void onInitializeClient() {
    // This entrypoint is suitable for setting up client-specific logic, such as
    // rendering.
    try {
      server = new DashboardServer(LOGGER);
      startServerThread();
    } catch (IOException e) {
      LOGGER.error("Failed to create dashboard server", e);
    }
  }

  private static void startServerThread() {
    Thread thread = new Thread(server::startServer, "sb-dashboard-server");
    thread.setDaemon(true);
    thread.start();
  }
}
