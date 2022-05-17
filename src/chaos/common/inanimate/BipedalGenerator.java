package chaos.common.inanimate;

import java.util.ArrayList;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.FrequencyTable;
import chaos.common.monster.Azer;
import chaos.common.monster.Baboon;
import chaos.common.monster.Banderlog;
import chaos.common.monster.Bandit;
import chaos.common.monster.Derro;
import chaos.common.monster.Faun;
import chaos.common.monster.Goblin;
import chaos.common.monster.Gorilla;
import chaos.common.monster.GrayElf;
import chaos.common.monster.Halfling;
import chaos.common.monster.Ogre;
import chaos.common.monster.Orc;
import chaos.common.monster.Spriggan;
import chaos.common.monster.Troll;
import chaos.common.monster.WoodElf;
import chaos.util.Random;

/**
 * Bipedal generator.
 *
 * @author Sean A. Irvine
 */
public class BipedalGenerator extends AbstractGenerator implements Bonus {

  /** List of classes this generator can produce. */
  private static final ArrayList<Class<? extends Actor>> GENERATEES = new ArrayList<>();
  static {
    GENERATEES.add(Faun.class);
    GENERATEES.add(Goblin.class);
    GENERATEES.add(Orc.class);
    GENERATEES.add(Troll.class);
    GENERATEES.add(Ogre.class);
    GENERATEES.add(Gorilla.class);
    GENERATEES.add(Halfling.class);
    GENERATEES.add(Baboon.class);
    GENERATEES.add(Derro.class);
    GENERATEES.add(Spriggan.class);
    GENERATEES.add(Azer.class);
    GENERATEES.add(Banderlog.class);
    GENERATEES.add(Bandit.class);
    GENERATEES.add(WoodElf.class);
    GENERATEES.add(GrayElf.class);
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
