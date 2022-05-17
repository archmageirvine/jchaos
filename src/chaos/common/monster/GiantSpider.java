package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.inanimate.Web;

/**
 * Giant spider.
 *
 * @author Sean A. Irvine
 */
public class GiantSpider extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 93);
    setDefault(Attribute.MOVEMENT, 2);
  }
  @Override
  public long getLosMask() {
    return 0x00007E7E7E7E0000L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Spider.class;
  }
  @Override
  public boolean update(final World world, final Cell cell) {
    if (super.update(world, cell)) {
      // already scheduled for death, no point in continuing.
      return true;
    }
    if (cell != null && world != null) {
      // Insert web under the spider, provide spider is alive and that there is
      // not already web below the spider.
      if (getState() == State.ACTIVE && !cell.contains(Web.class)) {
        final Web w = new Web();
        w.setOwner(getOwner());
        w.setRealm(getRealm());
        synchronized (world) {
          final Actor a = cell.pop();
          cell.push(w);
          cell.push(a);
        }
      }
    }
    return false;
  }
}
