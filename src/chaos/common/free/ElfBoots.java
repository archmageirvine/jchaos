package chaos.common.free;

import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;

/**
 * Elf boots.
 *
 * @author Sean A. Irvine
 */
public class ElfBoots extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return Attribute.AGILITY.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.AGILITY;
  }
}
