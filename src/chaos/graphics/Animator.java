package chaos.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.CellEvent;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.Tree;
import chaos.common.inanimate.ForceFence;
import chaos.sound.Sound;
import chaos.util.AttackCellEffectEvent;
import chaos.util.AudioEvent;
import chaos.util.BooleanLock;
import chaos.util.CellEffectEvent;
import chaos.util.ChaosProperties;
import chaos.util.CombatUtils;
import chaos.util.CurrentMoverEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.HighlightEvent;
import chaos.util.PolycellAttackEvent;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldDestroyEvent;
import chaos.util.PolyshieldEvent;
import chaos.util.PowerUpEvent;
import chaos.util.RingCellEffectEvent;
import chaos.util.ShieldDestroyedEvent;
import chaos.util.ShieldGrantedEvent;
import chaos.util.ShootIconEvent;
import chaos.util.Sleep;
import chaos.util.TextEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WingsIconEvent;
import irvine.tile.TileImage;
import irvine.tile.TwinkleEffect;

/**
 * Animate the specified World in the specified graphics environment.
 * @author Sean A. Irvine
 */
@SuppressWarnings("SuspiciousNameCombination")
public class Animator implements EventListener {

  private static final Sound SOUND = Sound.getSoundEngine();
  private static final Random RANDOM = new Random();

  private static final int HIGHLIGHT_PAUSE = ChaosProperties.properties().getIntProperty("chaos.highlight.pause", 3000);
  private static final int PAUSE50 = ChaosProperties.properties().getIntProperty("chaos.pause", 50);
  private static final int APAUSE = ChaosProperties.properties().getIntProperty("chaos.animator.pause", 800);
  private static final int PAUSE30 = ChaosProperties.properties().getIntProperty("chaos.pause30", 30);
  private static final int PRIORITY = ChaosProperties.properties().getIntProperty("chaos.animator.scale.priority", 1);
  private static final boolean GRAPHICS_SYNC = ChaosProperties.properties().getBooleanProperty("chaos.animator.sync", true);
  private static final int NONE = -1;

  private final TileManager mTM;
  private final ChaosScreen mScreen;
  private final Graphics mGraphics;
  private final World mWorld;
  private final Team mTeam;
  private final int mTileWidthBits;
  private final int mTileWidth;
  private final Thread mAnimationThread;
  private final EffectArray mEffectArray;
  private final int mPause30;
  private final int mPause50;
  private final int[] mCoordsWorkspace = new int[2];
  private final WeaponEffectHandler mWeaponEffectHandler;

  private final Image mKeyboardWingsCursor;
  private final Image mKeyboardShootCursor;

  private final BufferedImage mBlueTranslucentOverlay;
  private final BufferedImage mCyanTranslucentOverlay;
  private final BufferedImage mGreenTranslucentOverlay;

  private final int[] mPixelCoordX;
  private final int[] mPixelCoordY;

  private int mCurrentlyActivePlayer = Actor.OWNER_NONE;

  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  /**
   * Wait for the display.
   */
  public static void sync() {
    if (GRAPHICS_SYNC) {
      TOOLKIT.sync();
    }
  }

  /**
   * Construct a new animator.
   * @param w world to animate
   * @param screen screen to use
   * @param tileManager tile manager
   */
  public Animator(final World w, final ChaosScreen screen, final TileManager tileManager) {
    mWorld = w;
    mTeam = w.getTeamInformation();
    mScreen = screen;
    mScreen.fillMain(Color.BLACK);
    mGraphics = mScreen.getGraphics();
    if (mGraphics == null) {
      throw new NullPointerException("Failed to get a graphics object");
    }
    mTM = tileManager;
    mTileWidthBits = mTM.getWidthBits();
    mTileWidth = mTM.getWidth();
    mEffectArray = new EffectArray(mTileWidthBits);
    mPause30 = PAUSE30 / (mTileWidthBits - 3);
    mPause50 = PAUSE50 / (mTileWidthBits - 3);
    mBlueTranslucentOverlay = ImageLoader.translucentOverlay(mTileWidth, 0xFF);
    mCyanTranslucentOverlay = ImageLoader.translucentOverlayWithBorder(mTileWidth, 0xFF, 0xFFFF);
    mGreenTranslucentOverlay = ImageLoader.translucentOverlay(mTileWidth, 0x3F00);
    mAnimationThread = init();
    mKeyboardWingsCursor = ImageLoader.getImage("chaos/resources/icons/wings.png").getScaledInstance(mTileWidth, mTileWidth, BufferedImage.SCALE_SMOOTH);
    mKeyboardShootCursor = ImageLoader.getImage("chaos/resources/icons/shoot.png").getScaledInstance(mTileWidth, mTileWidth, BufferedImage.SCALE_SMOOTH);
    mWeaponEffectHandler = new WeaponEffectHandler(this, mScreen, (Graphics2D) mGraphics, mWorld, mTileWidthBits);
    mPixelCoordX = new int[mWorld.size()];
    mPixelCoordY = new int[mWorld.size()];
    // Compute the pixel coordinates for every cell
    for (int cell = 0; cell < mWorld.size(); ++cell) {
      mWorld.getCellCoordinates(cell, mCoordsWorkspace);
      mPixelCoordX[cell] = (mCoordsWorkspace[0] << mTileWidthBits) + mScreen.getXOffset();
      mPixelCoordY[cell] = (mCoordsWorkspace[1] << mTileWidthBits) + mScreen.getYOffset();
    }
  }

