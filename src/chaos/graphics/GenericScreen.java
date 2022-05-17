package chaos.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import chaos.Configuration;
import chaos.board.CastMaster;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Realm;
import chaos.sound.Sound;
import chaos.util.NameUtils;
import irvine.util.RuntimeUtils;
import irvine.util.graphics.FullScreen;
import irvine.util.graphics.Stipple;

/**
 * A generic screen for playing Chaos.
 * @author Sean A. Irvine
 */
public class GenericScreen extends FullScreen implements ChaosScreen {

  static final Image BLANK = ImageLoader.getImage("chaos/resources/icons/blank.png");
  static final Image EXIT = ImageLoader.getImage("chaos/resources/icons/exit.png");
  static final Image CONT = ImageLoader.getImage("chaos/resources/icons/return.png");
  private static final int CONT_WIDTH = Render.getWidthOfImage(CONT);

  private final int mCellBits;
  private final int mCellWidth;
  private final int mWidth;
  private final int mHeight;
  private final int mXOffset;
  private final int mYOffset;
  private final int mMainWidth;
  private final int mMainHeight;
  private final int mXTextOffset;
  private final int mYTextOffset;
  private final int mXRightOffset;
  private final int mYRightOffset;
  private final int mRightWidth;
  private final int mXPhaseOffset;
  private final int mYPhaseOffset;
  private final int mPhaseWidth;
  private final int mTextWidth;
  private final int mTextHeight;
  private final int mXCloseLim;
  private final int mXSoundLim;
  private final int mXContinue;
  private final int mYContinue;
  private final Object mLock = new Object();
  private transient BufferedImage mRightBlank = null;
  private transient BufferedImage mPhaseBlank = null;
  private transient BufferedImage mBottomBlank = null;
  private transient BufferedImage mCenterBlank = null;
  private transient ChaosFonts mFonts = null;
  private transient HandleExit mExitHandler = null;
  private transient PentagramHighlighter mPentagramHighlighter = null;
  private transient Graphics mGraphics;
  private transient Font mDefaultFont = null;

  /**
   * Construct a new instance of Chaos using specified screen configuration.
   * @param config screen configuration
   * @param fullScreen true to use full screen exclusive mode
   * @param screen which display to use
   */
  public GenericScreen(final Configuration config, final boolean fullScreen, final int screen) {
    super("Chaos", config.getPixelWidth(), config.getPixelHeight(), 24, fullScreen, screen);
    mWidth = config.getPixelWidth();
    mHeight = config.getPixelHeight();
    mCellWidth = config.getCellWidth();
    mCellBits = config.getCellBits();
    mXOffset = mCellWidth;
    mYOffset = mCellWidth;
    mMainWidth = config.getCols() * mCellWidth;
    mMainHeight = config.getRows() * mCellWidth;
    mXTextOffset = mCellWidth;
    mYTextOffset = mMainHeight + mYOffset + mCellWidth;
    mXRightOffset = mMainWidth + mXOffset + mCellWidth;
    mYRightOffset = 3 * mCellWidth;
    mRightWidth = mWidth - mCellWidth - mXRightOffset;
    mXPhaseOffset = mXRightOffset - mCellWidth / 2;
    mYPhaseOffset = mCellWidth + mCellWidth / 8;
    mPhaseWidth = mWidth - mXPhaseOffset - mCellWidth / 2;
    mTextWidth = mMainWidth;
    mTextHeight = 2 * mCellWidth;
    final int yCloseLim = EXIT.getHeight(null);
    final int iconWidth = Render.getWidthOfImage(EXIT);
    mXCloseLim = mWidth - iconWidth;
    mXSoundLim = mWidth - 3 * iconWidth;
    mXContinue = mWidth - (mWidth - mXRightOffset + CONT_WIDTH + mCellWidth) / 2;
    mYContinue = mHeight - mCellWidth - CONT_WIDTH;
    initTransients();
    setFocusTraversalKeysEnabled(false);
    addKeyListener(mExitHandler);
    // Add a listener that responds to click in the top right of the screen
    // where we will draw the small "x" to quit the game.  The dialog box
    // seems to open in the middle of the screen and be immovable which is
    // just what we want.
    addMouseListener(new GenericScreenMouseAdapter(mXCloseLim, yCloseLim, mXSoundLim, yCloseLim, iconWidth, mExitHandler, getGraphics()));
  }

