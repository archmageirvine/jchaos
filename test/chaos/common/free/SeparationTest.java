package chaos.common.free;

import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.monster.Lion;
import chaos.common.spell.Alliance;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class SeparationTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Separation();
  }

  public void testCast() {
    final Alliance a = new Alliance();
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
    final Separation s = new Separation();
    final TestListener listen = new TestListener(CellEffectType.TEAM_CHANGE);
    world.register(listen);
    s.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(WizardManager.WIZ_ARRAY_SIZE + 2, world.getTeamInformation().getTeam(1));
    assertEquals(4, world.getTeamInformation().getTeam(2));
    assertEquals(0, world.getTeamInformation().getTeam(Actor.OWNER_INDEPENDENT));
    world.deregister(listen);
    listen.checkAndReset();
    s.cast(world, w, world.getCell(5), null);
    s.cast(world, w, null, world.getCell(0));
    s.cast(world, null, world.getCell(5), world.getCell(0));
    s.cast(null, w, world.getCell(5), world.getCell(0));
  }
}
