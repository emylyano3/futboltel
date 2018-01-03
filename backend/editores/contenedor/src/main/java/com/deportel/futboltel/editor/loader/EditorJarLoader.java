package com.deportel.futboltel.editor.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.deportel.common.exception.PropertiesNotFoundException;
import com.deportel.common.utils.Utils;

public class EditorJarLoader
{
	private final Properties	properties;
	private final String		location;
	private final String		descExtension;
	private final static String	JAR_EXTENSION	= "jar";

	public EditorJarLoader (Properties properties)
	{
		this.properties = properties;
		this.location = this.properties.getProperty("EDITORS_LOCATION");
		this.descExtension= this.properties.getProperty("DESCRIPTOR_EXTENTION");
	}

	public List<EditorJar> getEditorsJars () throws PropertiesNotFoundException
	{
		List<File> jars = listFiles(JAR_EXTENSION);
		List<File> descriptors = listFiles(this.descExtension);
		Map<String, EditorJar> jarsMap = putAsMap(jars, descriptors);
		return new ArrayList<EditorJar>(jarsMap.values());
	}

	private Map<String, EditorJar> putAsMap (List<File> jars, List<File> descriptors) throws PropertiesNotFoundException
	{
		Map<String, EditorJar> map = new HashMap<String, EditorJar>();
		for (File desc : descriptors)
		{
			for (File jar : jars)
			{
				String jarName = getPureFileName(jar.getName());
				String descName = getPureFileName(desc.getName());
				if (jarName.equalsIgnoreCase(descName))
				{
					Properties p = Utils.loadProperties(desc.getPath());
					//TODO Verificar si existe el JAR y lanzar una ex si no
					EditorJar ej = new EditorJar(jar, p);
					map.put(getPureFileName(descName), ej);
				}
			}
		}
		return map;
	}

	private String getPureFileName (String fileName)
	{
		return Utils.removeFileExtension(fileName);
	}
	//
	//	private List<File> loadFiles (List<String> filesPaths)
	//	{
	//		List<File> files = new ArrayList<File>();
	//		for (String path : filesPaths)
	//		{
	//			files.add(new File(path));
	//		}
	//		// TODO LAnzar ex si algun archivo no existe
	//		return files;
	//	}

	private List<File> listFiles (String fileType)
	{
		File locationFolder = new File(this.location);
		List<File> result = new ArrayList<File>();
		if (locationFolder.exists() && locationFolder.isDirectory())
		{
			File[] files = locationFolder.listFiles();
			for (File file : files)
			{
				String extension = Utils.getFileExtension(file.getAbsolutePath());
				if (fileType.equalsIgnoreCase(extension))
				{
					result.add(file);
				}
			}
			return result;
		}
		return null;
	}
}
