package chaos.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import irvine.tile.ExplodingCircleEffect;
import irvine.tile.ExplodingSquareEffect;
import irvine.tile.ExplosionEffect;
import irvine.tile.TileEffect;
import irvine.tile.TileImage;
import irvine.tile.TwirlEffect;

/**
 * Various arrays of effects.
 * @author Sean A. Irvine
 */
class EffectArray {

  /** Lazy cache of shields. */
  private final HashMap<Color, BufferedImage[]> mShields = new HashMap<>();
  private final int mWidth;
  final BufferedImage[] mCorpseExplosionEffect;
  final BufferedImage[] mWhiteCircleExplode;
  final BufferedImage[] mGreenCircleExplode;
  final BufferedImage[] mRedCircleExplode;
  final BufferedImage[] mBlueCircleExplode;
  final BufferedImage[] mOrangeCircleExplode;
  final BufferedImage[] mWarp;
  final BufferedImage[] mTwirlEffect;

  private BufferedImage[] initCorpseExplode() {
    final TileEffect i = new ExplodingCircleEffect(mWidth, 0xFF000000, 0xFFFF0000);
    i.next();
    i.next();
    i.next();
    i.next();
    i.next();
    i.next();
    final ArrayList<BufferedImage> k = new ArrayList<>();
    for (final TileImage j : new ExplosionEffect(i.next(), 0xFF000000, false).list()) {
      k.add(j.toBufferedImage());
    }
    return k.toArray(new BufferedImage[0]);
  }

  private BufferedImage[] initCircleExplode(final int colour) {
    final ArrayList<BufferedImage> k = new ArrayList<>();
    for (final TileImage j : new ExplodingCircleEffect(mWidth, 0xFF000000, colour).list()) {
      k.add(j.toBufferedImage());
    }
    return k.toArray(new BufferedImage[0]);
  }

  private BufferedImage[] initWarp() {
    final ArrayList<BufferedImage> k = new ArrayList<>();
    for (final TileImage j : new ExplodingSquareEffect(mWidth, 0xFF000000, new int[] {~0, 0xFF000000}).list()) {
      k.add(j.toBufferedImage());
    }
    return k.toArray(new BufferedImage[0]);
  }

  private BufferedImage[] initTwirlEffect() {
    final ArrayList<BufferedImage> k = new ArrayList<>();
    for (final TileImage j : new TwirlEffect(mWidth, 0xFF000000, 0xFF00FFFF, 2, 30.0).list()) {
      k.add(j.toBufferedImage());
    }
    return k.toArray(new BufferedImage[0]);
  }

  EffectArray(final int bits) {
    mWidth = 1 << bits;
    mCorpseExplosionEffect = initCorpseExplode();
    mWhiteCircleExplode = initCircleExplode(~0);
    mGreenCircleExplode = initCircleExplode(0xFF00FF00);
    mRedCircleExplode = initCircleExplode(0xFFFF0000);
    mBlueCircleExplode = initCircleExplode(0xFF0000FF);
    mOrangeCircleExplode = initCircleExplode(0xFFFF6600);
    mWarp = initWarp();
    mTwirlEffect = initTwirlEffect();
  }

  BufferedImage[] getShieldArray(final Color fg) {
    BufferedImage[] res = mShields.get(fg);
    if (res == null) {
      final TileEffect i = new ExplodingCircleEffect(mWidth, 0xFF000000, 0xFF000000 | fg.getRGB());
      i.next();
      i.next();
      i.next();
      i.next();
      i.next();
      i.next();
      final TileImage circle = i.next();
      final ArrayList<BufferedImage> k = new ArrayList<>();
      for (final TileImage j : new ExplosionEffect(circle, 0xFF000000, false).list()) {
        k.add(j.toBufferedImage());
      }
      res = new BufferedImage[k.size() + 1];
      for (int l = 0, m = k.size() - 1; m >= 0; ++l, --m) {
        res[l] = k.get(m);
      }
      res[k.size()] = circle.toBufferedImage();
      mShields.put(fg, res);
    }
    return res;
  }
}
