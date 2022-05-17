package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Random;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.BowShooter;
import chaos.common.Castable;
import chaos.common.Inanimate;
import chaos.common.Realm;
import chaos.common.dragon.BlackDragon;
import chaos.common.dragon.BlueDragon;
import chaos.common.dragon.EmeraldDragon;
import chaos.common.dragon.GoldenDragon;
import chaos.common.dragon.GreenDragon;
import chaos.common.dragon.PlatinumDragon;
import chaos.common.dragon.RedDragon;
import chaos.common.dragon.ShadowDragon;
import chaos.common.dragon.WhiteDragon;
import chaos.common.monster.Bolter;
import chaos.common.monster.Dalek;
import chaos.common.monster.FireDemon;
import chaos.common.monster.Manticore;
import chaos.common.monster.OgreMage;
import chaos.common.monster.Pseudodragon;
import chaos.common.monster.Pyrohydra;
import chaos.common.mythos.Flerken;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.Sleep;

/**
 * Ranged combat effect.
 *
 * @author Sean A. Irvine
 */
public class RangedCombatGraphics {

  /** Random number generator. */
  private static final Random RANDOM = new Random();

  /** Color descriptions for breath weapons. */
  private static final HashMap<Class<? extends Actor>, Color[]> DRAGON_BREATH_COLOR = new HashMap<>();
  static {
    DRAGON_BREATH_COLOR.put(ShadowDragon.class, new Color[] {Color.RED, Color.RED.darker()});
    DRAGON_BREATH_COLOR.put(BlackDragon.class, new Color[] {Color.ORANGE, Color.ORANGE.darker()});
    DRAGON_BREATH_COLOR.put(BlueDragon.class, new Color[] {Color.WHITE, Color.GRAY});
    DRAGON_BREATH_COLOR.put(GoldenDragon.class, new Color[] {Color.GREEN, Color.ORANGE});
    DRAGON_BREATH_COLOR.put(PlatinumDragon.class, new Color[] {Color.YELLOW, Color.GRAY});
    DRAGON_BREATH_COLOR.put(RedDragon.class, new Color[] {Color.YELLOW});
    DRAGON_BREATH_COLOR.put(GreenDragon.class, new Color[] {Color.YELLOW});
    DRAGON_BREATH_COLOR.put(Pseudodragon.class, new Color[] {Color.WHITE});
    DRAGON_BREATH_COLOR.put(EmeraldDragon.class, new Color[] {Color.CYAN, Color.BLUE.brighter()});
    DRAGON_BREATH_COLOR.put(WhiteDragon.class, new Color[] {Color.CYAN});
  }

  private final ChaosScreen mScreen;
  private final World mWorld;
  private final Beam mBeam;
  private final Parabola mParabola;
  private final Animator mAnimator;

  RangedCombatGraphics(final World world, final ChaosScreen screen, final Graphics graphics, final Animator animator, final int widthBits) {
    mWorld = world;
    mScreen = screen;
    mAnimator = animator;
    mBeam = new Beam(mWorld, mScreen, graphics, widthBits);
    mParabola = new Parabola(mWorld, mScreen, graphics, widthBits);
  }

  void rangedCombatEffect(final int source, final int target, final Castable cause) {
    final Actor a = cause instanceof Actor ? (Actor) cause : mWorld.actor(source);
    assert a != null;
    final Actor t = mWorld.actor(target);
    final Color[] c;
    final Sound sound = Sound.getSoundEngine();
    final BooleanLock s;
    if (a instanceof BowShooter) {
      // choose sound effect based on target
      final String version;
      if (t == null || t.getRealm() == Realm.ETHERIC) {
        version = "_empty";
      } else if (t instanceof Inanimate) {
        version = "_thunk";
      } else {
        version = String.valueOf(RANDOM.nextInt(4));
      }
      s = sound.play("chaos/resources/sound/ranged/bow" + version, SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mParabola.parabola(source, target, Color.RED);
        mAnimator.drawCell(source);
        Sleep.sleep(50);
      }
      sound.wait(s, 5000);
      synchronized (mScreen.lock()) {
        mParabola.parabola(source, target, Color.BLACK);
        mAnimator.drawCell(source);
      }
    } else if ((c = DRAGON_BREATH_COLOR.get(a.getClass())) != null) {
      // have a dragon or pseudodragon
      s = sound.play("chaos/resources/sound/ranged/breath", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.breathWeapon(source, target, c);
      }
      sound.wait(s, 5000);
      synchronized (mScreen.lock()) {
        mBeam.breathWeapon(source, target, Color.BLACK);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof Pyrohydra) {
      s = sound.play("chaos/resources/sound/ranged/breath", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.boltWeapon(source, target, Color.CYAN);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof FireDemon) {
      s = sound.play("chaos/resources/sound/ranged/fireball", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.fireball(source, target);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof OgreMage) {
      s = sound.play("chaos/resources/sound/ranged/fireball", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.spinner(source, target);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof Dalek) {
      s = sound.play("chaos/resources/sound/ranged/dalek", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.boltWeapon(source, target, Color.YELLOW);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof Bolter) {
      s = sound.play("chaos/resources/sound/ranged/bolter", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.boltWeapon(source, target, Color.CYAN);
        mAnimator.drawCell(source);
      }
    } else if (a instanceof Manticore || a instanceof Flerken) {
      s = sound.play("chaos/resources/sound/ranged/bolter", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.boltWeapon(source, target, Color.RED);
        mAnimator.drawCell(source);
      }
    } else {
      s = sound.play("chaos/resources/sound/ranged/birdshot", SoundLevel.whatSoundLevel(a, t));
      synchronized (mScreen.lock()) {
        mBeam.birdShit(source, target, Color.WHITE);
        mAnimator.drawCell(source);
      }
    }
    sound.wait(s, 5000);
  }

}
