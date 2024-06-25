package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ExitTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Exit();
  }

  public void test() {
    final Exit e = new Exit();
    AbstractMonsterTest.checkAgainstSource(e);
    assertEquals(0, e.getCastFlags());
  }

  public void testOpen() {
    final Exit e = new Exit();
    assertFalse(e.isOpen());
    assertEquals("Exit (closed)", e.getPersonalName());
    e.setOpen(true);
    assertEquals("Exit (open)", e.getPersonalName());
    assertTrue(e.isOpen());
  }
}
