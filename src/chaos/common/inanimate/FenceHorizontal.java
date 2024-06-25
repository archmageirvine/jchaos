package chaos.common.inanimate;

import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.monster.Python;

/**
 * Horizontal fence.
 * @author Sean A. Irvine
 */
public class FenceHorizontal extends AbstractWall {
  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 5);
  }

  @Override
  public int getCastRange() {
    return 11;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Python();
    a.setOwner(getOwner());
    return a;
  }
}
