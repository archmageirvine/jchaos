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
import chaos.common.monster.Mummy;
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

  private static final ArrayList<Class<? extends Actor>> GENERATES = new ArrayList<>();

  static {
    GENERATES.add(Skeleton.class);
    GENERATES.add(SkeletonWarrior.class);
    GENERATES.add(Spectre.class);
    GENERATES.add(Zombie.class);
    GENERATES.add(JujuZombie.class);
    GENERATES.add(Wight.class);
    GENERATES.add(Wraith.class);
    GENERATES.add(Vampire.class);
    GENERATES.add(Haunt.class);
    GENERATES.add(Ghost.class);
    GENERATES.add(Ghast.class);
    GENERATES.add(Ghoul.class);
    GENERATES.add(CrimsonDeath.class);
    GENERATES.add(Drelb.class);
    GENERATES.add(Shadow.class);
    GENERATES.add(Mummy.class);
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
      return (Actor) FrequencyTable.instantiate(GENERATES.get(Random.nextInt(GENERATES.size())));
    } else {
      return null;
    }
  }
}