  private void initTransients() {
    mCenterBlank = Stipple.stipple(0, 0, mMainWidth, mMainHeight).toBufferedImage();
    final int yPentagramOffset = mHeight - 3 * mCellWidth;
    mRightBlank = Stipple.stipple(mXRightOffset, mYRightOffset, mWidth - mXRightOffset, yPentagramOffset - mYRightOffset).toBufferedImage();
    mPhaseBlank = Stipple.stipple(mXPhaseOffset, mYPhaseOffset, mPhaseWidth, 2 * mCellWidth).toBufferedImage();
    mBottomBlank = Stipple.stipple(mXTextOffset, mYTextOffset, mTextWidth, mTextHeight).toBufferedImage();
    mFonts = new ChaosFonts(mCellWidth);
    mExitHandler = new HandleExit(this, mMainWidth, mMainHeight);
    mGraphics = getGraphics();
    if (mGraphics == null && !RuntimeUtils.isTest()) {
      throw new RuntimeException("null graphics");
    }
    mDefaultFont = new JLabel().getFont();
    mPentagramHighlighter = new PentagramHighlighter(mCellWidth, mGraphics, mXOffset + mTextWidth, yPentagramOffset);
  }

  @Override
  public Object lock() {
    return mLock;
  }

  @Override
  public void blank() {
    final BufferedImage im = Stipple.stipple(0, 0, mWidth, mHeight).toBufferedImage();
    // Bug 181: Screen is sometimes starting black.  We definitely get into this
    // block, but the resulting operations are not always drawn!
    Waiter.waitUntilDisplayIsReady();
    mGraphics.drawImage(im, 0, 0, null);
    mGraphics.drawImage(mRightBlank, getXRight(), getYRight(), null);
    mPentagramHighlighter.reset();
    Render.renderImage(mGraphics, EXIT, mXCloseLim, 0);
    Render.renderImage(mGraphics, GenericScreenMouseAdapter.SOUND[Sound.getSoundEngine().getSoundLevel()], mXSoundLim, 0);
    setContinueGadget(false);
  }

  @Override
  public void blankRight() {
    mGraphics.drawImage(mRightBlank, getXRight(), getYRight(), null);
  }

  @Override
  public void blankCenter() {
    mGraphics.drawImage(mCenterBlank, mXOffset, mYOffset, null);
  }

  @Override
  public int getXOffset() {
    return mXOffset;
  }

  @Override
  public int getYOffset() {
    return mYOffset;
  }

  @Override
  public int getXRight() {
    return mXRightOffset;
  }

  @Override
  public int getYRight() {
    return mYRightOffset;
  }

  @Override
  public int getMainWidth() {
    return mMainWidth;
  }

  @Override
  public int getMainHeight() {
    return mMainHeight;
  }

  @Override
  public int getRightWidth() {
    return mRightWidth;
  }

  @Override
  public int getRightHeight() {
    return mHeight - mYOffset - mYRightOffset;
  }

  @Override
  public int getPowerUpXOffset() {
    return mXOffset;
  }

  @Override
  public int getPowerUpYOffset() {
    return mHeight - mYOffset;
  }

  @Override
  public void fillMain(final Color color) {
    synchronized (mGraphics) {
      mGraphics.setColor(color);
      mGraphics.fillRect(getXOffset(), getYOffset(), getMainWidth(), getMainHeight());
    }
  }

  @Override
  public void drawCell(final Image image, final int x, final int y) {
    synchronized (mGraphics) {
      final int xp = getXOffset() + (x << mCellBits);
      final int yp = getYOffset() + (y << mCellBits);
      if (image == null) {
        mGraphics.setColor(Color.BLACK);
        mGraphics.fillRect(xp, yp, mCellWidth, mCellWidth);
      } else {
        mGraphics.drawImage(image, xp, yp, null);
      }
    }
  }

  @Override
  public void writeMessage(final String message) {
    synchronized (mGraphics) {
      mGraphics.setFont(mDefaultFont);
      mGraphics.drawImage(mBottomBlank, mXTextOffset, mYTextOffset, null);
      mGraphics.setColor(Color.GREEN);
      // Add 1 px horizontal offset since "J" seemed to be outside box on some machines
      mGraphics.drawString(message, mXTextOffset + 1, mYTextOffset + mTextHeight - 10);
    }
  }

