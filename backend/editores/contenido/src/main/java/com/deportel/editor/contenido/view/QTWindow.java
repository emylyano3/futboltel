package com.deportel.editor.contenido.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import com.deportel.common.callback.CallBackLauncher;
import com.deportel.common.callback.CallBackListener;
import com.deportel.editor.contenido.controller.QTController;
import com.deportel.editor.contenido.main.ContenidoEditor;
import com.deportel.editor.contenido.model.ProjectTexts;
import com.deportel.guiBuilder.gui.GuiUtils;
import com.deportel.guiBuilder.gui.component.Dialog;
import com.deportel.guiBuilder.gui.component.Window;
import com.deportel.guiBuilder.model.GuiManager;
import com.deportel.persistencia.utils.ConsultaUtils;
import com.deportel.persistencia.utils.QueryParam;

/**
 * @author Emy
 */
public class QTWindow extends Dialog implements CallBackListener
{

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long				serialVersionUID	= 3827317597550611024L;

	private final GuiManager				manager;

	private final QTController				controller;

	private final List<JLabel>				inParamLabels		= new ArrayList<JLabel>();

	private final List<JFormattedTextField>	inParamTextFields	= new ArrayList<JFormattedTextField>();

	private Vector<String>					queryResultTableColumnNames;

	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	private static QTWindow instance;

	private QTWindow (GuiManager manager, QTController controller)
	{
		this.manager = manager;
		this.controller	= controller;
		this.setTitle(ContenidoEditor.getTexts().get(ProjectTexts.QT_TITLE));
	}

	public static QTWindow getInstance(GuiManager manager, QTController controller)
	{
		if (instance == null)
		{
			instance = new QTWindow(manager, controller);
		}
		return instance;
	}

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/**
	 * 
	 */
	public void setInParamsValues()
	{
		QueryParam param;
		for (int i = 0; i < this.inParamTextFields.size(); i++)
		{
			param = this.controller.getQueryInParams().get(i);
			param.setValue(this.inParamTextFields.get(i).getText());
		}
	}

