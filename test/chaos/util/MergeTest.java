package chaos.util;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.monster.Manticore;
import chaos.common.monster.Wight;
import chaos.common.monster.WoodElf;
import chaos.common.monster.Wraith;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class MergeTest extends TestCase {

  public void testMerge() {
    final Lion l = new Lion();
    final Cell c = new Cell(0);
    final Wraith e = new Wraith();
    c.push(e);
    Merge.merge(l, c);
    assertNull(c.peek());
    for (final Attribute attr : Attribute.values()) {
      final int expected = Math.min(100, e.getDefault(attr) + l.getDefault(attr));
      assertEquals(attr.toString(), expected, l.get(attr));
    }
    assertFalse(l.is(PowerUps.FLYING));
    assertFalse(l.is(PowerUps.ARCHERY));
    assertFalse(l.is(PowerUps.REINCARNATE));
    assertFalse(l.is(PowerUps.CLOAKED));
    assertEquals(0, l.get(PowerUps.HORROR));
  }

  public void testMergeElf() {
    final Lion l = new Lion();
    final Cell c = new Cell(0);
    final WoodElf e = new WoodElf();
    c.push(e);
    Merge.merge(l, c);
    assertEquals(e, c.peek());
    assertEquals(State.DEAD, e.getState());
    for (final Attribute attr : Attribute.values()) {
      final int expected = Math.min(100, e.getDefault(attr) + l.getDefault(attr));
      assertEquals(attr.toString(), expected, l.get(attr));
    }
    assertFalse(l.is(PowerUps.FLYING));
    assertTrue(l.is(PowerUps.ARCHERY));
    assertFalse(l.is(PowerUps.REINCARNATE));
    assertFalse(l.is(PowerUps.CLOAKED));
    assertEquals(0, l.get(PowerUps.HORROR));
  }

  public void testMergeEmpty() {
    final Lion l = new Lion();
    l.set(PowerUps.CLOAKED, 1);
    final Cell c = new Cell(0);
    Merge.merge(l, c);
    assertNull(c.peek());
    final Lion t = new Lion();
    for (final Attribute attr : Attribute.values()) {
      assertEquals(attr.toString(), l.get(attr), t.get(attr));
    }
    assertFalse(l.is(PowerUps.FLYING));
    assertFalse(l.is(PowerUps.REINCARNATE));
    assertTrue(l.is(PowerUps.CLOAKED));
  }

  public void testMergeWizard() {
    final Lion l = new Lion();
    final Cell c = new Cell(0);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    c.push(wiz);
    Merge.merge(l, c);
    assertEquals(wiz, c.peek());
    final Lion t = new Lion();
    for (final Attribute attr : Attribute.values()) {
      assertEquals(attr.toString(), l.get(attr), t.get(attr));
    }
  }

  public void testMergeMounted() {
    final Lion l = new Lion();
    final Cell c = new Cell(0);
    final Manticore e = new Manticore();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    e.setMount(wiz);
    c.push(e);
    Merge.merge(l, c);
    assertEquals(wiz, c.peek());
    final Lion t = new Lion();
    for (final Attribute attr : Attribute.values()) {
      final int expected = Math.min(100, e.get(attr) + t.get(attr));
      assertEquals(attr.toString(), expected, l.get(attr));
    }
    assertTrue(l.is(PowerUps.FLYING));
    assertFalse(l.is(PowerUps.REINCARNATE));
    assertFalse(l.is(PowerUps.ARCHERY));
  }

  public void testMergeReincarnation() {
    final Lion l = new Lion();
    final Cell c = new Cell(0);
    final Wraith e = new Wraith();
    e.set(PowerUps.REINCARNATE, e.reincarnation() != null ? 1 : 0);
    c.push(e);
    Merge.merge(l, c);
    assertTrue(c.peek() instanceof Wight);
    final Lion t = new Lion();
    for (final Attribute attr : Attribute.values()) {
      final int expected = Math.min(100, e.get(attr) + t.get(attr));
      assertEquals(attr.toString(), expected, l.get(attr));
    }
    assertFalse(l.is(PowerUps.FLYING));
    assertTrue(l.is(PowerUps.REINCARNATE));
  }

  public void testMergeHorrorCloaked() {
    final Lion l = new Lion();
    l.set(PowerUps.HORROR, 2);
    final Cell c = new Cell(0);
    final Wraith e = new Wraith();
    e.set(PowerUps.HORROR, 3);
    e.set(PowerUps.CLOAKED, 1);
    c.push(e);
    Merge.merge(l, c);
    assertNull(c.peek());
    final Lion t = new Lion();
    for (final Attribute attr : Attribute.values()) {
      final int expected = Math.min(100, e.get(attr) + t.get(attr));
      assertEquals(attr.toString(), expected, l.get(attr));
    }
    assertFalse(l.is(PowerUps.FLYING));
    assertFalse(l.is(PowerUps.REINCARNATE));
    assertEquals(5, l.get(PowerUps.HORROR));
    assertTrue(l.is(PowerUps.CLOAKED));
  }

}
