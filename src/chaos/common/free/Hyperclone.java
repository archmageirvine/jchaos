package chaos.common.free;

import java.util.ArrayList;
import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Clone;
import chaos.util.PolycellEffectEvent;
import chaos.util.Random;
import irvine.util.Pair;

/**
 * Hyperclone.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class Hyperclone extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      final int p = caster.getOwner();
      final ArrayList<Pair<Actor, Cell>> clones = new ArrayList<>();
      final HashSet<Cell> used = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a != null && a.getOwner() == p && a.getState() == State.ACTIVE && !(a instanceof Wizard)) {
          // Found an actor to clone.  First make sure there is a suitable place to put the clone.
          Cell choice = null;
          int count = 0;
          for (final Cell c : world.getCells(i, 1, 1, false)) {
            final Actor t = c.peek();
            if ((t == null || t.getState() == State.DEAD) && !used.contains(c) && Random.nextInt(++count) == 0) {
              choice = c;
            }
          }
          if (choice != null) {
            // There is a suitable cell, form the clone and remember it for insertion later
            used.add(choice);
            clones.add(new Pair<>(Clone.clone(a), choice));
          }
        }
      }
      if (!used.isEmpty()) {
        if (casterCell != null) {
          casterCell.notify(new AudioEvent(casterCell, caster, "hyperclone"));
        }
        world.notify(new PolycellEffectEvent(used, CellEffectType.TWIRL, caster));
        for (final Pair<Actor, Cell> pair : clones) {
          pair.right().push(pair.left());
        }
        world.notify(new PolycellEffectEvent(used, CellEffectType.REDRAW_CELL));
      } else if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
