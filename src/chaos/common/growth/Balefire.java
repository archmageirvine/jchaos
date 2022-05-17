package chaos.common.growth;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Realm;

/**
 * Balefire.
 * @author Sean A. Irvine
 */
public class Balefire extends Fire {

  {
    setRealm(Realm.ETHERIC);
    setDefault(Attribute.LIFE_RECOVERY, 3);
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Fire.class;
  }
}
