package chaos.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import chaos.Chaos;
import chaos.Configuration;
import chaos.board.Team;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.inanimate.Generator;
import chaos.common.wizard.Wizard;
import chaos.engine.HumanEngine;
import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;
import irvine.heraldry.Shape;
import irvine.heraldry.ShapeFactory;
import irvine.util.graphics.Stipple;

/**
 * Draw the current scores.
 *
 * @author Sean A. Irvine
 */
public class GenericScoreDisplay implements ScoreDisplay, MouseListener, KeyListener {

  private static final String LIFE_LABEL = "Life";

  private static final String TEAM_LABEL = "Team";

  private static final String SCORE_LABEL = "Score";

  private static final int NO_HUMAN_SLEEP = ChaosProperties.properties().getIntProperty("chaos.scoredisplay.nohuman.delay", 5000);

  private static final String TITLE = "Current Standings";
  /** Provides model locking. */
  private final BooleanLock mLock = new BooleanLock();

  private final World mWorld;
  private final ChaosScreen mScreen;
  private final Wizard[] mWizards;
  /** The order of the wizards on the previous round. */
  private Wizard[] mPreviousWizardOrder = null;
  private final Wizard mIndependent;
  private final Configuration mConfig;
  private final Image mDragonBackdrop;
  private final TexturePaint mTexture;

  /**
   * Construct a new score display.
   *
   * @param config game configuration
   * @param w the world
   * @param screen the screen
   */
  public GenericScoreDisplay(final Configuration config, final World w, final ChaosScreen screen) {
    if (config == null) {
      throw new NullPointerException();
    }
    mConfig = config;
    mWorld = w;
    mScreen = screen;
    // copy independents into wizard array
    final WizardManager wm = w.getWizardManager();
    final Wizard[] wizards = wm.getWizards();
    mWizards = new Wizard[wizards.length + 1];
    mIndependent = wm.getIndependent();
    System.arraycopy(wizards, 0, mWizards, 0, wizards.length);
    mWizards[wizards.length] = mIndependent;
    final Image dragonImage = ImageLoader.getImage("chaos/resources/backdrops/dragon.png");
    mDragonBackdrop = dragonImage.getScaledInstance(mScreen.getMainWidth(), mScreen.getMainHeight(), Image.SCALE_SMOOTH);
    final BufferedImage bi = Stipple.stipple(0, 0, 16, 16, 0xFF800000, 0xFF500000).toBufferedImage();
    final Rectangle r = new Rectangle(0, 0, bi.getWidth(), bi.getHeight());
    mTexture = new TexturePaint(bi, r);
  }

