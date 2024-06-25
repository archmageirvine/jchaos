package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Hydra.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Hydra extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 40);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.COMBAT, 7);
    setDefault(Attribute.AGILITY, 0);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x787CFEFC7E7E7E3EL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Crocodile.class;
  }
}
