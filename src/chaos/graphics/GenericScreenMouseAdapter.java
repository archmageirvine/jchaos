package chaos.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import chaos.sound.Sound;
import chaos.util.ChaosProperties;

/**
 * Mouse adapter for generic screen. Handles the close button and the
 * sound level icons.
 * @author Sean A. Irvine
 */
class GenericScreenMouseAdapter extends MouseAdapter {

  static final Image[] SOUND = {
    ImageLoader.getImage("chaos/resources/icons/sound0.png"),
    ImageLoader.getImage("chaos/resources/icons/sound1.png"),
    ImageLoader.getImage("chaos/resources/icons/sound2.png"),
  };

  private int mXCloseLim;
  private int mYCloseLim;
  private int mXSoundLim;
  private int mYSoundLim;
  private int mIconWidth;
  private HandleExit mExitHandler;
  private Graphics mGraphics;

  GenericScreenMouseAdapter(final int xCloseLim, final int yCloseLim, final int xSoundLim, final int ySoundLim, final int iconWidth, final HandleExit exitHandler, final Graphics graphics) {
    mYCloseLim = yCloseLim;
    mXCloseLim = xCloseLim;
    mXSoundLim = xSoundLim;
    mYSoundLim = ySoundLim;
    mIconWidth = iconWidth;
    mGraphics = graphics;
    mExitHandler = exitHandler;
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      if (e.getY() < mYCloseLim && e.getX() > mXCloseLim) {
        Sound.getSoundEngine().playwait("chaos/resources/sound/misc/Beep", Sound.SOUND_NONE);
        e.consume();
        // Run this in a new thread so it doesn't hold up event queue
        mExitHandler.quitDialog(mGraphics);
      } else if (e.getY() < mYSoundLim && e.getX() > mXSoundLim && e.getX() < mXSoundLim + mIconWidth) {
        Sound.getSoundEngine().playwait("chaos/resources/sound/misc/Beep", Sound.SOUND_NONE);
        e.consume();
        final int sl = (Sound.getSoundEngine().getSoundLevel() + 1) % SOUND.length;
        Sound.getSoundEngine().setSoundLevel(sl);
        ChaosProperties.properties().setProperty(ChaosProperties.SOUND_PROPERTY, String.valueOf(sl));
        ChaosProperties.properties().save();
        if (mGraphics != null) {
          Render.renderImage(mGraphics, SOUND[sl], mXSoundLim, 0);
        }
      }
    }
  }
}
