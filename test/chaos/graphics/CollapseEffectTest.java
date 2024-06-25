package chaos.graphics;

import chaos.board.World;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CollapseEffectTest extends OwnerChangeEffectTest {

  @Override
  String expected() {
    return "drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#";
  }

  @Override
  AbstractEffect effect(final World w) {
    return new CollapseEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null);
  }

}
