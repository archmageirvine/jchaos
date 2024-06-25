package chaos.common.inanimate;

import java.util.ArrayList;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.common.monster.Achiyalabopa;
import chaos.common.monster.Bat;
import chaos.common.monster.BirdLord;
import chaos.common.monster.Eagle;
import chaos.common.monster.Falcon;
import chaos.common.monster.FireBat;
import chaos.common.monster.Harpy;
import chaos.common.monster.Phoenix;
import chaos.common.monster.Spectator;
import chaos.common.monster.Vulture;
import chaos.util.Random;

/**
 * Aviary.
 * @author Sean A. Irvine
 */
public class Aviary extends AbstractGenerator implements Bonus {

  /**
   * List of classes this generator can produce.  Number of times an item is in
   * this list influences its frequency, normal frequencies are ignored.
   */
  private static final ArrayList<Class<? extends Monster>> GENERATEES = new ArrayList<>();

  static {
    GENERATEES.add(Achiyalabopa.class);
    GENERATEES.add(Bat.class);
    GENERATEES.add(Bat.class);
    GENERATEES.add(BirdLord.class);
    GENERATEES.add(Eagle.class);
    GENERATEES.add(Eagle.class);
    GENERATEES.add(Falcon.class);
    GENERATEES.add(FireBat.class);
    GENERATEES.add(Harpy.class);
    GENERATEES.add(Harpy.class);
    GENERATEES.add(Phoenix.class);
    GENERATEES.add(Spectator.class);
    GENERATEES.add(Vulture.class);
    GENERATEES.add(Vulture.class);
  }

  {
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 1);
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public int getBonus() {
    return 3;
  }

  @Override
  public Actor chooseWhatToGenerate() {
    // Every call, attempt to select one creature from the list specified above,
    // create a new instance and return it.
    return (Actor) FrequencyTable.instantiate(GENERATEES.get(Random.nextInt(GENERATEES.size())));
  }
}