  private int getContext(final Actor a, final int x, final int y) {
    if (a instanceof ForceFence) {
      int c = 0;
      if (mWorld.actor(x + 1, y) instanceof ForceFence) {
        c |= 1;
      }
      if (mWorld.actor(x - 1, y) instanceof ForceFence) {
        c |= 2;
      }
      if (mWorld.actor(x, y - 1) instanceof ForceFence) {
        c |= 4;
      }
      if (mWorld.actor(x, y + 1) instanceof ForceFence) {
        c |= 8;
      }
      return c;
    }
    return 0;
  }

  /* Draw the cell with the specified game coordinates (not pixel coordinates). */
  void drawCell(final int x, final int y) {
    synchronized (mScreen.lock()) {
      final Actor a = mWorld.actor(x, y);
      if (a == null) {
        mScreen.drawCell(null, x, y);
      } else {
        mScreen.drawCell(mTM.getTile(a, x, y, getContext(a, x, y)), x, y);
        if (a.getState() == State.ACTIVE) {
          final int owner = a.getOwner();
          if (owner == mCurrentlyActivePlayer) {
            mScreen.drawCell(a.isMoved() || a.get(Attribute.MOVEMENT) == 0 || !(a instanceof Monster) ? mBlueTranslucentOverlay : mCyanTranslucentOverlay, x, y);
          } else if (mTeam.getTeam(mCurrentlyActivePlayer) == mTeam.getTeam(owner)) {
            mScreen.drawCell(mGreenTranslucentOverlay, x, y);
          }
        }
      }
    }
  }

  void drawCell(final int cell) {
    mWorld.getCellCoordinates(cell, mCoordsWorkspace);
    drawCell(mCoordsWorkspace[0], mCoordsWorkspace[1]);
  }

  /** Currently highlighted cell (if any). */
  private int mHighlightedCell = NONE;
  private int mWingsCell = NONE;
  private int mShootCell = NONE;

  private void highlight(final int cell) {
    synchronized (mScreen.lock()) {
      if (mHighlightedCell != NONE && mHighlightedCell != cell) {
        drawCell(mHighlightedCell);
      }
      mHighlightedCell = cell;
      if (mHighlightedCell != NONE) {
        mScreen.highlight(mPixelCoordX[mHighlightedCell], mPixelCoordY[mHighlightedCell], mTileWidth);
      }
    }
  }

  private int keyCursor(final Image im, final int cell, int current) {
    synchronized (mScreen.lock()) {
      if (current != NONE && current != cell) {
        drawCell(current);
      }
      current = cell;
      if (current != NONE) {
        mGraphics.drawImage(im, mPixelCoordX[cell], mPixelCoordY[cell], null);
      }
    }
    return current;
  }

  private void wings(final int cell) {
    mWingsCell = keyCursor(mKeyboardWingsCursor, cell, mWingsCell);
  }

  private void shoot(final int cell) {
    mShootCell = keyCursor(mKeyboardShootCursor, cell, mShootCell);
  }

  private Set<Integer> mHighlightedCells = null;

