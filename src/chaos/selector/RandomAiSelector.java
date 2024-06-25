package chaos.selector;

import chaos.common.Castable;
import chaos.util.Random;

/**
 * This selector just makes a random selection.
 * @author Sean A. Irvine
 */
public class RandomAiSelector implements Selector {

  @Override
  public Castable[] select(final Castable[] castables, final boolean texas) {
    if (castables == null || castables.length == 0) {
      return new Castable[] {null, null};
    }
    final int chosen = Random.nextInt(castables.length);
    if (castables.length > 1) {
      int discard;
      do {
        discard = Random.nextInt(castables.length);
      } while (discard == chosen);
      return new Castable[] {castables[chosen], castables[discard]};
    }
    return new Castable[] {castables[chosen], null};
  }

  @Override
  public Castable[] selectBonus(final Castable[] spells, final int count) {
    final Castable[] res = new Castable[count];
    for (int i = 0; i < res.length; ++i) {
      int j;
      do {
        j = Random.nextInt(spells.length);
      } while (spells[j] == null);
      res[i] = spells[j];
      spells[j] = null;
    }
    return res;
  }
}