	/**
	 * 
	 */
	public void dumpResultIntoGrid (List<?> data)
	{
		resetTable();
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.QT_RESULT).getModel();
		if (!data.isEmpty())
		{
			if (data.get(0) instanceof Object[])
			{
				for (Object name2 : data)
				{
					model.addRow((Object[]) name2);
				}
			}
			else
			{
				for (Object name2 : data)
				{
					model.addRow(new Object[] {name2});
				}
			}
		}
	}

	// *********************************************************************************************************************
	// Metodos Privados
	// *********************************************************************************************************************

	/**
	 * Prepara el formato de la tabla donde se volcaran los datos
	 * obtenidos de la ejecucion de la consulta configurada.
	 */
	private void prepareOutTable()
	{
		List<QueryParam> outParams = new ArrayList<QueryParam>(this.controller.getQueryOutParams().values());
		int columnCount = outParams.size();
		this.queryResultTableColumnNames = new Vector<String>(columnCount);
		QueryParam param;
		for (int i = 0; i < columnCount; i++)
		{
			param = getParamWithIndex(outParams, i);
			this.queryResultTableColumnNames.add(param.getName() == null ? "" : param.getName());
		}
	}

	private QueryParam getParamWithIndex (List<QueryParam> outParams, int index)
	{
		for (QueryParam queryParam : outParams)
		{
			if (queryParam.getIndex() == index)
			{
				return queryParam;
			}
		}
		return null;
	}

	/**
	 *
	 */
	private void resetTable ()
	{
		DefaultTableModel model = (DefaultTableModel) this.manager.getTable(R.TABLES.QT_RESULT).getModel();
		model.setDataVector(new Vector<Object>(), this.queryResultTableColumnNames);
	}

	@Override
	public void setCloseOperation ()
	{
		this.setDefaultCloseOperation(Window.DISPOSE_ON_CLOSE);
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#arrange()
	 */
	@Override
	protected void arrange ()
	{
		arrangeWindow();
		arrangeGeneralPanel();
		arrangeInParamsPanel();
		arrangeResultPanel();
	}

	/**
	 * 
	 */
	private void arrangeWindow ()
	{
		getLayout().setVerticalGroup(getLayout().createSequentialGroup().add(this.manager.getPanel(R.PANELS.QT_GENERAL)));
		getLayout().setHorizontalGroup(getLayout().createParallelGroup().add(this.manager.getPanel(R.PANELS.QT_GENERAL)));
		getLayout().setAutocreateContainerGaps(true);
		getLayout().setAutocreateGaps(true);
	}

	/**
	 * 
	 */
	private void arrangeGeneralPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.QT_GENERAL).getLayout();
		gl.setAutocreateGaps(true);
		gl.setVerticalGroup
		(
				gl.createSequentialGroup()
				.add(this.manager.getPanel(R.PANELS.QT_PARAMS))
				.add(this.manager.getPanel(R.PANELS.QT_RESULT))
				.add
				(
						gl.createParallelGroup()
						.add(this.manager.getButton(R.BUTTONS.QT_EJECUTAR))
				)
		);

		gl.setHorizontalGroup
		(
				gl.createParallelGroup()
				.add(this.manager.getPanel(R.PANELS.QT_PARAMS))
				.add(this.manager.getPanel(R.PANELS.QT_RESULT))
				.add
				(
						gl.createSequentialGroup()
						.add(this.manager.getButton(R.BUTTONS.QT_EJECUTAR))
				)
		);
	}

	/**
	 * 
	 */
	private void arrangeInParamsPanel ()
	{
		GroupLayout groupLayout = (GroupLayout) this.manager.getPanel(R.PANELS.QT_PARAMS_SCROLL).getLayout();
		this.manager.getPanel(R.PANELS.QT_PARAMS_SCROLL).removeAll();
		ParallelGroup parallelGroup = groupLayout.createParallelGroup();
		SequentialGroup sequentialGroup = groupLayout.createSequentialGroup();
		groupLayout.setAutocreateGaps(true);
		groupLayout.setAutocreateContainerGaps(true);

		if (this.inParamLabels == null || this.inParamLabels.isEmpty())
		{
			Font font = new Font("Arial", Font.BOLD, 22);
			JLabel label = new JLabel ("No hay parámetros de entrada");
			label.setFont(font);
			label.setForeground(Color.WHITE);
			sequentialGroup.add(label);
			parallelGroup.add(label);
			groupLayout.setVerticalGroup(sequentialGroup);
			groupLayout.setHorizontalGroup(parallelGroup);
		}
		else
		{
			for (int i = 0; i < this.inParamLabels.size(); i++)
			{
				sequentialGroup.add(groupLayout.createParallelGroup().add(this.inParamLabels.get(i)).add(this.inParamTextFields.get(i)));
			}
			for (int i = 0; i < this.inParamLabels.size(); i++)
			{
				parallelGroup.add(groupLayout.createSequentialGroup().add(this.inParamLabels.get(i)).add(this.inParamTextFields.get(i)));
			}
			groupLayout.linkSize(this.inParamLabels.toArray(new Component[this.inParamLabels.size()]), GroupLayout.HORIZONTAL);
			groupLayout.setVerticalGroup(sequentialGroup);
			groupLayout.setHorizontalGroup(parallelGroup);
			GroupLayout gl2 = (GroupLayout) this.manager.getPanel(R.PANELS.QT_PARAMS).getLayout();
			gl2.setVerticalGroup(gl2.createSequentialGroup().add(this.manager.getScrollPane(R.SCROLL_PANES.QT_PARAMS)));
			gl2.setHorizontalGroup(gl2.createParallelGroup().add(this.manager.getScrollPane(R.SCROLL_PANES.QT_PARAMS)));
			gl2.setAutocreateContainerGaps(true);
			gl2.setAutocreateGaps(true);
		}
	}

	/**
	 * 
	 */
	private void arrangeResultPanel ()
	{
		GroupLayout gl = (GroupLayout) this.manager.getPanel(R.PANELS.QT_RESULT).getLayout();

		gl.setVerticalGroup(gl.createSequentialGroup().add(this.manager.getScrollPane(R.SCROLL_PANES.QT_GRID)));
		gl.setHorizontalGroup(gl.createParallelGroup().add(this.manager.getScrollPane(R.SCROLL_PANES.QT_GRID)));
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#doSettings()
	 */
	@Override
	public void doSettings ()
	{
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.QT_PARAMS), "Parámetros de Entrada");
		GuiUtils.addTitledBorder(this.manager.getPanel(R.PANELS.QT_RESULT), "Resultado");
		this.manager.getScrollPane(R.SCROLL_PANES.QT_PARAMS).setViewportView(this.manager.getPanel(R.PANELS.QT_PARAMS_SCROLL));
		this.manager.getScrollPane(R.SCROLL_PANES.QT_GRID).setViewportView(this.manager.getTable(R.TABLES.QT_RESULT));
		this.manager.getPanel(R.PANELS.QT_PARAMS).setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
		this.setMaximumSize(new Dimension(350, 700));
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.guiBuilder.gui.component.Window#init()
	 */
	@Override
	protected void init ()
	{
		prepareOutTable();
		createInParamsComponents();
		resetTable();
	}

	/**
	 * 
	 */
	private void createInParamsComponents ()
	{
		this.inParamLabels.clear();
		this.inParamTextFields.clear();
		JFormattedTextField ftf;
		QueryParam qp;
		for (Iterator<QueryParam> it = this.controller.getQueryInParams().iterator(); it.hasNext();)
		{
			qp = it.next();
			this.inParamLabels.add(new JLabel(qp.getName()));
			if (ConsultaUtils.INTEGER.equals(qp.getType()))
			{
				ftf = new JFormattedTextField(GuiUtils.integerFormat);
			}
			else if (ConsultaUtils.VARCHAR.equals(qp.getType()))
			{
				ftf = new JFormattedTextField();
			}
			else if (ConsultaUtils.DATE.equals(qp.getType()))
			{
				ftf = new JFormattedTextField(GuiUtils.dateFormat);
			}
			else
			{
				ftf = new JFormattedTextField();
			}
			this.inParamTextFields.add(ftf);
		}
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

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.callback.CallBackListener#getData()
	 */
	@Override
	public Object getData ()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.callback.CallBackListener#receiveCallBack(int)
	 */
	@Override
	public void receiveCallBack (int data)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.callback.CallBackListener#receiveCallBack(java.lang.String, com.deportel.futboltel.common.callback.CallBackLauncher)
	 */
	@Override
	public void receiveCallBack (String action, CallBackLauncher laucher)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.callback.CallBackListener#receiveCallBack(java.lang.String, java.lang.Object)
	 */
	@Override
	public void receiveCallBack (String action, Object data)
	{

	}

}