  /**
   * A set of cells to be highlighted or null.
   * @param cells cells to be highlighted
   */
  public void highlight(final Set<Integer> cells) {
    synchronized (mScreen.lock()) {
      if (cells != mHighlightedCells && mHighlightedCells != null) {
        for (final Integer c : mHighlightedCells) {
          drawCell(c);
        }
      }
      mHighlightedCells = cells;
    }
  }

  private void lightHighlight(final int cell) {
    synchronized (mScreen.lock()) {
      mScreen.lightHighlight(mPixelCoordX[cell], mPixelCoordY[cell], mTileWidth);
    }
  }

  private void playArray(final BufferedImage[] pics, final int[] x, final int[] y, final int sleep) {
    assert x.length == y.length;
    synchronized (mScreen.lock()) {
      for (final BufferedImage i : pics) {
        for (int k = 0; k < x.length; ++k) {
          mScreen.drawCell(i, x[k], y[k]);
        }
        Sleep.sleep(sleep);
      }
    }
  }

  private void playArray(final BufferedImage[] pics, final int x, final int y, final int sleep) {
    synchronized (mScreen.lock()) {
      for (final BufferedImage i : pics) {
        mScreen.drawCell(i, x, y);
        Sleep.sleep(sleep);
      }
    }
  }

  private void playArrayReversed(final BufferedImage[] pics, final int x, final int y, final int sleep) {
    synchronized (mScreen.lock()) {
      for (int j = pics.length - 1; j >= 0; --j) {
        mScreen.drawCell(pics[j], x, y);
        Sleep.sleep(sleep);
      }
    }
  }

  private String getSoundResource(final Actor a) {
    if (a instanceof Tree) {
      return "chaos/resources/sound/casting/arborist";
    }
    if (a instanceof Meditation) {
      return "chaos/resources/sound/casting/castle";
    }
    return "chaos/resources/sound/misc/CreatureCast";
  }

  private void monsterCastEvent(final Collection<Cell> cells, final Actor a) {
    final Realm realm = a.getRealm();
    final int prior = SoundLevel.whatSoundLevel(a);
    mScreen.highlight(realm);
    final int color = 0xFF000000 | realm.getColor().getRGB();
    final BooleanLock s = SOUND.play(getSoundResource(a), prior);
    final int level = SOUND.getSoundLevel();
    final int delay = prior <= level ? 20 : (level == Sound.SOUND_NONE ? 0 : 5);
    final TwinkleEffect twinkle = new TwinkleEffect(mTileWidth, 0, color, 40);
    TileImage t;
    while ((t = twinkle.next()) != null) {
      for (final Cell c : cells) {
        final int cell = c.getCellNumber();
        mGraphics.drawImage(t.toBufferedImage(), mPixelCoordX[cell], mPixelCoordY[cell], null);
      }
      Sleep.sleep(delay);
    }
    SOUND.wait(s, 5000);
    mScreen.highlight((Realm) null);
  }

  private void powerUpEffect(final PowerUpEvent ce) {
    final int[] xy = new int[2];
    mWorld.getCellCoordinates(ce.getCellNumber(), xy);
    final int prior = SoundLevel.whatSoundLevel(ce.getActor());
    final PowerUps type = ce.getPowerUp();
    final String sound = type == null ? null : type.castingSound();
    final BooleanLock status = SOUND.play(sound, prior);
    playArray(mEffectArray.mBlueCircleExplode, xy[0], xy[1], mPause30);
    SOUND.wait(status, 5000);
  }

  private void audioEffect(final AudioEvent ae) {
    final Actor actor = ae.getActor();
    final int prior = actor == null ? Sound.SOUND_NONE : SoundLevel.whatSoundLevel(ae.getCause(), actor);
    final String sound = "chaos/resources/sound/special/" + ae.getSoundEffect();
    SOUND.play(sound, prior);
    //final SoundStatus status = SOUND.play(sound, prior);
    //SOUND.wait(status, 5000);
  }

  private void renderPlus(final Graphics g, final int x, final int y, final int w) {
    final int u = w / 2;
    final int t = w / 4;
    final int v = w / 8;
    g.fillRect(x, y + u - v, w, t);
    g.fillRect(x + u - v, y, t, w);
  }

  private Color randomColor() {
    return new Color(RANDOM.nextInt(0x1000000));
  }

