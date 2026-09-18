package dev.temasaur.client.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import dev.temasaur.client.state.providers.StateProvider;
import dev.temasaur.client.state.providers.PlayerStateProvider;

public class DashboardServer {
  private final Logger logger;
  private final HttpServer server;
  private static final int PORT_NUMBER = 58008;

  public DashboardServer(Logger logger) throws IOException {
    this.logger = logger;
    server = createServer();
  }

  public void startServer() {
    server.start();
    logger.info("Dashboard server started on :{}", PORT_NUMBER);
  }

  private HttpServer createServer() throws IOException {
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT_NUMBER), 0);
    httpServer.createContext("/state", this::handleStateRequest);
    httpServer.setExecutor(null);
    return httpServer;
  }

  private void handleStateRequest(HttpExchange exchange) throws IOException {
    byte[] body = buildStateJson().getBytes(StandardCharsets.UTF_8);

    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, body.length);

    try (OutputStream out = exchange.getResponseBody()) {
      out.write(body);
    }
  }

  private static String buildStateJson() {
    StateProvider playerState = new PlayerStateProvider();
    int xpLevel = (int) playerState.collect();
    return String.format("{\"status\":\"ok\",\"xp_level\":%d}", xpLevel);
  }
}
