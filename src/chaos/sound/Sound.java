package chaos.sound;

import static irvine.math.r.Constants.TAU;
import static java.lang.Math.round;
import static java.lang.Math.sin;

import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import chaos.util.BooleanLock;
import irvine.util.string.StringUtils;

/**
 * Sound utility functions. Implemented as a singleton.<p>
 *
 * I have found the sound support on Linux under Java sucks.
 * I spent ages trying to use the various mixers and none of them seemed
 * to reliably function or contain all the functionality required. For
 * example, the older Java Sound Audio Engine has a severe bug in which
 * the end of sound play is reported prior to the sound actually having
 * finished -- sometimes so early that the sound has not yet even
 * started (4434125 in Java Bug Parade -- which is never going to be
 * fixed).  The "Direct Audio Device" cannot support more than 1 line on
 * my sound card and produces spurious messages about not been able to
 * handle formats even though I explicitly called the format checking
 * method prior to playing the sound!  It is also possible to read bytes
 * from audio streams after closing the stream (a problem I reported to
 * Sun but which they chose to ignore).<p>
 *
 * In the end I have opted to use dead reckoning to decide when a sound
 * should have finished and take the first mixer I find that supports
 * up to <code>MINIMUM_CLIP_LINES</code> lines.
 *
 * @author Sean A. Irvine
 */
public final class Sound {

