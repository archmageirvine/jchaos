package chaos.graphics;

import java.util.Arrays;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.util.ChaosProperties;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PlasmaWeaponTest extends TestCase {

  public void test() {
    ChaosProperties.properties().setProperty("chaos.plasma.time", "100");
    final MockScreen s = new MockScreen();
    final WeaponEffectEvent e = new WeaponEffectEvent(1, 2, WeaponEffectType.PLASMA, null, Attribute.LIFE);
    PlasmaWeapon.render(new World(3, 3), s, s.getGraphics(), e, 4, 2);
    final String x = s.toString();
    assertTrue(x.startsWith("|setColor(java.awt.Color[r="));
    assertTrue(x.contains("#drawPolyline(4)#setColor(java.awt.Color[r=0,g=0,b=0])#fillRect(6,1,5,2)#"));
  }

  private static class MyMockScreen extends MockScreen {
    @Override
    public int getXOffset() {
      return 1;
    }

    @Override
    public int getYOffset() {
      return 2;
    }
  }

  public void testCoords() {
    final MockScreen s = new MyMockScreen();
    assertEquals("[17, 18]", Arrays.toString(PlasmaWeapon.cellToCoords(new World(2, 2), s, 4, 3)));
  }
}
