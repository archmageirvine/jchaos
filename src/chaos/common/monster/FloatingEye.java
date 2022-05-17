package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Virtuous;

/**
 * Floating eye.
 *
 * @author Sean A. Irvine
 */
public class FloatingEye extends MaterialMonster implements Virtuous {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.INTELLIGENCE, 33);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 12);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getCastRange() {
    return 3;
  }
  @Override
  public long getLosMask() {
    return 0x3C7EFFFFFFFF7E18L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return MindFlayer.class;
  }
}
