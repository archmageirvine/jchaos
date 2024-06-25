package irvine.tile;

import java.util.List;
import java.util.Random;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class TwinkleEffectTest extends AbstractTileEffectTest {


  @Override
  public TileEffect getEffect() {
    return new TwinkleEffect(16, 0, ~0, 2, new Random(42));
  }

  public void testBadCons() {
    try {
      new TwinkleEffect(0, 0, 0, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad width", e.getMessage());
    }
    try {
      new TwinkleEffect(2, 0, 0, 1, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testLowSteps() {
    assertEquals(0, new TwinkleEffect(8, 0, 0, 0).list().size());
    assertEquals(0, new TwinkleEffect(8, 0, 0, -1).list().size());
  }

  public void testRandomly() {
    final Random r = new Random();
    for (int j = 0; j < 10; ++j) {
      final int s = 1 + r.nextInt(100);
      final int l = r.nextInt(100);
      final int bg = r.nextInt();
      final TileEffect e = new TwinkleEffect(s, bg, r.nextInt(), l, r);
      final List<TileImage> list = e.list();
      assertEquals(l, list.size());
      for (int i = 0; i < l; ++i) {
        final TileImage im = list.get(i);
        assertEquals(s, im.getWidth());
        assertEquals(s, im.getHeight());
        if (s > 8) {
          boolean ok = false;
          for (int y = 0; y < s && !ok; ++y) {
            for (int x = 0; x < s && !ok; ++x) {
              ok |= im.getPixel(x, y) == bg;
            }
          }
          assertTrue(ok);
        }
      }
    }
  }

  public void testSize16() {
    final TileEffect ef = new TwinkleEffect(16, 0x808081, 0xDEADBEEF, 100, new Random(42));
    final TileImage i = ef.next();
    assertEquals(16, i.getWidth());
    assertEquals(16, i.getHeight());
    assertEquals(-341842306, i.hashCode());
    for (int j = 0; j < 95; ++j) {
      ef.next();
    }
    assertEquals(-208576494, ef.next().hashCode());
    assertEquals(-822657798, ef.next().hashCode());
    assertEquals(-561434150, ef.next().hashCode());
    assertEquals(-1917414506, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize16comp() {
    final TileEffect ef = new TwinkleEffect(16, 0x808080, ~0xDEADBEEF, 100, new Random(42));
    final TileImage i = ef.next();
    assertEquals(16, i.getWidth());
    assertEquals(16, i.getHeight());
    assertEquals(-341845888, i.hashCode());
    assertEquals(1353508784, ef.next().hashCode());
    assertEquals(-1549415888, ef.next().hashCode());
    assertEquals(64034832, ef.next().hashCode());
    assertEquals(-1333339840, ef.next().hashCode());
    assertEquals(-510327152, ef.next().hashCode());
    assertEquals(-279585872, ef.next().hashCode());
    assertEquals(-1023103744, ef.next().hashCode());
    assertEquals(211852151, ef.next().hashCode());
    assertEquals(358369799, ef.next().hashCode());
    assertEquals(891109607, ef.next().hashCode());
    assertEquals(1960868279, ef.next().hashCode());
    assertEquals(-19658345, ef.next().hashCode());
    assertEquals(-1873915782, ef.next().hashCode());
    assertEquals(-1020829638, ef.next().hashCode());
    assertEquals(1491878531, ef.next().hashCode());
    assertEquals(218759294, ef.next().hashCode());
    assertEquals(201154394, ef.next().hashCode());
    assertEquals(130195783, ef.next().hashCode());
    assertEquals(-807677545, ef.next().hashCode());
    assertEquals(-1228021929, ef.next().hashCode());
    assertEquals(1238831111, ef.next().hashCode());
    assertEquals(1190959089, ef.next().hashCode());
    assertEquals(2062619409, ef.next().hashCode());
    assertEquals(-1245271359, ef.next().hashCode());
    assertEquals(-2095461327, ef.next().hashCode());
    assertEquals(-1854376278, ef.next().hashCode());
    assertEquals(1698490906, ef.next().hashCode());
    assertEquals(1280561027, ef.next().hashCode());
    assertEquals(-1515205021, ef.next().hashCode());
    assertEquals(-122356493, ef.next().hashCode());
    assertEquals(782599309, ef.next().hashCode());
    assertEquals(821484288, ef.next().hashCode());
    assertEquals(-610680816, ef.next().hashCode());
    assertEquals(-738683728, ef.next().hashCode());
    assertEquals(432636423, ef.next().hashCode());
    assertEquals(2100631786, ef.next().hashCode());
    assertEquals(-787425462, ef.next().hashCode());
    assertEquals(-1572085346, ef.next().hashCode());
    assertEquals(1812706606, ef.next().hashCode());
    assertEquals(-503477762, ef.next().hashCode());
    assertEquals(-1166256162, ef.next().hashCode());
    assertEquals(-1265469026, ef.next().hashCode());
    assertEquals(-675025634, ef.next().hashCode());
    assertEquals(1576658462, ef.next().hashCode());
    assertEquals(-610419801, ef.next().hashCode());
    assertEquals(-840052489, ef.next().hashCode());
    assertEquals(1161824028, ef.next().hashCode());
    assertEquals(-1875944980, ef.next().hashCode());
    assertEquals(1593067260, ef.next().hashCode());
    assertEquals(-982321629, ef.next().hashCode());
    assertEquals(-1420871485, ef.next().hashCode());
    assertEquals(-389020595, ef.next().hashCode());
    assertEquals(-1288222067, ef.next().hashCode());
    assertEquals(-644835126, ef.next().hashCode());
    assertEquals(1015980794, ef.next().hashCode());
    assertEquals(-80432483, ef.next().hashCode());
    assertEquals(275300109, ef.next().hashCode());
    assertEquals(-83768451, ef.next().hashCode());
    assertEquals(439401859, ef.next().hashCode());
    assertEquals(-2021840125, ef.next().hashCode());
    assertEquals(-285084214, ef.next().hashCode());
    assertEquals(180973578, ef.next().hashCode());
    assertEquals(668443300, ef.next().hashCode());
    assertEquals(774605332, ef.next().hashCode());
    assertEquals(2141567844, ef.next().hashCode());
    assertEquals(1851957561, ef.next().hashCode());
    assertEquals(1419397705, ef.next().hashCode());
    assertEquals(1247512025, ef.next().hashCode());
    assertEquals(621978851, ef.next().hashCode());
    assertEquals(641422971, ef.next().hashCode());
    assertEquals(-146303733, ef.next().hashCode());
    assertEquals(827017115, ef.next().hashCode());
    assertEquals(-1507207189, ef.next().hashCode());
    assertEquals(-1297260053, ef.next().hashCode());
    assertEquals(373506619, ef.next().hashCode());
    assertEquals(-2139832426, ef.next().hashCode());
    assertEquals(1645550150, ef.next().hashCode());
    assertEquals(-2125817458, ef.next().hashCode());
    assertEquals(-1885446452, ef.next().hashCode());
    assertEquals(-1922819172, ef.next().hashCode());
    assertEquals(671281500, ef.next().hashCode());
    assertEquals(-2128607636, ef.next().hashCode());
    assertEquals(1007222166, ef.next().hashCode());
    assertEquals(-141756584, ef.next().hashCode());
    assertEquals(268085128, ef.next().hashCode());
    assertEquals(-92335214, ef.next().hashCode());
    assertEquals(302949314, ef.next().hashCode());
    assertEquals(1257319128, ef.next().hashCode());
    assertEquals(1320705408, ef.next().hashCode());
    assertEquals(-886977616, ef.next().hashCode());
    assertEquals(-2031983856, ef.next().hashCode());
    assertEquals(127765168, ef.next().hashCode());
    assertEquals(1111860216, ef.next().hashCode());
    assertEquals(256413880, ef.next().hashCode());
    assertEquals(-391800886, ef.next().hashCode());
    assertEquals(-1450091366, ef.next().hashCode());
    assertEquals(1795751874, ef.next().hashCode());
    assertEquals(2071392930, ef.next().hashCode());
    assertEquals(-1740025390, ef.next().hashCode());
    /*
    Image x;
    while ((x = ef.next()) != null) {
      System.out.println("assertEquals(" + x.hashCode() + ", ef.next().hashCode());");
    }
    */
    assertNull(ef.next());
  }

  public void testSize16compx() {
    final TileEffect ef = new TwinkleEffect(16, 0x808079, 0xDEADBEEF, 100, new Random(42));
    final TileImage i = ef.next();
    assertEquals(16, i.getWidth());
    assertEquals(16, i.getHeight());
    for (int j = 0; j < 95; ++j) {
      ef.next();
    }
    assertEquals(-1923519965, ef.next().hashCode());
    assertEquals(-1735952501, ef.next().hashCode());
    assertEquals(-1996915285, ef.next().hashCode());
    assertEquals(-984156943, ef.next().hashCode());
    assertNull(ef.next());
  }


}

