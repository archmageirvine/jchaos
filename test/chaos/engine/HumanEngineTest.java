package chaos.engine;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import chaos.Chaos;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.dragon.EmeraldDragon;
import chaos.common.dragon.GreenDragon;
import chaos.common.free.Arborist;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.Gorilla;
import chaos.common.monster.Orc;
import chaos.common.monster.Python;
import chaos.common.wizard.Wizard1;
import chaos.graphics.MockScreen;
import chaos.sound.Sound;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.HighlightEvent;
import chaos.util.Sleep;
import chaos.util.TextEvent;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class HumanEngineTest extends TestCase {

  private MouseEvent cancelClick() {
    return new MouseEvent(new Container(), 1, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON3);
  }

  private MouseEvent click() {
    return new MouseEvent(new Container(), 1, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON1);
  }

  private static class MyAbortListener implements EventListener {

    private static final String[] MESSAGE = {
      "Arborist [0]",
      "Cast aborted",
    };

    private int mMessageIndex = 0;

    @Override
    public void update(final Event e) {
      if (e instanceof TextEvent) {
        Assert.assertEquals(MESSAGE[mMessageIndex++], e.toString());
      }
    }

    public boolean ok() {
      return mMessageIndex == MESSAGE.length;
    }
  }

  private static final Container DUMMY = new Container();

  private void move(final HumanEngine he, final int x, final int y) {
    he.mouseMoved(new MouseEvent(DUMMY, 1, 0, 0, x, y, 1, false, MouseEvent.NOBUTTON));
  }

  private void click1(final HumanEngine he, final int x, final int y) {
    final MouseEvent e = new MouseEvent(DUMMY, 1, 0, 0, x, y, 1, false, MouseEvent.BUTTON1);
    he.mouseClicked(e);
    Assert.assertTrue(e.isConsumed());
  }

  private void keyPress(final HumanEngine he, final int c) {
    final KeyEvent e = new KeyEvent(DUMMY, KeyEvent.KEY_PRESSED, 0, 0, c, '\0');
    he.keyPressed(e);
    Assert.assertTrue(e.isConsumed());
  }

  private void keyReleased(final HumanEngine he, final int c) {
    final KeyEvent e = new KeyEvent(DUMMY, KeyEvent.KEY_PRESSED, 0, 0, c, '\0');
    he.keyReleased(e);
    Assert.assertTrue(e.isConsumed());
  }

  public void testCastAbort() throws Exception {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World w = Chaos.getChaos().getWorld();
    final MockScreen s = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    final MyAbortListener l = new MyAbortListener();
    assertFalse(he.cast(null, null, null));
    try {
      he.cast(wiz, new Arborist(), w.getCell(0));
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    he.setScreen(s, Chaos.getChaos().getTileManager());
    he.register(l);
    try {
      final Thread t = new Thread(() -> {
        Sleep.sleep(400);
        he.mouseClicked(cancelClick());
      });
      t.start();
      final boolean res = he.cast(wiz, new Arborist(), w.getCell(0));
      t.join();
      assertEquals("setCursor(CAST)#addMouseListener(.)#addMouseMotionListener(.)#addKeyListener(.)#setContinueGadget(true)#setCursor(BLANK)#setContinueGadget(false)#removeKeyListener(.)#removeMouseMotionListener(.)#removeMouseListener(.)#blankRight()#highlight(null)#", s.toString());
      assertTrue(l.ok());
      assertFalse(res);
    } finally {
      he.deregister(l);
    }
  }

  private static class MyGoListener implements EventListener {

    private static final ArrayList<Class<? extends Event>> CHAIN = new ArrayList<>();
    static {
      CHAIN.add(HighlightEvent.class);
      CHAIN.add(CellEffectEvent.class);
      CHAIN.add(TextEvent.class);
    }

    private static final String[] MESSAGE = {
      "Arborist [0]",
    };

    private int mChainIndex = 0;
    private int mMessageIndex = 0;

    @Override
    public void update(final Event e) {
      //System.out.println(e);
      if (mChainIndex < CHAIN.size()) {
        Assert.assertTrue(e.toString(), CHAIN.get(mChainIndex++).isInstance(e));
      }
      if (e instanceof TextEvent) {
        Assert.assertEquals(MESSAGE[mMessageIndex++], e.toString());
      }
    }

    public boolean ok() {
      return mChainIndex == CHAIN.size() && mMessageIndex == MESSAGE.length;
    }
  }

  public void testCastGo() throws Exception {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World w = Chaos.getChaos().getWorld();
    final MockScreen s = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getWizardManager().setWizard(1, wiz);
    w.getCell(1).push(wiz);
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    assertFalse(he.cast(null, null, null));
    try {
      he.cast(wiz, new Arborist(), w.getCell(0));
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    he.setScreen(s, Chaos.getChaos().getTileManager());
    final MyGoListener l = new MyGoListener();
    he.register(l);
    try {
      final Thread t = new Thread(() -> {
        // In continue gadget
        move(he, MockScreen.CONTINUE_X, MockScreen.CONTINUE_Y);
        move(he, 42, 30);
        move(he, 30, 30);
        he.mouseClicked(click());
      });
      t.start();
      assertTrue(he.cast(wiz, new Arborist(), w.getCell(1)));
      t.join();
      final String str = s.toString();
      assertTrue(str, str.contains("setCursor(CAST)"));
      assertTrue(str, str.contains("addMouseListener(.)"));
      assertTrue(str, str.contains("addMouseMotionListener(.)"));
      assertTrue(str, str.contains("addKeyListener(.)"));
      assertTrue(str, str.contains("setContinueGadget(true)"));
      assertTrue(str, str.contains("removeMouseListener(.)"));
      assertTrue(str, str.contains("removeMouseMotionListener(.)"));
      assertTrue(str, str.contains("removeKeyListener(.)"));
      assertTrue(str, str.endsWith("#|I(500,160,null)#I(33,37,null)#I(500,160,null)#I(33,37,null)#"));
      assertTrue(l.ok());
      assertEquals(1, wiz.get(PowerUps.ARBORIST));
    } finally {
      he.deregister(l);
    }
  }

  public void testMovementEndTurn() throws Exception {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World w = Chaos.getChaos().getWorld();
    final MockScreen s = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getWizardManager().setWizard(1, wiz);
    w.getCell(1).push(wiz);
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    he.setScreen(s, Chaos.getChaos().getTileManager());
    final Thread t = new Thread(() -> {
      move(he, MockScreen.CONTINUE_X, MockScreen.CONTINUE_Y); // into Continue gadget
      move(he, 0, 0); // cell 0
      move(he, 17, 2); // cell 1
      click1(he, 17, 2); // click on wizard
      click1(he, 0, 0); // move him to cell 0
      click1(he, MockScreen.CONTINUE_X, MockScreen.CONTINUE_Y); // click Continue gadget
    });
    t.start();
    assertFalse(wiz.isMoved());
    wiz.setPersonalName("Wizard 1");
    he.moveAll(wiz);
    t.join();
    assertTrue(wiz.isMoved());
    assertEquals(wiz, w.actor(0));
    assertNull(w.actor(1));
    final String q = s.toString();
    assertTrue(q, q.contains("fillRect(510,115,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])"));
  }

  public void testMovementEndTurnKeyboard() throws Exception {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World w = Chaos.getChaos().getWorld();
    final MockScreen s = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.FLYING, 1);
    w.getWizardManager().setWizard(1, wiz);
    w.getCell(1).push(wiz);
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    he.setScreen(s, Chaos.getChaos().getTileManager());
    final Thread t = new Thread(() -> {
      click1(he, 17, 2); // click on wizard
      keyPress(he, KeyEvent.VK_Z);
      keyPress(he, KeyEvent.VK_W);
      keyPress(he, KeyEvent.VK_D);
      keyPress(he, KeyEvent.VK_X);
      keyPress(he, KeyEvent.VK_C);
      keyPress(he, KeyEvent.VK_Q);
      keyPress(he, KeyEvent.VK_D);
      keyPress(he, KeyEvent.VK_A);
      keyPress(he, KeyEvent.VK_E);
      keyPress(he, KeyEvent.VK_S);
      keyReleased(he, KeyEvent.VK_ENTER);
    });
    t.start();
    assertFalse(wiz.isMoved());
    wiz.setPersonalName("Wizard 1");
    he.moveAll(wiz);
    t.join();
    assertTrue(wiz.isMoved());
    assertEquals(wiz, w.actor(2));
    assertNull(w.actor(1));
    final String q = s.toString();
    assertTrue(q, q.contains("fillRect(510,115,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])"));
  }

  public void testNoMovementAttack() throws InterruptedException {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World world = Chaos.getChaos().getWorld();
    final MockScreen screen = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    world.getWizardManager().setWizard(1, wiz);
    final GreenDragon green = new GreenDragon();
    green.setOwner(1);
    green.set(Attribute.MOVEMENT, 0);
    green.set(Attribute.COMBAT, 100);
    green.set(Attribute.RANGED_COMBAT, 100);
    world.getCell(0).push(green);
    final Gorilla gorilla1 = new Gorilla();
    gorilla1.setOwner(2);
    world.getCell(1).push(gorilla1);
    final Gorilla gorilla2 = new Gorilla();
    gorilla2.setOwner(2);
    world.getCell(2).push(gorilla2);
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    he.setScreen(screen, Chaos.getChaos().getTileManager());
    final Thread t = new Thread(() -> {
      click1(he, 1, 2); // click on dragon
      click1(he, 17, 2); // click on dragon
      //keyPress(he, KeyEvent.VK_D); // engaged attack on gorilla1 in cell 1
      click1(he, 33, 2); // shoot on gorilla2 in cell 2
      keyReleased(he, KeyEvent.VK_ENTER); // end of turn
    });
    t.start();
    assertFalse(wiz.isMoved());
    he.moveAll(wiz);
    t.join();
    assertTrue(green.isMoved());
    assertEquals(State.DEAD, gorilla1.getState());
    assertEquals(1, green.getShotsMade());
    assertEquals(State.DEAD, gorilla2.getState());
  }

  public void testMountTreeThenShoot() throws InterruptedException {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World world = Chaos.getChaos().getWorld();
    final MockScreen screen = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    world.getWizardManager().setWizard(1, wiz);
    wiz.set(Attribute.RANGE, 1);
    wiz.set(Attribute.RANGED_COMBAT, 15);
    wiz.set(Attribute.SHOTS, 1);
    world.getCell(0).push(wiz);
    final MagicWood magicWood = new MagicWood();
    world.getCell(1).push(magicWood);
    final Python python = new Python();
    python.setOwner(2);
    world.getCell(2).push(python);
    // wiz - magic wood - python
    // Mount the tree, then should be able to shoot python
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    he.setScreen(screen, Chaos.getChaos().getTileManager());
    final Thread t = new Thread(() -> {
      click1(he, 1, 1); // click on wizard
      click1(he, 17, 2); // click on tree
      click1(he, 35, 2); // shoot python
      keyReleased(he, KeyEvent.VK_ENTER); // end of turn
    });
    t.start();
    he.moveAll(wiz);
    t.join();
    assertTrue(wiz.isMoved());
    assertEquals(wiz, magicWood.getMount());
    assertEquals(State.DEAD, python.getState());
  }

  public void testKillPromoteShootKill() throws InterruptedException {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    Chaos.reset();
    final World world = Chaos.getChaos().getWorld();
    final MockScreen screen = new MockScreen();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    world.getWizardManager().setWizard(1, wiz);
    final GreenDragon greenDragon = new GreenDragon();
    greenDragon.setOwner(wiz.getOwner());
    for (int k = 0; k < 6; ++k) {
      greenDragon.incrementKillCount();
    }
    world.getCell(0).push(greenDragon);
    final Orc orc = new Orc();
    orc.setOwner(2);
    world.getCell(1).push(orc);
    final Python python = new Python();
    python.setOwner(2);
    world.getCell(2).push(python);
    // green dragon - orc - python
    // Attack and kill orc, gets promoted to emerald, then shoot python
    final HumanEngine he = new HumanEngine(Chaos.getChaos(), 4);
    he.setScreen(screen, Chaos.getChaos().getTileManager());
    final Thread t = new Thread(() -> {
      click1(he, 1, 1); // click on dragon
      click1(he, 17, 2); // kill orc
      click1(he, 35, 2); // shoot python
      keyReleased(he, KeyEvent.VK_ENTER); // end of turn
    });
    t.start();
    he.moveAll(wiz);
    t.join();
    assertNull(world.actor(0));
    assertTrue(world.actor(1) instanceof EmeraldDragon);
    assertEquals(State.DEAD, orc.getState());
    assertEquals(State.DEAD, python.getState());
  }
}
