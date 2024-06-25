package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;

import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.FireDemon;
import chaos.common.monster.Lion;
import chaos.common.monster.Skeleton;
import chaos.common.spell.Archery;

/**
 * Draw the tiles indicating items on which a spell can be cast.
 * @author Sean A. Irvine
 */
final class DrawCastingTiles {

  private DrawCastingTiles() {
  }

  private static final StandardWall WALL = new StandardWall();
  private static final Lion LION = new Lion();
  private static final Skeleton SKELETON = new Skeleton();
  private static final GooeyBlob BLOB = new GooeyBlob();
  private static final Archery NEED_LOS = new Archery();
  private static final RedDragon DRAGON = new RedDragon();
  private static final FireDemon DEMON = new FireDemon();
  private static final Lion DEAD_LION = new Lion();

  static {
    DEAD_LION.setState(State.DEAD);
  }

  static void drawCastableTiles(final Caster caster, final TileManager tm, final Graphics g, final int w, final int y, int x, final int castingFlags) {
    if ((castingFlags & Castable.CAST_LOS) != 0 && (caster == null || !caster.is(PowerUps.CRYSTAL_BALL))) {
      g.drawImage(tm.getSpellTile(NEED_LOS), x, y, null);
      x += w;
    }
    if ((castingFlags & Castable.CAST_EMPTY) != 0) {
      g.setColor(Color.BLACK);
      g.fillRect(x, y, w, w);
      x += w;
    }
    if ((castingFlags & Castable.CAST_DEAD) != 0) {
      g.drawImage(tm.getTile(DEAD_LION, 0, 0, 0), x, y, null);
      x += w;
    }
    if ((castingFlags & Castable.CAST_LIVING) != 0) {
      if ((castingFlags & Castable.CAST_MUSTBEUNDEAD) == 0) {
        g.drawImage(tm.getSpellTile(LION), x, y, null);
        x += w;
        g.drawImage(tm.getSpellTile(DRAGON), x, y, null);
        x += w;
        g.drawImage(tm.getSpellTile(DEMON), x, y, null);
        x += w;
      }
      g.drawImage(tm.getSpellTile(SKELETON), x, y, null);
      x += w;
    }
    if ((castingFlags & Castable.CAST_GROWTH) != 0) {
      g.drawImage(tm.getSpellTile(BLOB), x, y, null);
      x += w;
    }
    if ((castingFlags & Castable.CAST_INANIMATE) != 0) {
      g.drawImage(tm.getSpellTile(WALL), x, y, null);
      //          x += w;
    }
  }
}
