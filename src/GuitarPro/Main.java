package GuitarPro;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4088702855241115710L;

	private Sounds sounds;
	
	/*
	 Taka deklaracja pozwala na używanie metod klasy Sounds za pomocą komendy
	 sounds.jakaś_klasa(parametr);
	 Moglibyśmy bezpośrednio wywoływać taką metodę poprzez 
	 Sounds.jakaś_metoda(parametr);
	 Ale wtedy ta metoda musiałaby być statyczna, a my takich nie robimy.
	 Przeważnie w reszcie programu posługuje się takimi deklaracjami, poza kilkoma wyjątkami,
	 które wyraźnie zaznacze.
	 */
	
	private MenuBar menu = new MenuBar();
	private JTabbedPane mainTab;	
	
	/*
	 Deklaracja głównego paska do zakładek, w którym są umieszczane poszczególne zakładki
	 */
	
	private JPanel mainPanel;	//deklaracja głównego panelu okna
	
	public Main()	//konstruktor klasy main
	{
		sounds = new Sounds();	//tworzymy nowy obiekt klasy Sounds pod "przykrywką" sounds
		
		mainTab = new JTabbedPane();
		mainPanel = new JPanel();
		mainTab.addTab("Sounds", sounds.Sound());	//dodajemy do głównego panelu zakładek zakładkę o nazwie Sounds
		mainPanel.add(mainTab);	//dodajemy do głównego panelu główny panel zakładek
		add(mainPanel);	//dodajemy główny panel do okna programu
		setJMenuBar(menu.makeMenu());
		/*
		 Dodajemy menu do okna programu poprzez metodę getMenu(), 
		 która znajduje się w klasie MenuBar.java
		 */
	}
	
	public static void GUI()	//statyczna metoda, która inicjalizuje interfejs
	{
		Main window = new Main();	//nowa instancja powyższego konstruktora
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(870, 650));
		window.setResizable(false);	//uniemożliwienie zmiany rozmiaru okna
	    window.setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable() //nowy wątek inicjalizujący klasę GUI()
		{
	         public void run()
	         {
	            GUI();
	         }
	      }
		  );
	}

}

