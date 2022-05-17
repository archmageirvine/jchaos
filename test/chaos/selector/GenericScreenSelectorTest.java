package chaos.selector;

import java.awt.Container;
import java.awt.event.MouseEvent;

import chaos.Chaos;
import chaos.Configuration;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.free.Arborist;
import chaos.common.monster.Iridium;
import chaos.common.wizard.Wizard1;
import chaos.graphics.MockScreen;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.Sleep;
import junit.framework.TestCase;

/**
 * Tests this selector.
 *
 * @author Sean A. Irvine
 */
public class GenericScreenSelectorTest extends TestCase {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
  }

  public void testNull() {
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setPersonalName("Wizard 1");
    final GenericScreenSelector s = new GenericScreenSelector(wiz1, new Configuration(), Chaos.getChaos().getTileManager(), Chaos.getChaos().getWorld(), Chaos.getChaos().getCastMaster());
    assertNull(s.select(null, false)[0]);
  }

  private static class CancelSelector extends GenericScreenSelector {
    CancelSelector(final Wizard1 wiz) {
      super(wiz, new Configuration(), Chaos.getChaos().getTileManager(), Chaos.getChaos().getWorld(), Chaos.getChaos().getCastMaster());
      new Thread(() -> {
        Sleep.sleep(200);
        mouseMoved(new MouseEvent(new Container(), 1, 0, 0, 20, 20, 1, false, MouseEvent.NOBUTTON));
        // In continue gadget
        mouseMoved(new MouseEvent(new Container(), 1, 0, 0, MockScreen.CONTINUE_X, MockScreen.CONTINUE_Y, 1, false, MouseEvent.NOBUTTON));
        mouseMoved(new MouseEvent(new Container(), 1, 0, 0, 532, 46, 1, false, MouseEvent.NOBUTTON));
        mouseMoved(new MouseEvent(new Container(), 1, 0, 0, 20, 20, 1, false, MouseEvent.NOBUTTON));
        mLock.setValue(true);
      }).start();
    }
  }

  public void testCancelAndBasicOperation() {
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setPersonalName("Wizard 1");
    final Castable[] list = {new Iridium(), new Arborist(), new Arborist(), new Iridium(), new Arborist()};
    final MockScreen screen = new MockScreen();
    final GenericScreenSelector s = new CancelSelector(wiz1);
    s.setScreen(screen);
    assertNull(s.select(list, false)[0]);
    assertEquals("writePhase(SPELL SELECTION,Wizard 1)#setCursor(CROSS)#setContinueGadget(true)#addMouseMotionListener(.)#addMouseListener(.)#addKeyListener(.)#highlight(null)#highlightContinue(true)#highlight(null)#highlightContinue(false)#writeMessage(chaos.common.wizard.Wizard1,chaos.common.free.Arborist)#highlight(none)#highlight(532,44,16,16)#highlight(null)#highlightContinue(false)#writeMessage()#removeKeyListener(.)#removeMouseListener(.)#removeMouseMotionListener(.)#writeMessage(Selected: Nothing)#blankRight()#highlight(null)#setContinueGadget(false)#|I(500,20,null)#I(532,20,null)#I(564,20,null)#I(500,44,null)#I(532,44,null)#I(532,44,null)#", screen.toString());
  }

  private static class ClickSelector extends GenericScreenSelector {

    private final BooleanLock mWaitForInfoPanel = new BooleanLock(false);
    private final BooleanLock mWaitForPostInfoPanel = new BooleanLock(false);

    ClickSelector(final MockScreen screen, final Wizard1 wiz) {
      super(wiz, new Configuration(), Chaos.getChaos().getTileManager(), Chaos.getChaos().getWorld(), Chaos.getChaos().getCastMaster());
      setScreen(screen);
      new Thread(() -> {
        while (!screen.getMouseListeners().contains(ClickSelector.this)) {
          try {
            Thread.sleep(10);
          } catch (final InterruptedException e) {
            // ignore
          }
        }
        final int x = 566;
        mouseMoved(new MouseEvent(new Container(), 1, 0, 0, x, 22, 1, false, MouseEvent.NOBUTTON));
        // Bring up info panel
        mouseClicked(new MouseEvent(new Container(), 1, 0, 0, x, 22, 1, false, MouseEvent.BUTTON3));
        try {
          mWaitForInfoPanel.waitUntilTrue(1000);
        } catch (final InterruptedException e) {
          // ignore
        }
        // Cancel info panel
        mouseClicked(new MouseEvent(new Container(), 1, 0, 0, 50, 50, 1, false, MouseEvent.BUTTON1));
        try {
          mWaitForPostInfoPanel.waitUntilTrue(1000);
        } catch (final InterruptedException e) {
          // ignore
        }
        Thread.yield();
        // Select iridium
        mouseClicked(new MouseEvent(new Container(), 1, 0, 0, x, 22, 1, false, MouseEvent.BUTTON1));

      }).start();
    }

    @Override
    protected void informationDisplay(final int cell) {
      mWaitForInfoPanel.setValue(true);
      super.informationDisplay(cell);
      mWaitForPostInfoPanel.setValue(true);
    }

  }

  public void testSelectBasicOperation() {
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setPersonalName("Wizard 1");
    wiz1.setState(State.ACTIVE);
    final Castable[] list = {new Arborist(), new Arborist(), new Iridium(), new Arborist(), new Arborist()};
    final Castable ir = list[2];
    final MockScreen screen = new MockScreen();
    final ClickSelector selector = new ClickSelector(screen, wiz1);
    final Castable castable = selector.select(list, false)[0];
    final String x = screen.toString();
    assertTrue(x, x.startsWith("writePhase(SPELL SELECTION,Wizard 1)#setCursor(CROSS)#setContinueGadget(true)#addMouseMotionListener(.)#addMouseListener(.)#addKeyListener(.)#highlight(null)#highlightContinue(false)#writeMessage(chaos.common.wizard.Wizard1,chaos.common.monster.Iridium)#highlight(material)#highlight("));
    assertEquals(ir, castable);
    assertTrue(x.contains("drawString(statistics,"));
  }

  private static class Bonus640Selector extends GenericScreenSelector {
    Bonus640Selector(final Wizard1 wiz) {
      super(wiz, new Configuration(), Chaos.getChaos().getTileManager(), Chaos.getChaos().getWorld(), Chaos.getChaos().getCastMaster());
      new Thread(() -> {
        Sleep.sleep(200);
        mLock.setValue(true);
      }).start();
    }
  }

  public void testSelectBonus() {
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setPersonalName("Wizard 1");
    final Castable[] list = {new Iridium()};
    final MockScreen screen = new MockScreen();
    final GenericScreenSelector s = new Bonus640Selector(wiz1);
    s.setScreen(screen);
    assertEquals(0, s.selectBonus(list, 1).length);
    assertEquals("writeMessage(Selecting 1 of 1 bonus spells.)#writePhase(BONUS SELECTION,Wizard 1)#setCursor(CROSS)#setContinueGadget(true)#addMouseMotionListener(.)#addMouseListener(.)#addKeyListener(.)#removeKeyListener(.)#removeMouseListener(.)#removeMouseMotionListener(.)#blankRight()#|I(500,20,null)#", screen.toString());
  }

  private static class NoSpellsSelector extends GenericScreenSelector {
    NoSpellsSelector(final Wizard1 wiz) {
      super(wiz, new Configuration(), Chaos.getChaos().getTileManager(), Chaos.getChaos().getWorld(), Chaos.getChaos().getCastMaster());
      new Thread(() -> {
        Sleep.sleep(200);
        mLock.setValue(true);
      }).start();
    }
  }

  public void testNoSpells() {
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setPersonalName("Wizard 1");
    final Castable[] list = new Castable[0];
    final MockScreen screen = new MockScreen();
    final GenericScreenSelector s = new NoSpellsSelector(wiz1);
    s.setScreen(screen);
    assertNull(s.select(list, false)[0]);
    final String str = screen.toString();
    assertTrue(str, str.startsWith("writePhase(SPELL SELECTION,Wizard 1)#setCursor(CROSS)#setContinueGadget(true)#addMouseMotionListener(.)#addMouseListener(.)#addKeyListener(.)#removeKeyListener(.)#removeMouseListener(.)#removeMouseMotionListener(.)#writeMessage(Selected: Nothing)#blankRight()#highlight(null)#setContinueGadget(false)#|setColor(java.awt.Color[r=255,g=255,b=0])#setFont()#getFontMetrics()#drawString(NO SPELLS!,"));
  }
}
