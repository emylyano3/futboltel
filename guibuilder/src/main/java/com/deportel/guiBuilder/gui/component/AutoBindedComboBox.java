package com.deportel.guiBuilder.gui.component;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 * Clase que extiene de {@link JComboBox} que posee la funcionalidad de bindeo
 * dinamico con una fuente de datos.</br> Especificamente soporta el enlazado
 * con una lista de elementos cualquiera. Existen dos posibilidades:</br>
 * <ul>
 * <li>La primera es que se enlace con una lista de componentes</br>
 * <li>La segunda es que se enlace con una lista de componentes especificando el
 * las propiedades que se desean enlazar.
 * <p/>
 * </ul>
 * <b>Por ejemplo:</b>
 * <ul>
 * <li>Se posee la clase Persona que tiene dos atributos, nombre y
 * apellido.</br>
 * <tt>AutoBindedComboBox bindedCombo = new {@link AutoBindedComboBox}<Persona>(personas, true);</tt>
 * Esto tiene como resultado que bindedCombo sea un combo que tiene como datos
 * desplegables, los datos existentes en la lista personas. El texto desplegado
 * en el combo, se obtiene llamando al toString de la clase Persona. Si se desea
 * un texto personalizado para mostrar los datos se deberá sobre escribir dicho
 * método.
 * <li>Se posee la misma clase Persona.</br>
 * <tt>AutoBindedComboBox bindedCombo = new {@link AutoBindedComboBox}<Persona>(personas, true, nombre);</tt>
 * Esto tiene como resultado que bindedCombo sea un combo que tiene como datos
 * desplegables, los datos existentes en la lista personas. El texto desplegado
 * en el combo, se obtiene llamando buscando en la clase del objeto que compone
 * la lista la property declarada. En este caso se buscará en la clase Persona,
 * la property nombre.
 * <p/>
 * <b>Aclaración!</b> Para que este segundo caso funcione correctamente, es
 * necesario que la clase Persona tenga todos los getters declarados para que se
 * pueda acceder a todos los atributos del componente.
 * </ul>
 * Si en el primer caso la implementacion del toString de la clase Persona fuera
 * <tt>return nombre</tt> el resultado seria identico al segundo caso.
 * 
 * @author Emy
 */
public class AutoBindedComboBox extends JComboBox
{

	// **************************************************************************************************
	// Constructor.
	// **************************************************************************************************

	public AutoBindedComboBox(List<?> list)
	{
		this(list, false);
	}

	public AutoBindedComboBox(List<?> list, boolean firstItemEmpty)
	{
		this.firstItemEmpty = firstItemEmpty;
		bindComboBoxWithList(list);
	}

	public AutoBindedComboBox(List<?> list, boolean firstItemEmpty, String...properties)
	{
		this.firstItemEmpty = firstItemEmpty;
		bindWithList(list, properties);
	}

	// **************************************************************************************************
	// Attributes.
	// **************************************************************************************************

	private static final long	serialVersionUID	= 1L;
	private boolean				firstItemEmpty		= false;
	private List<Object>		data;

	// **************************************************************************************************
	// Public Methods.
	// **************************************************************************************************

	/**
	 * 
	 * @return
	 */
	public boolean hasValidSelection ()
	{
		if (this.firstItemEmpty)
		{
			if (getSelectedIndex() > 0)
			{
				return true;
			}
		}
		else
		{
			if (getSelectedIndex() >= 0)
			{
				return true;
			}
		}
		return false;
	}

	// **************************************************************************************************
	// Private Methods.
	// **************************************************************************************************

	/**
	 * Realiza un bindeo dinámico entre el Combo y la lista de datos
	 * enviada. El texto de los datos mostrados en la lista desplegable del combo,
	 * se obtienen llamando al toString del objeto.
	 * 
	 * @param data
	 *            {@link List}
	 */
	@SuppressWarnings("unchecked")
	public void bindComboBoxWithList(List<?> data)
	{
		this.data = (List<Object>) data;
		final JComboBoxBinding<?, ?, JComboBox> binding = SwingBindings.createJComboBoxBinding
		(
				AutoBinding.UpdateStrategy.READ_WRITE,
				data,
				this
		);
		binding.bind();
		doSettings();
	}

