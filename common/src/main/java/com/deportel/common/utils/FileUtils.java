package com.deportel.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.deportel.common.exception.NotADirectoryException;
import com.deportel.common.exception.UserShowableException;

import static com.deportel.common.Constants.*;

public class FileUtils
{
	private static Logger	log		= Logger.getLogger(FileUtils.class);

	private static int		BUFFER	= 1024;

	/**
	 * Crea una carpeta en el path especificado. Chequea previamente que la
	 * carpeta no exista.
	 *
	 * @param path
	 *            El path de la carpeta
	 *
	 * @since 10/07/2011
	 */
	public static void createFolder(String path)
	{
		final File folder = new File(path);
		if (!folder.exists())
		{
			folder.mkdir();
		}
	}

	/**
	 * Borra todos los archivos y directorios creados temporalmente.
	 *
	 * @param El
	 *            path de los temporales
	 *
	 * @since 10/07/2011
	 */
	public static void deleteDir(String path)
	{
		final File target = new File(path);

		if (target.exists())
		{
			File[] content;
			if (!target.delete())
			{
				content = target.listFiles();
				for (final File element : content)
				{
					if (element.isDirectory())
					{
						deleteDir(element.getAbsolutePath());
					}
					else
					{
						element.delete();
					}
				}
				target.delete();
			}
		}
	}

	/**
	 * Empieza a buscar el directorio deseado desde la ruta pasada por parametro
	 * en sentido ascendente. Inspecciona el directorio actual, y si no esta la
	 * carpeta buscada, se sube un nivel y se continúa la busqueda.
	 *
	 * @param path
	 * @param dirName
	 *
	 * @since 10/10/2009
	 */
	public static String findAncestorDir(String path, String dirName)
	{
		Utils.validateParameters(path, dirName);
		log.debug("Pop up to dir from " + path + " to " + dirName);
		int idx;
		File ref;
		File[] list;
		while ((idx = findLastPathSeparator(path)) != -1 && !path.substring(idx, path.length()).equalsIgnoreCase(dirName))
		{
			// Subo un nivel
			path = path.substring(0, idx);
			// Creo un descriptor al directorio
			ref = new File(path);
			// Listo lo que el directorio tiene
			list = ref.listFiles();
			// Me fijo si entre ellos esta el directorio que quiero
			for (final File element : list)
			{
				if (element.isDirectory())
				{
					if (getFirstFolder(element.getPath()).equalsIgnoreCase(dirName))
					{
						return path + PATH_SEPARATOR + dirName;
					}
				}
			}
		}
		if (path == null || path.isEmpty())
		{
			throw new NoSuchElementException();
		}
		return path;
	}

	/**
	 * Se para en la root folder, y empieza a buscar en las carpetas hijas hasta
	 * que encuentra la carpeta deseada.
	 *
	 * @param rootFolderPath
	 * @return
	 * @throws NotADirectoryException
	 *
	 * @since 25/01/2011
	 */
	public static String findChildFolder(String rootFolderPath, String targetFolder)
	{
		Utils.validateParameters(rootFolderPath, targetFolder);
		final File rootFolder = new File(rootFolderPath);
		final File[] folderList = rootFolder.listFiles();
		String result = null;
		if (folderList != null)
		{
			for (final File folder : folderList)
			{
				if (folder.isDirectory() && !folder.isHidden())
				{
					if (folder.getName().equalsIgnoreCase(targetFolder))
					{
						return folder.getAbsolutePath();
					}
					else
					{
						result = findChildFolder(folder.getAbsolutePath(), targetFolder);
						if (result != null)
						{
							return result;
						}
					}
				}
			}
			return result;
		}
		else
		{
			return result;
		}
	}

	/**
	 * Parado en path, se sube hasta startPoint y a partir de ahi se comienza a
	 * buscar recursivamente hacia abajo en el arbol de directorios buscando el
	 * dirName especificado.<br/>
	 * Pre Condicion: startPoint debe estar sobre la carpeta a la que apunta
	 * path.
	 *
	 * @param path
	 *            La ruta inicial
	 * @param startPoint
	 *            El directorio hasta el que se sube para empezar la busqueda
	 * @param dirName
	 *            La carpeta que se esta buscando
	 *
	 * @throws NotADirectoryException
	 * @since 28/01/2011
	 */
	public static String findDir(String path, String dirName) throws NotADirectoryException
	{
		Utils.validateParameters(path, dirName);
		File start = new File(path);
		File parent;
		String target = "";
		boolean found = false;
		while (!found)
		{
			parent = start.getParentFile();
			target = findChildFolder(parent.getAbsolutePath(), dirName);
			if (!Utils.isNullOrEmpty(target))
			{
				found = true;
			}
			start = parent;
		}
		return target;
	}