  private void xp(final int cell, final int soundPriority) {
    final BooleanLock status = SOUND.play("chaos/resources/sound/misc/xp", soundPriority);
    final int x = mPixelCoordX[cell];
    final int y = mPixelCoordY[cell];
    final int w = mTileWidth / 2 - 2;
    for (int k = 0; k < 20; ++k) {
      mGraphics.setColor(randomColor());
      renderPlus(mGraphics, x, y, w);
      mGraphics.setColor(randomColor());
      renderPlus(mGraphics, x + mTileWidth - w, y, w);
      mGraphics.setColor(randomColor());
      renderPlus(mGraphics, x, y + mTileWidth - w, w);
      mGraphics.setColor(randomColor());
      renderPlus(mGraphics, x + mTileWidth - w, y + mTileWidth - w, w);
      Sleep.sleep(10);
    }
    SOUND.wait(status, 5000);
  }

  private void cellEffect(final CellEffectEvent ce) {
    final int[] xy = new int[2];
    mWorld.getCellCoordinates(ce.getCellNumber(), xy);
    final Actor xa = ce.getActor();
    final int prior = SoundLevel.whatSoundLevel(xa);
    final BooleanLock status;
    switch (ce.getEventType()) {
      case WIZARD_EXPLODE:
        new WizardExplodeEffect(mWorld, this, mTM).performEffect(mScreen, mGraphics, mWorld.getCell(ce.getCellNumber()), mTileWidth);
        break;
      case OWNER_CHANGE:
        new OwnerChangeEffect(mWorld, mTM).performEffect(mScreen, mGraphics, mWorld.getCell(ce.getCellNumber()), mTileWidth);
        break;
      case MEDITATION_COLLAPSE:
        new CollapseEffect(mWorld, mTM, xa).performEffect(mScreen, mGraphics, mWorld.getCell(ce.getCellNumber()), mTileWidth);
        break;
      case CHANGE_REALM:
        new RealmChangeEffect(mWorld, mTM, xa).performEffect(mScreen, mGraphics, mWorld.getCell(ce.getCellNumber()), mTileWidth);
        break;
      case FIREBALL_EXPLODE:
        final Collection<Cell> targets = mWorld.getCells(ce.getCellNumber(), 0, 1, false);
        final ImageList fireball = ImageList.getList("fireball", mTileWidthBits);
        final int level = SoundLevel.whatSoundLevel(xa, targets);
        new ImageListExplodeEffect(mWorld, this, fireball, "chaos/resources/sound/casting/Explode", level).performEffect(mScreen, mGraphics, mWorld.getCell(ce.getCellNumber()), mTileWidth);
        break;
      case BOMB:
        playBomb(ce.getCellNumber(), 0xFFFFE030, xa);
        break;
      case ICE_BOMB:
        playBomb(ce.getCellNumber(), 0xFF30C0FF, xa);
        break;
      case ATTACK_EVENT:
        final AttackCellEffectEvent ace = (AttackCellEffectEvent) ce;
        new AttackEffect(mWorld, mTM, ace.getOffence(), ace.getDamage(), ace.getType()).performEffect(mScreen, mGraphics, mWorld.getCell(ace.getCellNumber()), mTileWidth);
        break;
      case MOVEMENT_EVENT:
        //SOUND.playwait("chaos/resources/sound/misc/Beep", Sound.SOUND_INTELLIGENT);
        break;
      case MONSTER_CAST_EVENT:
        monsterCastEvent(Collections.singleton(mWorld.getCell(ce.getCellNumber())), xa);
        break;
      case HIGHLIGHT_EVENT:
        highlight(ce.getCellNumber());
        break;
      case CORPSE_EXPLODE:
        playArrayReversed(mEffectArray.mCorpseExplosionEffect, xy[0], xy[1], mPause30);
        break;
      case POWERUP:
        playArray(mEffectArray.mCorpseExplosionEffect, xy[0], xy[1], mPause30);
        break;
      case DEATH:
        status = SOUND.play("chaos/resources/sound/casting/DeathBringer", prior);
        playArray(mEffectArray.mBlueCircleExplode, xy[0], xy[1], mPause50);
        SOUND.wait(status, 5000);
        break;
      case EXPERIENCE:
        xp(ce.getCellNumber(), prior);
        break;
      case ACQUISITION:
        status = SOUND.play("chaos/resources/sound/casting/Acquisition", prior);
        playArray(mEffectArray.mCorpseExplosionEffect, xy[0], xy[1], mPause30);
        SOUND.wait(status, 5000);
        break;
      case SHIELD_GRANTED:
        playArray(mEffectArray.getShieldArray(new Color(0xFF00FF)), xy[0], xy[1], mPause30);
        break;
      case TWIRL:
        playArray(mEffectArray.mTwirlEffect, xy[0], xy[1], mPause30);
        break;
      case RAISE_DEAD:
        status = SOUND.play("chaos/resources/sound/casting/raise", prior);
        playArray(mEffectArray.mTwirlEffect, xy[0], xy[1], mPause30);
        SOUND.wait(status, 5000);
        break;
      case BONUS:
        SOUND.playwait("chaos/resources/sound/misc/bonus", prior);
        break;
      case POISON:
        SOUND.playwait("chaos/resources/sound/misc/poison", prior);
        break;
      case WHITE_CIRCLE_EXPLODE:
        playArray(mEffectArray.mWhiteCircleExplode, xy[0], xy[1], mPause50);
        break;
      case ORANGE_CIRCLE_EXPLODE:
        playArray(mEffectArray.mOrangeCircleExplode, xy[0], xy[1], mPause50);
        break;
      case REINCARNATE:
        playArrayReversed(mEffectArray.mOrangeCircleExplode, xy[0], xy[1], mPause50);
        break;
      case GREEN_CIRCLE_EXPLODE:
        playArray(mEffectArray.mGreenCircleExplode, xy[0], xy[1], mPause50);
        break;
      case WARP_IN:
        status = SOUND.play("chaos/resources/sound/misc/Teleport", prior);
        playArray(mEffectArray.mWarp, xy[0], xy[1], mPause50);
        SOUND.wait(status, 5000);
        break;
      case WARP_OUT:
        final BooleanLock sssss = SOUND.play(RANDOM.nextInt(20) == 0 ? "chaos/resources/sound/misc/WarpOut" : "chaos/resources/sound/misc/Teleport", prior);
        playArrayReversed(mEffectArray.mWarp, xy[0], xy[1], mPause50);
        SOUND.wait(sssss, 5000);
        break;
      case SPELL_FAILED:
        playArray(mEffectArray.mRedCircleExplode, xy[0], xy[1], mPause50);
        synchronized (mScreen.lock()) {
          drawCell(xy[0], xy[1]);
        }
        break;
      case REDRAW_CELL:
        synchronized (mScreen.lock()) {
          drawCell(xy[0], xy[1]);
        }
        break;
      case TEAM_CHANGE:
        SOUND.play("chaos/resources/sound/misc/woow", prior);
        break;
      case RING:
        if (ce instanceof RingCellEffectEvent) {
          ringEffect((RingCellEffectEvent) ce);
        }
        break;
      default:
        System.err.println("Unhandled celleffectevent: " + ce.getEventType());
        break;
    }
  }

