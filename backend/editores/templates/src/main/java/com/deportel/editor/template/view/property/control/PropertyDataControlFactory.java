package com.deportel.editor.template.view.property.control;

import java.awt.Container;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.deportel.common.utils.Utils;
import com.deportel.componentes.controller.AccionComponenteController;
import com.deportel.componentes.controller.AlineacionComponenteController;
import com.deportel.componentes.controller.TipoPropiedadController;
import com.deportel.componentes.modelo.AccionComponente;
import com.deportel.componentes.modelo.AlineacionComponente;
import com.deportel.componentes.modelo.Propiedad;
import com.deportel.guiBuilder.gui.component.ComponentObserver;

public class PropertyDataControlFactory
{
	private static final long	serialVersionUID	= 1L;

	public static PropertyDataControl getControl (Container parent, Propiedad propiedad, ComponentObserver observer)
	{
		String propTypeXmlTag = propiedad.getTipoPropiedad().getDTagXml();
		Properties properties = new Properties();
		if (TipoPropiedadController.TAGS_TEXT_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return new TextPropertyControl(parent, properties, propiedad, observer);
		}
		if (TipoPropiedadController.TAGS_COLOR_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return new ColorPropertyControl(parent, properties, propiedad, observer);
		}
		if (TipoPropiedadController.INT_PROPERTY_TYPE_VALUES.containsKey(propTypeXmlTag))
		{
			return new DimensionPropertyControl(parent, properties, propiedad, observer);
		}
		if (TipoPropiedadController.TAGS_ACTION_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return createActionPropertyControl(parent, propiedad, observer, properties);
		}
		if (TipoPropiedadController.TAGS_ALIGN_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return createAlignPropertyControl(parent, propiedad, observer, properties);
		}
		if (TipoPropiedadController.TAGS_IMAGE_PROPERTIES.containsKey(propTypeXmlTag))
		{
			return createImagePropertyControl(parent, propiedad, observer, properties);
		}
		return new TextPropertyControl(parent, properties, propiedad, observer);
	}

	private static PropertyDataControl createActionPropertyControl (Container parent, Propiedad propiedad, ComponentObserver observer, Properties properties)
	{
		List<AccionComponente> acciones = AccionComponenteController.getInstance(true).findAll();
		List<String> nombresAcciones = AccionComponenteController.getInstance(true).getAsNames(acciones);
		properties.put(ActionPropertyControl.EDITOR_CONTROL_NAMES, nombresAcciones);
		String currentPropValue = propiedad.getDRegularData();
		String[] codes = currentPropValue.split(",");
		List<AccionComponente> actions = AccionComponenteController.getInstance(true).findInCodes(Arrays.asList(codes));
		List<String> names = AccionComponenteController.getInstance(true).getAsNames(actions);
		properties.put(ActionPropertyControl.CURRENT_VALUE, Utils.toString(names, ","));
		return new ActionPropertyControl(parent, properties, propiedad, observer);
	}

	private static PropertyDataControl createAlignPropertyControl (Container parent, Propiedad propiedad, ComponentObserver observer, Properties properties)
	{
		List<AlineacionComponente> aligns = AlineacionComponenteController.getInstance(true).findAll();
		List<String> alignsNames = AlineacionComponenteController.getInstance(true).getAsNames(aligns);
		properties.put(AlignPropertyControl.EDITOR_CONTROL_NAMES, alignsNames);
		AlineacionComponente currentAlign = AlineacionComponenteController.getInstance(true).findByCodigo(propiedad.getDRegularData());
		String currentAlignName = currentAlign == null ? "" : currentAlign.getDNombre();
		properties.put(AlignPropertyControl.CURRENT_VALUE, currentAlignName);
		return new AlignPropertyControl(parent, properties, propiedad, observer);
	}

	private static PropertyDataControl createImagePropertyControl (Container parent, Propiedad propiedad, ComponentObserver observer, Properties properties)
	{
		return new ImagePropertyControl(parent, properties, propiedad, observer);
	}
}