	/**
	 * Busca los archivos de un tipo en la carpeta indicada
	 *
	 * @param folderPath
	 *            El path absoluto de la carpeta
	 * @param fileExtension
	 *            El tipo de archivo que se busca
	 * @return File [] con los archivos encontrados
	 * @throws NotADirectoryException
	 *
	 * @since 10/10/2009
	 */
	public static List<File> findFiles(String folderPath, String fileExtension) throws NotADirectoryException
	{
		Utils.validateParameters(folderPath, fileExtension);
		final File folder = new File(folderPath);
		if (!folder.isDirectory())
		{
			throw new NotADirectoryException();
		}
		final File[] auxList = folder.listFiles();
		final List<File> list = new ArrayList<File>();
		for (final File element : auxList)
		{
			if (getFileExtension(element.getAbsolutePath()).equalsIgnoreCase(fileExtension))
			{
				list.add(element);
			}
		}
		return list;
	}

	/**
	 *
	 * @param path
	 * @return
	 *
	 * @since 31/01/2011
	 */
	public static int findLastPathSeparator(String path)
	{
		int idx;
		if ((idx = path.lastIndexOf(PATH_SEPARATOR)) == -1)
		{
			idx = path.lastIndexOf(PATH_SEPARATOR_ALT);
		}
		return idx;
	}

	/**
	 * Recibe el path de la carpeta root de un proyecto y busca la carpeta que
	 * representa el paquete especificado. <b>Ejemplo:</p> Si recibe C:/ y
	 * org.continente.pais devuelve C:/src/main/java/org/continente/pais si es
	 * que dicha carpeta existe.
	 *
	 * @param rootPath
	 * @param packageName
	 * @return
	 * @throws UserShowableException
	 * @throws NotADirectoryException
	 *
	 * @since 25/01/2011
	 */
	public static String findPackageFolder(String rootPath, String packageName) throws UserShowableException
	{
		Utils.validateParameters(rootPath, packageName);
		final String[] packageFolders = Utils.split(packageName, PACKAGE_SEPARATOR);
		String headPath = "";
		if (packageFolders.length != 0)
		{
			headPath = findChildFolder(rootPath, packageFolders[0]);

			for (int i = 1; i < packageFolders.length; i++)
			{
				headPath += PATH_SEPARATOR + packageFolders[i];
			}
			final File packageFolder = new File(headPath);
			if (packageFolder.exists())
			{
				return headPath;
			}
			else
			{
				log.error("La direccion enviada no es un directorio o no existe");
				throw new UserShowableException("La direccion [" + packageFolder + "] no es un directorio o no existe");
			}
		}
		return headPath;
	}

	/**
	 * Gets the extension of a file from its path.
	 *
	 * @param path
	 *            The path.
	 * @return The extension.
	 *
	 * @since 10/10/2009
	 */
	public static String getFileExtension(String path)
	{
		Utils.validateParameters(path);
		log.debug("Getting file extension " + path);
		String ext = "";
		final int idx = path.lastIndexOf(DOT);
		if (idx != -1)
		{
			ext = path.substring(idx + 1);
		}
		return ext;
	}

	/**
	 * Finds the name of a file in a path.
	 *
	 * @param path
	 *            The path of the file.
	 * @return The name of the file.
	 *
	 * @since 10/10/2009
	 */
	public static String getFileNameFromPath(String path)
	{
		Utils.validateParameters(path);
		log.debug("Getting file name from " + path);
		int index;
		index = findLastPathSeparator(path);
		if (index != -1)
		{
			return path.substring(++index);
		}
		index = findLastPathSeparator(path);
		if (index != -1)
		{
			return path.substring(++index);
		}
		throw new NoSuchElementException();
	}

	/**
	 * Gets the name of the first folder found in the path.
	 *
	 * @param path
	 *            The path.
	 * @return The first folder found in the path.
	 *
	 * @since 10/10/2009
	 */
	public static String getFirstFolder(String path)
	{
		Utils.validateParameters(path);
		log.debug("Getting first folder from path " + path);
		String folder = "";
		int idx;
		final File file = new File(path);
		if (file.isDirectory())
		{
			idx = findLastPathSeparator(path) != -1 ? findLastPathSeparator(path) : findLastPathSeparator(path);
			folder = path.substring(idx + 1);
			return folder;
		}
		else
		{
			idx = findLastPathSeparator(path) != -1 ? findLastPathSeparator(path) : findLastPathSeparator(path);
			folder = path.substring(0, idx);
			return getFirstFolder(folder);
		}
	}