  private static final BasicStroke RING2 = new BasicStroke(2);

  private void ringEffect(final RingCellEffectEvent ce) {
    final int centre = ce.getCellNumber();
    mWorld.getCellCoordinates(centre, mRedrawXY);
    final int h = mTileWidth / 2;
    final int pixelRadius = ce.getRadius() * mTileWidth - h;
    final Graphics2D g = (Graphics2D) mGraphics;
    final Stroke oldStroke = g.getStroke();
    g.setStroke(RING2);
    g.setColor(ce.getColor());
    final Shape oldClip = g.getClip();
    g.setClip(mScreen.getXOffset(), mScreen.getYOffset(), mWorld.width() * mTileWidth, mWorld.height() * mTileWidth);
    g.drawOval(mPixelCoordX[centre] - pixelRadius + h + RANDOM.nextInt(6) - 3, mPixelCoordY[centre] - pixelRadius + h + RANDOM.nextInt(6) - 3, 2 * pixelRadius, 2 * pixelRadius);
    g.setStroke(oldStroke);
    g.setClip(oldClip);
  }

  private void playBomb(final int bombCell, final int color, final Actor source) {
    new BombEffect(mWorld, color, source).performEffect(mScreen, mGraphics, mWorld.getCell(bombCell), mTileWidth);
    // Redraw damaged cells
    for (final Cell r : mWorld.getCells(bombCell, 0, 1, false)) {
      drawCell(r.getCellNumber());
    }
  }

