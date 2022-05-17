package chaos.common;

import java.awt.Color;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RealmTest extends TestCase {

  public void testEnum() {
    assertEquals("none", Realm.NONE.toString());
    assertEquals("material", Realm.MATERIAL.toString());
    assertEquals("undead", Realm.ETHERIC.toString());
    assertEquals("dragon", Realm.DRACONIC.toString());
    assertEquals("demon", Realm.DEMONIC.toString());
    assertEquals("mythos", Realm.MYTHOS.toString());
    assertEquals("hyperastral", Realm.HYPERASTRAL.toString());
    assertEquals("hyadic", Realm.HYADIC.toString());
    assertEquals("subhyadic", Realm.SUBHYADIC.toString());
    assertEquals("core", Realm.CORE.toString());

    assertEquals(0, Realm.NONE.ordinal());
    assertEquals(1, Realm.MATERIAL.ordinal());
    assertEquals(2, Realm.ETHERIC.ordinal());
    assertEquals(3, Realm.DRACONIC.ordinal());
    assertEquals(4, Realm.DEMONIC.ordinal());
    assertEquals(5, Realm.MYTHOS.ordinal());
    assertEquals(6, Realm.HYPERASTRAL.ordinal());
    assertEquals(7, Realm.HYADIC.ordinal());
    assertEquals(8, Realm.SUBHYADIC.ordinal());
    assertEquals(9, Realm.CORE.ordinal());

    assertEquals(Realm.NONE, Realm.valueOf("NONE"));
    assertEquals(Realm.MATERIAL, Realm.valueOf("MATERIAL"));
    assertEquals(Realm.ETHERIC, Realm.valueOf("ETHERIC"));
    assertEquals(Realm.DRACONIC, Realm.valueOf("DRACONIC"));
    assertEquals(Realm.DEMONIC, Realm.valueOf("DEMONIC"));
    assertEquals(Realm.MYTHOS, Realm.valueOf("MYTHOS"));
    assertEquals(Realm.HYPERASTRAL, Realm.valueOf("HYPERASTRAL"));
    assertEquals(Realm.HYADIC, Realm.valueOf("HYADIC"));
    assertEquals(Realm.SUBHYADIC, Realm.valueOf("SUBHYADIC"));
    assertEquals(Realm.CORE, Realm.valueOf("CORE"));

    assertNull(Realm.NONE.getColor());
    assertEquals(Color.GREEN, Realm.MATERIAL.getColor());
    assertEquals(Color.MAGENTA, Realm.ETHERIC.getColor());
    assertEquals(Color.BLUE, Realm.DRACONIC.getColor());
    assertEquals(Color.RED, Realm.DEMONIC.getColor());
    assertEquals(Color.YELLOW, Realm.MYTHOS.getColor());
    assertEquals(Color.WHITE, Realm.HYPERASTRAL.getColor());
    assertEquals(Color.GRAY, Realm.HYADIC.getColor());
    assertEquals(Color.PINK, Realm.SUBHYADIC.getColor());
    assertEquals(Color.CYAN, Realm.CORE.getColor());
  }

  public void testAttacks() {
    try {
      Realm.realmCheck(null, Realm.NONE);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      Realm.realmCheck(Realm.NONE, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.NONE));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.ETHERIC));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.DRACONIC));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.SUBHYADIC));
    assertFalse(Realm.realmCheck(Realm.NONE, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.MATERIAL, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.ETHERIC));
    assertTrue(Realm.realmCheck(Realm.MATERIAL, Realm.DRACONIC));
    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.DEMONIC));
    assertTrue(Realm.realmCheck(Realm.MATERIAL, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.MATERIAL, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.MATERIAL, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.ETHERIC, Realm.MATERIAL));
    assertTrue(Realm.realmCheck(Realm.ETHERIC, Realm.ETHERIC));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.DRACONIC));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.ETHERIC, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.ETHERIC, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.DRACONIC, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.ETHERIC));
    assertTrue(Realm.realmCheck(Realm.DRACONIC, Realm.DRACONIC));
    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.DRACONIC, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.DRACONIC, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.DEMONIC, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.DEMONIC, Realm.MATERIAL));
    assertTrue(Realm.realmCheck(Realm.DEMONIC, Realm.ETHERIC));
    assertFalse(Realm.realmCheck(Realm.DEMONIC, Realm.DRACONIC));
    assertTrue(Realm.realmCheck(Realm.DEMONIC, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.DEMONIC, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.DEMONIC, Realm.HYPERASTRAL));
    assertTrue(Realm.realmCheck(Realm.DEMONIC, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.DEMONIC, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.DEMONIC, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.MYTHOS, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.MYTHOS, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.MYTHOS, Realm.ETHERIC));
    assertFalse(Realm.realmCheck(Realm.MYTHOS, Realm.DRACONIC));
    assertTrue(Realm.realmCheck(Realm.MYTHOS, Realm.DEMONIC));
    assertTrue(Realm.realmCheck(Realm.MYTHOS, Realm.MYTHOS));
    assertTrue(Realm.realmCheck(Realm.MYTHOS, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.MYTHOS, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.MYTHOS, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.MYTHOS, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.NONE));
    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.ETHERIC));
    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.DRACONIC));
    assertTrue(Realm.realmCheck(Realm.HYPERASTRAL, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.MYTHOS));
    assertTrue(Realm.realmCheck(Realm.HYPERASTRAL, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.HYPERASTRAL, Realm.HYADIC));
    assertTrue(Realm.realmCheck(Realm.HYPERASTRAL, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.HYPERASTRAL, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.CORE, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.MATERIAL));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.ETHERIC));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.DRACONIC));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.DEMONIC));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.MYTHOS));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.HYPERASTRAL));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.HYADIC));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.CORE, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.NONE));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.ETHERIC));
    assertTrue(Realm.realmCheck(Realm.HYADIC, Realm.DRACONIC));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.DEMONIC));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.HYPERASTRAL));
    assertTrue(Realm.realmCheck(Realm.HYADIC, Realm.HYADIC));
    assertFalse(Realm.realmCheck(Realm.HYADIC, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.HYADIC, Realm.CORE));

    assertFalse(Realm.realmCheck(Realm.SUBHYADIC, Realm.NONE));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.MATERIAL));
    assertFalse(Realm.realmCheck(Realm.SUBHYADIC, Realm.ETHERIC));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.DRACONIC));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.DEMONIC));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.MYTHOS));
    assertFalse(Realm.realmCheck(Realm.SUBHYADIC, Realm.HYPERASTRAL));
    assertFalse(Realm.realmCheck(Realm.SUBHYADIC, Realm.HYADIC));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.SUBHYADIC));
    assertTrue(Realm.realmCheck(Realm.SUBHYADIC, Realm.CORE));
  }
}