  private static final Comparator<Wizard> SCORE_COMPARATOR = (a, b) -> {
    if (a == b) {
      // this should only be true if both are null
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    if (a.getScore() == b.getScore()) {
      return b.getMass() - a.getMass();
    }
    return b.getScore() - a.getScore();
  };

  private void getMass() {
    for (final Wizard w : mWizards) {
      if (w != null) {
        w.setMass(0);
      }
    }
    for (int c = 0; c < mWorld.size(); ++c) {
      final Actor a = mWorld.actor(c);
      if (a != null && a.getState() == State.ACTIVE) {
        final Wizard w = mWorld.getWizardManager().getWizard(a);
        if (w != null) {
          w.setMass(w.getMass() + a.get(Attribute.LIFE));
        }
      }
    }
  }

  private HashMap<Integer, Integer> getTeamScores() {
    final HashMap<Integer, Integer> teamScore = new HashMap<>();
    final Team teamInformation = mWorld.getTeamInformation();
    for (final Wizard w : mWizards) {
      if (w != null) {
        final int team = teamInformation.getTeam(w);
        final int score = w.getScore();
        final Integer current = teamScore.get(team);
        teamScore.put(team, score + (current == null ? 0 : current));
      }
    }
    return teamScore;
  }

  /** Check for at least one human player. */
  private boolean isThereAHuman() {
    for (final Wizard w : mWizards) {
      if (w != null && w.getState() == State.ACTIVE && w.getPlayerEngine() instanceof HumanEngine) {
        return true;
      }
    }
    return false;
  }

  /** Image to use for independents on score. */
  private static final Image INDEPENDENT_IMAGE = Chaos.getChaos().getTileManager().getTile(new Generator(), 0, 0, 0);

  private int stringWidth(final Graphics g, final String s) {
    final FontMetrics fm = g.getFontMetrics();
    return fm == null ? 0 : fm.stringWidth(s);
  }

  @Override
  public void showScores(final int turn) {
    // sort based on score, then mass
    getMass();
    Arrays.sort(mWizards, SCORE_COMPARATOR);
    final Team team = mWorld.getTeamInformation();
    final HashMap<Integer, Integer> teamScores = getTeamScores();

    synchronized (mScreen.lock()) {
      final Graphics g = mScreen.getGraphics();
      if (g != null) {
        final int cw = mConfig.getCellWidth();
        final int pw = mConfig.getPixelWidth();
        final int ph = mConfig.getPixelHeight();
        final int cwh = cw / 2;
        final int x0 = mScreen.getXOffset() + 3 * cw;
        final int x1 = x0 + 2 * cw;
        g.drawImage(mDragonBackdrop, mScreen.getXOffset(), mScreen.getYOffset(), null);
        g.setFont(mScreen.getTitleFont());
        if (g instanceof Graphics2D) {
          final Graphics2D g2 = (Graphics2D) g;
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setPaint(mTexture);
        }
        final FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString(TITLE, mScreen.getXOffset() + cw, mScreen.getYOffset() + (fontMetrics == null ? cw : fontMetrics.getAscent()));
        g.setFont(mScreen.getTextFont());
        g.setColor(Color.RED);
        final int massX = mScreen.getXOffset() + mScreen.getMainWidth() - cw;
        final String turns = "Turn number: " + turn;
        g.drawString(turns, massX - stringWidth(g, turns), mScreen.getYOffset() + (fontMetrics == null ? cw : fontMetrics.getAscent()));
        g.setColor(Color.CYAN);
        final int y1 = (int) Math.round(ph * 0.1625);
        g.drawString("Wizard", x1, y1);
        g.setColor(Color.YELLOW);
        final int scoreX = mScreen.getXOffset() + (int) Math.round(pw * 0.546875);
        final int teamX = scoreX + (massX - scoreX) / 2;
        g.drawString(SCORE_LABEL, scoreX - stringWidth(g, SCORE_LABEL), y1);
        g.drawString(TEAM_LABEL, teamX - stringWidth(g, TEAM_LABEL), y1);
        g.setColor(Color.GREEN);
        g.drawString(LIFE_LABEL, massX - stringWidth(g, LIFE_LABEL), y1);
        g.setColor(Color.RED);
        g.drawLine(mScreen.getXOffset() + cwh, y1 + 2, massX + cwh, y1 + 2);
        final int delta = (int) Math.round(cw * 1.5625);
        final int q0 = 10 * cw / 16;
        final int q1 = 5 * cw / 16;
        final int dx = mScreen.getXOffset() + cwh;
        final int sh = cw + (Shape.heightAsInt(cw) - cw) / 2;
        // Only show as many wizards as will fit on the screen
        for (int i = 0, y = (int) Math.round(ph * 0.1375); i < mWizards.length && y < mScreen.getMainHeight() - cw; ++i, y += delta) {
          final Wizard w = mWizards[i];
          if (w != null) {
            // need wizard to look alive for image
            final State state = w.getState();
            w.setState(State.ACTIVE);
            final Image im = w == mIndependent ? INDEPENDENT_IMAGE : Chaos.getChaos().getTileManager().getTile(w, 0, 0, 0);
            g.drawImage(im, mScreen.getXOffset() + cw + cwh, y - cw, null);
            w.setState(state);
            if (w != mIndependent) {
              ShapeFactory.createShape(team.heraldicKey(team.getTeam(w))).render(g, cw, x0, y - sh);
            }
            g.setColor(Color.CYAN);
            g.setFont(mScreen.getTextFont());
            g.drawString(w.getPersonalName(), x1, y);
            g.setColor(Color.YELLOW);
            g.setFont(mScreen.getMonospaceFont());
            final String score = String.valueOf(w.getScore());
            g.drawString(score, scoreX - stringWidth(g, score), y);
            final Integer ts = teamScores.get(team.getTeam(w));
            if (ts != null) {
              final String tscore = String.valueOf(ts);
              g.drawString(tscore, teamX - stringWidth(g, tscore), y);
            }
            g.setColor(Color.GREEN);
            final String mass = String.valueOf(w.getMass());
            g.drawString(mass, massX - stringWidth(g, mass), y);
            if (mPreviousWizardOrder != null) {
              g.setColor(Color.RED);
              for (int j = 0; j < mPreviousWizardOrder.length; ++j) {
                if (mPreviousWizardOrder[j] == w) {
                  if (i == j) {
                    // same place
                    g.fillOval(dx, y - q1 - cwh, q0, q0);
                  } else if (i < j) {
                    // moving on up
                    g.fillPolygon(new int[] {dx, dx + q0, dx + q1}, new int[] {y - q1 + 1, y - q1 + 1, y - q0}, 3);
                  } else {
                    // moving down
                    g.fillPolygon(new int[] {dx, dx + q0, dx + q1}, new int[] {y - q0, y - q0, y - q1}, 3);
                  }
                }
              }
            }
          }
        }
        g.dispose();
        // remember order for next time
        mPreviousWizardOrder = new Wizard[mWizards.length];
        System.arraycopy(mWizards, 0, mPreviousWizardOrder, 0, mWizards.length);
        mLock.setValue(false);
        mScreen.addMouseListener(this);
        mScreen.addKeyListener(this);
        try {
          // only a short delay if there are no humans
          mLock.waitUntilTrue(isThereAHuman() ? 60000 : NO_HUMAN_SLEEP);
        } catch (final InterruptedException e) {
          // too bad
        }
        mScreen.removeKeyListener(this);
        mScreen.removeMouseListener(this);
      }
    }
  }

  @Override
  public void mousePressed(final MouseEvent e) { }
  @Override
  public void mouseReleased(final MouseEvent e) { }
  @Override
  public void mouseEntered(final MouseEvent e) { }
  @Override
  public void mouseExited(final MouseEvent e) { }
  @Override
  public void keyPressed(final KeyEvent e) { }
  @Override
  public void keyTyped(final KeyEvent e) { }

  @Override
  public void keyReleased(final KeyEvent e) {
    mLock.setValue(true);
    e.consume();
  }


  @Override
  public void mouseClicked(final MouseEvent e) {
    mLock.setValue(true);
    e.consume();
  }
}
