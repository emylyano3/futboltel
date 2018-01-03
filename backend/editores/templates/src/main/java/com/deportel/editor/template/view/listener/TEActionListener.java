package com.deportel.editor.template.view.listener;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.deportel.editor.common.core.EditorActionListener;
import com.deportel.editor.template.controller.BaseGuiController;
import com.deportel.editor.template.controller.IEGuiController;
import com.deportel.editor.template.controller.GuiControllerFactory;
import com.deportel.editor.template.controller.TEGuiController;
import com.deportel.editor.template.controller.TIGuiController;
import com.deportel.editor.template.view.R;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class TEActionListener extends EditorActionListener
{
	/**
	 * @param manager
	 */
	public TEActionListener(GuiManager manager)
	{
		super(manager);
		this.controllerFactory = new GuiControllerFactory(manager);
	}

	public final GuiControllerFactory controllerFactory;

	private static String currentMode;

	@Override
	public void actionPerformed (ActionEvent e)
	{
		JComponent source = (JComponent) e.getSource();
		this.log.debug("Se presiono el componente [" + source.getName() + "] del editor de contenido");
		if (R.BUTTONS.TI_MODE.equals(source.getName()))
		{
			setCurrentMode(source);
			TIGuiController.getInstance(this.manager).enableMode();
		}
		else if (R.BUTTONS.CE_NEW_MODE.equals(source.getName()))
		{
			setCurrentMode(source);
			TEGuiController.getInstance(this.manager).enableMode();
		}
		else if (R.BUTTONS.CE_EDIT_MODE.equals(source.getName()))
		{
			setCurrentMode(source);
			TEGuiController.getInstance(this.manager).enableMode();
		}
		else if (R.BUTTONS.CE_DELETE_MODE.equals(source.getName()))
		{
			setCurrentMode(source);
			TEGuiController.getInstance(this.manager).enableMode();
		}
		else if (R.BINDED_COMBOS.SEL_TEMA.equals(source.getName()))
		{
			TEGuiController.getInstance(this.manager).selectionDone(R.BINDED_COMBOS.SEL_TEMA);
		}
		else if (R.BINDED_COMBOS.SEL_TIPO_COMPONENTE.equals(source.getName()))
		{
			TEGuiController.getInstance(this.manager).selectionDone(R.BINDED_COMBOS.SEL_TIPO_COMPONENTE);
		}
		else if (R.BINDED_COMBOS.SEL_COMPONENTE.equals(source.getName()))
		{
			TEGuiController.getInstance(this.manager).selectionDone(R.BINDED_COMBOS.SEL_COMPONENTE);
		}
		else if (R.BUTTONS.SAVE.equals(source.getName()))
		{
			BaseGuiController controller = this.controllerFactory.getController(currentMode);
			controller.save();
		}
		else if (R.BUTTONS.BROWSE.equals(source.getName()))
		{
			TEGuiController.getInstance(this.manager).browseFile();
		}
		else if (R.BUTTONS.OK_IMPORT.equals(source.getName()))
		{
			TIGuiController.getInstance(this.manager).importComponentsDescriptor();
		}
		else if (R.BUTTONS.CANCEL_IMPORT.equals(source.getName()))
		{
			TIGuiController.getInstance(this.manager).cancelComponentsImport();
		}
		else if (R.BUTTONS.LOAD_FILE.equals(source.getName()))
		{
			TIGuiController.getInstance(this.manager).loadComponentsDescriptorFile();
		}
		else if (R.BUTTONS.CANCEL_EDIT.equals(source.getName()))
		{
			this.manager.getBindedCombo(R.BINDED_COMBOS.SEL_COMPONENTE).setSelectedIndex(0);
		}
		else if (R.BUTTONS.EI_ADD_IMAGE.equals(source.getName()))
		{
			IEGuiController.getInstance(this.manager).addNewImageDetailControls();
		}
		else if (R.BUTTONS.EI_DONE.equals(source.getName()))
		{
			IEGuiController.getInstance(this.manager).sourcesSelectionDone();
		}
	}

	public static void setCurrentMode (JComponent source)
	{
		currentMode = source.getName();
	}
}
