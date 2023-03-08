package chaos.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;

/**
 * Dump a text detailing the specified class.
 *
 * @author Sean A. Irvine
 */
public final class DumpCreature {

  private DumpCreature() { }

  private static final Attribute[] ATTRIBUTES = {
    Attribute.LIFE,
    Attribute.LIFE_RECOVERY,
    Attribute.MAGICAL_RESISTANCE,
    Attribute.MAGICAL_RESISTANCE_RECOVERY,
    Attribute.MOVEMENT,
    Attribute.MOVEMENT_RECOVERY,
    Attribute.AGILITY,
    Attribute.AGILITY_RECOVERY,
    Attribute.COMBAT,
    Attribute.COMBAT_RECOVERY,
    Attribute.RANGED_COMBAT,
    Attribute.RANGED_COMBAT_RECOVERY,
    Attribute.RANGE,
    Attribute.RANGE_RECOVERY,
    Attribute.SPECIAL_COMBAT,
    Attribute.SPECIAL_COMBAT_RECOVERY,
    Attribute.SHOTS,
  };

  private static final Class<?>[] FEATURES = {
    Growth.class,
    Singleton.class,
    Caster.class,
    Cat.class,
    Tree.class,
    Elemental.class,
    Rider.class,
    NoFlyOver.class,
    Killer.class,
    Bonus.class,
  };

  private static final String HEADER = "Name,Freq,Realm,L,Lr,MR,MRr,M,Mr,A,Ar,C,Cr,RC,RCr,R,Rr,SC,SCr,SH,Mult,Rein,Notes";

  private static String getName(final Actor cst) {
    return cst instanceof Named ? "Named " + cst.getName() : cst.getName();
  }

