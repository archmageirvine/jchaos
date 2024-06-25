package chaos.common.inanimate;

import java.util.ArrayList;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.FrequencyTable;
import chaos.common.Realm;
import chaos.common.monster.CrimsonDeath;
import chaos.common.monster.Drelb;
import chaos.common.monster.Ghast;
import chaos.common.monster.Ghost;
import chaos.common.monster.Ghoul;
import chaos.common.monster.Haunt;
import chaos.common.monster.JujuZombie;
import chaos.common.monster.Shadow;
import chaos.common.monster.Skeleton;
import chaos.common.monster.SkeletonWarrior;
import chaos.common.monster.Spectre;
import chaos.common.monster.Vampire;
import chaos.common.monster.Wight;
import chaos.common.monster.Wraith;
import chaos.common.monster.Zombie;
import chaos.util.Random;

/**
 * Tombstone.
 * @author Sean A. Irvine
 */
public class Tombstone extends AbstractGenerator implements Bonus {

  private static final ArrayList<Class<? extends Actor>> GENERATEES = new ArrayList<>();

  static {
    GENERATEES.add(Skeleton.class);
    GENERATEES.add(SkeletonWarrior.class);
    GENERATEES.add(Spectre.class);
    GENERATEES.add(Zombie.class);
    GENERATEES.add(JujuZombie.class);
    GENERATEES.add(Wight.class);
    GENERATEES.add(Wraith.class);
    GENERATEES.add(Vampire.class);
    GENERATEES.add(Haunt.class);
    GENERATEES.add(Ghost.class);
    GENERATEES.add(Ghast.class);
    GENERATEES.add(Ghoul.class);
    GENERATEES.add(CrimsonDeath.class);
    GENERATEES.add(Drelb.class);
    GENERATEES.add(Shadow.class);
  }

  {
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 1);
    setRealm(Realm.ETHERIC);
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
