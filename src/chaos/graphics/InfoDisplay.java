package chaos.graphics;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import chaos.Chaos;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Castable;
import chaos.common.Conveyance;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.spell.Quickshot;
import chaos.common.wizard.Wizard;
import chaos.util.NameUtils;
import irvine.heraldry.ShapeFactory;
import irvine.util.graphics.Stipple;

/**
 * Draw the information about an actor.
 * @author Sean A. Irvine
 */
public class InfoDisplay {

  private static final Font PHASE_FONT = new Font("SansSerif", Font.PLAIN, 9);

  /** Tiny images for statistics and applies. */
  private static final BufferedImage LIFE_IMAGE = ImageLoader.getImage("chaos/resources/icons/life.png");
  private static final BufferedImage MR_IMAGE = ImageLoader.getImage("chaos/resources/icons/magicalresistance.png");
  private static final BufferedImage INTEL_IMAGE = ImageLoader.getImage("chaos/resources/icons/intelligence.png");
  private static final BufferedImage AGILITY_IMAGE = ImageLoader.getImage("chaos/resources/icons/agility.png");
  private static final BufferedImage COMBAT_IMAGE = ImageLoader.getImage("chaos/resources/icons/combat.png");
  private static final BufferedImage SCOMBAT_IMAGE = ImageLoader.getImage("chaos/resources/icons/specialcombat.png");
  private static final BufferedImage RCOMBAT_IMAGE = ImageLoader.getImage("chaos/resources/icons/rangedcombat.png");
  private static final BufferedImage RANGE_IMAGE = ImageLoader.getImage("chaos/resources/icons/range.png");
  private static final BufferedImage MOVE_IMAGE = ImageLoader.getImage("chaos/resources/icons/movement.png");
  private static final BufferedImage RECOVER_IMAGE = ImageLoader.getImage("chaos/resources/icons/recovery.png");
  private static final BufferedImage MOUNT_IMAGE = ImageLoader.getImage("chaos/resources/icons/mount.png");
  private static final BufferedImage BONUS_IMAGE = ImageLoader.getImage("chaos/resources/icons/bonus.png");
  private static final BufferedImage PROMOTE_IMAGE = ImageLoader.getImage("chaos/resources/icons/promote.png");

  /**
   * Draw a statistic and its recovery rate.
   */
  private void boxPlot(final Graphics g, final int x, final int y, final int def, final int v, final int dr, final int recovery) {
    // draw main band
    final int scale;
    final int height;
    final int add;
    if (mWidth < 32) {
      scale = 2;
      height = 3;
      add = 1;
    } else {
      scale = 1;
      height = 6;
      add = 0;
    }
    g.setColor(def < 0 ? Color.CYAN : Color.BLUE);
    g.fillRect(x, y, (abs(def) + add) / scale, height);
    g.setColor(v < 0 ? Color.RED : Color.GREEN);
    g.fillRect(x, y, (abs(v) + add) / scale, height);
    // draw recovery
    g.setColor(dr < 0 ? Color.CYAN : Color.BLUE);
    g.fillRect(x, y + 8 / scale, (abs(dr) + add) / scale, height);
    g.setColor(recovery < 0 ? Color.RED : Color.GREEN);
    g.fillRect(x, y + 8 / scale, (abs(recovery) + add) / scale, height);
  }

  /**
   * Draw an integer statistic as boxes.
   */
  private void squarePlot(final Graphics g, final int x, final int y, final int def, final int v, final int dr, final int recovery) {
    final int scale = mWidth < 32 ? 2 : 1;
    final int height = mWidth < 32 ? 3 : 6;
    // draw main band
    g.setColor(def < 0 ? Color.CYAN : Color.BLUE);
    // Constant 20 below prevents box plotting exceeding available space. It is
    // less than ideal to have this, but it does save border corruption.
    final int limV = min(20, abs(v));
    final int limD = min(20, abs(def));
    final int lim = max(limV, limD);
    // need smaller step for very large moves
    final int step = (lim >= 16 ? 6 : (lim >= 13 ? 8 : 10)) / scale;
    final int ww = step == 3 ? 2 : (6 / scale);
    for (int i = 0, xx = x; i < lim; ++i, xx += step) {
      g.fillRect(xx, y, ww, height);
    }
    g.setColor(v < 0 ? Color.RED : Color.GREEN);
    for (int i = 0, xx = x; i < limV; ++i, xx += step) {
      g.fillRect(xx, y, ww, height);
    }
    // draw recovery
    g.setColor(dr < 0 ? Color.CYAN : Color.BLUE);
    for (int i = 0, xx = x; i < abs(dr); ++i, xx += step) {
      g.fillRect(xx, y + 8 / scale, ww, height);
    }
    g.setColor(recovery < 0 ? Color.RED : Color.GREEN);
    for (int i = 0, xx = x; i < abs(recovery); ++i, xx += step) {
      g.fillRect(xx, y + 8 / scale, ww, height);
    }
  }

