package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterRide;
import chaos.common.Monster;

/**
 * Tyrannosaurus rex.
 *
 * @author Sean A. Irvine
 */
public class TRex extends MaterialMonsterRide {
  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 19);
    setDefault(Attribute.INTELLIGENCE, 69);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.AGILITY, 5);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x60F078787C3E3F00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Elephant.class;
  }
}
