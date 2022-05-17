package chaos.common.free;

import chaos.Chaos;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.growth.VioletFungi;
import chaos.common.inanimate.ShadowWood;
import chaos.common.monster.GoblinBomb;
import chaos.common.monster.Skeleton;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class DisruptTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Disrupt();
  }

  public void test1() {
    final Disrupt a = new Disrupt();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 7);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final StoneGiant l = new StoneGiant();
    l.setOwner(2);
    l.set(Attribute.INTELLIGENCE, 13);
    world.getCell(0).push(l);
    final VioletFungi f = new VioletFungi();
    world.getCell(6).push(f);
    final ShadowWood sw = new ShadowWood();
    world.getCell(5).push(sw);
    castAndListenCheck(a, world, w, -1, CellEffectType.FADE_TO_RED, CellEffectType.REDRAW_CELL);
    assertEquals(State.DEAD, l.getState());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
    assertEquals(f, world.actor(6));
    assertEquals(sw, world.actor(5));
    assertEquals(State.ACTIVE, f.getState());
    assertEquals(State.ACTIVE, sw.getState());
    l.setState(State.ACTIVE);
    l.set(Attribute.INTELLIGENCE, 14);
    a.cast(world, w, null);
    assertEquals(State.ACTIVE, l.getState());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
    assertEquals(0, l.get(Attribute.INTELLIGENCE));
  }

  public void testBug163() {
    final Disrupt a = new Disrupt();
    final World world = Chaos.getChaos().getWorld();
    for (int k = 0; k < 3; ++k) {
      final Skeleton s = new Skeleton();
      s.set(Attribute.INTELLIGENCE, 1);
      s.setOwner(10);
      world.getCell(k).push(s);
    }
    final GoblinBomb g = new GoblinBomb();
    g.setRealm(Realm.ETHERIC);
    g.setOwner(10);
    g.set(Attribute.INTELLIGENCE, 0);
    world.getCell(world.width() + 1).pop();
    world.getCell(world.width() + 1).push(g);
    a.cast(world, new Wizard1(), null);
  }

}
