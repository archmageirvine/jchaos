package chaos.common.inanimate;

import java.util.ArrayList;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.FrequencyTable;
import chaos.common.dragon.BlackDragon;
import chaos.common.dragon.BlueDragon;
import chaos.common.dragon.GoldenDragon;
import chaos.common.dragon.GreenDragon;
import chaos.common.dragon.PlatinumDragon;
import chaos.common.dragon.RedDragon;
import chaos.common.dragon.ShadowDragon;
import chaos.common.dragon.WhiteDragon;
import chaos.common.monster.Pseudodragon;
import chaos.util.Random;

/**
 * Dragon nest.
 *
 * @author Sean A. Irvine
 */
public class DragonNest extends AbstractGenerator implements Bonus {

  /** List of classes this generator can produce. */
  private static final ArrayList<Class<? extends Actor>> GENERATEES = new ArrayList<>();
  static {
    GENERATEES.add(RedDragon.class);
    GENERATEES.add(BlackDragon.class);
    GENERATEES.add(PlatinumDragon.class);
    GENERATEES.add(BlueDragon.class);
    GENERATEES.add(WhiteDragon.class);
    GENERATEES.add(GoldenDragon.class);
    GENERATEES.add(ShadowDragon.class);
    GENERATEES.add(GreenDragon.class);
    GENERATEES.add(Pseudodragon.class);
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
    if (Random.nextInt(4) == 0) {
      // Generates 1 in 4 turns on average
      return (Actor) FrequencyTable.instantiate(GENERATEES.get(Random.nextInt(GENERATEES.size())));
    } else {
      return null;
    }
  }
}
