package chaos.graphics;

import chaos.board.World;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class LightningWeaponTest extends TestCase {

  public void test() {
    final World w = new World(5, 4);
    final WeaponEffectEvent we = new WeaponEffectEvent(0, 19, WeaponEffectType.LIGHTNING, null, null);
    final MockScreen screen = new MockScreen();
    LightningWeapon.render(w, screen, screen.getGraphics(), we, 8, 3, 100);
    final String s = screen.toString();
    assertTrue(s.contains("setColor(java.awt.Color[r=255,g=255,b=255])#drawPolyline(4)#"));
    assertTrue(s.contains("setColor(java.awt.Color[r=0,g=255,b=255])#drawPolyline(4)#"));
    assertTrue(s.contains("setColor(java.awt.Color[r=0,g=0,b=0])#drawPolyline(4)#"));
  }

}
