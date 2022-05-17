package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.Aesculapius;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class TouchOfGodTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new TouchOfGod();
  }

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  public void testCast() {
    final Castable x = new TouchOfGod();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(4, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.SHIELD_GRANTED) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(15, w.get(Attribute.SPECIAL_COMBAT));
    assertEquals(15, w.get(Attribute.COMBAT));
    assertEquals(15, w.get(Attribute.RANGED_COMBAT));
    assertEquals(15, w.get(Attribute.MOVEMENT));
    assertEquals(15, w.get(Attribute.RANGE));
    assertEquals(7, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(Attribute.LIFE.max(), w.get(Attribute.LIFE));
    assertEquals(Attribute.AGILITY.max(), w.get(Attribute.AGILITY));
    assertEquals(Attribute.INTELLIGENCE.max(), w.get(Attribute.INTELLIGENCE));
    assertEquals(Attribute.MAGICAL_RESISTANCE.max(), w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(Attribute.LIFE, w.getSpecialCombatApply());
    assertTrue(getRedraw());
    assertTrue(getCast());
    mCast = false;
    mRedraw = false;
    w.set(Attribute.SPECIAL_COMBAT, 22);
    w.set(Attribute.COMBAT, 23);
    w.set(Attribute.RANGED_COMBAT, 24);
    w.setSpecialCombatApply(Attribute.COMBAT);
    w.setCombatApply(Attribute.COMBAT);
    w.setRangedCombatApply(Attribute.COMBAT);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(22, w.get(Attribute.SPECIAL_COMBAT));
    assertEquals(23, w.get(Attribute.COMBAT));
    assertEquals(24, w.get(Attribute.RANGED_COMBAT));
    assertEquals(Attribute.LIFE, w.getSpecialCombatApply());
    assertEquals(Attribute.LIFE, w.getCombatApply());
    assertEquals(Attribute.LIFE, w.getRangedCombatApply());
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testCast2() {
    final Castable x = new TouchOfGod();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(4, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final Aesculapius s = new Aesculapius();
    final World world = new World(1, 1);
    world.getCell(0).push(s);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(-15, s.get(Attribute.SPECIAL_COMBAT));
    assertEquals(-15, s.get(Attribute.COMBAT));
    assertEquals(-15, s.get(Attribute.RANGED_COMBAT));
    assertEquals(15, s.get(Attribute.MOVEMENT));
    assertEquals(15, s.get(Attribute.RANGE));
    assertEquals(14, s.get(Attribute.LIFE_RECOVERY));
    assertEquals(Attribute.LIFE.max(), s.get(Attribute.LIFE));
    assertEquals(Attribute.AGILITY.max(), s.get(Attribute.AGILITY));
    assertEquals(Attribute.INTELLIGENCE.max(), s.get(Attribute.INTELLIGENCE));
    assertEquals(Attribute.MAGICAL_RESISTANCE.max(), s.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(Attribute.LIFE, s.getSpecialCombatApply());
    s.setSpecialCombatApply(Attribute.COMBAT);
    s.setCombatApply(Attribute.COMBAT);
    s.setRangedCombatApply(Attribute.COMBAT);
    s.set(Attribute.SPECIAL_COMBAT, -22);
    s.set(Attribute.RANGED_COMBAT, -24);
    s.set(Attribute.COMBAT, -23);
    assertEquals(-23, world.actor(0).get(Attribute.COMBAT));
    s.set(Attribute.LIFE_RECOVERY, Attribute.LIFE_RECOVERY.max());
    x.cast(world, w, world.getCell(0), null);
    assertEquals(-22, s.get(Attribute.SPECIAL_COMBAT));
    assertEquals(-23, s.get(Attribute.COMBAT));
    assertEquals(-24, s.get(Attribute.RANGED_COMBAT));
    assertEquals(Attribute.LIFE, s.getSpecialCombatApply());
    assertEquals(Attribute.LIFE, s.getCombatApply());
    assertEquals(Attribute.LIFE, s.getRangedCombatApply());
    assertEquals(Attribute.LIFE_RECOVERY.max(), s.get(Attribute.LIFE_RECOVERY));
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final TouchOfGod x = new TouchOfGod();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
