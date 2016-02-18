package GuitarPro;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SineDraw extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6031997145939993793L;
	private static final int scalefactor = 25; 
	/*
	 Ja to nazywam wsp�czynnik g�adko�ci. M�wi to tyle, jak du�o punkt�w na cykl rysuje program, 
	 to znaczy czy sinusoida jest bardziej tr�jk�tna czyli taki zygzak, czy bardziej jednak jak sinus. 
	 */
	private int maxWidth, maxHeight;
	
	private int[] cycles = new int[3];
	private int[] height = new int[3];
	private int[] start = new int[3];
	/*
	Deklaracja tablic przechowuj�cych warto�ci cz�stotliwo�ci (cycles), amplitud (height) oraz
	faz (start)
	 */
	
	private double hstep[] = new double[3]; //tablica przechowuj�ca odst�py mi�dzy kolejnymi punktami
	private double[][] radians = new double[3][]; //macierz przechowuj�ca warto�ci k�t�w podczas obliczania sinusa
	private int index; //zmienna m�wi�ca kt�ra fala jest aktualnie wybrana - przekazywana przez parametr funkcji
	private double[][] sines = new double[3][]; //macierz przechowuj�ca obliczone warto�ci sinusa
	private int[][] pts = new int[3][]; 
	/*
	 Powy�sza macierz jest to przechowywania warto�ci konkretnych y-k�w dla konkretnych x-�w. Dla
	 komputera to dzia�a w ten spos�b, �e on co odst�p mi�dzy dwoma punktami oblicza nowy x, oblicza
	 nowy k�t, now� warto�� sinusa i przyporz�dkowuje t� warto�� na osi y.
	 */
	private static final Color colors[] = {Color.RED, Color.GREEN, Color.YELLOW};
	//talica kolor�w jakie maj� wykresy
	
	BufferedImage bi;
	Graphics2D ig2; 
	
	public SineDraw()
	{
		setCycles(1, 0);
		setAmplitude(1, 0);
	}
	
	/*
	To jest metoda startowa. Wywo�uje si� za ka�dym razem kiedy program sie uruchamia. Ustawia ona 
	warto�� cz�stotliwo�ci dla fali 1 na 0 i tak samo amplitude.
	 */
	
	/*
	Teraz b�d� metody set, kt�re wywo�ywane by�y w klasie Sounds - jak sama nazwa wskazuje
	ustawiaj� one warto�ci f, A i fi. Ka�da z nich ma dwa parametry, pierwszy z nich m�wi jaka
	jest nowa warto�� zmiennej new... i zmienna ta zostaje zapisana do odpowiedniej tablicy o podanym
	indeksie index. W klasie Sounds wygl�da�o to tak:
	sines.setCycles(freqV[waveCount], waveCount);
	 */
	
	public void setCycles(int newCycles, int newIndex)
	{
		index = newIndex;
		cycles[index] = newCycles;
		repaintPlot(); //funkcja obliczaj�ca nowego sinusa na podstawie nowych warto�ci
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
	
	public void repaintPlot()
	{
		sines[index] = new double[cycles[index]];
		radians[index] = new double[cycles[index]];
		/*
		Wcze�niej zadeklarowali�my macierze
		sines = new double[3][];
		radians = new double[3][];
		Czyli zrobili�my macierze 3 na...no w�a�nie, ile? Powy�ej w�a�nie to definiujemy. Ka�da kolumna
		zawiera w sobie ilo�� wierszy r�wna cz�stotliwo�ci dla danej fali.
		 */
		
        for (int i = 0; i < cycles[index]; ++i) 
        {
        	radians[index][i] = (Math.PI / scalefactor) * i;
        	sines[index][i] = (double) height[index]/100*Math.sin(radians[index][i] + (double) start[index]/(57.29712));
        }	
		
		repaint();
		
		/*
		Powy�ej funkcja for obliczaj�ca warto�ci k�t�w i odpowiadaj�ce im warto�ci sinus�w dla ka�ego "i"
		a� osi�gni�ta zostanie warto�� cz�stotliwo�ci. Normalnie by�oby to ma�o punkt�w dla niskich 
		cz�stotliwo�ci, dlatego ilo�� tych punkt�w zwi�kszona zostaje poprzez wsp�czynnik g�adko�ci - 
		dzielenie przez niego. Nast�pnie wywo�ywana jest funkcja rysuj�ca.
		 */
	}

	public void paintComponent(Graphics g) //to z zaj�� kojarzy� powiniene�
	{
		super.paintComponent(g); //konstruktor klasy nadrz�dnej, �eby mo�na by�o spokojnie korzysta� z metod tej klasy
		setBackground(Color.BLACK); //ustawiamy t�o na czarne
		maxWidth = getWidth(); //pobieramy szeroko�� okna, w kt�rym rysujemy (tego czarnego)
		hstep[index] = ((double) maxWidth / (double) cycles[index]); //obliczamy odst�p czyli "krok ca�kowania"
		maxHeight = getHeight(); //pobieramy wysoko�� okna, w kt�rym rysujemy
        pts[index] = new int[cycles[index]]; //ponownie tworzymy macierz 3 x cz�stotliwo��
        
        for (int i = 0; i < cycles[index]; ++i) 
        {
        	pts[index][i] = (int) (sines[index][i] * maxHeight / 2 * 0.95 + maxHeight / 2);
        }
        
        /*
        Powy�ej nast�puje proces zag�szczania wykresu w miar� zwi�kszania cz�stotliwo�ci
         */
        
        bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
        ig2 = bi.createGraphics();
        
        for (int j = 0; j < 3; ++j)
        {
        	g.setColor(colors[j]);
        	ig2.setPaint(colors[j]);
        	
        	for (int i = 1; i < cycles[j]; ++i) 
            {
            	int x1 = (int) ((i - 1) * hstep[j]);
                int x2 = (int) (i * hstep[j]);
                int y1 = pts[j][i - 1];
                int y2 = pts[j][i];
                g.drawLine(x1, y1, x2, y2);
                ig2.drawLine(x1, y1, x2, y2);
            }
        }
        
        
        
        /*
        Powy�sza funkcja realizuje rysowanie wszystkich 3 funkcji na raz. Nie wiedzia�em jak zrobi�
        tak, �eby rysowa�a si� tylko jedna, wybrana, wi�c rysuj� si� 3 na raz. Dzia�a to w ten spos�b:
        p�tla nadrz�dna zawiera jedynie ustawienie koloru z tablicy. P�tla zaczyna od indeksu j = 0, wi�c
        ustawiony zostaje kolor o tym indeksie - czerwony. Nast�pnie przechodzimy do p�tli
        zagnie�d�onej w kt�rej nast�puje proces obliczania x-�w na podstawie kroku ca�kowania oraz y-�w
        dw�ch kolejnych punkt�w. Pro�ciej? Liczymy wsp�rz�dne punktu 1 i punktu 2 a potem funkcj� 
        drawLine ��czymy je lini� prost� - im g�ciej s� upakowane te punkty i im ich wi�cej, tym
        g�adsza jest sinusoida.
         */
        
	}
	
	public BufferedImage getImage()
	{
		return bi;
	}
	
}
