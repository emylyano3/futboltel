package com.deportel.editor.template.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;
import org.jdesktop.layout.LayoutStyle;

import com.deportel.editor.template.controller.IEGuiController;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Dialog;
import com.deportel.guiBuilder.model.GuiManager;

/**
 * Clase singleton que representa la ventana principal del editor.
 * 
 * @author Emy
 * @since 10/01/2011
 */
public class SSWindow extends Dialog
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private SSWindow (GuiManager manager)
	{
		this.setTitle("Source Selector");
		this.manager = manager;
	}

	public static SSWindow getInstance(GuiManager manager)
	{
		if (instance == null)
		{
			instance = new SSWindow(manager);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long	serialVersionUID	= 1L;

	private static Logger		log					= Logger.getLogger(SSWindow.class);

	private final GuiManager	manager;

	private static SSWindow		instance;


	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	@Override
	public void arrange ()
	{
		log.debug("Acomodando los componentes en el workbench");
		GroupLayout gl = getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup(gl.createSequentialGroup().add(this.manager.getPanel(R.PANELS.EI_GLOBAL)));
		gl.setHorizontalGroup(gl.createParallelGroup().add(this.manager.getPanel(R.PANELS.EI_GLOBAL)));
		arrangeImageEditPanel();
	}

	private void arrangeImageEditPanel ()
	{
		log.debug("Acomodando los componentes en el panel de edicion de imagenes");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.EI_GLOBAL).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.EI_DETAILS_GENERAL))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.EI_DETAILS_GENERAL))
		);
		arrangeImageDetailsEditionPanel();
	}

	private void arrangeImageDetailsEditionPanel()
	{
		log.debug("Acomodando los componentes en el panel de edicion para la edicion de imagenes");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.EI_DETAILS_GENERAL).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.EI_DETAILS))
				.add(this.manager.getPanel(R.PANELS.EI_TOOLS))
		);
		gl.setHorizontalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getScrollPane(R.SCROLL_PANES.EI_DETAILS))
				.add(this.manager.getPanel(R.PANELS.EI_TOOLS))
		);
		arrangeImageEditToolPanel();
	}

	private void arrangeImageEditToolPanel()
	{
		log.debug("Acomodando los componentes en el panel de herramientas para la edicion de imagenes");
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.EI_TOOLS).getLayout();
		gl.setAutocreateGaps(true);
		gl.setAutocreateContainerGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getButton(R.BUTTONS.EI_ADD_IMAGE))
				.add(this.manager.getButton(R.BUTTONS.EI_DONE))
		);
		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getButton(R.BUTTONS.EI_ADD_IMAGE))
				.add(this.manager.getButton(R.BUTTONS.EI_DONE))
		);
		gl.linkSize(new Component[] {
				this.manager.getButton(R.BUTTONS.EI_ADD_IMAGE),
				this.manager.getButton(R.BUTTONS.EI_DONE)
		});
	}

	public void initImageEditionDetails (Set<Integer> itemsKeys)
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.EI_DETAILS).getLayout();
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		ParallelGroup pgGeneral = gl.createParallelGroup();
		SequentialGroup sgGeneral = gl.createSequentialGroup();

		SequentialGroup sgControlDetail;
		ParallelGroup pgItem;
		for (Integer i : itemsKeys)
		{
			sgControlDetail = gl.createSequentialGroup();

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getLabel(IEGuiController.LBL_WIDTH + "_" + i));
			pgItem.add(this.manager.getComboBox(IEGuiController.CBB_WIDTH + "_" + i));
			sgControlDetail.add(pgItem);

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getLabel(IEGuiController.LBL_HEIGHT + "_" + i));
			pgItem.add(this.manager.getComboBox(IEGuiController.CBB_HEIGHT + "_" + i));
			sgControlDetail.add(pgItem);

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getButton(IEGuiController.BTN_BROWSE + "_" + i));
			sgControlDetail.add(pgItem);

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getButton(IEGuiController.BTN_SEE + "_" + i));
			sgControlDetail.add(pgItem);

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getButton(IEGuiController.BTN_DELETE + "_" + i));
			sgControlDetail.add(pgItem);

			pgGeneral.add(sgControlDetail);

			sgControlDetail = gl.createSequentialGroup();

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getLabel(IEGuiController.LBL_WIDTH + "_" + i));
			pgItem.add(this.manager.getLabel(IEGuiController.LBL_HEIGHT + "_" + i));
			sgControlDetail.add(pgItem);

			pgItem = gl.createParallelGroup();
			pgItem.add(this.manager.getComboBox(IEGuiController.CBB_WIDTH + "_" + i));
			pgItem.add(this.manager.getComboBox(IEGuiController.CBB_HEIGHT + "_" + i));
			pgItem.add(this.manager.getButton(IEGuiController.BTN_BROWSE + "_" + i));
			pgItem.add(this.manager.getButton(IEGuiController.BTN_SEE + "_" + i));
			pgItem.add(this.manager.getButton(IEGuiController.BTN_DELETE + "_" + i));
			sgControlDetail.add(pgItem);

			sgGeneral.add(sgControlDetail);
			sgGeneral.addPreferredGap(LayoutStyle.RELATED, 15, 15);

			gl.linkSize(
					new Component[] {
							this.manager.getComboBox(IEGuiController.CBB_WIDTH + "_" + i),
							this.manager.getComboBox(IEGuiController.CBB_HEIGHT + "_" + i),
							this.manager.getButton(IEGuiController.BTN_SEE + "_" + i),
							this.manager.getButton(IEGuiController.BTN_BROWSE + "_" + i),
							this.manager.getButton(IEGuiController.BTN_DELETE + "_" + i)
					},
					GroupLayout.VERTICAL
			);
			gl.linkSize(
					new Component[] {
							this.manager.getComboBox(IEGuiController.CBB_WIDTH + "_" + i),
							this.manager.getComboBox(IEGuiController.CBB_HEIGHT + "_" + i),
							this.manager.getButton(IEGuiController.BTN_SEE + "_" + i),
							this.manager.getButton(IEGuiController.BTN_BROWSE + "_" + i),
							this.manager.getButton(IEGuiController.BTN_DELETE + "_" + i),
					},
					GroupLayout.HORIZONTAL
			);
		}
		gl.setHorizontalGroup(pgGeneral);
		gl.setVerticalGroup(sgGeneral);
		this.manager.getPanel(R.PANELS.EI_DETAILS).updateUI();
	}

	public void clearImageEditionDetails()
	{
		this.manager.getPanel(R.PANELS.EI_DETAILS).removeAll();
		this.manager.getPanel(R.PANELS.EI_DETAILS).updateUI();
	}

	@Override
	public void doSettings ()
	{
		setResizable(false);
		createPanelsBorder();
		setModalityType(ModalityType.APPLICATION_MODAL);
		setScrollpanelViewports();
		setPreferredDimensions();
		setMinimumDimensions();
	}

	private void setPreferredDimensions ()
	{
		setPreferredSize(new Dimension(600, 400));
	}

	private void setMinimumDimensions ()
	{
		setMinimumSize(new Dimension(600, 400));
	}

	private void createPanelsBorder ()
	{
		GuiUtils.addTitledBorder
		(
				this.manager.getPanel(R.PANELS.EI_DETAILS_GENERAL), "Imágenes del componente"
		);
	}

	private void setScrollpanelViewports ()
	{
		this.manager.getScrollPane(R.SCROLL_PANES.EI_DETAILS).setViewportView
		(
				this.manager.getPanel(R.PANELS.EI_DETAILS)
		);
	}

	@Override
	protected void init ()
	{
		this.isInitilized = true;
	}

	@Override
	protected void uninit ()
	{

	}

	@Override
	public void update ()
	{

	}

	@Override
	public void setCloseOperation ()
	{
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
}
