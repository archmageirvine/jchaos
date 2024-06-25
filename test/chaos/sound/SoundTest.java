package chaos.sound;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import chaos.util.BooleanLock;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * JUnit tests for the Sound class.
 * @author Sean A. Irvine
 */
public class SoundTest extends TestCase {


  public void testIdempotence() {
    assertNotNull(Sound.getSoundEngine());
    assertEquals(Sound.getSoundEngine(), Sound.getSoundEngine());
  }

  public void testLength() {
    final Sound s = Sound.getSoundEngine();
    assertEquals(0, s.getMillisecondLength(null));
    assertEquals(0, s.getMillisecondLength(""));
    assertEquals(0, s.getMillisecondLength("no-such-file"));
    assertEquals(0, s.getMillisecondLength("chaos/sound/SoundTest.class"));
    assertEquals(155, s.getMillisecondLength("chaos/sound/swoosh"));
  }

  private static final SoundClipCache CACHE = new SoundClipCache(10000);

  private boolean audioOk() {
    try {
      ((Clip) AudioSystem.getLine(new Line.Info(Clip.class))).open(CACHE.getClip("chaos/sound/swoosh"));
      return true;
    } catch (final LineUnavailableException | UnsupportedAudioFileException e) {
      return false;
    } catch (final IOException e) {
      System.err.println(e.getMessage());
      return false;
    }
  }

