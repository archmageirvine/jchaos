package chaos.graphics;

import chaos.Chaos;
import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.MagicCastle;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import chaos.engine.AiEngine;
import chaos.engine.HumanEngine;
import chaos.sound.Sound;
import junit.framework.TestCase;

/**
 * Tests for corresponding class.
 * @author Sean A. Irvine
 */
public class SoundLevelTest extends TestCase {

  private int mLevel;

  @Override
  public void setUp() {
    mLevel = Sound.getSoundEngine().getSoundLevel();
  }

  @Override
  public void tearDown() {
    Sound.getSoundEngine().setSoundLevel(mLevel);
  }

  public void test() {
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_INTELLIGENT);
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(null, (Actor) null));
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    Chaos.getChaos().getWorld().getWizardManager().setWizard(1, w1);
    final Wizard2 w2 = new Wizard2();
    w2.setState(State.ACTIVE);
    Chaos.getChaos().getWorld().getWizardManager().setWizard(2, w2);
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(w1, w2));
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(w2, w1));
    w1.setPlayerEngine(new AiEngine(Chaos.getChaos().getWorld(), Chaos.getChaos().getMoveMaster(), Chaos.getChaos().getCastMaster()));
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(w1, w2));
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(w2, w1));
    w2.setPlayerEngine(new HumanEngine(Chaos.getChaos(), 5));
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(w1, w2));
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(w2, w1));
    final Horse h2 = new Horse();
    h2.setOwner(w2.getOwner());
    h2.setMount(w2);
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(w1, h2));
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(h2, w1));
    final Horse h1 = new Horse();
    h1.setOwner(w1.getOwner());
    h1.setMount(w1);
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(h1, h2));
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(h2, h1));
    final MagicCastle m = new MagicCastle();
    m.setOwner(w1.getOwner());
    m.setMount(w2);
    assertEquals(Sound.SOUND_ALL, SoundLevel.whatSoundLevel(h1, m));
    assertEquals(Sound.SOUND_INTELLIGENT, SoundLevel.whatSoundLevel(m, h1));
  }
}
