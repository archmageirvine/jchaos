package chaos.graphics;

import chaos.util.Sleep;

import java.awt.Graphics;

/**
 * Plotting based on a list of images
 * @author Sean A. Irvine
 */
public class ImageListPlotter implements Plotter {

  private final ImageList mImages;
  private final Graphics mGraphics;
  private final int mSleep;
  private final int mShift;
  private int mIndex = 0;
  private int mOldX = -1;
  private int mOldY = -1;

  ImageListPlotter(final ImageList images, final Graphics graphics, final int sleep) {
    mImages = images;
    mGraphics = graphics;
    mSleep = sleep;
    mShift = mImages.get(0).getHeight() / 2;
  }

  @Override
  public void plot(final int x, final int y) {
    if (mImages.getMask() != null && mOldX != -1) {
      mGraphics.drawImage(mImages.getMask(), mOldX - mShift, mOldY - mShift, null);
    }
    mGraphics.drawImage(mImages.get(mIndex), x - mShift, y - mShift, null);
    ++mIndex;
    mIndex %= mImages.size();
    mOldX = x;
    mOldY = y;
    Sleep.sleep(mSleep);
  }
}
