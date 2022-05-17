package chaos.board;

import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.beam.PlasmaBeam;
import chaos.common.free.Bless;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.Elm;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Rock;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.spell.Alliance;
import chaos.common.spell.Cloak;
import chaos.common.spell.Consecrate;
import chaos.common.spell.Exorcise;
import chaos.common.spell.Fireball;
import chaos.common.spell.Kill;
import chaos.common.spell.Lightning;
import chaos.common.spell.RaiseDead;
import chaos.common.spell.Request;
import chaos.common.spell.Sleep;
import chaos.common.spell.Stop;
import chaos.common.spell.Teleport;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CastMasterTest extends TestCase {

  public void testTrees() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final MagicWood mw = new MagicWood();
    w.getCell(0).push(mw);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    mw.setMount(wiz);
    final StringListener ll = new StringListener("Too close to an existing tree");
    cm.register(ll);
    try {
      assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 0, 0));
      assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 0, 1));
      assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 2));
      assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 0, 3));
      assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 0, 4));
      assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 5));
      assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 6));
      assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 7));
      assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 8));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testTreesLowerRight() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final MagicWood mw = new MagicWood();
    w.getCell(8).push(mw);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    mw.setMount(wiz);
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 8, 0));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 8, 1));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 8, 2));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 8, 3));
    assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 8, 4));
    assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 8, 5));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 8, 6));
    assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 8, 7));
    assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 8, 8));
  }

  public void testTreesWithAborist() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final MagicWood mw = new MagicWood();
    w.getCell(0).push(mw);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.ARBORIST, 1);
    mw.setMount(wiz);
    assertTrue(!cm.isLegalCast(wiz, new MagicWood(), 0, 0));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 1));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 2));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 3));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 4));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 5));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 6));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 7));
    assertTrue(cm.isLegalCast(wiz, new MagicWood(), 0, 8));
  }

  public void testDeadAspects() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Lion l = new Lion();
    w.getCell(0).push(l);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    assertTrue(!cm.isLegalCast(wiz, new RaiseDead(), 0, 0));
    assertTrue(!cm.isLegalCast(wiz, new RaiseDead(), 0, 1));
    assertTrue(cm.isLegalCast(wiz, new Stop(), 0, 0));
    l.setState(State.DEAD);
    final StringListener ll = new StringListener("Cannot cast on the dead");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Stop(), 0, 0));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
    assertTrue(cm.isLegalCast(wiz, new RaiseDead(), 0, 0));
    assertFalse(cm.isLegalCast(wiz, new Elm(), 0, 0));
  }

  public void testCrystalBall() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.CRYSTAL_BALL, 1);
    w.getCell(0).push(wiz);
    final Lion l = new Lion();
    w.getCell(1).push(l);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    w.getCell(2).push(l2);
    assertTrue(cm.isLegalCast(wiz, new RaiseDead(), 0, 2));
    final StringListener ll = new StringListener("No line of sight");
    cm.register(ll);
    try {
      wiz.set(PowerUps.CRYSTAL_BALL, 0);
      assertFalse(cm.isLegalCast(wiz, new RaiseDead(), 0, 2));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(cm.isLegalCast(wiz, new Teleport(), 0, 2));
    assertTrue(ll.seen());
  }

  public void testRange() {
    final World w = new World(1, 10);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    w.getCell(9).push(l2);
    final StringListener ll = new StringListener("Out of range");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new RaiseDead(), 0, 9));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
    wiz.set(PowerUps.WAND, 1);
    assertTrue(cm.isLegalCast(wiz, new RaiseDead(), 0, 9));
    wiz.set(PowerUps.WAND, 0);
    wiz.set(PowerUps.DEPTH, 1);
    assertTrue(cm.isLegalCast(wiz, new RaiseDead(), 0, 9));
    w.getCell(4).push(l2);
    assertTrue(cm.isLegalCast(null, new RaiseDead(), 0, 4));
  }

  public void testExorcise() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Lion l = new Lion();
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Can only cast on undead");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Exorcise(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testAlliance() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Lion l = new Lion();
    l.setOwner(Actor.OWNER_INDEPENDENT);
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Cannot cast on independents");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Alliance(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
    assertTrue(cm.isLegalCast(wiz, new Fireball(), 0, 1));
  }

  public void testCloak() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final GooeyBlob l = new GooeyBlob();
    l.setOwner(Actor.OWNER_INDEPENDENT);
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Cannot cast on growths");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Cloak(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(cm.isLegalCast(wiz, new Sleep(), 0, 1));
    w.getCell(1).push(new Lion());
    assertTrue(ll.seen());
    assertTrue(cm.isLegalCast(wiz, new Cloak(), 0, 1));
  }

  public void testNoWizardCell() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Wizard l = new Wizard1();
    l.setState(State.ACTIVE);
    l.setOwner(4);
    final Horse h = new Horse();
    h.setOwner(4);
    h.setMount(l);
    w.getCell(1).push(h);
    final StringListener ll = new StringListener("Cannot cast on cells containing a wizard");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Sleep(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
    assertTrue(cm.isLegalCast(wiz, new Fireball(), 0, 1));
  }

  private static class MyTeleport extends Teleport {
    @Override
    public int getCastFlags() {
      return CAST_EMPTY | CAST_LIVING;
    }
  }

  public void testNoExposedWizardCell() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Wizard l = new Wizard1();
    l.setState(State.ACTIVE);
    l.setOwner(4);
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Cannot cast on wizards");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Kill(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
    assertTrue(cm.isLegalCast(wiz, new MyTeleport(), 0, 1));
    assertTrue(cm.isLegalCast(wiz, new Fireball(), 0, 1));
  }

  public void testOnLiving() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Wizard l = new Wizard1();
    l.setState(State.ACTIVE);
    l.setOwner(4);
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Cannot cast on creatures");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Request(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testFreeCastable() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    assertFalse(cm.isLegalCast(wiz, new Bless(), 0, 1));
    assertTrue(cm.isLegalCast(wiz, new Bless(), 0, 0));
  }

  public void testOnInanimate() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Rock l = new Rock();
    l.setOwner(4);
    w.getCell(1).push(l);
    final StringListener ll = new StringListener("Cannot cast on inanimates");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Sleep(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testOnEmpty() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final StringListener ll = new StringListener("Cannot cast on empty space");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new Sleep(), 0, 1));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testOnAny() {
    final World w = new World(3, 3);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    assertTrue(cm.isLegalCast(wiz, new Consecrate(), 0, 1));
  }

  public void testPlasmaBeamWithDepth() {
    final World w = new World(1, 6);
    final CastMaster cm = new CastMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    wiz.set(PowerUps.DEPTH, 1);
    final StringListener ll = new StringListener("Out of range");
    cm.register(ll);
    try {
      assertFalse(cm.isLegalCast(wiz, new PlasmaBeam(), 0, 2));
    } finally {
      cm.deregister(ll);
    }
    assertTrue(ll.seen());
  }

  public void testGetRange() {
    final Lightning lightning = new Lightning();
    final Wizard1 wiz = new Wizard1();
    assertEquals(8, CastMaster.getRange(wiz, lightning));
    wiz.set(PowerUps.WAND, 2);
    assertEquals(10, CastMaster.getRange(wiz, lightning));
    wiz.set(PowerUps.DEPTH, 1);
    assertEquals(Castable.MAX_CAST_RANGE, CastMaster.getRange(wiz, lightning));
  }
}
