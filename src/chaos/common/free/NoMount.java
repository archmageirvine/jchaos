package chaos.common.free;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * No mount.
 * @author Sean A. Irvine
 */
public class NoMount extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    boolean soundPlayed = false;
    if (world != null && caster != null) {
      final Team t = world.getTeamInformation();
      for (final Wizard w : world.getWizardManager().getWizards()) {
        if (w != null && w.getState() == State.ACTIVE && t.getTeam(w) != t.getTeam(caster)) {
          if (casterCell != null && !soundPlayed) {
            casterCell.notify(new AudioEvent(casterCell, caster, "no_mount"));
            soundPlayed = true;
          }
          if (!w.is(PowerUps.RIDE)) {
            w.increment(PowerUps.NO_MOUNT, 6);
          }
          final Cell c = world.getCell(w);
          if (c != null) {
            // We found the cell containing the enemy wizard, try and choose an empty adjacent cell
            final Actor a = c.peek();
            if (a != w && a instanceof Conveyance && ((Conveyance) a).getMount() != null) {
              // In theory this must be a mount of some kind containing the wizards -- but check it
              Cell chosen = null;
              int p = 0;
              for (final Cell empty : world.getCells(c.getCellNumber(), 1, 1, false)) {
                if ((empty.peek() == null || empty.peek().getState() == State.DEAD) && Random.nextInt(++p) == 0) {
                  chosen = empty;
                }
              }
              if (chosen != null) {
                // Found an empty cell to force dismount into
                c.notify(new CellEffectEvent(c, CellEffectType.WARP_OUT));
                chosen.notify(new CellEffectEvent(chosen, CellEffectType.WARP_IN));
                chosen.push(((Conveyance) a).getMount());
                ((Conveyance) a).setMount(null);
                c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
                chosen.notify(new CellEffectEvent(chosen, CellEffectType.REDRAW_CELL));
              }
            }
          }
        }
      }
    }
  }
}
