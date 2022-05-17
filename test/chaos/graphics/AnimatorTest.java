package chaos.graphics;

import java.util.ArrayList;
import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.CellEvent;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import chaos.sound.Sound;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ChaosProperties;
import chaos.util.HighlightEvent;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldDestroyEvent;
import chaos.util.PolyshieldEvent;
import chaos.util.ShieldDestroyedEvent;
import chaos.util.ShieldGrantedEvent;
import chaos.util.ShootIconEvent;
import chaos.util.Sleep;
import chaos.util.TextEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import chaos.util.WingsIconEvent;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AnimatorTest extends TestCase {

  public void test1() {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    ChaosProperties.properties().setProperty("chaos.highlight.pause", "1");
    ChaosProperties.properties().setProperty("chaos.pause", "1");
    ChaosProperties.properties().setProperty("chaos.animator.pause", "1");
    ChaosProperties.properties().setProperty("chaos.pause30", "1");
    ChaosProperties.properties().setProperty("chaos.bomb.decay", "100");
    final World w = new World(3, 3);
    final MockScreen s = new MockScreen();
    final Animator a = new Animator(w, s, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16));
    a.start();
    try {
      Sleep.sleep(100);
    } finally {
      a.stop();
    }
    assertTrue(s.toString().startsWith("fillMain(java.awt.Color[r=0,g=0,b=0])#drawCell(0,0)#drawCell(1,0)#drawCell(2,0)#drawCell(0,1)#drawCell(1,1)#drawCell(2,1)#drawCell(0,2)#drawCell(1,2)#drawCell(2,2)#"));
  }

  private static final String EXPECTED = "writeMessage(CellEvent)#drawCell(2,0)#writeMessage(ShieldGrantedEvent)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#writeMessage(ShieldDestroyedEvent)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#drawCell(1,0)#writeMessage(CellEffectEvent)#writeMessage(WeaponEffectEvent)#drawCell(1,0)#writeMessage(HighlightEvent)#writeMessage(TextEvent)#writeMessage(Moron)#writeMessage(PolyshieldEvent)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#writeMessage(PolyshieldDestroyEvent)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#writeMessage(WingsIconEvent)#writeMessage(ShootIconEvent)#writeMessage("
    + "\nPCE:REDRAW_CELL)#drawCell(2,0)#drawCell(0,1)#writeMessage("
    + "\nPCE:CHANGE_REALM)#writeMessage("
    + "\nPCE:OWNER_CHANGE)#writeMessage("
    + "\nPCE:GREEN_CIRCLE_EXPLODE)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#writeMessage("
    + "\nPCE:HIGHLIGHT_EVENT)#writeMessage("
    + "\nPCE:ATTACK_EVENT)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#drawCell(2,0)#drawCell(0,1)#writeMessage("
    + "\nPCE:SPUNGER)#|setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(24,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(24,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(25,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(25,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(26,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(26,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(27,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(27,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(28,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(28,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(29,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(29,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(30,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(30,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(31,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(31,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(32,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(32,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(33,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(33,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(34,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(34,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(35,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(35,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(36,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(36,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(37,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(37,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(38,7,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(38,7,6,6)#setColor(java.awt.Color[r=255,g=255,b=255])#fillOval(39,7,6,6)#";

  private static final CellEffectType[] MY_TYPES = {
    CellEffectType.REDRAW_CELL,
    CellEffectType.CHANGE_REALM,
    CellEffectType.OWNER_CHANGE,
    CellEffectType.GREEN_CIRCLE_EXPLODE,
    CellEffectType.HIGHLIGHT_EVENT,
    CellEffectType.ATTACK_EVENT,
    CellEffectType.SPUNGER,
  };

  public void testRamThroughEvents() {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    ChaosProperties.properties().setProperty("chaos.highlight.pause", "1");
    ChaosProperties.properties().setProperty("chaos.pause", "1");
    ChaosProperties.properties().setProperty("chaos.animator.pause", "1");
    ChaosProperties.properties().setProperty("chaos.pause30", "1");
    ChaosProperties.properties().setProperty("chaos.bomb.decay", "100");
    final World w = new World(3, 3);
    final MockScreen s = new MockScreen();
    final ArrayList<Cell> h = new ArrayList<>();
    h.add(w.getCell(2));
    h.add(w.getCell(3));
    final HashSet<Integer> hi = new HashSet<>();
    hi.add(2);
    hi.add(3);
    final Lion lion = new Lion();
    final Animator a = new Animator(w, s, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16));
    final String pre = s.toString();
    s.writeMessage("CellEvent");
    a.update(new CellEvent(2));
    s.writeMessage("ShieldGrantedEvent");
    a.update(new ShieldGrantedEvent(1, lion, Attribute.LIFE));
    s.writeMessage("ShieldDestroyedEvent");
    a.update(new ShieldDestroyedEvent(1, lion, Attribute.LIFE));
    s.writeMessage("CellEffectEvent");
    a.update(new CellEffectEvent(1, CellEffectType.OWNER_CHANGE, lion));
    s.writeMessage("WeaponEffectEvent");
    a.update(new WeaponEffectEvent(1, 2, WeaponEffectType.BALL));
    s.writeMessage("HighlightEvent");
    a.update(new HighlightEvent(hi));
    s.writeMessage("TextEvent");
    a.update(new TextEvent("Moron"));
    s.writeMessage("PolyshieldEvent");
    a.update(new PolyshieldEvent(h, Attribute.LIFE, lion));
    s.writeMessage("PolyshieldDestroyEvent");
    a.update(new PolyshieldDestroyEvent(h, Attribute.LIFE, lion));
    s.writeMessage("WingsIconEvent");
    a.update(new WingsIconEvent(1));
    s.writeMessage("ShootIconEvent");
    a.update(new ShootIconEvent(1));
    for (final CellEffectType t : MY_TYPES) {
      s.writeMessage("\nPCE:" + t);
      a.update(new PolycellEffectEvent(h, t, lion));
    }

    final String x = s.toString();
    //    System.out.println(x);
    assertTrue(x.startsWith(pre + EXPECTED));
  }
}
