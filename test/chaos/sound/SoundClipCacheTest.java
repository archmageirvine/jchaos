package chaos.sound;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class SoundClipCacheTest extends TestCase {


  public void testBadConstruct() {
    try {
      new SoundClipCache(-1);
      fail("Bad memory");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      new SoundClipCache(0);
      fail("Bad memory");
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void testSmallMemory() throws IOException, UnsupportedAudioFileException {
    final SoundClipCache cache = new SoundClipCache(1024);
    assertEquals(0, cache.getMemoryUsage());
    // non-audio
    try {
      cache.getClip("chaos/sound/SoundClipCacheTest.class");
      fail("Accepted non-audio");
    } catch (final UnsupportedAudioFileException e) {
      // ok
    }
    // no such file
    assertNull(cache.getClip("chaos/sound/SoundClipCacheTest.clasx"));
    assertEquals(0, cache.getMemoryUsage());
    for (int i = 0; i < 10; ++i) {
      AudioInputStream ais = cache.getClip("chaos/sound/swoosh");
      assertNotNull(ais);
      int mem = cache.getMemoryUsage();
      assertTrue(mem >= 0);
      assertTrue(mem <= 3000);
      ais = cache.getClip("chaos/sound/bigswoosh");
      assertNotNull(ais);
      mem = cache.getMemoryUsage();
      assertTrue(mem >= 0);
      assertTrue(mem <= 3000);
    }
  }

  public void testBigMemory() throws IOException, UnsupportedAudioFileException {
    final SoundClipCache cache = new SoundClipCache(1000000);
    // non-audio
    try {
      cache.getClip("chaos/sound/SoundClipCacheTest.class");
      fail("Accepted non-audio");
    } catch (final UnsupportedAudioFileException e) {
      // ok
    }
    // no such file
    assertNull(cache.getClip("chaos/sound/SoundClipCacheTest.clasx"));
    for (int i = 0; i < 10; ++i) {
      AudioInputStream ais = cache.getClip("chaos/sound/swoosh");
      assertNotNull(ais);
      int mem = cache.getMemoryUsage();
      assertTrue(mem >= 0);
      assertTrue(mem <= 4000);
      ais = cache.getClip("chaos/sound/bigswoosh");
      assertNotNull(ais);
      mem = cache.getMemoryUsage();
      assertTrue(mem >= 0);
      assertTrue(mem <= 4000);
    }
  }

}