  /** Reused memory for drawing cells. */
  private final int[] mRedrawXY = new int[2];

  private void polycellEffect(final PolycellEffectEvent event) {
    final Collection<Cell> cells = event.getCells();
    final int[] xy = new int[2];
    final Castable cause = event.getCause();
    switch (event.getEventType()) {
      case REDRAW_CELL:
        synchronized (mScreen.lock()) {
          for (final Cell c : cells) {
            mWorld.getCellCoordinates(c.getCellNumber(), mRedrawXY);
            drawCell(mRedrawXY[0], mRedrawXY[1]);
          }
        }
        break;
      case FADE_TO_RED:
        synchronized (mScreen.lock()) {
          for (int k = 0; k < 20; ++k) {
            mGraphics.setColor(new Color(0x80, 0, 0, 0x10));
            for (final Cell c : cells) {
              final int cell = c.getCellNumber();
              mGraphics.fillRect(mPixelCoordX[cell], mPixelCoordY[cell], mTileWidth, mTileWidth);
            }
            Sleep.sleep(100);
          }
        }
        break;
      case CHANGE_REALM:
        new RealmChangeEffect(mWorld, mTM, cause instanceof Actor ? (Actor) cause : null).performEffect(mScreen, mGraphics, cells, mTileWidth);
        break;
      case OWNER_CHANGE:
        new OwnerChangeEffect(mWorld, mTM).performEffect(mScreen, mGraphics, cells, mTileWidth);
        break;
      case GREEN_CIRCLE_EXPLODE:
        // to do sound
        synchronized (mScreen.lock()) {
          for (final BufferedImage i : mEffectArray.mGreenCircleExplode) {
            for (final Cell c : cells) {
              final int cn = c.getCellNumber();
              if (cn >= 0) {
                mWorld.getCellCoordinates(cn, mRedrawXY);
                mScreen.drawCell(i, mRedrawXY[0], mRedrawXY[1]);
              }
            }
            Sleep.sleep(mPause50);
          }
        }
        break;
      case HIGHLIGHT_EVENT:
        synchronized (mScreen.lock()) {
          mGraphics.setXORMode(Color.GREEN);
          for (final Cell c : cells) {
            final int cell = c.getCellNumber();
            mGraphics.fillRect(mPixelCoordX[cell], mPixelCoordY[cell], mTileWidth, mTileWidth);
          }
          mGraphics.setPaintMode();
          Sleep.sleep(HIGHLIGHT_PAUSE);
        }
        break;
      case ATTACK_EVENT:
        final int d = event instanceof PolycellAttackEvent ? ((PolycellAttackEvent) event).getDamage() : cause instanceof Monster ? ((Monster) cause).get(Attribute.SPECIAL_COMBAT) : 0;
        new AttackEffect(mWorld, mTM, (Actor) cause, d, CombatUtils.SPECIAL).performEffect(mScreen, mGraphics, cells, mTileWidth);
        break;
      case TWIRL:
        final int[] x = new int[cells.size()];
        final int[] y = new int[cells.size()];
        int j = 0;
        for (final Cell c : cells) {
          mWorld.getCellCoordinates(c.getCellNumber(), xy);
          x[j] = xy[0];
          y[j++] = xy[1];
        }
        playArray(mEffectArray.mTwirlEffect, x, y, mPause30);
        break;
      case SPUNGER:
        final SpungerEffect se = new SpungerEffect(mTM, mWorld, SoundLevel.whatSoundLevel((Actor) cause, cells));
        se.performEffect(mScreen, mGraphics, cells, mTileWidth);
        break;
      case MONSTER_CAST_EVENT:
        monsterCastEvent(cells, (Actor) cause);
        break;
      default:
        System.err.println("Unhandled polycell event: " + event.getEventType());
        break;
    }
  }