  /**
   * Blanking for very bottom of the screen.
   */
  private final BufferedImage mBlank;
  private final BufferedImage mQuickshotImage;
  private final int mWidth;
  private final int mPanelHeight;
  private final Team mTeamInformation;
  private final World mWorld;
  private final TileManager mTileManager;

  private final ChaosScreen mScreen;
  private final Graphics mGraphics;
  private final BufferedImage mGrayPane;

  /**
   * Construct a new information display.
   *
   * @param screen the screen
   * @param graphics where to draw
   * @param tileManager tile manager
   * @param world chaos world
   */
  public InfoDisplay(final ChaosScreen screen, final Graphics graphics, final TileManager tileManager, final World world) {
    mScreen = screen;
    mGraphics = graphics;
    mTileManager = tileManager;
    mQuickshotImage = tileManager.getSpellTile(new Quickshot());
    mWidth = tileManager.getWidth();
    mBlank = Stipple.stipple(0, 0, mScreen.getMainWidth(), tileManager.getWidth()).toBufferedImage();
    mPanelHeight = 120 * mWidth / 16;
    mGrayPane = Stipple.stipple(mScreen.getXRight(), mScreen.getYRight(), mScreen.getRightWidth(), mPanelHeight, 0xFF505050, 0xFF484848).toBufferedImage();
    mWorld = world;
    mTeamInformation = world.getTeamInformation();
  }

  private BufferedImage applyImage(final Attribute apply) {
    switch (apply) {
      case LIFE:
        return LIFE_IMAGE;
      case LIFE_RECOVERY:
        return RECOVER_IMAGE;
      case MAGICAL_RESISTANCE:
      case MAGICAL_RESISTANCE_RECOVERY:
        return MR_IMAGE;
      case INTELLIGENCE:
      case INTELLIGENCE_RECOVERY:
        return INTEL_IMAGE;
      case AGILITY:
        return AGILITY_IMAGE;
      case MOVEMENT:
        return MOVE_IMAGE;
      case RANGE:
        return RANGE_IMAGE;
      case COMBAT:
        return COMBAT_IMAGE;
      case SPECIAL_COMBAT:
        return SCOMBAT_IMAGE;
      case RANGED_COMBAT:
        return RCOMBAT_IMAGE;
      default:
        return null;
    }
  }

  private void drawApply(final Graphics g, final Attribute apply, final int y) {
    final BufferedImage im = applyImage(apply);
    if (im != null) {
      final int x = mScreen.getXRight() + mScreen.getRightWidth() - 10;
      g.drawImage(im, x, y, null);
    }
  }

  private void drawPowerUp(final Graphics g, final BufferedImage image, final int x, final int y, final int count) {
    g.drawImage(image, x, y, null);
    if (count > 1) {
      for (int k = 0, j = x; k < min(count, mWidth >>> 1); ++k, j += 2) {
        g.drawLine(j, y + mWidth, j, y + mWidth - 2);
      }
    }
  }

  private int renderPowerUps(final Graphics g, final Actor actor, int x, final int pwy) {
    g.setColor(Color.RED);
    final TileManager tm = Chaos.getChaos().getTileManager();
    for (final PowerUps p : PowerUps.values()) {
      final int puCount = actor.get(p);
      if (puCount != 0) {
        final Castable c = p.getCastable();
        if (c != null) {
          drawPowerUp(g, tm.getSpellTile(c), x, pwy, puCount);
          x += mWidth;
        }
      }
    }
    return x;
  }

  private String getMountKillCount(final Actor actor) {
    if (actor instanceof Conveyance) {
      final Actor mount = ((Conveyance) actor).getMount();
      if (mount != null && mount.getKillCount() > 0) {
        return "/" + mount.getKillCount();
      }
    }
    return "";
  }

