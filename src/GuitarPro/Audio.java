package GuitarPro;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
public final class Audio 
{

    public static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2;                // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;                // 16-bit audio
    private static final double MAX_16_BIT = Short.MAX_VALUE;     // 32,767
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;  
    private double[] chordBuffer;
    private double[] soundBuffer;

    // not-instantiable
    Audio() { }

    // static initializer
    static { init(); }

    // open up an audio stream
private static void init() 
{

try 
{
    // 44,100 samples per second, 16-bit audio, mono, signed PCM, little Endian
    AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

    line = (SourceDataLine) AudioSystem.getLine(info);
    line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);

    buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE*10];
}

catch (Exception e) 
{
    System.out.println(e.getMessage());
    System.exit(1);
}

	// no sound gets made before this call
	line.start();
}



/**
 * Close standard audio.
 */
public static void close() 
{
	line.drain();
	line.stop();
}

/**
 * Write one sample (between -1.0 and +1.0) to standard audio. If the sample
 * is outside the range, it will be clipped.
 */
public static void play(double in) 
{
	
	// clip if outside [-1, +1]
	if (in < -1.0) in = -1.0;
	if (in > +1.0) in = +1.0;
	
	// convert to bytes
	short s = (short) (MAX_16_BIT * in);
	buffer[bufferSize++] = (byte) s;
	buffer[bufferSize++] = (byte) (s >> 8);   // little Endian
	
	// send to sound card if buffer is full        
	if (bufferSize >= buffer.length) 
	{
	    line.write(buffer, 0, buffer.length);
	    bufferSize = 0;
	}
}

/**
 * Write an array of samples (between -1.0 and +1.0) to standard audio. If a     sample
 * is outside the range, it will be clipped.
 */
public static void play(double[] input) 
{
	for (int i = 0; i < input.length; i++) 
	{
	    play(input[i]);
	}
}

private static double[] tone(double hz, double duration, double amplitude, double phase) 
{
	int N = (int) (Audio.SAMPLE_RATE * duration);
	double[] a = new double[N+1];
	
	for (int i = 0; i <= N; i++)
		a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / Audio.SAMPLE_RATE + phase/(57.29712));
	
	return a;
}

	public void calcChord(double[] frequencies, double[] amplitudes, double[] phases) throws LineUnavailableException 
	{
	    chordBuffer = tone(frequencies[0], 1.0, amplitudes[0], phases[0]);

	    for(int i = 1; i < frequencies.length; ++i) 
	    {
	        double[] harmonic = tone(frequencies[i], 1.0, amplitudes[i], phases[i]);

	        for(int j = 0; j < chordBuffer.length; ++j) 
	        {
	        	chordBuffer[j] += harmonic[j];
	        }
	    }

	    for(int i = 0; i < chordBuffer.length; ++i) 
	    {
	    	chordBuffer[i] /= frequencies.length;
	    }

	}
	
	public void playSound(double f, double a, double p) throws LineUnavailableException
	{
		soundBuffer = tone(f, 1.0, a, p);
		
		for(int i = 0; i < soundBuffer.length; ++i)
		{
			soundBuffer[i] = (soundBuffer[i]);
		}
		
		Audio.play(soundBuffer);
	}
	
	public void playChord()
	{
		Audio.play(chordBuffer);
	}
	
	public static void save(double in) 
	{
		AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true, false);
		File fileOut = Sounds.file1;
		
		// clip if outside [-1, +1]
		if (in < -1.0) in = -1.0;
		if (in > +1.0) in = +1.0;
		
		// convert to bytes
		short s = (short) (MAX_16_BIT * in);
		buffer[bufferSize++] = (byte) s;
		buffer[bufferSize++] = (byte) (s >> 8);   // little Endian
		
		// send to sound card if buffer is full        
		if (bufferSize >= buffer.length) 
		{
			try 
			{
				AudioInputStream inStream = new AudioInputStream(new ByteArrayInputStream(buffer), format, buffer.length/format.getFrameSize());
				AudioSystem.write(inStream, AudioFileFormat.Type.WAVE, fileOut);
				bufferSize = 0;
			}
			
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void save(double[] input) 
	{
		for (int i = 0; i < input.length; i++) 
		{
			save(input[i]);
		}
	}
	
	public void saveChord()
	{
		Audio.save(chordBuffer);
	}
}