  public void testGetSetLevel() {
    assertEquals(0, Sound.SOUND_NONE);
    assertEquals(1, Sound.SOUND_INTELLIGENT);
    assertEquals(2, Sound.SOUND_ALL);
    final Sound s = Sound.getSoundEngine();
    s.setSoundLevel(Sound.SOUND_INTELLIGENT);
    assertEquals(Sound.SOUND_INTELLIGENT, s.getSoundLevel());
    try {
      s.setSoundLevel(-1);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    assertEquals(Sound.SOUND_INTELLIGENT, s.getSoundLevel());
    try {
      s.setSoundLevel(10);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    assertEquals(Sound.SOUND_INTELLIGENT, s.getSoundLevel());
    s.setSoundLevel(Sound.SOUND_NONE);
    assertEquals(Sound.SOUND_NONE, s.getSoundLevel());
    s.setSoundLevel(Sound.SOUND_ALL);
    assertEquals(Sound.SOUND_ALL, s.getSoundLevel());
    s.setSoundLevel(Sound.SOUND_INTELLIGENT);
    assertEquals(Sound.SOUND_INTELLIGENT, s.getSoundLevel());
  }

  public void testPlay() {
    final Sound s = Sound.getSoundEngine();
    final int l = s.getMillisecondLength("chaos/sound/swoosh");
    assertTrue(l > 150);
    assertTrue(l < 160);
    if (s.isSoundAvailable() && audioOk()) {
      s.setSoundLevel(Sound.SOUND_ALL);
      long time = System.currentTimeMillis();
      BooleanLock ss = s.play("chaos/sound/swoosh", Sound.SOUND_NONE, 0.1, 0);
      assertNotNull(ss);
      s.wait(ss);
      time = System.currentTimeMillis() - time;
      assertTrue(String.valueOf(time), time > 100);
      assertTrue(time < 1000);
      time = System.currentTimeMillis();
      ss = s.play("chaos/sound/swoosh", Sound.SOUND_NONE, 0.1, -1);
      assertNotNull(ss);
      s.wait(ss);
      time = System.currentTimeMillis() - time;
      assertTrue(time > 100);
      assertTrue(time < 1000);
      time = System.currentTimeMillis();
      ss = s.play("chaos/sound/swoosh", Sound.SOUND_NONE, 0.1, 1);
      assertNotNull(ss);
      s.wait(ss);
      time = System.currentTimeMillis() - time;
      assertTrue(time > 100);
      assertTrue(time < 1000);
      time = System.currentTimeMillis();
      ss = s.play("chaos/sound/swoosh", Sound.SOUND_NONE, 0.2, 0);
      assertNotNull(ss);
      s.wait(ss);
      time = System.currentTimeMillis() - time;
      assertTrue(time > 100);
      assertTrue(time < 1000);
      time = System.currentTimeMillis();
      ss = s.play("chaos/sound/swoosh", Sound.SOUND_NONE, 0.3);
      assertNotNull(ss);
      s.wait(ss);
      time = System.currentTimeMillis() - time;
      assertTrue(time > 100);
      assertTrue(time < 1000);
    }
  }

  private long simulPlay(final Sound s) {
    final long time = System.currentTimeMillis();
    final BooleanLock ss2 = s.play("chaos/sound/bigswoosh", Sound.SOUND_NONE, 0.8);
    s.playwait("chaos/sound/swoosh", Sound.SOUND_NONE);
    s.wait(ss2);
    return System.currentTimeMillis() - time;
  }

  public void testSimulPlay() {
    final Sound s = Sound.getSoundEngine();
    if (s.isSoundAvailable() && audioOk()) {
      //final int a = s.getMillisecondLength("chaos/sound/bigswoosh");
      //final int b = s.getMillisecondLength("chaos/sound/swoosh");
      // Time sequential play
      // Do an initial unplayed timing just to make sure things get loaded
      final long t0 = System.currentTimeMillis();
      s.playwait("chaos/sound/bigswoosh", Sound.SOUND_NONE);
      s.playwait("chaos/sound/swoosh", Sound.SOUND_NONE);
      final long seq = System.currentTimeMillis() - t0;
      // Run the actual concurrent play timing test, do it a few times since point failure is possible
      for (int k = 0; k < 5; ++k) {
        if (simulPlay(s) < seq) {
          return; // Found proof
        }
      }
      fail("Simul play appeared no faster");
    }
  }

  public void testWait() {
    // wait on null should take next to no time
    final Sound s = Sound.getSoundEngine();
    long time = System.currentTimeMillis();
    s.wait(null);
    assertTrue(System.currentTimeMillis() - time < 100);
    time = System.currentTimeMillis();
    s.wait(null, 200);
    assertTrue(System.currentTimeMillis() - time < 100);
  }

  public void testSimulatedBless() {
    final Random r = new Random();
    final int d = 20;
    final int time = 1 + 2000 / d;
    Sound.getSoundEngine().startSynthetic();
    for (int k = 0; k < d; ++k) {
      final int freq = 600 + 4 * r.nextInt(100);
      Sound.getSoundEngine().playSynthetic(freq, 1.0, time, Sound.SOUND_NONE);
    }
    Sound.getSoundEngine().stopSynthetic();
  }

  private static class WrappedSourceDataLine implements SourceDataLine {

    private final StringBuilder mBuf = new StringBuilder();

    @Override
    public void open(final AudioFormat f) {
    }

    @Override
    public void open(final AudioFormat f, final int size) {
    }

    private void rle(final byte[] b, final int off, final int len) {
      int last = 0;
      int count = 0;
      mBuf.append('[');
      for (int k = 0; k < len; ++k) {
        if (b[k + off] != last) {
          if (count > 0) {
            mBuf.append(last).append('{').append(count).append("},");
          }
          last = b[k + off];
          count = 1;
        } else {
          ++count;
        }
      }
      if (count > 0) {
        mBuf.append(last).append('{').append(count).append("},");
      }
      mBuf.append(']');
    }

    @Override
    public int write(final byte[] b, final int off, final int len) {
      rle(b, off, len);
      return len;
    }

    @Override
    public int available() {
      return 1;
    }

    @Override
    public int getBufferSize() {
      return 1;
    }

    @Override
    public int getFramePosition() {
      return 0;
    }

    @Override
    public void drain() {
      mBuf.append("drain,");
    }

    @Override
    public void flush() {
      mBuf.append("flush,");
    }

    @Override
    public float getLevel() {
      return 1;
    }

    @Override
    public AudioFormat getFormat() {
      return null;
    }

    @Override
    public long getLongFramePosition() {
      return 0;
    }

    @Override
    public long getMicrosecondPosition() {
      return 0;
    }

    @Override
    public boolean isActive() {
      return true;
    }

    @Override
    public boolean isRunning() {
      return true;
    }

    @Override
    public boolean isOpen() {
      return true;
    }

    @Override
    public void start() {
      mBuf.append("start,");
    }

    @Override
    public void stop() {
      mBuf.append("stop,");
    }

    @Override
    public void close() {
      mBuf.append("close,");
    }

    @Override
    public void open() {
    }

    @Override
    public void addLineListener(final LineListener listener) {
    }

    @Override
    public void removeLineListener(final LineListener listener) {
    }

    @Override
    public Control getControl(final Control.Type control) {
      return null;
    }

    @Override
    public boolean isControlSupported(final Control.Type control) {
      return true;
    }

    @Override
    public Control[] getControls() {
      return null;
    }

    @Override
    public Line.Info getLineInfo() {
      return null;
    }

    @Override
    public String toString() {
      return mBuf.toString();
    }
  }

  public void testWithProxy() {
    final Sound s = Sound.getSoundEngine();
    final Object o = TestUtils.getField("mLine", s);
    final WrappedSourceDataLine wrap = new WrappedSourceDataLine();
    try {
      TestUtils.setField("mLine", s, wrap);
      s.startSynthetic();
      s.playSynthetic(2000, 1.0, 10, Sound.SOUND_NONE);
      s.stopSynthetic();
    } finally {
      TestUtils.setField("mLine", s, o);
    }
    assertEquals("start,[0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},][0{4},47{1},69{1},47{1},69{1},103{1},116{1},103{1},116{1},-86{1},126{1},-86{1},126{1},-74{1},96{1},-74{1},96{1},13{1},36{1},13{1},36{1},-13{1},-37{1},-13{1},-37{1},74{1},-97{1},74{1},-97{1},86{1},-127{1},86{1},-127{1},-103{1},-117{1},-103{1},-117{1},-47{1},-70{1},-47{1},-70{1},]drain,stop,", wrap.toString());
  }

}
