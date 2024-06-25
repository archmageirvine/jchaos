package chaos.graphics;

import java.awt.Graphics2D;

import chaos.board.Team;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import chaos.sound.Sound;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class WeaponEffectHandlerTest extends TestCase {

  public void check(final WeaponEffectEvent event, final String... expected) {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    final MockScreen screen = new MockScreen();
    final Graphics2D g = (Graphics2D) screen.getGraphics();
    final World world = new World(5, 5, new Team());
    final TileManager tm = new ActiveTileManager(5);
    final WeaponEffectHandler handler = new WeaponEffectHandler(new Animator(world, screen, tm), screen, g, world, 5);
    handler.weaponEffect(event);
    final String s = g.toString();
    //System.out.println(s);
    TestUtils.containsAll(s, expected);
  }

  public void testBirdShit() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.BALL),
      "setColor(java.awt.Color[r=255,g=255,b=255])",
      "fillOval(15,16,6,6)",
      "setColor(java.awt.Color[r=0,g=0,b=0])",
      "fillOval(15,23,6,6)"
    );
  }

  public void testTriBeam() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.MONSTER_CAST_EVENT, new Lion(), Attribute.LIFE),
      "setColor(java.awt.Color[r=0,g=255,b=0])",
      "L(-9,-10,-9,-11)",
      "L(-11,-10,-11,-11)",
      "L(15,76,15,76)"
    );
  }

  public void testFireball() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.FIREBALL),
      "I(-1,0,null)",
      "I(-1,17,null)",
      "I(-1,63,null)#"
    );
  }

  public void testSpinner() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.SPINNER),
      "I(-1,0,null)",
      "I(-1,17,null)",
      "I(-1,63,null)#"
    );
  }

  public void testLine() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.LINE),
      "setColor(java.awt.Color[r=255,g=255,b=255])",
      "L(16,16,16,80)"
    );
  }

  public void testIceBeam() {
    check(new WeaponEffectEvent(0, 10, WeaponEffectType.ICE_BEAM),
      "setColor(java.awt.Color[r=255,g=255,b=255])",
      "setColor(java.awt.Color[r=0,g=255,b=255])",
      "L(15,16,15,16)"
    );
  }
}
