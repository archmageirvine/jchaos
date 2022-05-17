package chaos.common.monster;

import chaos.Chaos;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.common.Named;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.util.NameUtils;
import chaos.util.Random;

/**
 * The named deity.
 * @author Sean A. Irvine
 */
public class NamedDeity extends Caster implements Named, NoDeadImage {

  private static final FrequencyTable SPELL_TABLE = new FrequencyTable("chaos/resources/deity-frequency.txt", false);

  private String mPersonalName;

  /**
   * Construct a deity with specified name.
   * @param name name of deity
   */
  public NamedDeity(final String name) {
    mPersonalName = name;
  }

  /** Construct a deity. */
  public NamedDeity() {
    this(NameUtils.getPersonalName(NamedDeity.class));
  }

  {
    setRealm(Realm.HYPERASTRAL);
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.LIFE_RECOVERY, 10);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 2);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    set(PowerUps.DEPTH, 1);
    set(PowerUps.CRYSTAL_BALL, 1);
    setPlayerEngine(Chaos.getChaos().getAI());
  }

  @Override
  public long getLosMask() {
    return 0x7FFFFFFFFFFFFFFFL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  @Override
  public String getPersonalName() {
    return mPersonalName;
  }

  @Override
  public Castable getCastable() {
    // Casts a spell 2/3 of the time
    return Random.nextInt(3) == 0 ? null : FrequencyTable.instantiate(SPELL_TABLE.getBonusRandom());
  }

  @Override
  public void setPersonalName(final String name) {
    mPersonalName = name;
  }
}
