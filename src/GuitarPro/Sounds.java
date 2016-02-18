package GuitarPro;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Sounds implements ActionListener //implementujemy metody klasy wbudowanej ActionListener, żeby móc z niej swobodnie korzystać
{
	private JPanel soundPanel, slidersPanel, wavesPanel, componentWaves, resultWave, buttonPanel;
	private JSlider amplitudeSlider, frequencySlider, phaseSlider;
	private JTextField amplitudeValue, frequencyValue, phaseValue;
	int[] ampV = {0,0,0};
	int[] freqV = {0,0,0};
	int[] phV = {0,0,0};
	/*
	 Powyżej zadeklarowane są trzy tablice przechowujące wartości amplitudy, fazy, częstotliwości
	 dla każdej z nich. To chyba jasne, że jest to jakby macierz lub tabelka, gdzie po lewej,
	 czyli wiersze podpisane masz, do której fali są to parametry, a u góry czyli kolumny
	 to poszczególne parametry. Zauważ, że są to tablice jednowymiarowe, w sensie z jednym [],
	 bo nie potrzeba większych - dlatego napisałem, że te trzy tablice razem tworzą jakby macierz.
	 */
	
	int waveCount; //to jest licznik mówiący o tym, którą falę aktualnie mamy wybraną 1,2 czy 3

	private SineDraw sines = new SineDraw(); //te same triki co w klasie Main
	private TotalSineDraw totalSinus = new TotalSineDraw();
	private Audio audio = new Audio();
	
	public static JLabel amplitudeLabel, frequencyLabel, phaseLabel, componentWavesLabel, resultWaveLabel;
	public static JButton play, playTriade, saveImage, saveWave;
	public static String info_message;
	public static String err_message;
	public static String cancel_message;
	public static String internal_error;
	public static String wave_1;
	public static String wave_2;
	public static String wave_3;
	public static String err_label;
	public static String save_label;
	public static String info_label;
	public static String save_component;
	public static String save_super;
	/*
	Powyższe pola zadeklarowane zostały jako statyczne właśnie po to, żeby można sie było do nich
	odwoływać bezpośrednio w klasie MenuBar potrzebnej do zmiany języka, a nie żeby pisać get i set
	dla każdej. Ogarnij klasę MenuBar, żeby lepiej zrozumieć.
	 */
	
	private JComboBox<String> choice; //deklaracja rozwijanej listy do wyboru fali
	
	public static File file1, file2;
	/*
	 Tutaj zadeklarowany jest obiekt File - jak rozumiesz, plik - do którego będziemy zapisywać dźwięk.
	 Nie ma tutaj file = new File(), dlatego że wtedy stworzylibyśmy na stałe jakiś plik do którego
	 ciągle byśmy zapisywali, niezależnie od tego gdzie byśmy wskazali, żeby sie zapisał.
	 Dużo poniżej napotkasz się na definicję tego pliku poprzez specjalną funkcję, która dopiero
	 wtedy wskaże lokalizację dla docelowego pliku. Jest to obiekt statyczny, właśnie po to,
	 żeby można się było do niego bezpośrednio odwołać w innej klasie, ale to ogarniesz o co chodzi potem.
	 */
	
	public JPanel Sound()
	{
		
		//tutaj jest cała masa definicji zadeklarowanych wyżej obiektów
		
		amplitudeValue = new JTextField();
		frequencyValue = new JTextField();
		phaseValue = new JTextField();
		
		soundPanel = new JPanel();
		soundPanel.setLayout(new BorderLayout()); //ustawienie automatycznego layoutu
		
		slidersPanel = new JPanel();
		slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.PAGE_AXIS));
		
		/*
		Tutaj ustawiony jest layout dla panelu suwaków, żeby wszystko było jedno pod drugim.
		Zwrot PAGE_AXIS mówi właśnie tyle, co "ustaw elementy wzdłuż osi kartki/strony".
		 */
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		/*
		Jak widzisz każdy panel ma inny layout ustawiony, albo raczej każdy ma taki jak musi mieć,
		żeby to wszystko wyglądało jak wygląda. Poczytaj sobie w necie, popatrz jak wygląda i czym sie
		charakteryzuje każdy z layoutów, wtedy będziesz mniej więcej wiedział dlaczego ten ma tak, a 
		inny inaczej. Generalnie przypomnij sobie jak Ci mówiłem o tym, że całe okno ma strukturę
		pudełkową, czyli pudełko w pudełku w pudełku itd. Dlatego poszczególne panele mają swoje własne
		layouty, bo nie da sie ustawić jednego dla całego okna tak, żeby wszystko było jak trzeba.
		 */
		
		amplitudeSlider = new JSlider(0,100,0);
		amplitudeSlider.setPaintLabels(true);
		amplitudeSlider.setMajorTickSpacing(10);
		amplitudeSlider.setMinorTickSpacing(2);
		
		/*
		Deklaracja suwaka amplitudy. Parametry w new JSlider() to:
		1. wartość minimalna suwaka
		2. wartość maksymalna suwaka
		3. wartość na jaką ustawiony jest domyślnie suwak przy starcie programu
		Potem jest funkcja, która mówi "zrób widoczną podziałkę pod suwakiem". A poniżej jest powiedziane
		"ustaw większą kreskę podziałki co 10".
		 */
		
		frequencySlider = new JSlider(0,10000,0);
		frequencySlider.setPaintLabels(true);
		frequencySlider.setMajorTickSpacing(2000);
		frequencySlider.setMinorTickSpacing(100);
		
		phaseSlider = new JSlider(0,360,0);
		phaseSlider.setPaintLabels(true);
		phaseSlider.setMajorTickSpacing(30);
		phaseSlider.setMinorTickSpacing(10);
		
		amplitudeLabel = new JLabel();
		frequencyLabel = new JLabel();
		phaseLabel = new JLabel();
		componentWavesLabel = new JLabel();
		resultWaveLabel = new JLabel();
		
		play = new JButton();
		playTriade = new JButton();
		saveImage = new JButton();
		saveWave = new JButton();
		
		MenuBar.setLanguage("en");
		
		/*
		Stworzenie etykiet do każdego z suwaków z ich nazwami
		 */
		
		slidersPanel.add(amplitudeLabel);
		slidersPanel.add(amplitudeSlider);
		slidersPanel.add(amplitudeValue);
		slidersPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		slidersPanel.add(frequencyLabel);
		slidersPanel.add(frequencySlider);
		slidersPanel.add(frequencyValue);
		slidersPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		slidersPanel.add(phaseLabel);
		slidersPanel.add(phaseSlider);
		slidersPanel.add(phaseValue);
		slidersPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		/*
		Tutaj dodajemy wszystkie elemenety do panelu suwaków w takiej kolejności w jakiej mają być
		widoczne - to dlatego, że layout tego panelu był "jedno pod drugim". Polecenie
		Box.createRigidArea(new Dimension(0,10)) tworzy po prostu odstęp o szerokości x = 0 i wysokości
		y = 10 pikseli. Nic ponadto.
		 */
		
		buttonPanel.add(play);
		buttonPanel.add(playTriade);
		buttonPanel.add(saveImage);
		buttonPanel.add(saveWave);
		
		play.addActionListener(this);
		playTriade.addActionListener(this);
		saveImage.addActionListener(this);
		saveWave.addActionListener(this);
		
		/*
		Tutaj dodajemy masowo ActionListenery do guzików, żeby po kliknięciu każdego z nich działo się
		to co zaprogramujemy. Cały trik polega tutaj na tym, że piszemy this zamiast opisywać od razu
		każdy z osobna. This oznacza tyle co:
		obiekt.addActionListener(ten obiekt, do którego przypisujemy ActionListenera)
		Robimy tak dlatego, że na górze w deklaracji klasy mamy: 
		implements ActionListener
		a na dole tej strony jest metoda:
		@Override
		public void actionPerformed(ActionEvent e)
		Kiedy wciskamy dany guzik program widzi, że jest do niego przypisany action listener, ale nie
		zdefiniowany tutaj, tylko właśnie tam na dole, a this jakby tylko go "upewnia", że 
		konkretne działanie opisane na dole jest na pewno do tego guzika, który wcisnęliśmy. Wyczujesz to
		mam nadzieję.
		 */
		
		choice = new JComboBox<String>();
		choice.addItem(wave_1);
		choice.addItem(wave_2);
		choice.addItem(wave_3);
		choice.setEditable(false);	
		
		/*
		Dodajemy do tej rozwijanej listy z falami właśnie poszczególne pola z ich nazwami.
		setEditable(false) mówi tyle co: "nie pozwól, żeby użytkownik mógł zmienić nazwy tych pól".
		 */

	    choice.addActionListener(new ActionListener() //dodajemy action listenera do listy z falami
	    {
	      public void actionPerformed(ActionEvent e) 
	      {
	    	  waveCount = choice.getSelectedIndex();
	    	  /*
	    	  Rozwijana lista to jest troche jak tablica, w sensie każdy element w niej umieszczony
	    	  ma swój indeks przyporządkowany tak, w jakiej kolejności umieszczone zostały elemenety
	    	  w liscie, przy czym tak samo jak w tablicy elementy numerowane są od zera! Więc funckja
	    	  getSelectedIndex() mówi tyle co: "weź zobacz, który element został wybrany i zapisz do
	    	  zmiennej waveCount jej indeks".
	    	   */
	    	  amplitudeSlider.setValue(ampV[waveCount]);
	    	  frequencySlider.setValue(freqV[waveCount]);
	    	  phaseSlider.setValue(phV[waveCount]);
	    	  
	    	  /*
	    	  Tutaj przechodzimy do sedna sprawy. Ponieważ wyżej zapisaliśmy do zmiennej waveCount
	    	  indeks wybranego elementu teraz program wie, którą fale wybraliśmy. Powyższe komendy
	    	  ustawiają wartości suwaków na te wartości, jakie są zapisane w tablicach zadeklarowanych
	    	  dużo wyżej. I tak, za każdym razem kiedy klikniemy na inną falę, zmieni się indeks zapisany
	    	  w waveCount, co spowoduje, że suwak amplitudy ustawi się na wartość ampV[waveCount] i 
	    	  reszta tak samo. Nie spowoduje to zmiany samej wartości ampV[waveCount] tylko przestawi
	    	  suwak. Czujesz to mam nadzieję.
	    	   */
	      }
	    });
		
		buttonPanel.add(choice); //dodajemy tę listę do panelu guzików
		
		slidersPanel.add(buttonPanel); //dodajemy panel guzików do panelu suwaków
		
		/*
		Teraz nastąpi bardzo długa deklaracja różnych elementów dla każdego z suwaków, tak
		żeby działało, tak jak działa. Generalnie rzecz biorąc te wszystkie polecenia są
		skopiowane po prostu i w dużej mierze są jednakowe, dlatego opiszę tylko jedną z nich, bo reszta
		jest niemalże identyczna. Jak ogarniesz jedną to reszte też. Nie pomijaj jednak tych następnych
		deklaracji, bo są elementy dodatkowe albo różne od poprzednich i ich nie wyłapiesz jak przelecisz.
		 */
		
		amplitudeSlider.addMouseListener(new MouseListener()
		{
			
			/*
			Dodaliśmy do suwaka słuchacza myszki - w końcu chcemy móc przesuwać ten suwak i żeby to
			powodowało jakiś efekt. Teraz będziemy przeciążać definicje metod dla tego słuchacza.
			@override = przeciążenie, a przeciążenie = redefiniowanie na własne potrzeby.
			 */

			@Override
			public void mouseClicked(MouseEvent e) 
			{ 
				/*
				 Przeciążyliśmy metodę do obsługi zdarzenia, kiedy klawisz myszki zostanie
				 wciśnięty. Poniższy warunek
				 if (e.getX() >= 0 && e.getX() <= 100)
				 mówi tyle co: "pobierz wartość x z suwaka i jeżeli jest ona pomiędzy wartością 0 (min)
				 a 100 (max) to:"
				 */
				if (e.getX() >= 0 && e.getX() <= 100) 
				{
					ampV[waveCount] = (int) e.getX(); 
					/*
					Pamiętasz jak mówiłem o indeksie, który jest determinowany tym którą fale mamy wybraną?
					Tutaj właśnie to wykorzystujemy. Do tablicy ampV do elementu o indeksie waveCount
					przypisywana jest wartość pobrana z suwaka. Teraz program będzie pamiętał, że np fala
					o numerze 1 ma amplitudę równą 3000 Hz itd.
					 */
					amplitudeValue.setText("" + ampV[waveCount]); 
					/*
					Tutaj mówimy programowi, żeby w okienku, które jest pod podziałką dla suwaka 
					amplitudy wyświetlona została wartość tego suwaka w postaci liczby. Jak zmieni się
					indeks wybranej fali wtedy zmieni się również i to, poniewaz jest w komendzie napisane
					setText("" + ampV[waveCount]).
					 */
				}
			}
			
			/*
			 Poniższe metody nas nie interesują, bo ich nie wykorzystujemy
			 */
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		
		/*
		 Teraz dodajemy słuchacza zmian myszki dla suwaka amplitudy. Chodzi o to, żeby jak sie 
		 myszka przesunie z wciśniętym klawiszem to program wywoływał konkretna akcję.
		 */
		
		amplitudeSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) 
			{
				amplitudeValue.setText("" + amplitudeSlider.getValue());
				/*
				Ten sam trik co poprzednio. Jeżeli myszka się przesuwa to na bieżąco zmienia się 
				wartość wyświetlana w okienku.
				 */
				ampV[waveCount] = ((JSlider) e.getSource()).getValue();
				/*
				Tutaj na bieżąco pobieramy wartość z suwaka i zapisujemy ją do tablicy o konkretnym
				indeksie.
				 */
				sines.setAmplitude(ampV[waveCount], waveCount);
				/*
				To jest wywołanie metody klasy SineDraw pod przykrywką sines. Chodzi tutaj o to,
				żeby metoda rysująca w klasie SineDraw na biężąco śledziła zmiany amplitudy
				danej fali i rysowała ją na bieżąco. Metoda 
				setAmplitude(ampV[waveCount], waveCount)
				mówi tyle: "powiedz mi jaką amplitudę mam teraz narysować i nie zapomnij dodać
				dla której fali zmienia się ta amplituda".
				 */
				totalSinus.setAmplitude(ampV[waveCount], waveCount);
				/*
				Tu jest powtórka tego co wyżej tylo dla funkcji rysującej superpozycję fal.
				 */
			}
		});
		
		amplitudeValue.addKeyListener(new KeyListener() 
		{
			/*
			Tutaj dodajemy słuchacza klawiatury. Gdy wpiszemy wartość liczbową do okienka pod podziałką
			to chcemy, żeby suwak się na tę wartość ustawił no i żeby sie funkcja przerysowała.
			 */

			@Override
			public void keyTyped(KeyEvent e) 
			{
				
				/*
				Poniżej w warunku if zastosowany jest kod ASCII dla klawiszy. Poczytaj sobie więcej
				o tym w internecie, bo to było już na C na pierwszym roku. Tutaj chodzi o to:
				"jeżeli wciśnięty zostanie klawisz enter to:". 
				 */
				
				if ((int) e.getKeyChar() == 10) 
				{ 
					int i = 0; //zadeklarowanie zmiennej i
					
					try 
					{
						i = Integer.parseInt(amplitudeValue.getText()); 
						/*
						Pobiera wartość liczbową wpisaną w okienku i próbuje przekształcić ją do int
						i przypisać do zmiennej i
						 */
					}
					
					catch (Exception ex) 
					{
						i = 0; //jeżeli wartość i = 0 no to jest to wyjątek i nie rób nic
					}
					
					if (i < 0) 
						i = 0;
					/*
					Jeżeli wartość i jest mniejsza od 0 no to nie jest to poprawna wartość (bo
					amplituda nie może być mniejsza od 0) więc ustaw automatycznie wartość amplitudy
					na możliwe minimum czyli 0 - a wtedy jest to wyjątek
					 */
					
					else if (i > 100)
						i = 100;
					/*
					Ten sam trik co wyżej, jak wartość amplitudy wpiszesz większą niż 100 to ustaw na
					możliwe maksimum czyli 100. Te ograniczenia są spowodowane tym, że okienko
					w którym rysujemy fale ma ograniczoną powierzchnię no i suwak ma tylko zakres 0-100,
					a nie, że w przyrodzie max amplituda to 100.
					 */

					amplitudeSlider.setValue(i); //ustaw wartość suwaka na wpisaną wartość
					ampV[waveCount] = i; //nie zapomnij zmienić wartości amplitudy w tablicy
				}
			}
			
			//to nas nie interesuje
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		/*
		Skończyliśmy omawiać suwak amplitudy. Poniżej dla każdego z suwaków są identyczne niemalże
		polecenia, także zrozumienie ich nie powinno być już dla Ciebie trudne. Jest kilka małych 
		elementów dodatkowych ale to szukaj komentarzy.
		 */
		
		frequencySlider.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e) 
			{ 
				if (e.getX() >= 0 && e.getX() <= 10000) 
				{
					freqV[waveCount] = (int) e.getX();
					frequencyValue.setText("" + freqV[waveCount]);
					frequencySlider.setValue(freqV[waveCount]);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) 
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) 
			{
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) 
			{
				
			}
		}
		);
		
		frequencySlider.addChangeListener(new ChangeListener() 
		{

			@Override
			public void stateChanged(ChangeEvent e) 
			{
				frequencyValue.setText("" + frequencySlider.getValue());
				freqV[waveCount] = ((JSlider) e.getSource()).getValue();
				sines.setCycles(freqV[waveCount], waveCount);
				totalSinus.setCycles(freqV[waveCount], waveCount);
			}
		});
		
		frequencyValue.addKeyListener(new KeyListener() 
		{

			@Override
			public void keyTyped(KeyEvent e) {

				if ((int) e.getKeyChar() == 10) 
				{ 
					int i = 0;
					
					try 
					{
						i = Integer.parseInt(frequencyValue.getText()); 
					} 
					catch (Exception ex) 
					{
						i = 0;
					}
					
					if (i < 0)
						i = 0;
					
					else if (i > 10000)
						i = 10000;

					frequencySlider.setValue(i);
					freqV[waveCount] = i;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		phaseSlider.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e) 
			{ 
				if (e.getX() >= 0 && e.getX() <= 360) 
				{
					phV[waveCount] = (int) e.getX();
					phaseValue.setText("" + phV[waveCount]);
					phaseSlider.setValue(phV[waveCount]);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		
		phaseSlider.addChangeListener(new ChangeListener() 
		{

			@Override
			public void stateChanged(ChangeEvent e) 
			{
				phaseValue.setText("" + phaseSlider.getValue());
				phV[waveCount] = ((JSlider) e.getSource()).getValue();
				sines.setPhase(phV[waveCount], waveCount);
				totalSinus.setPhase(phV[waveCount], waveCount);
			}
		});
		
		phaseValue.addKeyListener(new KeyListener() 
		{ 

			@Override
			public void keyTyped(KeyEvent e) 
			{

				if ((int) e.getKeyChar() == 10) 
				{ 
					int i = 0;
					
					try 
					{
						i = Integer.parseInt(phaseValue.getText()); 
					} 
					catch (Exception ex) 
					{
						i = 0;
					}
					
					if (i < 0)
						i = 0;
					
					else if (i > 360) 
						i = i - 360;
					
					/*
					Tutaj jest mały dodatek. Z matematyki wiemy, ze koło ma max 360 stopni i tak samo
					funkcja sinusoidalna ma okres 2 pi. Więc jeżeli przesuniemy falę o więcej niż 2 pi
					to efekt będzie taki sam jakbyśmy przesunęli falę o tylko ten fragment co jest
					powyżej 2 pi i stąd powyższy warunek. To wszystko tyczy się wartości wpisywanej
					do okienka pod podziałką.
					 */

					phaseSlider.setValue(i);
					phV[waveCount] = i;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) 
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) 
			{
				// TODO Auto-generated method stub

			}
		});
		
		wavesPanel = new JPanel();
		wavesPanel.setLayout(new BoxLayout(wavesPanel, BoxLayout.PAGE_AXIS));
		
		componentWaves = new JPanel();
		componentWaves.setBackground(new Color(255,255,255)); //ustawienie czarnego tła
		componentWaves.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED)); //bajer, nieistotny
		
		
		resultWave = new JPanel();
		resultWave.setBackground(new Color(255,255,255));
		resultWave.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		wavesPanel.setPreferredSize(new Dimension(850, 290));
		wavesPanel.add(componentWavesLabel);
		wavesPanel.add(sines);
		wavesPanel.add(resultWaveLabel);
		wavesPanel.add(totalSinus);
		
		soundPanel.add(slidersPanel, BorderLayout.SOUTH);
		soundPanel.add(wavesPanel, BorderLayout.NORTH);
		
		return soundPanel;
		/*
		cała ta wielka metoda zwraca cały panel zakładkowy, żeby można go było wrzucić w klasie Main,
		ponieważ funkcja ma parametry
		addTab("nazwa_zakładki", panel_zakładki)
		 */
	}
	
	/*
	Tutaj jest wcześniej wiele razy opisywana metoda do nasłuchiwania przycisków. To wszystko zrobione
	zostało w ten sposób, żeby nie trzeba było od nowa dla każdego przycisku osobnego słuchacza pisać,
	tylko żeby zrobić jak poniżej 
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		/*
		I tu jest właśnie ta wygoda. Piszemy jednego słuchacza i tylko patrzymy, który guzik został
		wciśnięty, pobieramy jego dane i nazwę i potem mamy ify, które mówią "jeżeli ten guzik to zrób to
		a jeżeli inny to tamto" 
		 */
		Object object = e.getSource();
		
		if(object == play)
		{
			try 
			{
				audio.playSound(freqV[waveCount], ampV[waveCount], phV[waveCount]);
				/*
				 Wywołanie metody pod przykrywką - odtworzenie dźwięku o częstotliwości,
				 amplitudzie i fazie dla konkretnej fali
				 */
			} 
			
			catch (LineUnavailableException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(object == playTriade)
		{
            double[] freqArray  = new double[3];
            double[] ampArray = new double[3];
            double[] phArray = new double[3];

            for(int i = 0; i < 3; ++i) 
            {
                freqArray[i]  = freqV[i];
                ampArray[i] = ampV[i];
                phArray[i] = phV[i];
            }
            
            /*
            Miejscowe zadeklarowanie trzech tablic pomocniczych. Są one stworzone dlatego, że w trakcie
            tworzenia programu zadeklarowałem wszystkie zmienne jako inty, a potem znalazłem przykład
            funkcji odtwarzającej dźwięk, której argumentem były double, więc funkcja poniżej przyjmuje
            double i napisanie
            calcChord(freqV, ampV, phV)
            byłoby błędem, bo te tablice przechowują inty, a nie double. Więc te tablice pomocnicze
            przechowują te same wartości tylko, że jedne są inty a drugie double, żeby funkcja działała.
             */

            try 
            {
				audio.calcChord(freqArray, ampArray, phArray);
				audio.playChord();
				//obliczenie parametrów akordu i jego odtworzenie
			} 
            
            catch (LineUnavailableException e1) 
            {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(object == saveImage)
		{
			FileDialog saveImage = new FileDialog(new JFrame(), save_component, FileDialog.SAVE);
			saveImage.setVisible(true);
			String filePath = saveImage.getFile();
			String catalogPath = saveImage.getDirectory();
			
			FileDialog saveSuperpose = new FileDialog(new JFrame(), save_super, FileDialog.SAVE);
			saveSuperpose.setVisible(true);
			String filePath2 = saveSuperpose.getFile();
			String catalogPath2 = saveSuperpose.getDirectory();
			
			file1 = new File(catalogPath + filePath + "_component.jpg");
			file2 = new File(catalogPath2 + filePath2 + "_superposition.jpg");
			
			if(filePath == null || filePath2 == null)
				JOptionPane.showMessageDialog(null, err_message, err_label, JOptionPane.ERROR_MESSAGE);
			
			else
			{
				try
				{
					ImageIO.write(sines.getImage(),"jpg", file1);
					ImageIO.write(totalSinus.getImage(), "jpg", file2);
					JOptionPane.showMessageDialog(null, info_message, info_label, JOptionPane.INFORMATION_MESSAGE);
				}
				
				catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null, err_message, err_label, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		if(object == saveWave)
		{
			/*
			tutaj znajdują się polecenia do zapisu dźwięku. Żeby zrozumieć w pełni co tu sie dzieje
			ogarnij klasę FileTypeFilter
			 */
			
			FileDialog saveImage = new FileDialog(new JFrame(), save_label, FileDialog.SAVE);
			saveImage.setVisible(true);
			String filePath = saveImage.getFile();
			String catalogPath = saveImage.getDirectory();
			
			file1 = new File(catalogPath + filePath + ".wav");
			
			if(filePath == null)
				JOptionPane.showMessageDialog(null, err_message, err_label, JOptionPane.ERROR_MESSAGE);
			
			else
			{
				
				double[] freqArray  = new double[3];
	            double[] ampArray = new double[3];
	            double[] phArray = new double[3];

	            for(int i = 0; i < 3; ++i) 
	            {
	                freqArray[i]  = freqV[i];
	                ampArray[i] = ampV[i];
	                phArray[i] = phV[i];
	            }
	            
	            //ponownie lokalne tablice pomocnicze i funkcje obliczająca parametry i zapisująca dźwięk
				
				try
				{
					audio.calcChord(freqArray, ampArray, phArray);
					audio.saveChord();
				}
				
				catch (Exception err)
				{
					JOptionPane.showMessageDialog(null, err_message, err_label, JOptionPane.ERROR_MESSAGE);
				}
				
				JOptionPane.showMessageDialog(null, info_message, info_label, JOptionPane.INFORMATION_MESSAGE);
			}	
		}
	}
	
}



