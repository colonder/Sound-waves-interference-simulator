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
	 Ja to nazywam wspó³czynnik g³adkoœci. Mówi to tyle, jak du¿o punktów na cykl rysuje program, 
	 to znaczy czy sinusoida jest bardziej trójk¹tna czyli taki zygzak, czy bardziej jednak jak sinus. 
	 */
	private int maxWidth, maxHeight;
	
	private int[] cycles = new int[3];
	private int[] height = new int[3];
	private int[] start = new int[3];
	/*
	Deklaracja tablic przechowuj¹cych wartoœci czêstotliwoœci (cycles), amplitud (height) oraz
	faz (start)
	 */
	
	private double hstep[] = new double[3]; //tablica przechowuj¹ca odstêpy miêdzy kolejnymi punktami
	private double[][] radians = new double[3][]; //macierz przechowuj¹ca wartoœci k¹tów podczas obliczania sinusa
	private int index; //zmienna mówi¹ca która fala jest aktualnie wybrana - przekazywana przez parametr funkcji
	private double[][] sines = new double[3][]; //macierz przechowuj¹ca obliczone wartoœci sinusa
	private int[][] pts = new int[3][]; 
	/*
	 Powy¿sza macierz jest to przechowywania wartoœci konkretnych y-ków dla konkretnych x-ów. Dla
	 komputera to dzia³a w ten sposób, ¿e on co odstêp miêdzy dwoma punktami oblicza nowy x, oblicza
	 nowy k¹t, now¹ wartoœæ sinusa i przyporz¹dkowuje tê wartoœæ na osi y.
	 */
	private static final Color colors[] = {Color.RED, Color.GREEN, Color.YELLOW};
	//talica kolorów jakie maj¹ wykresy
	
	BufferedImage bi;
	Graphics2D ig2; 
	
	public SineDraw()
	{
		setCycles(1, 0);
		setAmplitude(1, 0);
	}
	
	/*
	To jest metoda startowa. Wywo³uje siê za ka¿dym razem kiedy program sie uruchamia. Ustawia ona 
	wartoœæ czêstotliwoœci dla fali 1 na 0 i tak samo amplitude.
	 */
	
	/*
	Teraz bêd¹ metody set, które wywo³ywane by³y w klasie Sounds - jak sama nazwa wskazuje
	ustawiaj¹ one wartoœci f, A i fi. Ka¿da z nich ma dwa parametry, pierwszy z nich mówi jaka
	jest nowa wartoœæ zmiennej new... i zmienna ta zostaje zapisana do odpowiedniej tablicy o podanym
	indeksie index. W klasie Sounds wygl¹da³o to tak:
	sines.setCycles(freqV[waveCount], waveCount);
	 */
	
	public void setCycles(int newCycles, int newIndex)
	{
		index = newIndex;
		cycles[index] = newCycles;
		repaintPlot(); //funkcja obliczaj¹ca nowego sinusa na podstawie nowych wartoœci
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
		Wczeœniej zadeklarowaliœmy macierze
		sines = new double[3][];
		radians = new double[3][];
		Czyli zrobiliœmy macierze 3 na...no w³aœnie, ile? Powy¿ej w³aœnie to definiujemy. Ka¿da kolumna
		zawiera w sobie iloœæ wierszy równa czêstotliwoœci dla danej fali.
		 */
		
        for (int i = 0; i < cycles[index]; ++i) 
        {
        	radians[index][i] = (Math.PI / scalefactor) * i;
        	sines[index][i] = (double) height[index]/100*Math.sin(radians[index][i] + (double) start[index]/(57.29712));
        }	
		
		repaint();
		
		/*
		Powy¿ej funkcja for obliczaj¹ca wartoœci k¹tów i odpowiadaj¹ce im wartoœci sinusów dla ka¿ego "i"
		a¿ osi¹gniêta zostanie wartoœæ czêstotliwoœci. Normalnie by³oby to ma³o punktów dla niskich 
		czêstotliwoœci, dlatego iloœæ tych punktów zwiêkszona zostaje poprzez wspó³czynnik g³adkoœci - 
		dzielenie przez niego. Nastêpnie wywo³ywana jest funkcja rysuj¹ca.
		 */
	}

	public void paintComponent(Graphics g) //to z zajêæ kojarzyæ powinieneœ
	{
		super.paintComponent(g); //konstruktor klasy nadrzêdnej, ¿eby mo¿na by³o spokojnie korzystaæ z metod tej klasy
		setBackground(Color.BLACK); //ustawiamy t³o na czarne
		maxWidth = getWidth(); //pobieramy szerokoœæ okna, w którym rysujemy (tego czarnego)
		hstep[index] = ((double) maxWidth / (double) cycles[index]); //obliczamy odstêp czyli "krok ca³kowania"
		maxHeight = getHeight(); //pobieramy wysokoœæ okna, w którym rysujemy
        pts[index] = new int[cycles[index]]; //ponownie tworzymy macierz 3 x czêstotliwoœæ
        
        for (int i = 0; i < cycles[index]; ++i) 
        {
        	pts[index][i] = (int) (sines[index][i] * maxHeight / 2 * 0.95 + maxHeight / 2);
        }
        
        /*
        Powy¿ej nastêpuje proces zagêszczania wykresu w miarê zwiêkszania czêstotliwoœci
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
        Powy¿sza funkcja realizuje rysowanie wszystkich 3 funkcji na raz. Nie wiedzia³em jak zrobiæ
        tak, ¿eby rysowa³a siê tylko jedna, wybrana, wiêc rysuj¹ siê 3 na raz. Dzia³a to w ten sposób:
        pêtla nadrzêdna zawiera jedynie ustawienie koloru z tablicy. Pêtla zaczyna od indeksu j = 0, wiêc
        ustawiony zostaje kolor o tym indeksie - czerwony. Nastêpnie przechodzimy do pêtli
        zagnie¿d¿onej w której nastêpuje proces obliczania x-ów na podstawie kroku ca³kowania oraz y-ów
        dwóch kolejnych punktów. Proœciej? Liczymy wspó³rzêdne punktu 1 i punktu 2 a potem funkcj¹ 
        drawLine ³¹czymy je lini¹ prost¹ - im gêœciej s¹ upakowane te punkty i im ich wiêcej, tym
        g³adsza jest sinusoida.
         */
        
	}
	
	public BufferedImage getImage()
	{
		return bi;
	}
	
}