  private static String getReincarnation(final Actor cst) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    if (cst instanceof Monster) {
      final Monster m = (Monster) cst;
      final Class<? extends Monster> reincarnation = m.reincarnation();
      if (reincarnation != null) {
        return getName(reincarnation.getDeclaredConstructor().newInstance());
      }
    }
    return "";
  }

  private static void append(final StringBuilder notes, final String s) {
    if (notes.length() > 0) {
      notes.append(", ");
    }
    notes.append(s);
  }

  private static String sanitize(final String s) {
    return s.toLowerCase(Locale.getDefault()).replace('_', ' ');
  }

  private static String dumpCsvActor(final Actor cst) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    final StringBuilder sb = new StringBuilder(getName(cst)).append(',')
      .append(FrequencyTable.DEFAULT.getFrequency(cst.getClass())).append(',')
      .append(cst.getRealm()).append(',')
      ;
    for (final Attribute attr : ATTRIBUTES) {
      sb.append(cst.get(attr)).append(',');
    }
    if (cst instanceof Multiplicity) {
      sb.append(((Multiplicity) cst).getMultiplicity());
    } else {
      sb.append('1');
    }
    sb.append(',');
    sb.append(getReincarnation(cst)).append(',');
    final StringBuilder notes = new StringBuilder();
    if (cst instanceof Promotion) {
      final Promotion p = (Promotion) cst;
      notes.append("Promotes to ").append(getName(p.promotion().getDeclaredConstructor().newInstance())).append('(').append(p.promotionCount()).append(')');
    }
    if (cst instanceof Monster) {
      final Monster m = (Monster) cst;
      if (m.getCombatApply() != Attribute.LIFE) {
        append(notes, "C applies to " + sanitize(m.getCombatApply().toString()));
      }
      if (m.getRangedCombatApply() != Attribute.LIFE) {
        append(notes, "RC applies to " + sanitize(m.getRangedCombatApply().toString()));
      }
      if (m.getSpecialCombatApply() != Attribute.LIFE) {
        append(notes, "SC applies to " + sanitize(m.getSpecialCombatApply().toString()));
      }
    }
    for (final PowerUps p : PowerUps.values()) {
      final int v = cst.get(p);
      if (v >= 1) {
        if (notes.length() > 0) {
          notes.append(", ");
        }
        notes.append(p == PowerUps.FLYING ? "Flying" : p.getCastable().getName());
        if (v > 1) {
          notes.append('(').append(v).append(')');
        }
      }
    }
    if (cst instanceof Rideable) {
      append(notes, "Rideable");
    } else if (cst instanceof Mountable) {
      append(notes, "Mount");
    } else if (cst instanceof Meditation) {
      append(notes, "Meditation");
    } else if (cst instanceof Conveyance) {
      append(notes, "Conveyance");
    }
    if (cst instanceof AbstractGenerator) {
      append(notes, "Generator");
    }
    for (final Class<?> feature : FEATURES) {
      if (feature.isInstance(cst)) {
        append(notes, feature.getSimpleName());
      }
    }
    sb.append('"').append(notes).append('"');
    return sb.toString();
  }

  private static void dumpCsvAll() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
    System.out.println(HEADER);
    for (final Class<? extends Castable> castable : FrequencyTable.ALL.getCastableClasses()) {
      final Castable cst = castable.getDeclaredConstructor().newInstance();
      if (cst instanceof Actor && (!(cst instanceof Wizard) || cst instanceof Wizard1)) {
        System.out.println(dumpCsvActor((Actor) cst));
      }
    }
  }

  /**
   * Dump all the information about a Castable.
   * @param args class name
   * @exception Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {

    if (args.length == 0) {
      System.err.println("USAGE: DumpCreature class");
      return;
    }
    if ("--csv".equals(args[0])) {
      dumpCsvAll();
      return;
    }

    final Class<? extends Castable> cl = Class.forName(args[0]).asSubclass(Castable.class);
    final Castable cst = cl.getDeclaredConstructor().newInstance();
    System.out.println(cst.getName());
    System.out.println(cst.getDescription());
    System.out.println("Frequency:          " + FrequencyTable.DEFAULT.getFrequency(cl));

    if (cst instanceof Actor) {
      final Actor a = (Actor) cst;
      long mask = a.getLosMask();
      System.out.println("LOS mask:           " + Long.toHexString(mask));
      System.out.println("   +--------+");
      for (int i = 0; i < 8; ++i) {
        System.out.print("   |");
        for (int j = 0; j < 8; ++j) {
          System.out.print(mask < 0L ? "*" : ".");
          mask <<= 1;
        }
        System.out.println('|');
      }
      System.out.println("   +--------+");
      System.out.println("Realm:              " + a.getRealm());
      System.out.println("Life:               " + a.get(Attribute.LIFE) + " " + a.get(Attribute.LIFE_RECOVERY));
      System.out.println("MR:                 " + a.get(Attribute.MAGICAL_RESISTANCE) + " " + a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    }

    if (cst instanceof Monster) {
      final Monster m = (Monster) cst;
      System.out.println("Intelligence:       " + m.get(Attribute.INTELLIGENCE) + " " + m.get(Attribute.INTELLIGENCE_RECOVERY));
      System.out.println("Agility:            " + m.get(Attribute.AGILITY) + " " + m.get(Attribute.AGILITY_RECOVERY));
      System.out.println("Combat:             " + m.get(Attribute.COMBAT) + " " + m.get(Attribute.COMBAT_RECOVERY));
      System.out.println("Movement:           " + m.get(Attribute.MOVEMENT) + " " + m.get(Attribute.MOVEMENT_RECOVERY));
      System.out.println("Ranged Combat:      " + m.get(Attribute.RANGED_COMBAT) + " " + m.get(Attribute.RANGED_COMBAT_RECOVERY));
      System.out.println("Range:              " + m.get(Attribute.RANGE) + " " + m.get(Attribute.RANGE_RECOVERY));
      System.out.println("Special Combat:     " + m.get(Attribute.SPECIAL_COMBAT) + " " + m.get(Attribute.SPECIAL_COMBAT_RECOVERY));
      System.out.println("Flying:             " + m.is(PowerUps.FLYING));
      Monster r = m;
      Class<? extends Monster> re;
      System.out.print("Reincarnation:      ");
      do {
        re = r.reincarnation();
        System.out.print(re == null ? "null" : (re.getName() + "->"));
        if (re != null) {
          try {
            r = re.getDeclaredConstructor().newInstance();
          } catch (final Exception e) {
            e.printStackTrace();
          }
        }
      } while (re != null);
      System.out.println();
    }

    if (cst instanceof Promotion) {
      System.out.println("Promotion:          " + ((Promotion) cst).promotion().getName() + " " + ((Promotion) cst).promotionCount());
    }

    if (cst instanceof Growth) {
      System.out.println("Is a growth");
    }

    if (cst instanceof Humanoid) {
      System.out.println("Is a humanoid");
    }

    if (cst instanceof Cat) {
      System.out.println("Is a cat");
    }

    if (cst instanceof Meditation) {
      System.out.println("Is a meditation");
    }

    if (cst instanceof Mountable) {
      System.out.println("Is a mount");
    }

    if (cst instanceof Rideable) {
      System.out.println("Is a level 2 mount");
    }

    if (cst instanceof Bonus) {
      System.out.println("Kill bonus is " + ((Bonus) cst).getBonus() + " spells");
    }

    if (cst instanceof Caster) {
      if (cst instanceof Unicaster) {
        System.out.println("Is a uni-spellcaster");
      } else {
        System.out.println("Is a spellcaster");
      }
    }
  }

}