	/**
	 * Realiza un bindeo dinámico entre el Combo y la lista de datos enviada. Los datos mostrados en la lista
	 * desplegable, serán aquellos listados en el parametro properties.<br/>
	 * <b>Ejemplo:</b>
	 * <ul>
	 * <li>Se posee la clase Persona con el siguiente formato:<br/>
	 * <tt>class Persona { private String nombre; private String apellido; } </br>
	 * </tt>
	 * Si se recibe una {@link List} de Persona, y <tt>nombre</tt> como listado de properties,
	 * cuando se depliegue el combo se mostraran los nombres de las personas de la lista.
	 * 
	 * @param data
	 *            {@link List}
	 * @param propertyName
	 *            El nombre de la property que se va a bindear
	 * @throws IllegalArgumentException
	 *             Si la property es <tt>null</tt>.
	 */
	@SuppressWarnings("unchecked")
	public void bindWithList(List<?> data, String...properties) throws IllegalArgumentException
	{
		this.data = (List<Object>) data;
		final JComboBoxBinding<?, ?, JComboBox> binding = SwingBindings.createJComboBoxBinding
		(
				AutoBinding.UpdateStrategy.READ_WRITE,
				data,
				this
		);
		AutoBindedComboRenderer renderer = new AutoBindedComboRenderer(properties);
		this.setRenderer(renderer);
		binding.bind();
		doSettings();
	}

	/**
	 * 
	 */
	private void doSettings ()
	{
		checkFirstItemSet();
	}

	/**
	 * 
	 */
	private void checkFirstItemSet ()
	{
		if (this.firstItemEmpty)
		{
			setFirstItemEmpty();
		}
	}

	/**
	 * 
	 */
	private void setFirstItemEmpty ()
	{
		List<Object> aux = new ArrayList<Object>();
		aux.add(null);
		aux.addAll(this.data);
		this.data.clear();
		this.data.addAll(aux);
		setSelectedIndex(OUT_OF_RANGE);
		this.updateUI();
	}

	// **************************************************************************************************
	// Getters & Setters.
	// **************************************************************************************************

	public void setFirstItemEmpty (boolean firstItemEmpty)
	{
		if (firstItemEmpty)
		{
			if (!this.firstItemEmpty)
			{
				setFirstItemEmpty();
			}
		}
		else
		{
			if (this.firstItemEmpty)
			{
				this.data.remove(0);
			}
		}
		this.firstItemEmpty = firstItemEmpty;
	}

	class AutoBindedComboRenderer extends JLabel implements ListCellRenderer
	{
		private static final long	serialVersionUID	= 894504944763145726L;

		private final List<String>	properties;

		private List<Method>		methods;

		private boolean				initialized			= false;

		public AutoBindedComboRenderer(String...properties)
		{
			this.properties = Arrays.asList(properties);
			setOpaque(true);
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			try
			{
				initProperties(value);
				setText(getRenderText(value));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * @throws NoSuchMethodException
		 * @throws SecurityException
		 */
		private void initProperties (Object value) throws SecurityException, NoSuchMethodException
		{
			if (!this.initialized && value != null)
			{
				Class<?> clazz = value.getClass();
				List<Field> aux = Arrays.asList(clazz.getDeclaredFields());
				this.methods = new ArrayList<Method>();
				Field field;
				Method getter;
				String getterName;
				for (Iterator<Field> it = aux.iterator(); it.hasNext();)
				{
					field = it.next();
					if (this.properties.contains(field.getName()))
					{
						getterName = "get" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1,field.getName().length());
						getter = clazz.getMethod(getterName, new Class<?>[] {});
						this.methods.add(getter);
					}
				}
				this.initialized = true;
			}
		}

		/**
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * @throws InvocationTargetException
		 */
		private String getRenderText (Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			if (value == null)
			{
				return " ";
			}
			Method field;
			String text = "";
			for (Iterator<Method> it = this.methods.iterator(); it.hasNext();)
			{
				field = it.next();
				text += field.invoke(value, new Object[] {});
				text += it.hasNext() ? " - " : "";
			}
			return text;
		}
	}

	public static final byte	OUT_OF_RANGE	= -1;
}