  private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("chaos.debug", "false"));

  /** Constant indicating sound should be turned off. */
  public static final int SOUND_NONE = 0;
  /** Constant indicating intelligent sound. */
  public static final int SOUND_INTELLIGENT = 1;
  /** Constant indicating full sound. */
  public static final int SOUND_ALL = 2;

  /** The single instance of this class. */
  private static Sound sSingleton = null;
  private static final int MINIMUM_CLIP_LINES = 1;
  /** The sample cache. */
  private static final SoundClipCache CACHE = new SoundClipCache(1000000);
  /** Sample rate for note playing. */
  private static final int I_SAMPLE_RATE = 22050;
  private static final double SAMPLE_RATE = I_SAMPLE_RATE;

  private Mixer mMixer = null;
  private int mSoundLevel = SOUND_INTELLIGENT;
  /* Used for playing notes. */
  private final SourceDataLine mLine;

  /** Get line to use for manually generated sound. */
  private static SourceDataLine getLine(final Mixer mixer) {
    final AudioFormat format =
      new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                      I_SAMPLE_RATE, // samples / sec
                      16,            // sample size in bits
                      2,             // stereo
                      4,             // frame size in bytes
                      I_SAMPLE_RATE, // frames / sec
                      false);        // little endian
    final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    if (!mixer.isLineSupported(info)) {
      if (DEBUG) {
        System.err.println("Line unsupported");
      }
      return null;
    }
    try {
      return (SourceDataLine) AudioSystem.getLine(info);
    } catch (final LineUnavailableException e) {
      return null;
    }
  }


  /**
   * Establishes the connection to the underlying sound system,
   * and selects the most appropriate mixer.  This may need to
   * be modified as more platforms and architectures are considered.
   */
  private Sound() {
    // attempt to get a mixer with the support we want
    final Mixer.Info[] mi = AudioSystem.getMixerInfo();
    if (mi != null) {
      if (DEBUG) {
        StringUtils.message("Total mixers: " + mi.length);
      }
      for (final Mixer.Info mii : mi) {
        if (DEBUG) {
          StringUtils.message("Mixer: " + mii);
        }
        final Mixer m = AudioSystem.getMixer(mii);
        final int lines = m.getMaxLines(new Line.Info(Clip.class));
        // The following seemed to break when I went from 1.6 to 1.7
        // Changing this to >= 1 enabled it work with the added bonus that
        // sound now worked mixed with normal music play.
        if (lines >= MINIMUM_CLIP_LINES || lines == AudioSystem.NOT_SPECIFIED) {
//          if (DEBUG) {
//            Line.Info[] li = m.getTargetLineInfo();
//            System.err.println(mii.toString() + " ");
//            for (final Line.Info aLi : li) {
//              System.err.println("  -> " + aLi);
//            }
//            li = m.getSourceLineInfo();
//            for (final Line.Info aLi : li) {
//              System.err.println("  <- " + aLi);
//              if (aLi instanceof DataLine.Info) {
//                final DataLine.Info dataLineInfo = (DataLine.Info) aLi;
//                for (final AudioFormat af : dataLineInfo.getFormats()) {
//                  System.err.println(af.toString());
//                }
//              }
//            }
//          }
          mMixer = m;
          SourceDataLine line = getLine(m);
          if (line != null) {
            try {
              line.open();
            } catch (final LineUnavailableException e) {
              line = null;
            }
          }
          if (line != null) {
            mLine = line;
            return;
          }
        }
      }
      System.err.println("No sound possible, no suitable mixer was found");
    }
    mLine = null;
  }

  @Override
  protected void finalize() {
    if (mLine != null) {
      mLine.drain();
      mLine.stop();
      mLine.close();
    }
  }

  /**
   * Provides the only way to get a handle to the sound engine.
   *
   * @return the sound engine
   */
  public static synchronized Sound getSoundEngine() {
    if (sSingleton == null) {
      sSingleton = new Sound();
    }
    return sSingleton;
  }

  /**
   * Determine whether or not sound is available.
   *
   * @return true if sound is available
   */
  public boolean isSoundAvailable() {
    return mMixer != null;
  }

  /**
   * Set the sound level.  One of <code>SOUND_NONE</code>,
   * <code>SOUND_INTELLIGENT</code>, <code>SOUND_ALL</code>.
   *
   * @param level sound level
   * @exception IllegalArgumentException if the given level is invalid.
   */
  public void setSoundLevel(final int level) {
    if (level != SOUND_NONE && level != SOUND_INTELLIGENT && level != SOUND_ALL) {
      throw new IllegalArgumentException();
    }
    mSoundLevel = level;
  }

  /**
   * Return the current level of sound effects.  One of <code>SOUND_NONE</code>,
   * <code>SOUND_INTELLIGENT</code>, <code>SOUND_ALL</code>.
   *
   * @return sound level
   */
  public int getSoundLevel() {
    return mSoundLevel;
  }

  /** For speed we lazily precompute volumes in the decibel scale. */
  private static double[] sVolumeToDB = null;

  /**
   * Set the volume on this line.  This function converts from
   * a linear 'loudness' argument into a decibel rating.  The
   * supplied volume should be in the range [0,1], other values
   * will result in an exception.
   * If the line does not support volume modification then no
   * action is taken.
   *
   * @param line line to set volume for
   * @param volume volume to set
   */
  private static synchronized void setVolume(final Line line, final double volume) {
    if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
      final FloatControl vc = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
      if (sVolumeToDB == null) {
        // perform a one time computation to linearize the scale
        sVolumeToDB = new double[101];
        sVolumeToDB[0] = vc.getMinimum();
        for (int i = 1; i < 101; ++i) {
          sVolumeToDB[i] = Math.max(sVolumeToDB[0], 10 * Math.log(0.01 * i));
        }
      }
      vc.setValue((float) sVolumeToDB[(int) (volume * 100)]);
    }
  }

  /**
   * Set the sound position, see play().
   *
   * @param line line to set pan position for
   * @param pan pan value in [-1,1]
   */
  private static void setPan(final Line line, final double pan) {
    if (line.isControlSupported(FloatControl.Type.PAN)) {
      ((FloatControl) line.getControl(FloatControl.Type.PAN)).setValue((float) pan);
    }
  }

  /**
   * Get the length of specified clip in milliseconds.
   * @param name name of clip
   * @return length in milliseconds
   */
  public int getMillisecondLength(final String name) {
    try {
      if (name == null) {
        return 0;
      }
      final AudioInputStream noise = CACHE.getClip(name);
      if (noise == null) {
        if (DEBUG) {
          StringUtils.message("No sound called: " + name);
        }
        return 0;
      }
      return (int) ((1000 * noise.getFrameLength()) / noise.getFormat().getFrameRate());
    } catch (final UnsupportedAudioFileException | IOException e) {
      if (DEBUG) {
        StringUtils.message("Problem getting length of " + name);
      }
      return 0;
    }
  }

  private static final int BUFFER_SIZE = 4096;

  /**
   * Asynchronously launch a sound clip, playing it at the specified
   * volume.  If sound is not available, or there is some other
   * sound related problem null is returned.  The pan controls the
   * positioning of the sound in the sound field, a value of -1 is
   * the extreme left and +1 the extreme right. Behaviour for pan
   * values below -1 or above +1 is undefined.
   *
   * @param name the clip to play
   * @param level the priority level of the sound
   * @param volume volume to play at 0 &lt;= volume &lt;= 1
   * @param pan pan aspect in range -1 &lt;= pan &lt;= 1
   * @return object to check status of the play
   */
  public BooleanLock play(final String name, final int level, final double volume, final double pan) {
    if (name == null) {
      return null;
    }
    if (mMixer != null && level <= getSoundLevel()) {
      final AudioInputStream noise;
      try {
        noise = CACHE.getClip(name);
      } catch (final UnsupportedAudioFileException | IOException e) {
        if (DEBUG) {
          System.err.println(e.getMessage());
        }
        return null;
      }
      if (noise == null) {
        // Just in case resource was missing
        return null;
      }
      final AudioFormat format = noise.getFormat();
      final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      if (!mMixer.isLineSupported(info)) {
        if (DEBUG) {
          System.err.println("Line unsupported");
        }
        return null;
      }
      try {
        if (DEBUG) {
          StringUtils.message("Attempting to play: " + name);
        }
        final SourceDataLine dataLine = (SourceDataLine) mMixer.getLine(info);
        final BooleanLock ss = new BooleanLock();
        dataLine.open();
        // these parameters must come after the open!
        setVolume(dataLine, volume);
        setPan(dataLine, pan);
        dataLine.start();
        new Thread(() -> {
          try {
            final byte[] samples = new byte[BUFFER_SIZE];
            int count;
            while ((count = noise.read(samples, 0, BUFFER_SIZE)) != -1) {
              dataLine.write(samples, 0, count);
            }
          } catch (final IOException e) {
            if (DEBUG) {
              StringUtils.message(e.getMessage());
            }
          }
          if (dataLine.isRunning()) {
            dataLine.drain();
          }
          ss.setValue(true);
          if (dataLine.isOpen()) {
            dataLine.close();
          }
        }).start();
        if (DEBUG) {
          StringUtils.message("Clip started, returning SoundStatus object");
        }
        return ss;
      } catch (final LineUnavailableException e) {
        if (DEBUG) {
          System.err.println(e.getMessage());
        }
      }
    }
    return null;
  }

  /**
   * Asynchronously launch a sound clip, playing it at the specified
   * volume.  If sound is not available, or there is some other
   * sound related problem null is returned.
   *
   * @param name the clip to play
   * @param level priority level of the sound
   * @param volume volume to play at 0 &lt;= volume &lt;= 1
   * @return object to check status of the play
   */
  public BooleanLock play(final String name, final int level, final double volume) {
    return play(name, level, volume, 0);
  }

  /**
   * Asynchronously launch a sound clip, playing it at its native
   * volume.  If sound is not available, or there is some other
   * sound related problem null is returned.
   *
   * @param name the clip to play
   * @param level priority level of the sound
   * @return object to check status of the play
   */
  public BooleanLock play(final String name, final int level) {
    return play(name, level, 1, 0);
  }

  /**
   * Convenience method to wait on a sound to finish.  It is generally easier
   * to use this method because it copes with the possibility of null input.
   *
   * @param status the sound status to wait on
   * @param msTimeout maximum time to wait in milliseconds
   */
  public void wait(final BooleanLock status, final long msTimeout) {
    if (status != null) {
      try {
        status.waitUntilTrue(msTimeout);
      } catch (final InterruptedException e) {
        // ok
      }
    }
  }

  /**
   * Convenience method to wait on a sound to finish.  It is generally easier
   * to use this method because it copes with the possibility of null input.
   *
   * @param status the sound status to wait on
   */
  public void wait(final BooleanLock status) {
    wait(status, 0);
  }

  /**
   * Convenience play method, which plays and waits for the sound
   * to finish before returning.
   *
   * @param name the clip to play
   * @param level priority level of sound
   */
  public void playwait(final String name, final int level) {
    wait(play(name, level, 1, 0));
  }

  private static final double MAX_AMPLITUDE = 32760;
  private final byte[] mBuffer = new byte[4 * I_SAMPLE_RATE]; // fix this

  /**
   * Play a note of the given frequency.
   *
   * @param freq frequency to play
   * @param volume volume
   * @param duration length of note
   * @param level priority level of sound
   */
  public void playSynthetic(final int freq, double volume, final int duration, final int level) {
    if (level <= getSoundLevel()) {
      if (mLine == null) {
        return;
      } else if (volume < 0) {
        volume = 0;
      } else if (volume > 1) {
        volume = 1;
      }
      volume *= MAX_AMPLITUDE;
      // prepare waveform
      final int numSamplesInWave = (int) round(SAMPLE_RATE / freq);
      int j = 0;
      for (int i = 0; i < numSamplesInWave; ++i) {
        final double sine = sin((TAU * i) / numSamplesInWave);
        final int sample = (int) (sine * volume);
        final byte lo = (byte) sample;
        final byte hi = (byte) (sample >> 8);
        mBuffer[j++] = lo;
        mBuffer[j++] = hi;
        mBuffer[j++] = lo;
        mBuffer[j++] = hi;
      }
      // write it
      for (int k = 0; k < duration; ++k) {
        for (int i = 0; i < j;) {
          i += mLine.write(mBuffer, i, j - i);
        }
      }
    }
  }

  /**
   * Start producing synthetic sound.
   */
  public void startSynthetic() {
    if (mLine != null) {
      mLine.start();
    }
  }

  /**
   * Stop producing synthetic sound.
   */
  public void stopSynthetic() {
    if (mLine != null) {
      mLine.drain();
      mLine.stop();
    }
  }

  /**
   * Noddy main.
   *
   * @param args see usage message
   */
  public static void main(final String[] args) {
    if (args.length == 0) {
      getSoundEngine().startSynthetic();
      getSoundEngine().playSynthetic(500, 1.0, 200, SOUND_NONE);
      for (int i = 1; i < 5; ++i) {
        for (int j = 50; j < 10000; j += 7) {
          getSoundEngine().playSynthetic(j, 1.0 - i / 10.0, 1, SOUND_NONE);
        }
      }
      getSoundEngine().stopSynthetic();
      try {
        Thread.sleep(60000);
      } catch (final InterruptedException e) {
        // too bad
      }
      //      System.err.println("USAGE: Sound [-x] [-s] resource*");
    } else if ("-s".equals(args[0])) {
      for (int i = 1; i < args.length; ++i) {
        System.out.println(getSoundEngine().getMillisecondLength(args[i]) + "\t" + args[i]);
      }
    } else if ("-b".equals(args[0])) {
      final Random r = new Random();
      final int lim = Integer.parseInt(args[1]);
      final int time = 1 + 1000 / lim;
      getSoundEngine().startSynthetic();
      for (int k = 0; k < lim; ++k) {
        final int freq = 200 + r.nextInt(400);
        getSoundEngine().playSynthetic(freq, 1.0, time, Sound.SOUND_NONE);
      }
      getSoundEngine().stopSynthetic();
    } else if ("-x".equals(args[0])) {
      getSoundEngine().startSynthetic();
      for (int i = 1; i < 5; ++i) {
        for (int j = 0; j < 1000; j += 10) {
          getSoundEngine().playSynthetic(2000 - j, 1.0, 4, Sound.SOUND_NONE);
          getSoundEngine().playSynthetic(1500 + j, 1.0, 2, Sound.SOUND_NONE);
          getSoundEngine().playSynthetic(2000 - j, 1.0, 4, Sound.SOUND_NONE);
        }
        for (int j = 0; j < 1000; j += 10) {
          getSoundEngine().playSynthetic(1000 + j, 1.0, 4, Sound.SOUND_NONE);
          getSoundEngine().playSynthetic(2500 - j, 1.0, 2, Sound.SOUND_NONE);
          getSoundEngine().playSynthetic(1000 + j, 1.0, 4, Sound.SOUND_NONE);
        }
      }
      try {
        Thread.sleep(1000);
      } catch (final InterruptedException e) {
        // too bad
      }
      getSoundEngine().stopSynthetic();
    } else {
      for (final String arg : args) {
        System.out.println(arg);
        getSoundEngine().playwait(arg, SOUND_NONE);
      }
    }
  }

  /** Play a simple ding, indicative of a failed action. */
  public static void ding() {
    getSoundEngine().playwait("chaos/resources/sound/misc/Ding", Sound.SOUND_INTELLIGENT);
  }

  /** Play a simple beep, indicative of a successful action. */
  public static void beep() {
    getSoundEngine().playwait("chaos/resources/sound/misc/Beep", Sound.SOUND_INTELLIGENT);
  }
}
