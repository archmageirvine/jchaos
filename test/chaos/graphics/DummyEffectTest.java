package chaos.graphics;

import java.awt.Graphics;
import java.util.Collection;

import chaos.board.Cell;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DummyEffectTest extends TestCase {

  private static class MyEffect extends AbstractEffect {
    @Override
    public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
      Assert.assertEquals(1, cells.size());
    }
  }

  public void test() {
    final MockScreen screen = new MockScreen();
    new MyEffect().performEffect(screen, screen.getGraphics(), new Cell(2), 16);
    assertEquals("", screen.toString());
  }

  private static class YourEffect extends AbstractEffect {
    @Override
    public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
      Assert.fail();
    }
  }

  public void testNull() {
    final MockScreen screen = new MockScreen();
    new YourEffect().performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
  }
}
