package GuitarPro;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TotalSineDraw extends JPanel
{
	/*
	Ta klasa jest niemal¿e uproszczon¹ kopi¹ klasy SineDraw - uproszczon¹ dlatego, ¿e rysujemy tylko
	jedn¹ falê a nie 3. S¹dzê wiêc, ¿e do ostatniej metody zrozumienie tego nie bêdzie dla Ciebie trudne.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7565247492366964272L;
	private static final int scalefactor = 50;
	private int[] cycles = new int[3];
	private int[] height = new int[3];
	private int[] start = new int[3];
	private double[] amp = new double[3];
	
	private int index;
	private double[] sines;
	private int[] pts;
	private double[] superPosition;
	
	BufferedImage bi;
	Graphics2D ig2;
	
	public TotalSineDraw()
	{
		setCycles(0, 0);
		setAmplitude(0, 0);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setBackground(Color.BLACK);
		int maxWidth = getWidth();
		double hstep = (double) maxWidth / (double) Audio.SAMPLE_RATE;
		int maxHeight = getHeight();
        pts = new int[Audio.SAMPLE_RATE];
        
        bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        ig2 = bi.createGraphics();
        
        for (int i = 0; i < Audio.SAMPLE_RATE; ++i) 
        {
        	pts[i] = (int) (sines[i] * maxHeight / 2 * 0.95 + maxHeight / 2);
        }
        
        g.setColor(Color.CYAN);
        ig2.setPaint(Color.CYAN);
        
        for (int i = 1; i < Audio.SAMPLE_RATE; ++i) 
        {
        	int x1 = (int) ((i - 1) * hstep);
            int x2 = (int) (i * hstep);
            int y1 = pts[i - 1];
            int y2 = pts[i];
            g.drawLine(x1, y1, x2, y2);
            ig2.drawLine(x1, y1, x2, y2);
        }
        
	}
	
	public void setCycles(int newCycles, int newIndex)
	{
		index = newIndex;
		cycles[index] = newCycles;
		repaintPlot();
	}
	
	public void setAmplitude(int newHeight, int newIndex)
	{
		index = newIndex;
		height[index] = newHeight;
		repaintPlot();
	}
	
	public void setPhase(int newPhase, int newIndex)
	{
		index = newIndex;
		start[index] = newPhase;
		repaintPlot();
	}
	
	private static double[] superPosition(double hz, double amplitude, double phase) 
	{
		int N = (int) (Audio.SAMPLE_RATE);
		double[] a = new double[N+1];
		for (int i = 0; i <= N; i++)
			a[i] = amplitude * Math.sin(2 * Math.PI/scalefactor * i * hz / Audio.SAMPLE_RATE + phase/(57.29712));
		return a;
	}
	
	public void repaintPlot()
	{
		
		for(int i = 0; i < 3; ++i)
		{
			amp[i] = (double) height[i]/100;
		} 
		/*
		 Tutaj nastêpuje skalowanie amplitudy ka¿dej z fal, tak, ¿eby wszystkie 3 ustawione na max
		 dawa³y najwiêksz¹ amplitudê wypadkow¹ zajmuj¹c¹ ca³e okienko.
		 */
		
		sines = superPosition(cycles[0], amp[0], start[0]);
		
		for(int i = 1; i < cycles.length; ++i) 
	    {
	        superPosition = superPosition(cycles[i], amp[i], start[i]);

	        for(int j = 0; j < sines.length; ++j) 
	        {
	            sines[j] += superPosition[j];
	        }
	    }

	    for(int i = 0; i < sines.length; ++i) 
	    {
	        sines[i] /= cycles.length;
	    }
		
		repaint();
	}
	
	public BufferedImage getImage()
	{
		return bi;
	}
	
}
