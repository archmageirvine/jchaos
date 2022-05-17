package chaos.common.spell;

import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.monster.Lion;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class SimulacrumTest extends ReplicateTest {

  @Override
  public Castable getCastable() {
    return new Simulacrum();
  }

  @Override
  protected boolean checkEqual(final Lion a, final Lion b) {
    return a.getState() == b.getState()
      && a.getOwner() == b.getOwner()
      && a.get(Attribute.LIFE_RECOVERY) == b.get(Attribute.LIFE_RECOVERY)
      && a.get(PowerUps.HORROR) == b.get(PowerUps.HORROR)
      && a.is(PowerUps.REINCARNATE) == b.is(PowerUps.REINCARNATE)
      && a.get(Attribute.COMBAT) == b.get(Attribute.COMBAT)
      && a.get(Attribute.INTELLIGENCE) == b.get(Attribute.INTELLIGENCE);
  }

}