  @Override
  public void writeMessage(final Caster caster, final Castable castable, final TileManager tm) {
    if (castable != null) {
      synchronized (mGraphics) {
        mGraphics.setFont(mDefaultFont);
        final int w = tm.getWidth();
        mGraphics.drawImage(mBottomBlank, mXTextOffset, mYTextOffset, null);
        final int y = mYTextOffset + 8;
        int x = mXTextOffset;
        if (caster != null) {
          mGraphics.drawImage(tm.getSpellTile(caster), x, y, null);
          x += w;
        }
        mGraphics.drawImage(tm.getSpellTile(castable), x, y, null);
        x += w + 8;
        final String message = NameUtils.getTextName(castable) + " [" + CastMaster.getRange(caster, castable) + ']';
        mGraphics.setColor(Color.GREEN);
        mGraphics.drawString(message, x, mYTextOffset + mTextHeight - 10);
        final FontMetrics fm = mGraphics.getFontMetrics();
        x += fm.stringWidth(message) + 8;
        final int cflags = castable.getCastFlags();
        if ((cflags & Castable.CAST_ANY) != 0 || castable instanceof FreeCastable) {
          mGraphics.setColor(Color.YELLOW);
          mGraphics.drawString("ANY", x, mYTextOffset + mTextHeight - 10);
        } else {
          DrawCastingTiles.drawCastableTiles(caster, tm, mGraphics, w, y, x, cflags);
        }
      }
    }
  }

  private void writePhase(final String text, final FontMetrics fm, final int y) {
    if (text != null) {
      synchronized (mGraphics) {
        final int w = mPhaseWidth - fm.stringWidth(text);
        if (w >= 0) {
          mGraphics.drawString(text, mXPhaseOffset + w / 2, mYPhaseOffset + y * mHeight / 480);
        }
      }
    }
  }

  @Override
  public void writePhase(final String phase, final String player, final BufferedImage image) {
    synchronized (mGraphics) {
      mGraphics.drawImage(mPhaseBlank, mXPhaseOffset, mYPhaseOffset, null);
      if (image != null) {
        mGraphics.drawImage(image, mXPhaseOffset + mPhaseWidth - image.getWidth(null), mYPhaseOffset, null);
      }
      mGraphics.setColor(Color.YELLOW);
      mGraphics.setFont(mFonts.getPhaseFont());
      final FontMetrics fm = mGraphics.getFontMetrics();
      writePhase(phase, fm, 10);
      writePhase(player, fm, 20);
    }
  }

  @Override
  public void writePhase(final String phase, final String player) {
    writePhase(phase, player, null);
  }

  @Override
  public void setCursor(final CursorName name) {
    name.setCursor(this);
  }

  @Override
  public void highlight(final int x, final int y, final int w, final int h) {
    synchronized (mGraphics) {
      HighlightUtils.highlight(mGraphics, x, y, w, h);
    }
  }

  @Override
  public void highlight(final int x, final int y, final int w) {
    highlight(x, y, w, w);
  }

  @Override
  public void lightHighlight(final int x, final int y, final int w) {
    synchronized (mGraphics) {
      HighlightUtils.lightHighlight(mGraphics, x, y, w);
    }
  }

  @Override
  public void highlight(final Realm realm) {
    synchronized (mGraphics) {
      mPentagramHighlighter.highlight(realm, Color.ORANGE, 0);
    }
  }

  @Override
  public void highlight(final Realm[] realm) {
    synchronized (mGraphics) {
      mPentagramHighlighter.highlight(realm);
    }
  }

  /** Current activation state of continue gadget. */
  private boolean mContinueGadgetState;

  /** Actually render the continue gadget in its current state. */
  private void drawContinue() {
    Render.renderImage(mGraphics, mContinueGadgetState ? CONT : BLANK, mXContinue, mYContinue);
  }

  @Override
  public void setContinueGadget(final boolean state) {
    mContinueGadgetState = state;
    drawContinue();
  }

  @Override
  public boolean isPositionInContinueGadget(final int x, final int y) {
    return x >= mXContinue && y >= mYContinue && x < mXContinue + CONT_WIDTH && y < mYContinue + CONT_WIDTH;
  }

  @Override
  public void highlightContinue(final boolean state) {
    if (state) {
      highlight(mXContinue, mYContinue, CONT_WIDTH);
    } else {
      drawContinue();
    }
  }

  @Override
  public Font getTitleFont() {
    return mFonts.getTitleFont();
  }

  @Override
  public Font getTextFont() {
    return mFonts.getTextFont();
  }

  @Override
  public Font getMonospaceFont() {
    return mFonts.getMonospaceFont();
  }

  @Override
  public Font getPhaseFont() {
    return mFonts.getPhaseFont();
  }

  @Override
  public int getXPhaseOffset() {
    return mXPhaseOffset;
  }

  @Override
  public int getPhaseWidth() {
    return mPhaseWidth;
  }

  @Override
  public Shape clipToArena(final Graphics graphics) {
    final Shape oldClip = graphics.getClip();
    graphics.setClip(getXOffset(), getYOffset(), getMainWidth(), getMainHeight());
    return oldClip;
  }
}
