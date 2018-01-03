package com.deportel.guiBuilder.gui.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.deportel.common.Constants;

/**
 * @author Emy
 * @since 24/02/2011
 */
public class FileBrowser extends JComponent
{
	public static final int		OPEN_DIALOG			= JFileChooser.OPEN_DIALOG;

	public static final int		SAVE_DIALOG			= JFileChooser.SAVE_DIALOG;

	private JFileChooser		chooser;

	private final String		ENCODING			= "ISO-8859-1";

	/**
	 * Para determinar el tipo de dialogo del selector. Por ejemplo si es para abrir archivos o para guardar.
	 */
	private int					dialogType;

	/**
	 * Para determinar si el tipo de archivo a abrirse existe ya en el archivo de memoria de paths.
	 */
	private boolean				fileTypeFound		= false;

	/**
	 * Determina si el file picker le permitira al usuario ver otros archivos que no sean del tipo declarado por
	 * {@link #fileType}
	 */
	private boolean				acceptAllFiles		= false;

	/**
	 * Determina el tipo de archivos que el usuario podra seleccionar a traves del file picker.
	 */
	private final String		fileType;

	/**
	 * Descriptor para el archivo de memoria de paths.
	 */
	private final File			pathsMemFile;

	/**
	 * Para guardar la ruta que devuelva el chooser, una vez que el usuario termine la seleccion.
	 */
	private String				pathFound;

	/**
	 * Es el nombre del archivo de memoria de rutas.
	 */
	private static final String	pathsMemoryFilePath	= "pathsMemory";

	private static final String	DELIMITER			= "&";

	private static final String	RETURN				= "\n";

	private static final long	serialVersionUID	= 1L;

	/**
	 * Crea un selector de archivos, al cual se le setean la descripcion y extension del tipo de archivo que se acepta,
	 * es decir, el tipo de archivo que se permitira visualizar al usuario al momento de navegar las carpetas.
	 * <b>Ejemplo:</b> Si quiero crear un selector de imagenes BMP, los parámetros correctos serían:<br/>
	 * <b>desc:</b> Bitmap File <b>type:</b> BMP
	 *
	 * @param desc
	 *            La descripcion del tipo archivo
	 * @param fileType
	 *            La extension del tipo de archivo
	 * @since 24/02/2011
	 */
	public FileBrowser(String fileDesc, String fileType)
	{
		this(fileDesc, fileType, JFileChooser.OPEN_DIALOG);
	}

	/**
	 * Crea un selector de archivos, al cual se le setean la descripcion y extension del tipo de archivo que se acepta,
	 * es decir, el tipo de archivo que se permitira visualizar al usuario al momento de navegar las carpetas.
	 * <b>Ejemplo:</b> Si quiero crear un selector de imagenes BMP, los parámetros correctos serían:<br/>
	 * <b>desc:</b> Bitmap File <b>type:</b> BMP
	 *
	 * @param desc
	 *            La descripcion del tipo archivo
	 * @param fileType
	 *            La extension del tipo de archivo
	 * @param dialogType
	 *            El tipo de dialogo. Determina si la ventana se abrira para guardado o apertura de archivos.
	 *
	 * @since 24/02/2011
	 */
	public FileBrowser(String fileDesc, String fileType, int dialogType)
	{
		this.fileType = fileType;
		this.pathsMemFile = new File(pathsMemoryFilePath).getAbsoluteFile();
		String line = null;
		try
		{
			final Scanner scanner = new Scanner(this.pathsMemFile);
			while (!this.fileTypeFound && scanner.hasNextLine())
			{
				line = scanner.nextLine();
				if (line.indexOf(this.fileType) != -1)
				{
					this.fileTypeFound = true;
				}
			}
			scanner.close();
			if (this.fileTypeFound)
			{
				this.chooser = new JFileChooser(line.substring(line.indexOf(DELIMITER) + 1));
			}
			else
			{
				this.chooser = new JFileChooser();
			}
			this.chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			this.chooser.setFileFilter(new FileNameExtensionFilter(fileDesc, this.fileType));
			this.chooser.setAcceptAllFileFilterUsed(this.acceptAllFiles);
			this.dialogType = dialogType;
		}
		catch (final IOException e)
		{
			try
			{
				saveNewFileType(fileType);
			}
			catch (final IOException e1)
			{
				e1.printStackTrace();
			}
			this.chooser = new JFileChooser();
		}
	}

