package chaos.engine;

import chaos.board.CastMaster;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.inanimate.MagicCastle;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class IntelligentMeditationCasterTest extends TestCase {

  private static final String EXPECTED_MW1 =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + ".........MWM........\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  private static final String EXPECTED_MW2 =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "........M...M.......\n"
      + "..........M.........\n"
      + "........M.W.M.......\n"
      + "..........M.........\n"
      + "........M...M.......\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  private static final String EXPECTED_MWH1 =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + ".........MHM........\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  private static final String EXPECTED_MWH2 =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "........M...M.......\n"
      + "..........M.........\n"
      + "........M.H.M.......\n"
      + "..........M.........\n"
      + "........M...M.......\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  private static final String EXPECTED_MW_ARBORIST =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + ".........MMM........\n"
      + ".........MWM........\n"
      + ".........MMM........\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  /**
   * Dump a rough text representation of the world
   * @param world the world
   * @return string form
   */
  public static String dumpWorld(final World world) {
    final StringBuilder sb = new StringBuilder();
    for (int y = 0; y < world.height(); ++y) {
      for (int x = 0; x < world.width(); ++x) {
        final Actor a = world.actor(x, y);
        sb.append(a == null ? '.' : a.getName().charAt(0));
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  public void testTypicalMagicWood() {
    final World world = new World(20, 20, new Team());
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz = world.getWizardManager().getWizard(1);
    wiz.setState(State.ACTIVE);
    world.getCell(10, 10).push(wiz);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz, world.getCell(10, 10), new MagicWood(), 1));
    final String actual = dumpWorld(world);
    assertTrue(actual, EXPECTED_MW1.equals(actual) || EXPECTED_MW2.equals(actual));
  }

  public void testMountedMagicWood() {
    final World world = new World(20, 20, new Team());
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz = world.getWizardManager().getWizard(1);
    wiz.setState(State.ACTIVE);
    final Horse horse = new Horse();
    horse.setOwner(1);
    horse.setMount(wiz);
    world.getCell(10, 10).push(horse);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz, world.getCell(10, 10), new MagicWood(), 1));
    final String actual = dumpWorld(world);
    assertTrue(actual, EXPECTED_MWH1.equals(actual) || EXPECTED_MWH2.equals(actual));
  }

  public void testArboristMagicWood() {
    final World world = new World(20, 20, new Team());
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz = world.getWizardManager().getWizard(1);
    wiz.setState(State.ACTIVE);
    wiz.increment(PowerUps.ARBORIST);
    world.getCell(10, 10).push(wiz);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz, world.getCell(10, 10), new MagicWood(), 1));
    assertEquals(EXPECTED_MW_ARBORIST, dumpWorld(world));
  }

  public void testNoLegalTargets() {
    final World world = new World(1, 1, new Team());
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz = world.getWizardManager().getWizard(1);
    wiz.setState(State.ACTIVE);
    world.getCell(0).push(wiz);
    assertFalse(IntelligentMeditationCaster.cast(world, castMaster, wiz, world.getCell(0), new MagicWood(), 1));
  }

  public void testMagicCastleEnemy() {
    final Team team = new Team();
    final World world = new World(4, 1, team);
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz1 = world.getWizardManager().getWizard(1);
    wiz1.setState(State.ACTIVE);
    world.getCell(1).push(wiz1);
    final Wizard wiz2 = world.getWizardManager().getWizard(2);
    wiz2.setState(State.ACTIVE);
    world.getCell(3).push(wiz2);
    team.separate(1);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz1, world.getCell(1), new MagicCastle(), 1));
    assertEquals("MW.W\n", dumpWorld(world));
  }

  public void testMagicCastleFriend() {
    final Team team = new Team();
    final World world = new World(4, 1, team);
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz1 = world.getWizardManager().getWizard(1);
    wiz1.setState(State.ACTIVE);
    world.getCell(1).push(wiz1);
    final Wizard wiz2 = world.getWizardManager().getWizard(2);
    wiz2.setState(State.ACTIVE);
    world.getCell(3).push(wiz2);
    team.setTeam(1, 2);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz1, world.getCell(1), new MagicCastle(), 1));
    assertEquals(".WMW\n", dumpWorld(world));
  }

  private static final String EXPECTED_MW_FRIEND =
    "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + ".........MWMW.......\n"
      + "....................\n"
      + "........M.M.M.......\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n";

  public void testTypicalMagicWoodWithFriend() {
    final Team team = new Team();
    final World world = new World(20, 20, team);
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz1 = world.getWizardManager().getWizard(1);
    wiz1.setState(State.ACTIVE);
    world.getCell(10, 10).push(wiz1);
    final Wizard wiz2 = world.getWizardManager().getWizard(2);
    wiz2.setState(State.ACTIVE);
    world.getCell(12, 10).push(wiz2);
    team.setTeam(1, 2);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz1, world.getCell(10, 10), new MagicWood(), 1));
    assertEquals(EXPECTED_MW_FRIEND, dumpWorld(world));
  }

  private static final String EXPECTED_MC1_FRIEND =
    "WM..................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "...................W\n";

  private static final String EXPECTED_MC2_FRIEND =
    "W...................\n"
      + "M...................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "....................\n"
      + "...................W\n";

  public void testTypicalMagicWoodWithFriend2() {
    final Team team = new Team();
    final World world = new World(20, 20, team);
    final CastMaster castMaster = new CastMaster(world);
    final Wizard wiz1 = world.getWizardManager().getWizard(1);
    wiz1.setState(State.ACTIVE);
    world.getCell(0, 0).push(wiz1);
    final Wizard wiz2 = world.getWizardManager().getWizard(2);
    wiz2.setState(State.ACTIVE);
    world.getCell(19, 19).push(wiz2);
    team.setTeam(1, 2);
    assertTrue(IntelligentMeditationCaster.cast(world, castMaster, wiz1, world.getCell(0, 0), new MagicCastle(), 1));
    final String s = dumpWorld(world);
    assertTrue(EXPECTED_MC1_FRIEND.equals(s) || EXPECTED_MC2_FRIEND.equals(s));
  }

}
