package chaos.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.sound.Sound;
import chaos.sound.SoundSelection;
import chaos.util.BooleanLock;
import chaos.util.Sleep;

/**
 * Wizard explosion effect.
 * @author Sean A. Irvine
 */
public class WizardExplodeEffect extends AbstractEffect {

  private final World mWorld;
  private final Animator mAnimator;
  private final TileManager mTM;

  WizardExplodeEffect(final World world, final Animator animator, final TileManager tm) {
    mWorld = world;
    mAnimator = animator;
    mTM = tm;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      final Sound sound = Sound.getSoundEngine();
      final int[] pxy = new int[2];
      final int widthBits = mTM.getWidthBits();
      for (final Cell c : cells) {
        final int cellNumber = c.getCellNumber();
        final Actor a = mWorld.actor(cellNumber);
        if (a != null) {
          final BufferedImage wi = mTM.getSpellTile(a);
          final BooleanLock status = sound.play(SoundSelection.getDeathSound(a), Sound.SOUND_INTELLIGENT);
          synchronized (screen.lock()) {
            mWorld.getCellCoordinates(cellNumber, pxy);
            final int px = (pxy[0] << widthBits) + screen.getXOffset();
            final int py = (pxy[1] << widthBits) + screen.getYOffset();
            final int imw = wi.getWidth();
            final int rlimit = screen.getXOffset() + mWorld.width() * width - imw;
            final int blimit = screen.getYOffset() + mWorld.height() * width - imw;
            final int delta = imw / 2;
            boolean ok;
            int xl = px;
            int xr = xl;
            int yu = py;
            int yd = yu;
            do {
              ok = false;
              xl -= delta;
              yu -= delta;
              xr += delta;
              yd += delta;
              if (xl >= screen.getXOffset()) {
                ok = true;
                graphics.drawImage(wi, xl, py, null);
              }
              if (xr <= rlimit) {
                ok = true;
                graphics.drawImage(wi, xr, py, null);
              }
              if (yu >= screen.getYOffset()) {
                ok = true;
                graphics.drawImage(wi, px, yu, null);
              }
              if (yd <= blimit) {
                ok = true;
                graphics.drawImage(wi, px, yd, null);
              }
              Animator.sync();
              Sleep.sleep(15);
            } while (ok);
            // Redraw damaged cells.
            final int[] xy = new int[2];
            mWorld.getCellCoordinates(cellNumber, xy);
            xl = xy[0];
            xr = xl;
            yu = xy[1];
            yd = yu;
            final int ww = mWorld.width();
            final int hh = mWorld.height();
            do {
              ok = false;
              if (--xl >= 0) {
                ok = true;
                mAnimator.drawCell(xl, xy[1]);
              }
              if (++xr < ww) {
                ok = true;
                mAnimator.drawCell(xr, xy[1]);
              }
              if (--yu >= 0) {
                ok = true;
                mAnimator.drawCell(xy[0], yu);
              }
              if (++yd < hh) {
                ok = true;
                mAnimator.drawCell(xy[0], yd);
              }
            } while (ok);
          }
          sound.wait(status, 10000);
        }
      }
    }
  }
}
