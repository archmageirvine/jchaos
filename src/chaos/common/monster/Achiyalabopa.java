package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;

/**
 * The achiyalabopa.
 *
 * @author Sean A. Irvine
 */
public class Achiyalabopa extends MaterialMonster implements Promotion {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.INTELLIGENCE, 19);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getCastRange() {
    return 2;
  }
  @Override
  public long getLosMask() {
    return 0x0000EEFFFF670200L;
  }
  @Override
  public Class<? extends Monster> promotion() {
    return Phoenix.class;
  }
  @Override
  public int promotionCount() {
    return 2;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Vulture.class;
  }

}
