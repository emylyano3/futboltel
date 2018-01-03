package com.deportel.editor.template.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

import com.deportel.editor.template.controller.IEGuiController;
import com.deportel.editor.template.controller.TEGuiController;

public class SelectSourcesListener implements ActionListener
{
	private static final Logger log = Logger.getLogger(SelectSourcesListener.class);

	private final IEGuiController controller;
	private final TEGuiController owner;

	public SelectSourcesListener(TEGuiController owner, IEGuiController controller)
	{
		this.owner = owner;
		this.controller = controller;
	}

	@Override
	public void actionPerformed (ActionEvent e)
	{
		log.debug("Habilitando el controller de seleccion de imagenes");
		this.controller.setSelectedComponent(this.owner.getSelectedComponent());
		this.controller.enableMode();
	}
}