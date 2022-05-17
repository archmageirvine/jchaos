package chaos.common;

import chaos.board.Cell;
import chaos.board.World;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.NameUtils;
import chaos.util.Random;
import chaos.util.TextEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * This extension of Actor is made for the generators.  It provides
 * calls used in generation.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractGenerator extends Actor implements NoDeadImage, Inanimate {
  {
    setRealm(Realm.MATERIAL);
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 100);
  }
  @Override
  public int getCastRange() {
    return 14;
  }
  @Override
  public long getLosMask() {
    return ~0L;
  }
  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public int getCastFlags() {
    return Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_GROWTH | Castable.CAST_LOS;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public void increment(final Attribute attr, final int inc) {
    if (attr != Attribute.MOVEMENT) {
      super.increment(attr, inc);
    }
  }

  /**
   * When called should produce an actor to be generated.  If nothing is
   * to be generated then null should be returned.
   *
   * @return actor to generate
   */
  public abstract Actor chooseWhatToGenerate();

  /**
   * Handle generation by a generator.
   * @param w world containing this generator
   * @param cellNumber cell number of this generator
   */
  public void generate(final World w, final int cellNumber) {
    if (getState() != State.ACTIVE) {
      // Only active generators should produce things
      return;
    }
    final Actor choice = chooseWhatToGenerate();
    if (choice instanceof Singleton && w.isAlive(choice.getClass()) != OWNER_NONE) {
      // Don't generate singleton if one is already present
      return;
    }
    if (choice != null) {
      // choose a random empty adjacent cell
      Cell c = null;
      int z = 1;
      for (final Cell cell : w.getCells(cellNumber, 1, 1, false)) {
        final Actor a = cell.peek();
        if (a == null || a.getState() == State.DEAD) {
          // found a suitable cell, change with probability 1/z
          if (Random.nextInt(z++) == 0) {
            c = cell;
          }
        }
      }
      if (c != null) {
        // found somewhere, so do the cast
        choice.setOwner(getOwner());
        c.notify(new TextEvent("Generator: " + NameUtils.getTextName(choice)));
        c.notify(new WeaponEffectEvent(cellNumber, c.getCellNumber(), WeaponEffectType.MONSTER_CAST_EVENT, choice));
        c.notify(new CellEffectEvent(c, CellEffectType.MONSTER_CAST_EVENT, choice));
        c.push(choice);
      }
    }
  }

}
