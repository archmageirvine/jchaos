package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.wizard.Wizard;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class AllianceTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Alliance();
  }

  public void testCast() {
    final Alliance a = new Alliance();
    assertEquals(Castable.MAX_CAST_RANGE, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH | Castable.CAST_DEAD | Castable.CAST_SINGLE | Castable.CAST_NOINDEPENDENTS, a.getCastFlags());
    final World world = new World(20, 2);
    final Wizard w = new Wizard1();
    w.setOwner(1);
    world.getCell(0).push(w);
    final Lion l = new Lion();
    l.setOwner(2);
    world.getCell(5).push(l);
    assertEquals(3, world.getTeamInformation().getTeam(1));
    assertEquals(4, world.getTeamInformation().getTeam(2));
    a.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(4, world.getTeamInformation().getTeam(1));
    assertEquals(4, world.getTeamInformation().getTeam(2));
    assertEquals(0, world.getTeamInformation().getTeam(Actor.OWNER_INDEPENDENT));
    l.setOwner(Actor.OWNER_INDEPENDENT);
    a.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(0, world.getTeamInformation().getTeam(Actor.OWNER_INDEPENDENT));
    assertEquals(4, world.getTeamInformation().getTeam(1));
    assertEquals(4, world.getTeamInformation().getTeam(2));
  }
}
