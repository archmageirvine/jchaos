package chaos.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.WeakHashMap;

import javax.imageio.ImageIO;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Conveyance;
import chaos.common.Growth;
import chaos.common.Named;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.free.Horde;
import chaos.common.growth.Flood;
import chaos.common.growth.GreenOoze;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.Spawner;
import chaos.common.monster.Gollop;
import chaos.common.monster.NamedDeity;
import chaos.common.monster.NamedSnake;
import irvine.tile.TileSetReader;
import irvine.util.Pair;
import irvine.util.graphics.BufferedImageUtils;
import irvine.util.io.IOUtils;

/**
 * Provides access to a set of dynamic tile set.
 *
 * @author Sean A. Irvine
 */
public class ActiveTileManager extends AbstractTileManager {

  private static final SeamlessTiling FLOOD = new SeamlessTiling("chaos/graphics/generic/water.jpg");
  private static final SeamlessTiling OOZE = new SeamlessTiling("chaos/graphics/generic/ooze.jpg");

  /** Maintains tile set based on castable. */
  private final HashMap<String, TileSet> mCastableToTiles = new HashMap<>();

  /** Maintains animation state information for active tiles. */
  private final WeakHashMap<Castable, AnimState> mAnimationState = new WeakHashMap<>();

  private static class AnimState {
    int mAnimState;
  }

  /** Image to use for unsatisfiable requests */
  private final BufferedImage mUnknown;
  /** Tile specifications mapping. */
  private final HashMap<String, String> mTileSpec = new HashMap<>();
  /** Location of graphics resources. */
  private final TileSetReader mTileSetReader;
  private final int mBits;

