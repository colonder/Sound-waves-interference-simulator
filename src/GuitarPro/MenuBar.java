package GuitarPro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class MenuBar extends JFrame implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6424091854427376407L;
	private JMenuBar menuBar; //Tworzymy główny pasek menu
	private JMenu language, program; //Tworzymy kategorie menu
	private JMenuItem UKLang, DELang, FRLang, ESPLang, POLLang, RUSLang, about, exit; //Tworzymy poszczególne opcje w poszczególnych kategoraich
	private static ResourceBundle labels; //tworzymy dostęp do plików źródłowych o językach
	private static Locale currentLocale; //tworzymy lokalizację
	private static String aboutApplication; //Tworzymy String przechowujący opis aplikacji
	private static String confirm_exit = "Are you sure you want to quit?";
	private static String exit_label;
	private static String about_label;
	private static String LangMenu = "Language";
	private static String progMenu;
	private static String exitMenu;
	private static String aboutMenu;
	private static String[] languagesArray = {"Englisch", "Deutsch", "Français", "Español", "Polski", "\u0440\u0443\u0441\u0441\u043A\u0438\u0439"};
	
	/*
	 Poniższy kod do momentu implementacji Acion Listenerów jest bardzo prosty, włącz sobie program
	 i obczajaj po kolei patrząc co sie dzieje
	 */
	public MenuBar()
	{
		
	}
	
	public JMenuBar makeMenu()
	{
		menuBar = new JMenuBar();
		language = new JMenu(LangMenu);
		program = new JMenu(progMenu);
		
		UKLang = new JMenuItem(languagesArray[0]);
		DELang = new JMenuItem(languagesArray[1]);
		FRLang = new JMenuItem(languagesArray[2]);
		ESPLang = new JMenuItem(languagesArray[3]);
		POLLang = new JMenuItem(languagesArray[4]);
		RUSLang = new JMenuItem(languagesArray[5]);
		exit = new JMenuItem(exitMenu);
		about = new JMenuItem(aboutMenu);

		UKLang.addActionListener(this);
		DELang.addActionListener(this);
		POLLang.addActionListener(this);
		FRLang.addActionListener(this);
		ESPLang.addActionListener(this);
		RUSLang.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);
		
		language.add(UKLang);
		language.add(DELang);
		language.add(FRLang);
		language.add(POLLang);
		language.add(ESPLang);
		language.add(RUSLang);
		
		program.add(about);
		program.add(exit);
		
		menuBar.add(Box.createVerticalGlue());
		menuBar.add(program);
		menuBar.add(language);
		
		return menuBar;
	}
	
	/*
	 Kiedy zerkniesz do plików po lewej, tych co sie nazywają LabelsBundle one mają po lewej napisane
	 słowa kluczowe, a po prawej treść w danym języku. Poniższe funkcje zasysają te opisy w danym języku
	 - kazde pole ma swoje unikatowe słowo klucz, na podstawie którego zasysa treść. Odwołujemy sie tutaj
	 bezpośrednio do pól zdefiniowanych w klasie Sounds, dlatego, że są one statyczne. Po wybraniu
	 konkretnego języka program szuka słów kluczowych dla każdego pola i przypisuje mu nową wartość.
	 */
	
	public static void setLanguage(String lang)
	{
		currentLocale = new Locale(lang);
		labels = ResourceBundle.getBundle("GuitarPro.LabelsBundle_" + lang, currentLocale);
		
		Sounds.amplitudeLabel.setText(labels.getString("amplitude_label"));
		Sounds.frequencyLabel.setText(labels.getString("frequency_label"));
		Sounds.phaseLabel.setText(labels.getString("phase_label"));
		Sounds.componentWavesLabel.setText(labels.getString("component_waves_label"));
		Sounds.resultWaveLabel.setText(labels.getString("result_wave_label"));
		Sounds.play.setText(labels.getString("play_button"));
		Sounds.playTriade.setText(labels.getString("play_triade_button"));
		Sounds.saveImage.setText(labels.getString("save_image_button"));
		Sounds.saveWave.setText(labels.getString("save_sample_button"));
		Sounds.info_message = labels.getString("save_success_message");
		Sounds.err_message = labels.getString("save_error_message");
		Sounds.wave_1 = labels.getString("wave_1");
		Sounds.wave_2 = labels.getString("wave_2");
		Sounds.wave_3 = labels.getString("wave_3");
		Sounds.cancel_message = labels.getString("cancel_message");
		Sounds.internal_error = labels.getString("internal_error");
		Sounds.err_label = labels.getString("err_label");
		Sounds.save_label = labels.getString("save_label");
		Sounds.info_label = labels.getString("info_label");
		Sounds.save_component = labels.getString("save_component");
		Sounds.save_super = labels.getString("save_super");
		
		aboutApplication = labels.getString("about");
		confirm_exit = labels.getString("confirm_exit");
		exit_label = labels.getString("exit_label");
		about_label = labels.getString("about_label");
		LangMenu = labels.getString("lang_menu");
		exitMenu = labels.getString("exit_menu");
		aboutMenu = labels.getString("about_menu");
		progMenu = labels.getString("prog_menu");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object input = e.getSource();
		
		if(input == UKLang)
		{
			setLanguage("en");
		}
		
		else if(input == DELang)
		{
			setLanguage("de");
		}
		
		else if(input == FRLang)
		{
			setLanguage("fr");
		}
		
		else if(input == POLLang)
		{
			setLanguage("pl");
		}
		
		else if(input == ESPLang)
		{
			setLanguage("esp");
		}
		
		else if(input == RUSLang)
		{
			setLanguage("rus");
		}
		
		else if(input == about)
			JOptionPane.showMessageDialog(null, aboutApplication, about_label, JOptionPane.INFORMATION_MESSAGE); //po wciśnięciu about pojawia się okno z opisem apki
		
		else if(input == exit)
		{	
			 int exitChoice = JOptionPane.showConfirmDialog(null, confirm_exit, exit_label, JOptionPane.YES_NO_OPTION);
			
			if(exitChoice == JOptionPane.YES_OPTION)
				System.exit(0);
			
			else {}
		}
		
	}
	
}
