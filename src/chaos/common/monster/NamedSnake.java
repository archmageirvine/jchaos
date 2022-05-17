package chaos.common.monster;

import chaos.Chaos;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.common.Named;
import chaos.common.NoDeadImage;
import chaos.common.Realm;
import chaos.util.NameUtils;

/**
 * The named snake.
 * @author Sean A. Irvine
 */
public class NamedSnake extends Caster implements Named, NoDeadImage {

  private static final FrequencyTable SPELL_TABLE = new FrequencyTable("chaos/resources/snake-frequency.txt", false);

  private String mPersonalName;

  /**
   * Construct a deity with specified name.
   * @param name name of deity
   */
  public NamedSnake(final String name) {
    mPersonalName = name;
  }

  /** Construct a deity. */
  public NamedSnake() {
    this(NameUtils.getPersonalName(NamedSnake.class));
  }

  {
    setRealm(Realm.HYPERASTRAL);
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.LIFE_RECOVERY, 10);
    setDefault(Attribute.INTELLIGENCE, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 10);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    setPlayerEngine(Chaos.getChaos().getAI());
  }

  @Override
  public long getLosMask() {
    return 0x00001838182C1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return KingCobra.class;
  }

  @Override
  public String getPersonalName() {
    return mPersonalName;
  }

  @Override
  public Castable getCastable() {
    return FrequencyTable.instantiate(SPELL_TABLE.getBonusRandom());
  }

  @Override
  public void setPersonalName(final String name) {
    mPersonalName = name;
  }
}
