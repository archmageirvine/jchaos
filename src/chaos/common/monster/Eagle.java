package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Eagle.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Eagle extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.INTELLIGENCE, 47);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 93);
    setDefault(Attribute.MOVEMENT, 6);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0000607EFE060000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Gryphon.class;
  }
}