	/**
	 * Gets the name of the first folder found in the path.
	 *
	 * @param path
	 *            The path.
	 * @return The first folder found in the path.
	 *
	 * @since 10/10/2009
	 */
	public static String getFirstFolderPath(String path)
	{
		Utils.validateParameters(path);
		log.debug("Getting first folder from path " + path);
		String folder = "";
		int idx;
		final File file = new File(path);
		if (file.isDirectory())
		{
			return path;
		}
		else
		{
			idx = findLastPathSeparator(path);
			folder = path.substring(0, idx);
			return getFirstFolderPath(folder);
		}
	}

	/**
	 * Determines if a file is of a specific type. It also verifies if the file
	 * is not hidden.
	 *
	 * @param file
	 *            The file to analyze.
	 * @param ext
	 *            The extension of the file that wants to be checked.
	 * @return <code>true</code> if the file has the extension passed as
	 *         parameter, <code>false</code> if not.
	 *
	 * @since 10/10/2009
	 */
	public static boolean isFileType(File file, String ext)
	{
		Utils.validateParameters(file, ext);
		log.debug("Analizing file type");
		final String path = file.getPath();
		if (!file.isDirectory() && !file.isHidden() && getFileExtension(path).equalsIgnoreCase(ext))
		{
			return true;
		}
		return false;
	}

	/**
	 * Determines if a file is type of fileType.
	 *
	 * @param path
	 *            The file path.
	 * @param fileType
	 *            The type.
	 * @return <code>true</code> if the file is type of fileType,
	 *         <code>false</code> if not.
	 *
	 * @since 10/10/2009
	 */
	public static boolean isFileType(String path, String fileType)
	{
		Utils.validateParameters(path, fileType);
		log.debug("Analizing file type");
		return getFileExtension(path).equalsIgnoreCase(fileType);
	}

	/**
	 * Loads a file in a {@link StringBuilder}.
	 *
	 * @param file
	 *            The file to be loaded
	 * @return {@link StringBuilder} with the file loaded
	 * @throws FileNotFoundException
	 *
	 * @since 10/10/2009
	 */
	public static StringBuilder loadFile(File file) throws FileNotFoundException
	{
		Utils.validateParameters(file);
		log.debug("Loading file " + file.getName() + "...");
		final StringBuilder loaded = new StringBuilder();
		try
		{
			String line;
			final Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				loaded.append(line);
				loaded.append(RETURN);
			}
		}
		catch (final FileNotFoundException e)
		{
			log.error("File " + file.getName() + " not found");
			throw new FileNotFoundException();
		}
		log.debug("Loading file " + file.getName() + " ended");
		return loaded;
	}

	/**
	 * Loads a file in a {@link StringBuilder}.
	 *
	 * @param file
	 *            The file to be loaded
	 * @return {@link StringBuilder} with the file loaded
	 * @throws FileNotFoundException
	 *
	 * @since 10/10/2009
	 */
	public static StringBuilder loadFile(String path) throws FileNotFoundException
	{
		Utils.validateParameters(path);
		log.debug("Loading file " + path + "...");
		final StringBuilder loaded = new StringBuilder();
		try
		{
			String line;
			final File file = new File(path);
			final Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
				line = scanner.nextLine();
				loaded.append(line);
				loaded.append(RETURN);
			}
		}
		catch (final FileNotFoundException e)
		{
			log.error("File " + path + " not found");
			throw new FileNotFoundException();
		}
		log.debug("Loading file " + path + " ended");
		return loaded;
	}

	/**
	 * Removes the extension from a file name.
	 *
	 * @param fileName
	 *            The name of the file.
	 * @return The name with no extension.
	 *
	 * @since 10/10/2009
	 */
	public static String removeFileExtension(String fileName)
	{
		Utils.validateParameters(fileName);
		log.debug("Removing extension from file name " + fileName);
		final int index = fileName.lastIndexOf(DOT);
		if (index != -1)
		{
			return fileName.substring(0, index);
		}
		else
		{
			return fileName;
		}
	}

	/**
	 * Recibe un archivo como {@link InputStream} y lo escribe en la ruta
	 * especificada como outPath.
	 *
	 * @param is
	 *            El archivo como input stream
	 * @param outPath
	 *            El path donde se escrbira el archivo
	 * @throws IOException
	 */
	public void writeFile(InputStream is, String outPath) throws IOException
	{
		Utils.validateParameters(is, outPath);
		int read = 0;
		final byte[] bytes = new byte[BUFFER];
		final BufferedInputStream bis = new BufferedInputStream(is, BUFFER);
		final FileOutputStream fos = new FileOutputStream(new File(outPath));
		final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
		while ((read = bis.read(bytes)) > 0)
		{
			bos.write(bytes, 0, read);
		}
		bos.flush();
		bos.close();
	}
}