	/**
	 * Muestra la interfaz de seleccion al usuario para que este realice la seleccion del archivo.
	 *
	 * @return La ruta del archivo que el usuario seleccionó.
	 * @since 24/02/2011
	 */
	public String selectFile()
	{
		final int returnVal = showDialog();
		try
		{
			switch (returnVal)
			{
			case JFileChooser.APPROVE_OPTION:
				this.pathFound = this.chooser.getSelectedFile().getAbsolutePath();
				if (new File(this.pathFound).exists() || this.dialogType == JFileChooser.SAVE_DIALOG)
				{
					if (this.pathFound.indexOf(Constants.DOT) == -1)
					{
						this.pathFound += Constants.DOT + this.fileType;
					}
					updatePathsMemoryFile(this.fileType, this.pathFound);
					return this.pathFound;
				}
				else
				{
					return "";
				}

			case JFileChooser.CANCEL_OPTION:
			default:
				return "";
			}
		}
		catch (final IOException e)
		{
			return "";
		}
	}

	/**
	 * Muestra el dialogo del selector y setea el texto del boton de aceptacion de acuerdo con el tipo de dialogo que
	 * haya sido seteado previamente.
	 *
	 * @return El estado del selector una vez cerrado:<br/>
	 *         <b>JFileChooser.CANCEL_OPTION </b><br/>
	 *         <b>JFileChooser.APPROVE_OPTION </b><br/>
	 *         <b>JFileCHooser.ERROR_OPTION </b> if an error occurs or the dialog is dismissed <br/>
	 * @since 25/02/2011
	 */
	private int showDialog()
	{
		String buttonText;
		switch (this.dialogType)
		{
		case JFileChooser.OPEN_DIALOG:
			buttonText = "Abrir";
			break;
		case JFileChooser.SAVE_DIALOG:
			buttonText = "Guardar";
			break;
		case JFileChooser.CUSTOM_DIALOG:
			buttonText = "Abrir";
			break;
		default:
			buttonText = "Abrir";
		}
		this.chooser.setDialogType(this.dialogType);
		return this.chooser.showDialog(this, buttonText);
	}

	/**
	 * Guarda un nuevo tipo de archivo.
	 *
	 * @param fileType
	 * @throws IOException
	 * @since 24/02/2011
	 */
	private void saveNewFileType(String fileType) throws IOException
	{
		final FileOutputStream fos = new FileOutputStream(this.pathsMemFile);
		fos.write((fileType + DELIMITER + RETURN).getBytes(this.ENCODING));
		fos.close();
	}

	/**
	 * Actualiza el archivo de memorias de paths. Si existe un path para el tipo de archivo especificado por fileType lo
	 * reemplaza por el nuevo path especificado por el parametro pathFound. En caso de que no exista un path anterior
	 * para el tipo de archivo especificado, entonces se agrega una nueva entrada en el archivo de memoria de paths.
	 *
	 * @param fileType
	 *            La extension del archivo del cual se quiere actualizar/agregar la ruta de acceso.
	 * @param pathFound
	 *            El path correspondiente al fileType
	 * @throws IOException
	 *             En caso de exista un error durante la escritura o lectura del archivo de memoria de paths.
	 * @since 24/02/2011
	 */
	private void updatePathsMemoryFile(String fileType, String pathFound) throws IOException
	{
		try
		{
			final int to = pathFound.lastIndexOf(Constants.PATH_SEPARATOR_ALT) == -1 ? pathFound.lastIndexOf(Constants.PATH_SEPARATOR) : pathFound.lastIndexOf(Constants.PATH_SEPARATOR_ALT);
			final String pathTrimed = pathFound.substring(0, to);
			final Scanner scanner = new Scanner(this.pathsMemFile);
			boolean replaced = false;
			final StringBuilder aux = new StringBuilder();
			aux.append(fileType + DELIMITER + pathTrimed + RETURN);
			StringBuilder file = new StringBuilder();
			while (scanner.hasNextLine())
			{
				final StringBuilder line = new StringBuilder(scanner.nextLine());
				int idx;
				if ((idx = line.indexOf(fileType)) != -1 && !line.equals(aux))
				{
					line.replace(idx + fileType.length() + 1, line.length(), "");
					line.append(pathTrimed);
					replaced = true;
				}
				file = file.append(line);
				file.append(RETURN);
			}
			if (!replaced)
			{
				file.append(aux);
			}
			scanner.close();
			final FileOutputStream fos = new FileOutputStream(pathsMemoryFilePath);
			fos.write(file.toString().getBytes(this.ENCODING));
			fos.close();
		}
		catch (final Exception e)
		{

		}
	}

	/**
	 * Devuelve si el selector acepta todos los archivos o no.
	 *
	 * @return <tt>true</tt> si el selector acepta todos los archivos <tt>false</tt> en caso contrario.
	 * @since 24/02/2011
	 */
	public boolean isAcceptAllFiles()
	{
		return this.acceptAllFiles;
	}

	/**
	 * Setea si el selector acepta todos los archivos o no.
	 *
	 * @param boolean acceptAllFiles
	 * @since 24/02/2011
	 */
	public void setAcceptAllFiles(boolean acceptAllFiles)
	{
		this.acceptAllFiles = acceptAllFiles;
	}

}
