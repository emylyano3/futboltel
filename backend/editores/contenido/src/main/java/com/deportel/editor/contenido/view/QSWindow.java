package com.deportel.editor.contenido.view;

import java.awt.Dimension;

import javax.swing.ListSelectionModel;

import org.jdesktop.layout.GroupLayout;

import com.deportel.editor.contenido.main.ContenidoEditor;
import com.deportel.editor.contenido.model.ProjectTexts;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Dialog;
import com.deportel.guiBuilder.gui.component.Window;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * @author Emy
 */
public class QSWindow extends Dialog
{
	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long			serialVersionUID	= 4029263642940010749L;

	private final GuiManager			manager;

	private static String				mode;

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static QSWindow instance;

	private QSWindow(GuiManager manager)
	{
		this.manager = manager;
		this.setTitle(ContenidoEditor.getTexts().get(ProjectTexts.QS_TITLE));
	}

	public static QSWindow getInstance(GuiManager manager, String mode)
	{
		if (instance == null)
		{
			instance = new QSWindow(manager);
		}
		QSWindow.mode = mode;
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void setCloseOperation ()
	{
		this.setDefaultCloseOperation(Window.DISPOSE_ON_CLOSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#arrange()
	 */
	@Override
	protected void arrange ()
	{
		arrangeWindow();
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#doSettings()
	 */
	@Override
	public void doSettings ()
	{
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.QS_GRID), "Panel se selección");
		this.manager.getScrollPane(R.SCROLL_PANES.QS_GRID).setViewportView(this.manager.getTable(R.TABLES.QS_GRID));
		setButtons();
		setGrid();
		this.setMaximumSize(new Dimension(350, 700));
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	/**
	 * 
	 */
	private void arrangeWindow ()
	{
		getLayout().setVerticalGroup(getLayout().createSequentialGroup().add(this.manager.getPanel(R.PANELS.QS_GENERAL)));
		getLayout().setHorizontalGroup(getLayout().createParallelGroup().add(this.manager.getPanel(R.PANELS.QS_GENERAL)));
		getLayout().setAutocreateContainerGaps(true);
		getLayout().setAutocreateGaps(true);
		arrangeGeneralPanel();
	}

	/**
	 * 
	 */
	private void arrangeGeneralPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.QS_GENERAL).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.QS_GRID))
				.add(this.manager.getPanel(R.PANELS.QS_BUTTONS))
		);

		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.QS_GRID))
				.add(this.manager.getPanel(R.PANELS.QS_BUTTONS))
		);
		arrangeButtonPanel();
		arrangeGridPanel();
	}

	/**
	 * 
	 */
	private void arrangeGridPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.QS_GRID).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.QS_GRID))
		);

		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.QS_GRID))
		);
	}

	/**
	 * 
	 */
	private void arrangeButtonPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.QS_BUTTONS).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getButton(R.BUTTONS.QS_EDIT))
				.add(this.manager.getButton(R.BUTTONS.QS_DELETE))
				.add(this.manager.getButton(R.BUTTONS.QS_CANCEL))
		);

		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getButton(R.BUTTONS.QS_EDIT))
				.add(this.manager.getButton(R.BUTTONS.QS_DELETE))
				.add(this.manager.getButton(R.BUTTONS.QS_CANCEL))
		);
	}

	/**
	 * 
	 */
	private void setButtons ()
	{
		if (R.BUTTONS.DELETE.equals(mode))
		{
			this.manager.getButton(R.BUTTONS.QS_DELETE).setVisible(true);
			this.manager.getButton(R.BUTTONS.QS_EDIT).setVisible(false);
		}
		else
		{
			this.manager.getButton(R.BUTTONS.QS_DELETE).setVisible(false);
			this.manager.getButton(R.BUTTONS.QS_EDIT).setVisible(true);
		}
	}

	/**
	 * 
	 */
	private void setGrid ()
	{
		if (R.BUTTONS.DELETE.equals(mode))
		{
			this.manager.getTable(R.TABLES.QS_GRID).setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		else
		{
			this.manager.getTable(R.TABLES.QS_GRID).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#init()
	 */
	@Override
	protected void init ()
	{
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#uninit()
	 */
	@Override
	protected void uninit ()
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#update()
	 */
	@Override
	public void update ()
	{

	}

}
