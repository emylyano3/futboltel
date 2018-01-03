package com.deportel.guiBuilder.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.deportel.common.exception.UserShowableException;
import com.deportel.common.utils.Utils;

/**
 * Provee varias funcionalidades basicas y de uso general
 * para las interfaces graficas.
 * @author Emy
 * @since 10/02/2010
 */
public class GuiUtils
{
	private static final Logger	log	= Logger.getLogger(GuiUtils.class);

	/**
	 * Crea un componente de interfaz rigido cuadrado que se puede agregar para generar
	 * espacios entre otros componentes
	 * @param space El espacio deseado
	 * @return El Componente que trabajara como espaciador
	 * @since 21/05/2010
	 */
	public static JComponent space(int space)
	{
		return (JComponent) Box.createRigidArea(new Dimension(space, space));
	}

	/**
	 * Crea un componente de interfaz rigido rectangular de dimensiones w y h (ancho y alto)
	 * que se puede agregar para generar espacios entre otros componentes
	 * @param w El ancho deseado
	 * @param h El alto deseado
	 * @return El Componente que trabajara como espaciador
	 * @since 21/05/2010
	 */
	public static JComponent space(int w, int h)
	{
		return (JComponent) Box.createRigidArea(new Dimension(w, h));
	}

	/**
	 * Creates a panel with titled border.
	 * 
	 * @param title
	 * @param dim
	 * @return Component
	 * @since 21/05/2010
	 */
	public static JPanel makeBorderPanel(String title, Dimension dim)
	{
		Utils.validateParameters(title, dim);
		JPanel panel = new JPanel();
		panel.setPreferredSize(dim);
		TitledBorder titled;
		titled = BorderFactory.createTitledBorder(title);
		panel.setBorder(titled);
		return panel;
	}

	/**
	 * Creates a panel with titled border.
	 * @param title
	 * @param font
	 * @return Component
	 * @since 18/07/2010
	 */
	public static JPanel makeBorderPanel(String title, Font font)
	{
		Utils.validateParameters(title, font);
		JPanel panel = new JPanel();
		TitledBorder titled;
		titled = BorderFactory.createTitledBorder(title);
		titled.setTitleFont(font);
		panel.setBorder(titled);
		return panel;
	}

	/**
	 * Toma un JPanel y le asigna un Borde con Titulo.
	 * 
	 * @param panel El panel al que se le asignara el borde
	 * @param title El titulo del borde
	 */
	public static void addTitledBorder (JPanel panel, String title)
	{
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
		titledBorder.setTitleFont(panel.getFont());
		panel.setBorder(titledBorder);
	}

