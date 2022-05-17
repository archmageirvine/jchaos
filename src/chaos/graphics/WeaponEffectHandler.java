package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.WeaponEffectEvent;

/**
 * Handle actual rendering of weapon effects.
 * @author Sean A. Irvine
 */
public class WeaponEffectHandler {

  private static final Sound SOUND = Sound.getSoundEngine();
  private static final Color DEMOTION_COLOR = new Color(0x664D40);
  private static final Color PROMOTION_COLOR = new Color(0xC68D80);

  private final Animator mAnimator;
  private final ChaosScreen mScreen;
  private final World mWorld;
  private final Beam mBeam;
  private final int mWidthBits;
  private final int mWidth;
  private final RangedCombatGraphics mRangedCombat;
  private final Graphics2D mGraphics;

  WeaponEffectHandler(final Animator a, final ChaosScreen screen, final Graphics2D graphics, final World world, final int widthBits) {
    mAnimator = a;
    mScreen = screen;
    mGraphics = graphics;
    mWorld = world;
    mWidthBits = widthBits;
    mWidth = 1 << widthBits;
    mBeam = new Beam(mWorld, mScreen, mGraphics, mWidthBits);
    mRangedCombat = new RangedCombatGraphics(mWorld, mScreen, mGraphics, mAnimator, mWidthBits);
  }

  private void tribeam(final int source, final int target, final Color color) {
    synchronized (mScreen.lock()) {
      mBeam.tribeam(source, target, color, SOUND.getSoundLevel() == Sound.SOUND_NONE);
      mAnimator.drawCell(source);
    }
  }

  private void birdShit(final int source, final int target, final Color color) {
    final BooleanLock s = SOUND.play("chaos/resources/sound/ranged/fireball", SoundLevel.whatSoundLevel(mWorld.actor(source), mWorld.actor(target)));
    synchronized (mScreen.lock()) {
      mBeam.birdShit(source, target, color);
      mAnimator.drawCell(source);
    }
    SOUND.wait(s, 5000);
  }

  private void fireball(final int source, final int target) {
    final BooleanLock s = SOUND.play("chaos/resources/sound/ranged/fireball", SoundLevel.whatSoundLevel(mWorld.actor(source), mWorld.actor(target)));
    synchronized (mScreen.lock()) {
      mBeam.fireball(source, target);
      mAnimator.drawCell(source);
    }
    SOUND.wait(s, 5000);
  }

  private void spinner(final int source, final int target) {
    final BooleanLock s = SOUND.play("chaos/resources/sound/ranged/fireball", SoundLevel.whatSoundLevel(mWorld.actor(source), mWorld.actor(target)));
    synchronized (mScreen.lock()) {
      mBeam.spinner(source, target);
      mAnimator.drawCell(source);
    }
    SOUND.wait(s, 5000);
  }

  private void playIceBeam(final int source, final int target) {
    final BooleanLock s = SOUND.play("chaos/resources/sound/ranged/breath", SoundLevel.whatSoundLevel(mWorld.actor(source), mWorld.actor(target)));
    synchronized (mScreen.lock()) {
      mBeam.breathWeapon(source, target, Color.CYAN, Color.WHITE, Color.GRAY);
    }
    SOUND.wait(s, 5000);
    synchronized (mScreen.lock()) {
      mBeam.breathWeapon(source, target, Color.BLACK);
      mAnimator.drawCell(source);
    }
  }

  private Color color(final WeaponEffectEvent we) {
    return ((Actor) we.getCause()).getRealm().getColor();
  }

  private Color attrColor(final WeaponEffectEvent we) {
    final Attribute attr = we.getAttribute();
    return attr == null ? Color.WHITE : attr.getColor();
  }

  void weaponEffect(final WeaponEffectEvent we) {
    final int source = we.getSource();
    final int target = we.getTarget();
    switch (we.getEventType()) {
      case BALL:
        birdShit(source, target, attrColor(we));
        break;
      case MONSTER_CAST_EVENT:
        tribeam(source, target, color(we));
        break;
      case FIREBALL:
        fireball(source, target);
        break;
      case SPINNER:
        spinner(source, target);
        break;
      case LINE:
        mAnimator.line(source, target, attrColor(we));
        break;
      case UNLINE:
        mAnimator.line(source, target, Color.BLACK);
        break;
      case TREE_CAST_EVENT:
        birdShit(source, target, color(we).darker());
        break;
      case STONE_CAST_EVENT:
        tribeam(source, target, color(we).darker());
        break;
      case BRAIN_BEAM_EVENT:
        tribeam(source, target, Attribute.INTELLIGENCE.getColor());
        break;
      case RANGED_COMBAT_EVENT:
        mRangedCombat.rangedCombatEffect(source, target, we.getCause());
        break;
      case ICE_BEAM:
        playIceBeam(source, target);
        break;
      case LIGHTNING:
        LightningWeapon.render(mWorld, mScreen, mGraphics, we, mWidth, mWidthBits, 1000);
        break;
      case THUNDERBOLT:
        ThunderboltWeapon.render(mWorld, mScreen, mGraphics, we, mWidth, mWidthBits);
        break;
      case DEMOTION:
        WavyWeapon.render(mWorld, mScreen, mGraphics, we, mWidth, mWidthBits, DEMOTION_COLOR);
        break;
      case PROMOTION:
        WavyWeapon.render(mWorld, mScreen, mGraphics, we, mWidth, mWidthBits, PROMOTION_COLOR);
        break;
      case PLASMA:
        PlasmaWeapon.render(mWorld, mScreen, mGraphics, we, mWidth, mWidthBits);
        break;
      default:
        System.err.println("Unhandled weaponeffectevent: " + we.getEventType());
        break;
    }
  }
}
