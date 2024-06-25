package chaos.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Realm;

/**
 * Interface implemented by all Chaos screens.
 * @author Sean A. Irvine
 */
public interface ChaosScreen {

  /**
   * Perform the screen's notion of blanking.  Note this need not actually
   * corresponding to an empty display.  It can contain arbitrary decoration
   * of the screen, especially in portions not be used for drawing in the
   * game.  This method should attempt to actively render and decorations
   * it requires.
   */
  void blank();

  /**
   * Perform the screen's notion of blanking the right area of the screen.
   */
  void blankRight();

  /**
   * Perform the screen's notion of blanking the center area of the screen.
   */
  void blankCenter();

  /**
   * Get the x-offset to the main drawing area.
   * @return x-offset
   */
  int getXOffset();

  /**
   * Get the y-offset to the main drawing area.
   * @return y-offset
   */
  int getYOffset();

  /**
   * Get the width of the main playing area in pixels.
   * @return width of main playing area
   */
  int getMainWidth();

  /**
   * Get the height of the main playing area in pixels.
   * @return height of main playing area
   */
  int getMainHeight();

  /**
   * Get the x-offset to free space at the right of the screen.
   * @return x-offset
   */
  int getXRight();

  /**
   * Get the y-offset to free space at the right of the screen.
   * @return y-offset
   */
  int getYRight();

  /**
   * Get the width of the right area in pixels.
   * @return width of right area
   */
  int getRightWidth();

  /**
   * Get the height of the right area in pixels.
   * @return height of right area
   */
  int getRightHeight();

  /**
   * Get the horizontal offset of the region where power-ups can be displayed.
   * @return horizontal offset of power-ups
   */
  int getPowerUpXOffset();

  /**
   * Get the vertical offset of the region where power-ups can be displayed.
   * @return vertical offset of power-ups
   */
  int getPowerUpYOffset();

  /**
   * Sets the state of the continue gadget.  Note this does not activate
   * any listener for the gadget.  It is initially false.
   * @param state true to activate continue
   */
  void setContinueGadget(boolean state);

  /**
   * Returns true if the coordinates specify a screen position that overlaps
   * with the continue gadget.
   * @param x x-coordinate
   * @param y y-coordinate
   * @return true if over continue
   */
  boolean isPositionInContinueGadget(int x, int y);

  /**
   * Write a message into the primary text area.
   * @param message message to write
   */
  void writeMessage(String message);

  /**
   * Write spell information into the primary text area. If <code>castable</code>
   * is null, then no action is taken.
   * @param caster caster (may be null)
   * @param castable castable
   * @param tm tile manager
   */
  void writeMessage(Caster caster, Castable castable, TileManager tm);

  /**
   * Write a short piece of text describing what the main activity currently
   * is.  If either string is null then nothing is displayed for that field.
   * This is intended to display short strings only, long string may silently
   * be discarded. If the supplied image is not null, then it is also drawn
   * in the phase part of the display.
   * @param phase phase string
   * @param player player name
   * @param image image to draw
   */
  void writePhase(String phase, String player, BufferedImage image);

  /**
   * Write a short piece of text describing what the main activity currently
   * is.  If either string is null then nothing is displayed for that field.
   * This is intended to display short strings only, long string may silently
   * be discarded.
   * @param phase phase string
   * @param player player name
   */
  void writePhase(String phase, String player);

  /**
   * Fill the main play area in the specified color.
   * @param color color to fill with
   */
  void fillMain(Color color);

  /**
   * Draw the given image into the specified cell of the main playing area.
   * Should handle the case of a null image.
   * @param image image to draw
   * @param x cell x-coordinate
   * @param y cell y-coordinate
   */
  void drawCell(Image image, int x, int y);

  /**
   * Highlight a specified rectangle.  The highlighting can only be removed
   * by redrawing the affected area.
   * @param x x-coordinate in pixels
   * @param y y-coordinate in pixels
   * @param w width in pixels
   * @param h height in pixels
   */
  void highlight(int x, int y, int w, int h);