	/**
	 * Loads an image from a file.
	 * @param imageFile
	 * @return {@link BufferedImage}
	 * @since 18/07/2010
	 */
	public static BufferedImage loadImage(File imgFile)
	{
		Utils.validateParameters(imgFile);
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(imgFile);
			log.debug("Image readed");
		}
		catch (IOException e)
		{
			log.debug("Image not found: " + imgFile.getName());
		}
		return img;
	}

	/**
	 * Carga un icono desde un archivo de
	 * imagen.
	 * @param path
	 * @return {@link ImageIcon}
	 * @since 15/03/2011
	 */
	public static ImageIcon loadIcon(String path, Class <?> clazz)
	{
		try
		{
			InputStream is = clazz.getResourceAsStream(path);
			byte [] bytes = Utils.isToByteArray(is);
			ImageIcon icon = new ImageIcon(bytes);
			return icon;
		}
		catch (NullPointerException e)
		{
			log.error("Puede que la Clase " + clazz +" utilizada como referencia de carga de recursos, sea nula, no exista o no sea encontrada por el class loader");
			return null;
		}
		catch (IllegalArgumentException e)
		{
			log.error("Puede que el InputStream creado a partir del path: [" + path + "] sea nulo");
			return null;
		}
	}

	/**
	 * Loads an image from a file.
	 * 
	 * @param path El path absoluto de la imganen
	 * @return {@link BufferedImage}
	 * @since 18/07/2010
	 */
	public static BufferedImage loadImage(String path)
	{
		Utils.validateParameters(path);
		BufferedImage img = null;
		File imgFile = new File(path);
		try
		{
			img = ImageIO.read(imgFile);
			log.debug("Image readed");
		}
		catch (IOException e)
		{
			log.debug("Image not found: " + imgFile.getName());
		}
		return img;
	}

	/**
	 * Crea una imagen
	 * @param path
	 * @param description
	 * @param clazz
	 */
	public static Image createImage(String path, String description, Class <?> clazz)
	{
		Utils.validateParameters(path, description);
		InputStream is = clazz.getResourceAsStream(path);

		if (is == null)
		{
			log.error("Resource not found: " + path);
			return null;
		}
		else
		{
			byte [] byteData = Utils.isToByteArray(is);
			return (new ImageIcon(byteData, description)).getImage();
		}
	}

	/**
	 * Crea un boton al cual se le setea el listener, que es quien atiende los eventos del boton, un
	 * nombre, un icono y un tooltip que es un mensaje al usuario que explica la utilidad del boton.
	 * @param listener La clase que atiende los eventos del boton. Esa clase debe implementar {@link ActionListener}
	 * @param name El nombre del boton
	 * @param iconPath El path absoluto del icono
	 * @param toolTip El texto informativo
	 * @return {@link JButton} El boton creado.
	 * @since 18/01/2011
	 */
	public static JButton createButton (Class <ActionListener> listener, String name, String iconPath, String toolTip)
	{
		Utils.validateParameters(listener, name, iconPath, toolTip);
		JButton button = new JButton();
		try
		{
			button.addActionListener(listener.newInstance());
			button.setName(name);
			String className = "com.deportel.futboltel.common.gui.utils.Utils";
			URL url = Class.forName(className).getResource(iconPath);
			ImageIcon icon = new ImageIcon(url);
			button.setIcon(icon);
		}
		catch (InstantiationException e)
		{
			log.error("Error instanciando la clase que hara de listener para el boton: " + name);
		}
		catch (IllegalAccessException e)
		{
			log.error("No se puedo acceder a la clase que se envio como listener del boton: " + name);
		}
		catch (ClassNotFoundException e)
		{
			log.error("No se encontro la clase " + name);
		}
		button.setToolTipText(toolTip);
		return button;
	}

	/**
	 * Setea el look & feel de la aplicacion.
	 * @param lookAndFeelName El nombre del look & feel que se quiere setear
	 * @since 18/01/2011
	 */
	public static void setLookAndFeel (String lookAndFeelName) throws UserShowableException
	{
		Utils.validateParameters(lookAndFeelName);
		try
		{
			UIManager.setLookAndFeel(lookAndFeelName);
		}
		catch (Exception e)
		{
			log.error("Error seteando el look & feel: " + lookAndFeelName);
			throw new UserShowableException("Look and Feel no soportado [" + lookAndFeelName + "]");
		}
	}

	/**
	 * 
	 * @param masterWindow
	 * @param lookAndFeelName
	 */
	public static void updateLookAndFeel (String lookAndFeelName) throws UserShowableException
	{
		try
		{
			UIManager.setLookAndFeel(lookAndFeelName);
		}
		catch (Exception e)
		{
			System.err.println("Error cargando el L&F: " + e);
			throw new UserShowableException("Look and Feel no soportado [" + lookAndFeelName + "]");
		}
		Frame[] allFrame = JFrame.getFrames();
		Window[] allWindow;
		for (Frame element : allFrame)
		{
			SwingUtilities.updateComponentTreeUI(element);
			allWindow = element.getOwnedWindows();
			for (Window element2 : allWindow)
			{
				updateWindow(element2);
			}
		}
	}

	/**
	 * 
	 * @param window
	 */
	private static void updateWindow (Window window)
	{
		SwingUtilities.updateComponentTreeUI(window);
		Window[] children = window.getOwnedWindows();
		for (Window element : children)
		{
			updateWindow(element);
		}
	}

	/** Para formatear el texto a integer */
	public static NumberFormat	integerFormat;

	/** Para formatear el texto a fecha en formato DD/MM/AAAA */
	public static DateFormat	dateFormat;

	static
	{
		integerFormat = NumberFormat.getNumberInstance();
		integerFormat.setMaximumFractionDigits(0);
		dateFormat = DateFormat.getDateInstance();
		dateFormat.setCalendar(new GregorianCalendar());
	}

}