package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.BowShooter;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.Named;
import chaos.util.NameUtils;

/**
 * The named centaur.
 * @author Sean A. Irvine
 */
public class NamedCentaur extends MaterialMonsterMount implements BowShooter, Named {

  private String mPersonalName;

  private NamedCentaur(final String name) {
    setDefault(Attribute.LIFE, 18);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 30);
    setDefault(Attribute.MAGICAL_RESISTANCE, 48);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGE, 4);
    setDefault(Attribute.RANGED_COMBAT, 3);
    mPersonalName = name;
  }

  /**
   * Construct a named centaur with randomly selected name.
   */
  public NamedCentaur() {
    this(NameUtils.getPersonalName(NamedCentaur.class));
  }

  @Override
  public long getLosMask() {
    return 0x00E0F0FFFF773600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Centaur.class;
  }

  @Override
  public String getPersonalName() {
    return mPersonalName;
  }

  @Override
  public void setPersonalName(final String name) {
    mPersonalName = name;
  }
}
