package chaos.sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.util.Random;
import irvine.util.io.IOUtils;

/**
 * Select sound resources to be used in various situations.
 * @author Sean A. Irvine
 */
public final class SoundSelection {

  private SoundSelection() {
  }

  private static void initMap(final Map<String, List<String>> map, final String resource) {
    // read sound file map, expects class name -> sound name
    try (final BufferedReader r = IOUtils.reader(resource)) {
      String line;
      while ((line = r.readLine()) != null) {
        if (!line.isEmpty() && line.charAt(0) != '#') {
          final String[] parts = line.split("\\s+");
          if (parts.length == 2) {
            final List<String> sounds = map.computeIfAbsent(parts[0], k -> new ArrayList<>());
            sounds.add(parts[1]);
          }
        }
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Map from castable to lists of possible sounds. */
  private static Map<String, List<String>> sSpecialCombatMap;

  /**
   * Get a sound resource associated with "special" combat performed by a
   * given castable. For ordinary creatures, this is the appropriate
   * sound for "special combat" attacks, but this routine can also be used
   * to selected sounds for a limited number of other magical attacks.
   * If no appropriate sound is available then null is returned.  If there
   * is more than one possible sound, then one is selected at random.
   * @param c castable to get sound for
   * @return sound resource or null
   */
  public static synchronized String getSpecialCombatSound(final Castable c) {
    if (sSpecialCombatMap == null) {
      // lazily initialize the special combat mapping
      sSpecialCombatMap = new HashMap<>();
      initMap(sSpecialCombatMap, "chaos/resources/sound/special.txt");
    }
    final List<String> choices = sSpecialCombatMap.get(c.getClass().getName());
    if (choices == null) {
      // If the requested sound is not in the map, then it is most
      // likely because the recipient has a shocker.
      return Random.nextBoolean() ? "chaos/resources/sound/special/shocker" : "chaos/resources/sound/special/shocker1";
    } else {
      return choices.get(Random.nextInt(choices.size()));
    }
  }

  /** Map from castable to lists of possible sounds. */
  private static Map<String, List<String>> sCombatMap;

  /**
   * Get a sound resource associated with ordinary combat performed by a
   * given castable.
   *
   * If no appropriate sound is available then null is returned.  If there
   * is more than one possible sound, then one is selected at random.
   * @param c castable to get sound for
   * @return sound resource or null
   */
  public static synchronized String getCombatSound(final Castable c) {
    if (c == null) {
      return null;
    }
    if (sCombatMap == null) {
      // lazily initialize the  combat mapping
      sCombatMap = new HashMap<>();
      initMap(sCombatMap, "chaos/resources/sound/combat.txt");
    }
    final List<String> choices = sCombatMap.get(c.getClass().getName());
    if (choices == null) {
      return null;
    } else {
      return choices.get(Random.nextInt(choices.size()));
    }
  }

  /** Map from castable to lists of possible sounds. */
  private static Map<String, List<String>> sDeathMap;

  /**
   * Get a sound resource associated with death of an actor.
   *
   * If no appropriate sound is available then null is returned.  If there
   * is more than one possible sound, then one is selected at random.
   * @param c castable to get sound for
   * @return sound resource or null
   */
  public static synchronized String getDeathSound(final Castable c) {
    if (sDeathMap == null) {
      synchronized (SoundSelection.class) {
        // lazily initialize the  death mapping
        sDeathMap = new HashMap<>();
        initMap(sDeathMap, "chaos/resources/sound/death.txt");
      }
    }
    final List<String> choices = sDeathMap.get(c.getClass().getName());
    if (choices == null) {
      return null;
    } else {
      return choices.get(Random.nextInt(choices.size()));
    }
  }

  /** Root location of all sounds. */
  private static final String SDIR = "chaos/resources/sound/";

  /** Maps to the root directory of sound effects. */
  private static final Map<Object, String> SOUND_ROOTS = new HashMap<>();
  /** Used in the selection of humanoid attack sounds. */
  private static List<String> sHumanoidAttackSounds = null;
  /** Used in the selection of humanoid defence sounds. */
  private static List<String> sHumanoidDefenceSounds = null;
  /** Used in the selection of generic combat sounds. */
  private static List<String> sGenericCombatSounds = null;

  /**
   * Initialize the sounds for a specific list.
   * @param list list to initialize
   * @param subdir subdirectory of sounds
   */
  private static void initSoundList(final List<String> list, final String subdir) {
    try {
      final InputStream is = SoundSelection.class.getClassLoader().getResourceAsStream(SDIR + subdir + ".txt");
      if (is == null) {
        throw new RuntimeException(subdir + " not found");
      }
      try {
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
          String line;
          while ((line = r.readLine()) != null) {
            list.add(line);
          }
        }
      } finally {
        is.close();
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    SOUND_ROOTS.put(list, SDIR + subdir + "/");
  }

  /** Initialize humanoid attack sounds. */
  private static synchronized void initHumanoidAttackSounds() {
    if (sHumanoidAttackSounds == null) {
      sHumanoidAttackSounds = new ArrayList<>();
      initSoundList(sHumanoidAttackSounds, "hattack");
    }
  }

  /** Initialize humanoid defence sounds. */
  private static synchronized void initHumanoidDefenceSounds() {
    if (sHumanoidDefenceSounds == null) {
      sHumanoidDefenceSounds = new ArrayList<>();
      initSoundList(sHumanoidDefenceSounds, "hdefend");
    }
  }

  /** Initialize generic combat sounds. */
  private static synchronized void initGenericCombatSounds() {
    if (sGenericCombatSounds == null) {
      sGenericCombatSounds = new ArrayList<>();
      initSoundList(sGenericCombatSounds, "genericcombat");
    }
  }

  /**
   * Select a value at random from the specified list.
   * @param list map to examine
   * @return randomized value
   */
  private static String selectRandomized(final List<String> list) {
    return SOUND_ROOTS.get(list) + list.get(Random.nextInt(list.size()));
  }

  /**
   * Selects a (simultaneous) sequence of sound clips to be played when
   * <code>offence</code> attacks <code>defence</code> with an attack
   * of strength <code>damage</code>.  The result is an array of sounds
   * to be passed to the sound system.   It is expected that each sound
   * should be played with a reducing volume, say 20% for each entry.
   *
   * The first result is generally a sound appropriate for the offensive
   * actor.  The second result is a corresponding defence sound.  The third
   * result (when present) is additional sound (e.g. a clang on metal).
   * These three sounds should be played simultaneously.  If the result
   * contains more than three sounds they should each be played offset by
   * some small time (say 100 milliseconds).
   *
   * Any individual entry in the result may be null. Such entries should
   * simply be skipped, but should still incur a volume reduction.
   * @param offence the attacker
   * @param defence the defender
   * @param rawDamage the strength of the attack
   * @return sound resources to be played
   */
  public static String[] getBattleSounds(final Actor offence, final Actor defence, final int rawDamage) {
    final int damage = Math.abs(rawDamage);
    // try and get specific sounds first
    String o1 = getCombatSound(offence);
    String o2 = null;
    String d1 = getCombatSound(defence);
    String d2 = null;

    // select offence sounds
    if (o1 == null) {
      if (offence instanceof Humanoid) {
        initHumanoidAttackSounds();
        o1 = selectRandomized(sHumanoidAttackSounds);
        if (damage >= 5) {
          o2 = selectRandomized(sHumanoidAttackSounds);
        }
      } else {
        initGenericCombatSounds();
        o1 = selectRandomized(sGenericCombatSounds);
      }
    }

    // delect defence sounds
    if (d1 == null) {
      if (defence instanceof Humanoid) {
        initHumanoidDefenceSounds();
        d1 = selectRandomized(sHumanoidDefenceSounds);
        if (damage >= 5) {
          d2 = selectRandomized(sHumanoidDefenceSounds);
        }
      } else if (defence instanceof Growth) {
        // growths essentially silent
      } else if (!(defence instanceof Monster)) {
        // non-growth, inanimate, make a thud sound for defence
        d1 = SDIR + "genericcombat/" + (damage <= 11 ? "SwooshThud" : "BigSwooshThud");
      } else {
        initGenericCombatSounds();
        d1 = selectRandomized(sGenericCombatSounds);
      }
    }

    if (rawDamage >= 0) {
      if (o2 == null || d2 == null) {
        return new String[] {o1, d1};
      } else {
        return new String[] {o1, d1, o2, d2};
      }
    } else {
      return new String[] {o1};
    }
  }

}
