package chaos.engine;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.Elemental;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.State;
import chaos.common.Wall;
import chaos.common.wizard.Wizard;

/**
 * Threat assessment used by the AI.
 * @author Sean A. Irvine
 */
public final class Weight {

  private Weight() {
  }

  private static double statRating(final Attribute a) {
    switch (a) {
      case AGILITY:
        return 0.1;
      case INTELLIGENCE:
      case MAGICAL_RESISTANCE:
        return 0.3;
      case MOVEMENT:
        return 0.5;
      case SPECIAL_COMBAT:
        return 1.2;
      default:
        return 1;
    }
  }

  private static double absoluteLifeRating(final Actor a) {
    return Math.max(a.get(Attribute.LIFE), a.getDefault(Attribute.LIFE)) * (1 + Math.log(Math.max(1, a.get(Attribute.LIFE_RECOVERY))));
  }

  private static final double DEAD_WEIGHT = 0.5;

  /**
   * Return a number representing how lethal the given actor is.  The higher
   * the number the more lethal the actor it represents.  This is a raw
   * rating that does not take into account the position of the actor.
   * @param a an <code>Actor</code> value
   * @return weight
   */
  public static double lethality(final Actor a) {
    if (a == null) {
      return 0;
    }
    if (a.getState() == State.DEAD) {
      return DEAD_WEIGHT;
    }
    double x = Math.sqrt(absoluteLifeRating(a));
    x += Math.log(1 + a.get(Attribute.MAGICAL_RESISTANCE));
    if (a instanceof Monster) {
      final Monster m = (Monster) a;
      x += Math.log(1 + 5 * m.get(Attribute.MOVEMENT));
      x += Math.log(1 + Math.log(1 + m.get(Attribute.AGILITY)));
      x += Math.log(Math.sqrt(1 + m.get(Attribute.INTELLIGENCE)));
      final double c = m.get(Attribute.COMBAT) * statRating(m.getCombatApply());
      if (c >= 0) {
        x += c / 2.0;
      } else {
        x -= c / 5.0;
      }
      final double rc = m.get(Attribute.RANGED_COMBAT) * statRating(m.getRangedCombatApply());
      if (rc >= 0) {
        x += rc / 1.5;
        x += m.get(Attribute.RANGE) / 4.0;
      } else {
        x -= rc / 5.0;
      }
      final double sc = m.get(Attribute.SPECIAL_COMBAT) * statRating(m.getSpecialCombatApply());
      if (sc >= 0) {
        x += sc;
      } else {
        x -= sc / 3.0;
      }
      if (m.is(PowerUps.FLYING)) {
        ++x;
      }
      if (m instanceof Caster) {
        x += 5;
      }
      if (a instanceof Promotion) {
        ++x;
      }
      if (a instanceof Elemental) {
        x += lethality(((Elemental) a).getElementalReplacement());
      }
    }
    if (a instanceof Growth) {
      final Growth g = (Growth) a;
      final int mulg = g.getGrowthType() == Growth.GROW_BY_COMBAT ? 1 : 2;
      x += 2 * Math.log(g.growthRate()) * mulg;
    }
    if (a instanceof Conveyance) {
      x *= 1.5;
    } else if (a instanceof Inanimate) {
      x *= 0.4;
    }
    if (a instanceof Wizard) {
      x *= 8;
    }
    if (a instanceof AbstractGenerator) {
      x *= 8;
    }
    if (a instanceof Wall) {
      x *= 0.4;
    }
    switch (a.getRealm()) {
      case ETHERIC:
      case MYTHOS:
        x *= 1.5;
        break;
      case DRACONIC:
        x *= 1.1;
        break;
      case DEMONIC:
        x *= 1.7;
        break;
      default:
        break;
    }
    return x;
  }

}

