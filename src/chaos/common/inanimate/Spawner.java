package chaos.common.inanimate;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.util.Random;

/**
 * Spawner
 *
 * @author Sean A. Irvine
 */
public class Spawner extends AbstractGenerator {

  private final Monster mSpawn;

  /**
   * Construct a new spawner of specified type
   * @param spawn monster type
   */
  public Spawner(final Monster spawn) {
    mSpawn = spawn;
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.LIFE_RECOVERY, 0);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 0);
  }

  /** Construct a new spawner of random type. */
  public Spawner() {
    this(FrequencyTable.DEFAULT.getRandomMonster());
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  /**
   * Get the type of monster produced by this spawner.
   * @return type of monster
   */
  public Monster getSpawn() {
    return mSpawn;
  }

  @Override
  public Actor chooseWhatToGenerate() {
    return Random.nextBoolean() ? (Actor) FrequencyTable.instantiate(mSpawn.getClass()) : null; // 50% chance
  }
}
