package com.deportel.futboltel.editor.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.deportel.common.exception.PropertiesNotFoundException;
import com.deportel.editor.common.core.Editor;
import com.deportel.editor.common.core.EditorContainer;

public class EditorLoader
{
	private final EditorJarLoader jarLoader;

	public EditorLoader(EditorJarLoader finder)
	{
		this.jarLoader = finder;
	}

	public List<EditorDescription> getEditors (EditorContainer container) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		List<EditorDescription> result = new ArrayList<EditorDescription>();
		try
		{
			List<EditorJar> editorsJars = this.jarLoader.getEditorsJars();
			for (EditorJar editorJar : editorsJars)
			{
				URL jarUrl = editorJar.getJar().toURI().toURL();
				URLClassLoader classLoader = new URLClassLoader(new URL[] {jarUrl}, this.getClass().getClassLoader());
				String classToLoadName = editorJar.getJad().getProperty("editor.class");
				@SuppressWarnings("unchecked")
				Class<Editor> classToLoad = (Class<Editor>) Class.forName(classToLoadName, true, classLoader);
				@SuppressWarnings("unchecked")
				Class<EditorContainer>[] attTypes = new Class[]{EditorContainer.class};
				Method constructor = classToLoad.getMethod("getInstance", attTypes);
				Editor editor = (Editor) constructor.invoke(null, container);
				EditorDescription ed = new EditorDescription(editor, editorJar.getJad());
				result.add(ed);
			}
		}
		catch (PropertiesNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