  /**
   * Show the information panel for the specified actor. If the given actor
   * is null then the panel is cleared.
   *
   * @param actor actor to display
   * @param panel 0 or 1 to select upper or lower panel
   * @throws IllegalArgumentException if panel number is invalid
   */
  public void showInfo(final Actor actor, final int panel) {
    if (panel != 0 && panel != 1) {
      throw new IllegalArgumentException();
    }
    final int xr = mScreen.getXRight(); // x-offset to the panel
    int yr = mScreen.getYRight(); // y-offset to panel, adjust for panel number
    if (panel == 1) {
      yr += mPanelHeight + 20 * mWidth / 16;
    }
    final int width = mScreen.getRightWidth();
    mScreen.highlight((Realm) null);
    mGraphics.drawImage(mGrayPane, xr, yr, null);
    mGraphics.drawImage(mBlank, mScreen.getPowerUpXOffset(), mScreen.getPowerUpYOffset(), null);
    if (actor != null) {
      mScreen.highlight(actor.getRealm());
      mGraphics.setColor(actor.getState() == State.DEAD ? Color.RED : Color.YELLOW);
      mGraphics.setFont(PHASE_FONT);
      final FontMetrics fm = mGraphics.getFontMetrics();
      final String name = NameUtils.getTextName(actor);
      final int w = width - (fm == null ? 0 : fm.stringWidth(name));
      if (w >= 0) {
        mGraphics.drawString(name, xr + w / 2, yr + 10);
      }
      yr += 15;
      final int dy = 10 * mWidth / 16;
      mGraphics.drawImage(LIFE_IMAGE, xr + 1, yr, null);
      int specialX = xr + width;
      if (actor instanceof Conveyance && ((Conveyance) actor).getMount() != null) {
        specialX -= MOUNT_IMAGE.getWidth();
        mGraphics.drawImage(MOUNT_IMAGE, specialX, yr, null);
      }
      if (actor instanceof Bonus) {
        specialX -= BONUS_IMAGE.getWidth();
        mGraphics.drawImage(BONUS_IMAGE, specialX, yr, null);
      }
      if (actor instanceof Promotion) {
        specialX -= PROMOTE_IMAGE.getWidth();
        mGraphics.drawImage(PROMOTE_IMAGE, specialX, yr, null);
      }
      boxPlot(mGraphics, xr + 10, yr, actor.getDefault(Attribute.LIFE), actor.get(Attribute.LIFE), actor.getDefault(Attribute.LIFE_RECOVERY), actor.get(Attribute.LIFE_RECOVERY));
      yr += dy;
      mGraphics.drawImage(MR_IMAGE, xr + 1, yr, null);
      boxPlot(mGraphics, xr + 10, yr, actor.getDefault(Attribute.MAGICAL_RESISTANCE), actor.get(Attribute.MAGICAL_RESISTANCE), actor.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY), actor.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
      final int pwy = mScreen.getPowerUpYOffset();
      int x = mScreen.getPowerUpXOffset();
      if (actor instanceof Monster) {
        final Monster m = (Monster) actor;
        yr += dy;
        mGraphics.drawImage(INTEL_IMAGE, xr + 1, yr, null);
        boxPlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.INTELLIGENCE), m.get(Attribute.INTELLIGENCE), m.getDefault(Attribute.INTELLIGENCE_RECOVERY), m.get(Attribute.INTELLIGENCE_RECOVERY));
        yr += dy;
        mGraphics.drawImage(AGILITY_IMAGE, xr + 1, yr, null);
        boxPlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.AGILITY), m.get(Attribute.AGILITY), m.getDefault(Attribute.AGILITY_RECOVERY), m.get(Attribute.AGILITY_RECOVERY));
        yr += dy;
        mGraphics.drawImage(COMBAT_IMAGE, xr + 1, yr, null);
        final int combat = m.get(Attribute.COMBAT);
        boxPlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.COMBAT), combat, m.getDefault(Attribute.COMBAT_RECOVERY), m.get(Attribute.COMBAT_RECOVERY));
        if (combat != 0) {
          drawApply(mGraphics, m.getCombatApply(), yr);
        }
        yr += dy;
        mGraphics.drawImage(SCOMBAT_IMAGE, xr + 1, yr, null);
        final int scombat = m.get(Attribute.SPECIAL_COMBAT);
        boxPlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.SPECIAL_COMBAT), scombat, m.getDefault(Attribute.SPECIAL_COMBAT_RECOVERY), m.get(Attribute.SPECIAL_COMBAT_RECOVERY));
        if (scombat != 0) {
          drawApply(mGraphics, m.getSpecialCombatApply(), yr);
        }
        yr += dy;
        mGraphics.drawImage(RCOMBAT_IMAGE, xr + 1, yr, null);
        final int rcombat = m.get(Attribute.RANGED_COMBAT);
        boxPlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.RANGED_COMBAT), rcombat, m.getDefault(Attribute.RANGED_COMBAT_RECOVERY), m.get(Attribute.RANGED_COMBAT_RECOVERY));
        if (rcombat != 0) {
          drawApply(mGraphics, m.getRangedCombatApply(), yr);
        }
        yr += dy;
        mGraphics.drawImage(RANGE_IMAGE, xr + 1, yr, null);
        squarePlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.RANGE), m.get(Attribute.RANGE), m.getDefault(Attribute.RANGE_RECOVERY), m.get(Attribute.RANGE_RECOVERY));
        yr += dy;
        mGraphics.drawImage(MOVE_IMAGE, xr + 1, yr, null);
        squarePlot(mGraphics, xr + 10, yr, m.getDefault(Attribute.MOVEMENT), m.get(Attribute.MOVEMENT), m.getDefault(Attribute.MOVEMENT_RECOVERY), m.get(Attribute.MOVEMENT_RECOVERY));
        if (m.get(Attribute.SHOTS) > 1) {
          mGraphics.drawImage(mQuickshotImage, x, pwy, null);
          x += mWidth;
        }
      }
      if (actor.getState() == State.ACTIVE) {
        x = renderPowerUps(mGraphics, actor, x, pwy);
      }
      if (actor instanceof Conveyance) {
        final Actor u = ((Conveyance) actor).getMount();
        if (u instanceof Wizard) {
          mGraphics.drawImage(mTileManager.getSpellTile(u), x, pwy, null);
          x += mWidth;
          renderPowerUps(mGraphics, u, x, pwy);
        }
      }
      yr = mScreen.getYRight() + mPanelHeight - 3 * mWidth / 16;
      if (panel == 1) {
        yr += mPanelHeight + 20 * mWidth / 16;
      }
      final String wname;
      switch (actor.getState()) {
        case ACTIVE:
          final Wizard ww = mWorld.getWizardManager().getWizard(actor.getOwner());
          wname = ww == null ? "" : ww.getPersonalName();
          if (!"Independent".equals(wname)) {
            ShapeFactory.createShape(mTeamInformation.heraldicKey(mTeamInformation.getTeam(actor))).render(mGraphics, 12, xr + mScreen.getRightWidth() - 14, yr - 15);
          }
          break;
        case ASLEEP:
          wname = "ASLEEP";
          break;
        case DEAD:
          wname = "DEAD";
          break;
        default:
          wname = "";
          break;
      }
      mGraphics.setColor(Color.YELLOW);
      final int ww = width - (fm == null ? 0 : fm.stringWidth(wname));
      if (ww >= 0) {
        mGraphics.drawString(wname, xr + ww / 2, yr);
      } else {
        final int space = wname.indexOf(' ');
        if (space >= 0) {
          final String line1 = wname.substring(0, space);
          final String line2 = wname.substring(space + 1);
          final int ww1 = width - (fm == null ? 0 : fm.stringWidth(line1));
          if (ww1 >= 0) {
            mGraphics.drawString(line1, xr + ww1 / 2, yr - 8);
          }
          final int ww2 = width - (fm == null ? 0 : fm.stringWidth(line2));
          if (ww2 >= 0) {
            mGraphics.drawString(line2, xr + ww2 / 2, yr + 1);
          }
        }
      }
      final int killCount = actor.getKillCount();
      final String mountKillCount = getMountKillCount(actor);
      if (killCount > 0 || !mountKillCount.isEmpty()) {
        mGraphics.setColor(Color.RED);
        mGraphics.drawString(String.valueOf(killCount) + mountKillCount, xr + 5, yr);
      }
    }
  }

}