  /**
   * Creates a new tile manager.
   * @param bits bits per tile
   */
  public ActiveTileManager(final int bits) {
    mBits = bits;
    final int w = 1 << bits;
    final String resourceDir = "chaos/graphics/active" + w;
    try {
      // initialize the unknown image
      try (final InputStream in = new BufferedInputStream(Objects.requireNonNull(ActiveTileManager.class.getClassLoader().getResourceAsStream(resourceDir + "/unknown.jpg")))) {
        mUnknown = ImageIO.read(in);
      }
      try (final BufferedReader r = IOUtils.reader("chaos/graphics/active.txt")) {
        String line;
        while ((line = r.readLine()) != null) {
          if (!line.isEmpty() && line.charAt(0) != '#') {
            final int space = line.indexOf(' ');
            mTileSpec.put(line.substring(0, space), line.substring(space).trim());
          }
        }
      }
      mTileSetReader = new TileSetReader(bits, resourceDir);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Initialize the tile set for a castable.
   *
   * @param castableName the name to get the TileSet for
   * @return the resulting TileSet.
   */
  protected TileSet init(String castableName) {
    try {
      final int ci = castableName.lastIndexOf('.');
      if (ci != -1) {
        castableName = castableName.substring(ci + 1);
      }
      final String spec = mTileSpec.get(castableName);
      if (spec != null) {
        return new TileSet(mTileSetReader, spec);
      }
      return null;
    } catch (final IOException e) {
      //      throw new RuntimeException("TileSet for " + c + " could not be established");
      // silently ignore, just means no imagery for this Castable is available
      return null;
    }
  }

  private HashMap<Pair<Class<? extends Named>, String>, TileSet> mNamedColouredTileSets = new HashMap<>();

  private static final int[] SOURCE = {0xFF0000, 0x0000FF, 0x00FF00, 0xFFFF00, 0x00FFFF, 0xFF0FF, 0xFFEF00};
  private static final int[][] REPLACEMENTS = {
    {0xFF0000, 0x3B1111, 0x2B1A5C, 0x35423A, 0xCB6215, 0xCB1533, 0x703333}, // "coat" color
    {0xEBB08C, 0x8C5331, 0xECA267, 0xE7B394, 0xD7B7C7}, // skin color
    {0x20582E, 0x8FD09E, 0xDD0000, 0xE1DA12, 0x3838F1}, // "cloak" color
    {0xEDE5A4, 0x4D4332, 0x806232, 0x662F1A}, // hair color
    {0xFEFEFE, 0x00FFFC, 0x80FFFF, 0xE76AE8}, // weapon color
    {0xDD0000, 0x00DD00, 0x0000DD, 0xE57216}, // glove color
    {0x303030, 0xC00000, 0x505050, 0x7F4040}
  };

  private TileSet getNamedColoredTileSet(final Named c) {
    final String name = c.getPersonalName();
    final Pair<Class<? extends Named>, String> key = new Pair<>(c.getClass(), name);
    final TileSet ts = mNamedColouredTileSets.get(key);
    if (ts != null) {
      return ts;
    }
    // This is the first time we have requested this tile set
    final TileSet baseTileSet = init(c.getClass().getName());
    if (baseTileSet == null) {
      System.out.println("Huh failed to get base tile set for " + c.getClass());
      return null;
    }
    final BufferedImage[] recoloured = new BufferedImage[baseTileSet.getNumberOfImages()];
    for (int k = 0; k < recoloured.length; ++k) {
      BufferedImage im = baseTileSet.getImage(k);
      int hash = name.hashCode();
      if (hash < 0) {
        im = BufferedImageUtils.flip(im);
        hash = ~hash;
      }
      for (int j = 0; j < SOURCE.length; ++j) {
        final int modulus = REPLACEMENTS[j].length;
        final int replacementColor = REPLACEMENTS[j][hash % modulus];
        hash /= modulus;
        im = BufferedImageUtils.replace(im, SOURCE[j], replacementColor);
      }
      recoloured[k] = im;
    }
    final TileSet newTileSet = new TileSet(recoloured, baseTileSet.getTileSetType());
    mNamedColouredTileSets.put(key, newTileSet);
    return newTileSet;
  }

  private static final Color ALPHA_BLUE = new Color(0xA06060FF, true);
  private static final Color ALPHA_CYAN = new Color(0x6000FFFF, true);
  private static final Color ALPHA_RED = new Color(0xA0FF4040, true);
  private static final Color ALPHA_PINK = new Color(0xA0FF79FF, true);

  private TileSet getTileSetWithRing(final TileSet inner, final Color c1, final Color c2) {
    final BufferedImage im = BufferedImageUtils.copy(inner.getSpellImage());
    final Graphics2D g = (Graphics2D) im.getGraphics();
    if (g != null) {
      g.setStroke(new BasicStroke(3));
      g.setColor(c1);
      g.drawOval(3, 3, im.getWidth() - 6, im.getHeight() - 6);
      g.setStroke(new BasicStroke());
      g.setColor(c2);
      g.drawOval(3, 3, im.getWidth() - 6, im.getHeight() - 6);
      g.dispose();
    }
    return new TileSet(new BufferedImage[] {im}, TileSet.TileSetType.SEQUENTIAL);
  }

  private static final int BLACK = 0xFF000000;

  private BufferedImage getHyadicTile(final BufferedImage image) {
    final BufferedImage im = BufferedImageUtils.copy(image);
    // Left shift every third row
    for (int y = 0; y < im.getHeight(); y += 3) {
      for (int x = 0; x < im.getWidth() - 1; ++x) {
        im.setRGB(x, y, im.getRGB(x + 1, y));
      }
      im.setRGB(im.getWidth() - 1, y, BLACK);
    }
    // Right shift every third row
    for (int y = 2; y < im.getHeight(); y += 3) {
      for (int x = im.getWidth() - 1; x > 0; --x) {
        im.setRGB(x, y, im.getRGB(x - 1, y));
      }
      im.setRGB(0, y, BLACK);
    }
    return im;
  }

  private TileSet getHyadicTileSet(final TileSet inner) {
    final BufferedImage[] res = new BufferedImage[inner.getNumberOfImages()];
    for (int k = 0; k < res.length; ++k) {
      res[k] = getHyadicTile(inner.getImage(k));
    }
    return new TileSet(res, inner.getTileSetType());
  }

  private TileSet getSpawnerTileSet(final Spawner spawner) {
    return getTileSetWithRing(getTileSet(spawner.getSpawn()), ALPHA_BLUE, ALPHA_CYAN);
  }

  private TileSet getHordeTileSet(final Horde horde) {
    return getTileSetWithRing(getTileSet(horde.getSpawn()), ALPHA_RED, ALPHA_PINK);
  }

  private TileSet getTileSet(final Castable c) {
    assert c != null;
    String tileSetName = c.getClass().getName();
    final boolean mounted = c instanceof Conveyance && ((Conveyance) c).getMount() != null;
    TileSet ts = null;
    if (mounted) {
      final String mname = tileSetName + "_M";
      ts = mCastableToTiles.get(mname);
      if (ts == null) {
        // load and install appropriate tile set
        ts = init(mname);
        if (ts != null) {
          mCastableToTiles.put(mname, ts);
        }
      }
      if (ts != null) {
        tileSetName = mname;
      }
    }
    if (c instanceof Gollop) { // others could be like this in future
      final int life = Math.min(9, ((Gollop) c).get(Attribute.LIFE) / 10);
      final String mname = tileSetName + "_" + life;
      ts = mCastableToTiles.get(mname);
      if (ts == null) {
        // load and install appropriate tile set
        ts = init(mname);
        if (ts != null) {
          mCastableToTiles.put(mname, ts);
        }
      }
      if (ts != null) {
        tileSetName = mname;
      }
    }
    if (c instanceof Spawner) {
      ts = getSpawnerTileSet((Spawner) c);
    } else if (c instanceof Horde) {
      ts = getHordeTileSet((Horde) c);
    } else if (c instanceof NamedDeity || c instanceof NamedSnake) {
      ts = getNamedColoredTileSet((Named) c);
    } else if (ts == null) {
      ts = mCastableToTiles.computeIfAbsent(tileSetName, this::init);
    }
    if (ts != null && c instanceof Actor && ((Actor) c).getRealm() == Realm.HYADIC) {
      final String hyadicName = tileSetName + "_HYADIC";
      final TileSet tsHyadic = mCastableToTiles.get(hyadicName);
      if (tsHyadic == null) {
        ts = getHyadicTileSet(ts);
        mCastableToTiles.put(hyadicName, ts);
      } else {
        ts = tsHyadic;
      }
    }
    return ts;
  }

  /** Stores replacement images for poisoned objects. */
  private final HashMap<BufferedImage, BufferedImage> mPoisonedCache = new HashMap<>();

  private BufferedImage poisoned(final BufferedImage im) {
    final BufferedImage p = mPoisonedCache.get(im);
    if (p != null) {
      return p;
    }
    final BufferedImage pi = BufferedImageUtils.grayScale(im);
    mPoisonedCache.put(im, pi);
    return pi;
  }

  @Override
  public BufferedImage getTile(final Castable c, final int x, final int y, final int context) {
    if (c == null) {
      return null;
    }
    final BufferedImage im;
    // These next two must be before the general tile lookup and are safe
    // since we never need a dead image for them.
    if (c instanceof Flood) {
      im = FLOOD.getTile(x, y, mBits);
    } else if (c instanceof GreenOoze) {
      im = OOZE.getTile(x, y, mBits);
    } else {
      final TileSet ts = getTileSet(c);
      if (ts == null) {
        return mUnknown;
      }
      if (c instanceof Actor) {
        final Actor a = (Actor) c;
        if (a.getState() == State.DEAD) {
          im = ts.getDeadImage();
        } else if (ts.getTileSetType() == TileSet.TileSetType.FOUR_CONTEXT) {
          im = ts.getImage(context);
        } else {
          if (ts.isRandomSelect()) {
            // Handle actors with multiple static images to choose from
            im = ts.getImage(Math.abs(System.identityHashCode(a) % ts.getNumberOfImages()));
          } else {
            // consult weak hash map for animation state
            AnimState state = mAnimationState.get(c);
            if (state == null) {
              // make a new mapping
              state = new AnimState();
              mAnimationState.put(c, state);
            } else if (a.getState() != State.ASLEEP && (!(a instanceof Exit) || ((Exit) a).isOpen())) {
              // bump to the next state
              state.mAnimState = ts.nextFrameIndex(state.mAnimState);
            }
            im = ts.getImage(state.mAnimState);
          }
        }
      } else {
        // general castable
        im = ts.getImage(0);
      }
    }
    if (c instanceof Actor && ((Actor) c).is(PowerUps.CLOAKED)) {
      return BufferedImageUtils.darken(im);
    }
    if ((c instanceof Growth && ((Actor) c).is(PowerUps.NO_GROW)) || (c instanceof Exit && !((Exit) c).isOpen())) {
      return poisoned(im);
    }
    return im;
  }

  @Override
  public BufferedImage getSpellTile(final Castable c) {
    if (c == null) {
      return null;
    }
    if (c instanceof Flood) {
      return FLOOD.getTile(0, 0, mBits);
    }
    if (c instanceof GreenOoze) {
      return OOZE.getTile(0, 0, mBits);
    }
    final TileSet ts = getTileSet(c);
    if (ts == null) {
      return mUnknown;
    }
    return ts.getImage(0);
  }

  @Override
  public int getWidthBits() {
    return mBits;
  }
}
