package dev.temasaur.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import net.fabricmc.api.ClientModInitializer;

public class SbDashboardClient implements ClientModInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger("sb-dashboard");
  private static HttpServer server;
  private static final int PORT_NUMBER = 58008;

  @Override
  public void onInitializeClient() {
    // This entrypoint is suitable for setting up client-specific logic, such as
    // rendering.
    startServerThread();
  }

  private static void startServerThread() {
    Thread thread = new Thread(SbDashboardClient::startServer, "sb-dashboard-server");
    thread.setDaemon(true);
    thread.start();
  }

  private static void startServer() {
    try {
      server = createServer();
      server.start();
      LOGGER.info("Dashboard server started on :{}", PORT_NUMBER);
    } catch (IOException e) {
      LOGGER.error("Failed to start dashboard server", e);
    }
  }

  private static HttpServer createServer() throws IOException {
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT_NUMBER), 0);
    httpServer.createContext("/state", exchange -> handleStateRequest(exchange));
    httpServer.setExecutor(null);
    return httpServer;
  }

  private static void handleStateRequest(HttpExchange exchange) throws IOException {
    byte[] body = buildStateJson().getBytes(StandardCharsets.UTF_8);

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, body.length);

    try (OutputStream out = exchange.getResponseBody()) {
      out.write(body);
    }
  }

  private static String buildStateJson() {
    // Placeholder — this is where StateCollector will eventually plug in.
    return "{\"status\":\"ok\"}";
  }
}