  @Override
  public void update(final Event e) {
    if (e instanceof CellEvent) {
      final int[] xy = new int[2];
      mWorld.getCellCoordinates(((CellEvent) e).getCellNumber(), xy);
      drawCell(xy[0], xy[1]);
    } else if (e instanceof TextEvent) {
      mScreen.writeMessage(e.toString());
    } else if (e instanceof ShieldGrantedEvent) {
      final ShieldGrantedEvent sge = (ShieldGrantedEvent) e;
      final ShieldEffect ef = new ShieldEffect(mWorld, sge.getAttribute(), sge.getActor());
      ef.performEffect(mScreen, mGraphics, mWorld.getCell(sge.getCellNumber()), mTileWidth);
    } else if (e instanceof ShieldDestroyedEvent) {
      final ShieldDestroyedEvent sde = (ShieldDestroyedEvent) e;
      final UnshieldEffect ef = new UnshieldEffect(mWorld, sde.getAttribute(), sde.getActor());
      ef.performEffect(mScreen, mGraphics, mWorld.getCell(sde.getCellNumber()), mTileWidth);
    } else if (e instanceof PowerUpEvent) {
      powerUpEffect((PowerUpEvent) e);
    } else if (e instanceof AudioEvent) {
      audioEffect((AudioEvent) e);
    } else if (e instanceof CellEffectEvent) {
      cellEffect((CellEffectEvent) e);
    } else if (e instanceof WeaponEffectEvent) {
      mWeaponEffectHandler.weaponEffect((WeaponEffectEvent) e);
    } else if (e instanceof PolyshieldEvent) {
      final PolyshieldEvent pe = (PolyshieldEvent) e;
      final ShieldEffect ef = new ShieldEffect(mWorld, pe.getAttribute(), pe.getCause());
      ef.performEffect(mScreen, mGraphics, pe.getCells(), mTileWidth);
    } else if (e instanceof PolyshieldDestroyEvent) {
      final PolyshieldDestroyEvent pe = (PolyshieldDestroyEvent) e;
      final UnshieldEffect ef = new UnshieldEffect(mWorld, pe.getAttribute(), pe.getCause());
      ef.performEffect(mScreen, mGraphics, pe.getCells(), mTileWidth);
    } else if (e instanceof PolycellEffectEvent) {
      polycellEffect((PolycellEffectEvent) e);
    } else if (e instanceof HighlightEvent) {
      highlight(((HighlightEvent) e).getCells());
    } else if (e instanceof WingsIconEvent) {
      wings(((WingsIconEvent) e).getCell());
    } else if (e instanceof ShootIconEvent) {
      shoot(((ShootIconEvent) e).getCell());
    } else if (e instanceof CurrentMoverEvent) {
      mCurrentlyActivePlayer = ((CurrentMoverEvent) e).getCurrentPlayer();
    }
  }

  private Thread init() {
    final Thread animator = new Thread() {
      @Override
      public synchronized void run() {
        while (!Thread.currentThread().isInterrupted()) {
          if (mHighlightedCells != null) {
            for (int c = 0; c < mWorld.size(); ++c) {
              synchronized (mScreen.lock()) {
                drawCell(c);
                if (c == mHighlightedCell) {
                  highlight(c);
                } else if (mHighlightedCells != null && mHighlightedCells.contains(c)) {
                  // Check for null above is essential, because our lock
                  // is not high enough to guarantee that mHighlightedCells
                  // has not changed between the initial test and the position in the loop.
                  lightHighlight(c);
                }
                if (c == mWingsCell) {
                  wings(c);
                } else if (c == mShootCell) {
                  shoot(c);
                }
              }
            }
          } else {
            for (int c = 0; c < mWorld.size(); ++c) {
              synchronized (mScreen.lock()) {
                drawCell(c);
                if (c == mHighlightedCell) {
                  highlight(c);
                }
                if (c == mWingsCell) {
                  wings(c);
                } else if (c == mShootCell) {
                  shoot(c);
                }
              }
            }
          }
          // pause until next frame
          try {
            this.wait(APAUSE);
          } catch (final InterruptedException e) {
            return;
          }
        }
      }
    };
    if (PRIORITY != 1) {
      animator.setPriority(Math.max(1, animator.getPriority() / PRIORITY));
    }
    return animator;
  }

  void stop() {
    mAnimationThread.interrupt();
    try {
      mAnimationThread.join();
    } catch (final InterruptedException e) {
      // too bad
    }
  }

  /** Start the animator. */
  public void start() {
    mAnimationThread.start();
  }

  void line(final int source, final int target, final Color color) {
    final int delta = mTileWidth / 2;
    synchronized (mScreen.lock()) {
      mGraphics.setColor(color);
      mGraphics.drawLine(mPixelCoordX[source] + delta, mPixelCoordY[source] + delta, mPixelCoordX[target] + delta, mPixelCoordY[target] + delta);
      sync();
    }
  }

}
