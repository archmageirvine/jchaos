package chaos.common;

import java.util.Locale;

import chaos.common.free.Amnesia;
import chaos.common.free.Arborist;
import chaos.common.free.BattleCry;
import chaos.common.free.Coercion;
import chaos.common.free.Confidence;
import chaos.common.free.CrystalBall;
import chaos.common.free.DeadRevenge;
import chaos.common.free.Depth;
import chaos.common.free.Double;
import chaos.common.free.IrvinesInvulnerability;
import chaos.common.free.Level;
import chaos.common.free.LichLord;
import chaos.common.free.MagicBow;
import chaos.common.free.MagicKnife;
import chaos.common.free.MagicWand;
import chaos.common.free.MoveIt;
import chaos.common.free.Necropotence;
import chaos.common.free.NoMount;
import chaos.common.free.Ride;
import chaos.common.free.Talisman;
import chaos.common.free.Torment;
import chaos.common.free.Triple;
import chaos.common.free.Uncertainty;
import chaos.common.free.WizardWings;
import chaos.common.spell.Archery;
import chaos.common.spell.Cloak;
import chaos.common.spell.EarthquakeShield;
import chaos.common.spell.FireShield;
import chaos.common.spell.FloodShield;
import chaos.common.spell.Freeze;
import chaos.common.spell.Herbicide;
import chaos.common.spell.Horror;
import chaos.common.spell.LifeLeech;
import chaos.common.spell.MagicSword;
import chaos.common.spell.Reflector;
import chaos.common.spell.Reincarnate;

/**
 * Enumeration of possible power-ups.
 * @author Sean A. Irvine
 */
public enum PowerUps {
  /** Irvine's invulnerability. */
  INVULNERABLE(new IrvinesInvulnerability()),
  /** Battle cry. */
  BATTLE_CRY(new BattleCry()),
  /** Additional move. */
  MOVE_IT(new MoveIt()),
  /** Heisenberg's uncertainty. */
  UNCERTAINTY(new Uncertainty()),
  /** Will become undead on death. */
  LICH_LORD(new LichLord()),
  /** Has a bow. */
  BOW(new MagicBow()),
  /** Has a sword. */
  SWORD(new MagicKnife()),
  /** Will become a generator on death. */
  DEAD_REVENGE(new DeadRevenge()),
  /** Can ride level 2 mounts. */
  RIDE(new Ride()),
  /** Has the double spell cast. */
  DOUBLE(new Double()),
  /** Has the triple spell cast. */
  TRIPLE(new Triple()),
  /** Has the arborist ability. */
  ARBORIST(new Arborist()),
  /** Has a fire shield. */
  FIRE_SHIELD(new FireShield()),
  /** Has a flood shield. */
  FLOOD_SHIELD(new FloodShield()),
  /** Has a earthquake shield. */
  EARTHQUAKE_SHIELD(new EarthquakeShield()),
  /** Frozen, cannot move. */
  FROZEN(new Freeze()),
  /** Has the depth power up. */
  DEPTH(new Depth()),
  /** Has a reflector. */
  REFLECT(new Reflector()),
  /** Is currently amnesia. */
  AMNESIA(new Amnesia()),
  /** Level of the wizard. */
  LEVEL(new Level()),
  /** Has ability to fly. */
  FLYING(new WizardWings()),
  /** Can attack creatures from any realm. */
  ATTACK_ANY_REALM(new MagicSword()),
  /** Has been coerced. */
  COERCION(new Coercion()),
  /** Is tormented. */
  TORMENT(new Torment()),
  /** Is suffering from necropotence. */
  NECROPOTENCE(new Necropotence()),
  /** Has confidence. */
  CONFIDENCE(new Confidence()),
  /** Wand count. */
  WAND(new MagicWand()),
  /** Has a crystal ball. */
  CRYSTAL_BALL(new CrystalBall()),
  /** Does not need line-of-sight for shooting. */
  ARCHERY(new Archery()),
  /** Actor is cloaked. */
  CLOAKED(new Cloak()),
  /** Actor has horror. */
  HORROR(new Horror()),
  /** Has reincarnation. */
  REINCARNATE(new Reincarnate()),
  /** Talisman for extended special combat. */
  TALISMAN(new Talisman()),
  /** Will steal life from other creations to stay alive if possible. */
  LIFE_LEECH(new LifeLeech()),
  /** This actor will not grow (applicable to growths). */
  NO_GROW(new Herbicide()),
  /** This actor cannot mount this turn. */
  NO_MOUNT(new NoMount()),
  ;

  /** Associated castable. */
  private final Castable mCastable;

  PowerUps(final Castable c) {
    mCastable = c;
  }

  /**
   * Return the castable associated with this power-up.  If there is no
   * associated castable then null is returned.
   *
   * @return associated castable
   */
  public Castable getCastable() {
    return mCastable;
  }

  /**
   * Return resource (if any) that can be used for sound when casting this effect.
   *
   * @return sound effect
   */
  public String castingSound() {
    return "chaos/resources/sound/casting/" + toString().toLowerCase(Locale.getDefault());
  }
}
