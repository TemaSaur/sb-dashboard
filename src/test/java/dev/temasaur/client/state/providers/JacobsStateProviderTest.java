package dev.temasaur.client.state.providers;

import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

public class JacobsStateProviderTest {

  private final List<BiConsumer<String, Map<String, Object>>> extractors = List.of(
      JacobsStateProvider::extractCropTop,
      JacobsStateProvider::extractMedalAmount,
      JacobsStateProvider::extractOver,
      JacobsStateProvider::extractBelow);

  @Test
  public void shouldExtractCropTop() {
    Map<String, Object> state = new HashMap<>();

    JacobsStateProvider.extractCropTop(" ○ Potato ◆ Top 31.5%", state);
    assertEquals("Potato", state.get("crop"));
    assertEquals(31.5, state.get("top"));
  }

  @Test
  public void shouldExtractMedalAmount() {
    Map<String, Object> state = new HashMap<>();

    JacobsStateProvider.extractMedalAmount(" BRONZE with 112,066", state);
    assertEquals("BRONZE", state.get("medal"));
    assertEquals(112066, state.get("amount"));
  }

  @Test
  public void shouldExtractOver() {
    Map<String, Object> state = new HashMap<>();

    JacobsStateProvider.extractOver(" 85,346 over No Medal", state);
    assertEquals(85346, state.get("over"));
  }

  @Test
  public void shouldExtractBelow() {
    Map<String, Object> state = new HashMap<>();

    JacobsStateProvider.extractBelow(" 9,451 below Silver", state);
    assertEquals(9451, state.get("below"));
  }

  @Test
  public void shouldExtractFullState() {
    Map<String, Object> state = new HashMap<>();

    List<String> validLines = List.of(
        " ○ Potato ◆ Top 31.5%",
        " BRONZE with 112,066",
        " 85,346 over No Medal",
        " 9,451 below Silver");

    for (String line : validLines) {
      for (var extractor : extractors)
        extractor.accept(line, state);
    }

    assertEquals("Potato", state.get("crop"));
    assertEquals(31.5, state.get("top"));
    assertEquals("BRONZE", state.get("medal"));
    assertEquals(112066, state.get("amount"));
    assertEquals(85346, state.get("over"));
    assertEquals(9451, state.get("below"));
  }

  @Test
  public void shouldNotExtractIrrelevantLines() {
    Map<String, Object> state = new HashMap<>();

    for (var extractor : extractors) {
      extractor.accept(" Farming Fortune: \ue0511786", state);
      extractor.accept(" Copper: 5,651", state);
      extractor.accept(" [Lvl 133] Rose Dragon", state);
      extractor.accept(" Strength: \ue00d280", state);
      extractor.accept("Profile: Strawberry", state);
      extractor.accept("Jacob's Contest: 5m 32s left", state);
    }
    assertTrue(state.isEmpty());
  }

  /*
   * @Test
   * public static void shouldWorkOnRealData() {
   * 
   * List.of(
   * "",
   * "",
   * "",
   * "           Island",
   * " Gems: 75",
   * "Stats:",
   * " Farming Fortune: \ue0511786",
   * "",
   * " Cooldown: READY",
   * "",
   * "",
   * "",
   * " Gary",
   * " 85,346 over No Medal",
   * "               Info",
   * " Garden Level: XV",
   * "",
   * " Vinyl Collector",
   * "Jacob's Contest: 5m 32s left",
   * "",
   * "",
   * "Profile: Strawberry",
   * " Sowdust: 1,881,688",
   * "",
   * " Alive: 0",
   * " Taylor",
   * "",
   * " Next Visitor: Queue Full!",
   * " Time Left: INACTIVE",
   * " Strength: \ue00d280",
   * " Spray: None",
   * "",
   * " Interest: 7 Hours",
   * "",
   * "",
   * " 9,451 below Silver",
   * " Gwendolyn",
   * "               Info",
   * "Visitors: (5)",
   * " Bank: 0",
   * "               Info",
   * " Bonus Pest Chance: \ue019304",
   * " 994,719.5/1.9M XP (52.7%)",
   * "",
   * "",
   * "",
   * " Bonus: INACTIVE ",
   * "",
   * " Fuel: 61.7k",
   * "TemaSaur",
   * "",
   * "",
   * " Organic Matter: 2.9k",
   * "",
   * "",
   * " [Lvl 133] Rose Dragon",
   * "",
   * "Skills:",
   * " Dante Goon",
   * "",
   * "",
   * " Stored Compost: 20",
   * "Area: Garden",
   * "Pet:",
   * "",
   * "",
   * " Server: mini64DJ",
   * " BRONZE with 112,066",
   * "          Guests (0)",
   * " SB Level: [162] 23/100 XP",
   * " Speed: \ue022400",
   * "",
   * " Farming 60: MAX",
   * "",
   * " ○ Potato ◆ Top 31.5%",
   * "Pests:",
   * "",
   * "Composter:",
   * "[162] TemaSaur ❤",
   * "",
   * " Copper: 5,651");
   * }
   */
}
