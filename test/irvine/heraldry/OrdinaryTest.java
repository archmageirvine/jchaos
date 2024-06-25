package irvine.heraldry;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class OrdinaryTest extends TestCase {

  public void testOrdinary() {
    TestUtils.testEnum(Ordinary.class, "[ANNULET, BARRY, BARS, BEND, BEND_SINISTER, BENDLETS, BENDLETS_SINISTER, BENDY, BILLETS, BORDURE, CANTON, CHEVRONELS, CHIEF, CROSS, FESS, FLANCHES, FUSIL, GYRONNY, INESCUTCHEON, NONE, ORLE, PALE, PALLETS, PALY, PARTY_PER_BEND, PARTY_PER_BEND_SINISTER, PARTY_PER_CHEVRON, PARTY_PER_CROSS, PARTY_PER_FESS, PARTY_PER_PALE, PARTY_PER_SALTIRE, PILE, RUSTRE, SALTIRE]");
  }
}