  /**
   * Highlight a specified square.  The highlighting can only be removed
   * by redrawing the affected area.
   * @param x x-coordinate in pixels
   * @param y y-coordinate in pixels
   * @param w width in pixels (also used for the height)
   */
  void highlight(int x, int y, int w);

  /**
   * Highlight a specified realm.  Not all clients need support this as it
   * is for purely aesthetic reasons.  If the supplied realm is null then
   * all currently highlighted realms are unhighlighted.
   * @param realm realm to highlight.
   */
  void highlight(Realm realm);

  /**
   * Highlight specified realms.  Not all clients need support this as it
   * is for purely aesthetic reasons.  If the supplied realm is null or
   * of zero length then all currently highlighted realms are unhighlighted.
   * The highlighting of the first element is deemed to be more important
   * than the highlighting of the second, and so on.
   * @param realms realms to highlight.
   */
  void highlight(Realm[] realms);

  /**
   * Lightly highlight a specified square.  The highlighting can
   * only be removed by redrawing the affected area.
   * @param x x-coordinate in pixels
   * @param y y-coordinate in pixels
   * @param w width in pixels (also used for the height)
   */
  void lightHighlight(int x, int y, int w);

  /**
   * Highlight status of the continue gadget.
   * @param state whether or not the continue gadget is highlighted
   */
  void highlightContinue(boolean state);

  /**
   * Set the cursor for the screen.
   * @param name name of cursor
   */
  void setCursor(CursorName name);

  /**
   * Get the drawing surface for this screen.
   * @return graphics
   */
  Graphics getGraphics();

  /**
   * Add the specified mouse listener.
   * @param mouseListener mouse listener to add
   */
  void addMouseListener(MouseListener mouseListener);

  /**
   * Add the specified mouse motion listener.
   * @param mouseMotionListener mouse motion listener to add
   */
  void addMouseMotionListener(MouseMotionListener mouseMotionListener);

  /**
   * Add the specified key listener.
   * @param keyListener key listener to add
   */
  void addKeyListener(KeyListener keyListener);

  /**
   * Remove the specified mouse listener.
   * @param mouseListener mouse listener to remove
   */
  void removeMouseListener(MouseListener mouseListener);

  /**
   * Remove the specified mouse motion listener.
   * @param mouseMotionListener mouse motion listener to remove
   */
  void removeMouseMotionListener(MouseMotionListener mouseMotionListener);

  /**
   * Remove the specified key listener.
   * @param keyListener key listener to remove
   */
  void removeKeyListener(KeyListener keyListener);

  /**
   * Return an object that can be synchronized against to lock access to this
   * screen.
   * @return lock object
   */
  Object lock();

  /**
   * Perform any tidy up need before exiting. Behaviour of the screen is
   * undefined after this method is called.
   */
  void close();

  /**
   * Get a font suitable for rendering titles on this screen.
   * @return title font
   */
  Font getTitleFont();

  /**
   * Get a font suitable for rendering text on this screen.
   * @return text font
   */
  Font getTextFont();

  /**
   * Get a font suitable for rendering monospace text on this screen.
   * @return monospace text font
   */
  Font getMonospaceFont();

  /**
   * Get a font suitable for rendering phase information on this screen.
   * @return phase font
   */
  Font getPhaseFont();

  /**
   * Pixel offset to the phase area of the screen.
   * @return pixel offset
   */
  int getXPhaseOffset();

  /**
   * Pixel width of the phase area of the screen.
   * @return pixel width
   */
  int getPhaseWidth();

  /**
   * Clip a given graphics to the main area portion of the screen, returning the
   * previous clipping region
   * @param graphics the graphics
   * @return original clipping bounds (so they can be restored later)
   */
  Shape clipToArena(final Graphics graphics);
}
