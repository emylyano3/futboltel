package com.deportel.guiBuilder.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.deportel.common.exception.UserShowableException;
import com.deportel.guiBuilder.gui.component.Popup;
import com.deportel.guiBuilder.presentation.GuiBuilderPresentation;

public class ButtonListener implements ActionListener
{
	private final static Logger	log	= Logger.getLogger(ButtonListener.class);

	private final JTextField	tfProjectPath;
	private final JTextField	tfXmlFileName;
	private final JTextField	tfPackageName;

	public ButtonListener (JTextField projectPath, JTextField xmlFileName, JTextField packageName)
	{
		this.tfProjectPath = projectPath;
		this.tfXmlFileName = xmlFileName;
		this.tfPackageName = packageName;
	}

	@Override
	public void actionPerformed (ActionEvent event)
	{
		log.debug("Componente: " + event.getSource() + " Comando: " + event.getActionCommand());
		try
		{
			GuiBuilderPresentation.generateInterface(this.tfProjectPath.getText(), this.tfXmlFileName.getText(), this.tfPackageName.getText());
			Popup.getInstance("Se generó correctamente la interfaz", Popup.POPUP_INFO).showGui();
		}
		catch (UserShowableException e)
		{
			Popup.getInstance(e.getMessage(), Popup.POPUP_ERROR).showGui();
		}
	}
}
