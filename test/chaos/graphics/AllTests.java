package chaos.graphics;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ActiveTileManagerTest.class);
    suite.addTestSuite(AnimatorTest.class);
    suite.addTestSuite(AttackEffectTest.class);
    suite.addTestSuite(AttackTest.class);
    suite.addTestSuite(BeamTest.class);
    suite.addTestSuite(BombEffectTest.class);
    suite.addTestSuite(ChaosFontsTest.class);
    suite.addTestSuite(CollapseEffectTest.class);
    suite.addTestSuite(CursorNameTest.class);
    suite.addTestSuite(CursorsTest.class);
    suite.addTestSuite(DrawCastingTilesTest.class);
    suite.addTestSuite(DummyEffectTest.class);
    suite.addTestSuite(DummyTileManagerTest.class);
    suite.addTestSuite(EffectArrayTest.class);
    suite.addTestSuite(GenericScoreDisplayTest.class);
    suite.addTestSuite(GenericScreenTest.class);
    suite.addTestSuite(HandleExitTest.class);
    suite.addTestSuite(HighlightUtilsTest.class);
    suite.addTestSuite(ImageListExplodeEffectTest.class);
    suite.addTestSuite(ImageListPlotterTest.class);
    suite.addTestSuite(ImageListTest.class);
    suite.addTestSuite(ImageLoaderTest.class);
    suite.addTestSuite(InfoDisplayTest.class);
    suite.addTestSuite(InformationPanelTest.class);
    suite.addTestSuite(LightningWeaponTest.class);
    suite.addTestSuite(OwnerChangeEffectTest.class);
    suite.addTestSuite(ParabolaTest.class);
    suite.addTestSuite(PentagramTest.class);
    suite.addTestSuite(PentagramHighlighterTest.class);
    suite.addTestSuite(PlasmaWeaponTest.class);
    suite.addTestSuite(RangedCombatGraphicsTest.class);
    suite.addTestSuite(RealmChangeEffectTest.class);
    suite.addTestSuite(RenderTest.class);
    suite.addTestSuite(ShieldEffectTest.class);
    suite.addTestSuite(SoundLevelTest.class);
    suite.addTestSuite(SpungerEffectTest.class);
    suite.addTestSuite(TextScoreDisplayTest.class);
    suite.addTestSuite(TileManagerFactoryTest.class);
    suite.addTestSuite(TileSetTest.class);
    suite.addTestSuite(UnshieldEffectTest.class);
    suite.addTestSuite(SeamlessTilingTest.class);
    suite.addTestSuite(WaiterTest.class);
    suite.addTestSuite(WeaponEffectHandlerTest.class);
    suite.addTestSuite(WizardExplodeEffectTest.class);
    suite.addTestSuite(chaos.graphics.active16.Active16TileManagerTest.class);
    suite.addTestSuite(chaos.graphics.active32.Active32TileManagerTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
