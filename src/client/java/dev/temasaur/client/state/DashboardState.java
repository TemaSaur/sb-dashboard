package dev.temasaur.client.state;

import java.util.Map;

public record DashboardState(long timestamp, Map<String, Object> data) {
}
