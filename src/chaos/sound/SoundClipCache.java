package chaos.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import irvine.util.io.IOUtils;


/**
 * Provides a cache for sound clips.  Provides streams for sound
 * clips and makes an attempt to remember the clip for future
 * use.  It will attempt to limit memory usage to a value
 * near the value given at construction time.  It does not directly
 * support any kind of sound play.
 * @author Sean A. Irvine
 */
public class SoundClipCache {

  /** Target maximum bytes for the cache. */
  private final int mMemoryLimit;
  /** Current memory usage. */
  private int mCurrentUsage = 0;
  /** Cached sounds. */
  private final Map<String, byte[]> mSounds = new LinkedHashMap<>();

  /**
   * Construct a new sound clip cache with the specified target
   * maximum memory usage.
   * @param memoryLimit target maximum memory use
   */
  public SoundClipCache(final int memoryLimit) {
    if (memoryLimit <= 0) {
      throw new IllegalArgumentException("Memory limit must be positive");
    }
    mMemoryLimit = memoryLimit;
  }

  /**
   * Return a stream for the specified sound resource.
   * @param name sound to be obtained
   * @return stream for the sound
   * @throws IOException if an I/O error occurs.
   * @throws UnsupportedAudioFileException if the audio file is not in an acceptable
   * format.
   */
  public AudioInputStream getClip(final String name) throws IOException, UnsupportedAudioFileException {
    byte[] noise = mSounds.remove(name);
    if (noise == null) {
      // attempt to loadModel the sound
      final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
      if (is == null) {
        // just in case the sound is missing
        return null;
      }
      try {
        try (final BufferedInputStream bis = new BufferedInputStream(is)) {
          noise = IOUtils.readData(bis);
        }
      } finally {
        is.close();
      }
      // check and update memory usage
      mCurrentUsage += noise.length;
      if (mCurrentUsage > mMemoryLimit) {
        // using too much memory, throw away the oldest entries
        for (final Iterator<byte[]> i = mSounds.values().iterator(); mCurrentUsage > mMemoryLimit && i.hasNext(); ) {
          mCurrentUsage -= i.next().length;
          i.remove();
        }
      }
    }
    mSounds.put(name, noise);
    final ByteArrayInputStream is = new ByteArrayInputStream(noise);
    try {
      return AudioSystem.getAudioInputStream(is);
    } catch (final UnsupportedAudioFileException e) {
      // remove the entry from the cache and rethrow the exception
      mCurrentUsage -= mSounds.remove(name).length;
      is.close();
      throw e;
    }
  }

  /**
   * Return the current memory usage of this cache in bytes.
   * @return memory usage
   */
  protected int getMemoryUsage() {
    return mCurrentUsage;
  }
}
