package chaos.common.monster;

import java.util.ArrayList;

import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Cat;
import chaos.common.FrequencyTable;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.Singleton;

/**
 * Cat Lord.
 * @author Sean A. Irvine
 */
public class CatLord extends Polycaster implements Humanoid, Singleton {

  @SuppressWarnings("unchecked")
  private static Class<? extends Castable>[] makeArray(final int n) {
    return (Class<? extends Castable>[]) new Class<?>[n];
  }

  private static Class<? extends Castable>[] sTypesOfCat = null;

  private static Class<? extends Castable>[] getCats() {
    if (sTypesOfCat == null) {
      // Lazy instantiation to avoid construction during listing of castables
      final ArrayList<Class<? extends Castable>> typesOfCat = new ArrayList<>();
      for (final Class<? extends Castable> clazz : FrequencyTable.DEFAULT.getCastableClasses()) {
        if (clazz != CatLord.class && Cat.class.isAssignableFrom(clazz)) {
          typesOfCat.add(clazz);
        }
      }
      sTypesOfCat = typesOfCat.toArray(makeArray(typesOfCat.size()));
    }
    return sTypesOfCat;
  }

  /** Cat lord. */
  public CatLord() {
    super(12, getCats());
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  {
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.INTELLIGENCE, 73);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x0CDFFF7E5BBFFFC0L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Lion.class;
  }
}
