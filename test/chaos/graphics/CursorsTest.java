package chaos.graphics;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import irvine.math.IntegerUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CursorsTest extends TestCase {

  private void checkCursor(final Container c, final String name) {
    final Cursor cc = c.getCursor();
    assertEquals(name, cc.getName());
    assertEquals(Cursor.CUSTOM_CURSOR, cc.getType());
  }

  public void testBlank() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setBlankCursor(c);
    checkCursor(c, "BLANK");
  }

  public void testShoot() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setShootCursor(c);
    checkCursor(c, "SHOOT");
  }

  public void testWings() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setWingsCursor(c);
    checkCursor(c, "WINGS");
  }

  public void testCast() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setCastCursor(c);
    checkCursor(c, "CAST");
  }

  public void testCrossCursor() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setCrossCursor(c);
    checkCursor(c, "CROSS");
  }

  public void testDismount() {
    if (GraphicsEnvironment.isHeadless()) {
      return;
    }
    final Container c = new Container();
    Cursors.setDismountCursor(c);
    checkCursor(c, "DISMOUNT");
  }

  public void testDismountImage() {
    final BufferedImage im = Cursors.dismountImage(16, 16, 16);
    final int[] rgb = im.getRGB(0, 0, 16, 16, null, 0, 16);
    final long sum = IntegerUtils.sum(rgb);
    assertEquals(-133691452, sum);
  }

  public void testCrossImage() {
    final BufferedImage im = Cursors.crossImage(16, 16);
    final int[] rgb = im.getRGB(0, 0, 16, 16, null, 0, 16);
    final long sum = IntegerUtils.sum(rgb);
    assertEquals(-208892812, sum);
  }
}
