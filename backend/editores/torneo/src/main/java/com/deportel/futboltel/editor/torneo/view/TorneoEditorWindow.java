package com.deportel.futboltel.editor.torneo.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.jdesktop.layout.GroupLayout;

import com.deportel.futboltel.common.callback.CallBackLauncher;
import com.deportel.futboltel.common.callback.CallBackListener;
import com.deportel.futboltel.editor.common.core.EditorContainer;
import com.deportel.futboltel.editor.common.core.EditorImpl;
import com.deportel.futboltel.editor.torneo.main.TorneoEditor;
import com.deportel.futboltel.editor.torneo.model.ProjectValues;
import com.deportel.futboltel.guiBuilder.gui.component.ImageRadioButton;
import com.deportel.futboltel.guiBuilder.gui.component.ToolBar;
import com.deportel.futboltel.guiBuilder.model.GuiManager;
import com.deportel.futboltel.guiBuilder.presentation.GuiBuilderPresentation;
import com.deportel.futboltel.persistencia.sp.torneo.MostrarFechasDeTorneo;
import com.deportel.futboltel.persistencia.sp.torneo.MostrarIdJugador;
import com.deportel.futboltel.persistencia.sp.torneo.MostrarPartidosPorFechayTorneo;
import com.deportel.futboltel.persistencia.sp.torneo.MostrarTorneos;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;


/**
 * Clase singleton que representa la ventana principal del editor.
 * 
 * @author Emy
 * @since 10/01/2011
 */

public class TorneoEditorWindow extends EditorImpl implements CallBackListener, ActionListener
{
	// *********************************************************************************************************************
	// Constructor Singleton
	// *********************************************************************************************************************

	public static TorneoEditorWindow getInstance (EditorContainer container)
	{
		if (instance == null)
		{
			instance = new TorneoEditorWindow(container);
		}
		return instance;
	}

	private TorneoEditorWindow(EditorContainer container)
	{
		this.container = container;
	}

	// *********************************************************************************************************************
	// Atributos
	// *********************************************************************************************************************

	private static final long			serialVersionUID	= 1L;

	private static Logger				log					= Logger.getLogger(TorneoEditorWindow.class);

	// private EditorLog userLog = EditorLog.getLogger(this);

	private Properties					properties			= TorneoEditor.getProperties();

	private String						XML_FILE_PATH		= this.properties.getProperty(ProjectValues.GUI_DEFINITION_FILE_PATH);

	private GuiManager					manager				= GuiBuilderPresentation.getManager(this.XML_FILE_PATH, TorneoEditorWindow.class);

	// private Text texts = TemplateEditor.getTexts();

	private static TorneoEditorWindow	instance;

	//	private Map<String, ImageIcon>		icons				= new HashMap<String, ImageIcon>();

	// *********************************************************************************************************************
	// Metodos Publicos
	// *********************************************************************************************************************

	/*
	 * (non-Javadoc)
	 * @see
	 * com.deportel.futboltel.common.gui.components.Window#update()
	 */
	public void arrangeComponents ()
	{
		this.CargarFiltroTorneo();

		GroupLayout panelLayout = new GroupLayout(this.workbench.getWorkPanel());
		this.workbench.getWorkPanel().setLayout(panelLayout);

		FlowLayout  layoutLabels = new FlowLayout(3,5,10);
		FlowLayout	layoutTablas = new FlowLayout(3,5,10);


		this.manager.getPanel(R.PANELS.PANEL_FILTROS).setLayout(layoutLabels);
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL).setLayout(layoutTablas);
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE).setLayout(layoutTablas);

		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getLabel(R.LABELS.LABEL_TORNEO));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getComboBox(R.COMBOBOXES.TORNEO));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getLabel(R.LABELS.LABEL_FECHA));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getComboBox(R.COMBOBOXES.FECHA));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getLabel(R.LABELS.LABEL_PARTIDO));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getComboBox(R.COMBOBOXES.PARTIDO));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL).add(this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE).add(this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getLabel(R.LABELS.LABEL_LOCAL));
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).add(this.manager.getLabel(R.LABELS.LABEL_VISITANTE));


		panelLayout.setAutocreateContainerGaps(true);
		panelLayout.setHorizontalGroup
		(
				panelLayout.createParallelGroup()

				.add(this.manager.getPanel(R.PANELS.PANEL_FILTROS))
				.add(this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS))
				.add(this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL))
				.add(this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE))
				.add(this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE))


		);
		panelLayout.setVerticalGroup
		(
				panelLayout.createSequentialGroup()

				.add(this.manager.getPanel(R.PANELS.PANEL_FILTROS))
				.add(this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS))
				.add(this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL))
				.add(this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE))
				.add(this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE))

		);

	}


	public void doSettings ()
	{
		log.debug("Se efectua el doSettings de la pantalla del Editor Torneo");
		//		initEditors();

		JScrollPane scrollLocal = new JScrollPane(this.manager.getTable(R.TABLES.TABLA_LOCAL));
		scrollLocal.setPreferredSize(new Dimension(450, 100));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL).add(scrollLocal);

		JScrollPane scrollVisitante = new JScrollPane(this.manager.getTable(R.TABLES.TABLA_VISITANTE));
		scrollVisitante.setPreferredSize(new Dimension(450, 100));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE).add(scrollVisitante);

		this.manager.getLabel(R.LABELS.LABEL_LOCAL).setVisible(false);
		this.manager.getLabel(R.LABELS.LABEL_VISITANTE).setVisible(false);
		this.manager.getPanel(R.PANELS.PANEL_FILTROS).setBorder(BorderFactory.createTitledBorder("Filtros"));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_LOCAL).setBorder(BorderFactory.createTitledBorder("Plantel Local"));
		this.manager.getPanel(R.PANELS.PANEL_EVENTOS_VISITANTE).setBorder(BorderFactory.createTitledBorder("Plantel Visitante"));
		this.manager.getComboBox(R.COMBOBOXES.FECHA).addActionListener(new AccionesCombo(this.manager.getComboBox(R.COMBOBOXES.TORNEO),this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS),this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE)));
		this.manager.getComboBox(R.COMBOBOXES.TORNEO).addActionListener(new AccionesCombo(this.manager.getComboBox(R.COMBOBOXES.TORNEO),this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS),this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE)));
		this.manager.getComboBox(R.COMBOBOXES.PARTIDO).addActionListener(new AccionesCombo(this.manager.getComboBox(R.COMBOBOXES.TORNEO),this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS),this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE)));
		this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA).addActionListener(new AccionesBoton(this.manager.getComboBox(R.COMBOBOXES.TORNEO),this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE),this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA),this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS),this.manager.getButton(R.BUTTONS.BOTON_REINICIAR),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE),this.manager.getTable(R.TABLES.TABLA_LOCAL),this.manager.getTable(R.TABLES.TABLA_VISITANTE)));
		this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS).addActionListener(new AccionesBotonEvento(this.manager.getComboBox(R.COMBOBOXES.TORNEO),this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getButton(R.BUTTONS.BOTON_VOLVER_FECHA),this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE),this.manager.getButton(R.BUTTONS.BOTON_INGRESAR_EVENTOS),this.manager.getButton(R.BUTTONS.BOTON_REINICIAR),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE),this.manager.getTable(R.TABLES.TABLA_LOCAL),this.manager.getTable(R.TABLES.TABLA_VISITANTE)));
		this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL).addActionListener(new AccionesBotonConfirmar(this.manager.getTable(R.TABLES.TABLA_LOCAL),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE),this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE)));
		this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE).addActionListener(new AccionesBotonConfirmar(this.manager.getTable(R.TABLES.TABLA_VISITANTE),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE), this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL), this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE)));
		this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE).addActionListener(new AccionesBotonActualizar(this.manager.getComboBox(R.COMBOBOXES.FECHA),this.manager.getComboBox(R.COMBOBOXES.PARTIDO),this.manager.getLabel(R.LABELS.LABEL_LOCAL),this.manager.getLabel(R.LABELS.LABEL_VISITANTE),this.manager.getTable(R.TABLES.TABLA_LOCAL),this.manager.getTable(R.TABLES.TABLA_VISITANTE),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_LOCAL),this.manager.getButton(R.BUTTONS.BOTON_GUARDAR_EVENTOS_VISITANTE),this.manager.getButton(R.BUTTONS.BOTON_ACTUALIZAR_BASE)));
	}

	public static class HibernateUtil{

		private static final SessionFactory sessionFactory;

		static {
			try {

				sessionFactory = new AnnotationConfiguration().configure("hibernate-Torneo.cfg.xml").buildSessionFactory();
			} catch (Throwable ex) {
				// Log exception!
				throw new ExceptionInInitializerError(ex);
			}
		}

		public static Session getSession() throws HibernateException {
			return sessionFactory.openSession();
		}
	}



	public void CargarFiltroTorneo()
	{

		String add;

		Session s = HibernateUtil.getSession();
		s.beginTransaction();

		String[] items = new String[] {"item1"};
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);

		model.removeAllElements();

		Query q = s.getNamedQuery("getMostrarTorneos");

		List<?> result = q.list();

		s.getTransaction().commit();
		s.close();

		Iterator<?> i = result.iterator();

		try{
			while (i.hasNext())
			{
				MostrarTorneos mostrartorneos = (MostrarTorneos) i.next();
				add = mostrartorneos.getTorneo().toString();

				log.debug(add);
				model.addElement(add.toString());

			}

			this.manager.getComboBox(R.COMBOBOXES.TORNEO).setModel(model);
		}catch(Exception e){
			e.printStackTrace();
		}

	}





	public void CargarFiltroFecha(String torneo,JComboBox fecha)
	{

		String add;

		JComboBox fec = fecha;

		Session s = HibernateUtil.getSession();
		s.beginTransaction();

		String[] items = new String[] {"item1"};
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);

		model.removeAllElements();

		Query q = s.getNamedQuery("getMostrarFechasDeTorneo");
		q.setString(0, torneo);

		List<?> result = q.list();

		s.getTransaction().commit();
		s.close();

		Iterator<?> i = result.iterator();

		try{
			while (i.hasNext())
			{
				MostrarFechasDeTorneo mostrarfechasdetorneo = (MostrarFechasDeTorneo) i.next();
				add = mostrarfechasdetorneo.getFechaNum().toString();
				model.addElement(add);

			}

			fec.setModel(model);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.deportel.futboltel.common.gui.utils.CallBackListener#getData
	 * ()
	 */
	public Object getData ()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @seecom.deportel.futboltel.common.gui.utils.CallBackListener#
	 * receiveCallBack(int)
	 */
	public void receiveCallBack (int action)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.utils.CallBackListener#receiveCallBack(java.lang.String, com.deportel.futboltel.common.gui.utils.CallBackLauncher)
	 */
	public void receiveCallBack (String action, CallBackLauncher laucher)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.common.gui.utils.CallBackListener#receiveCallBack(java.lang.String, java.lang.Object)
	 */
	public void receiveCallBack (String action, Object data)
	{

	}

	/*
	 * (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.Editor#getName()
	 */
	public String getName ()
	{
		return TorneoEditor.getName();
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.EditorImpl#getIconName(java.lang.String)
	 */
	@Override
	protected String getTryIconName (String type)
	{
		return this.properties.getProperty(type);
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.EditorImpl#getIconDescription(java.lang.String)
	 */
	@Override
	protected String getTryIconDescription (String type)
	{
		return this.properties.getProperty(ProjectValues.TRAY_ICON_DESCRIPTION);
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.Editor#getTrayButton()
	 */
	@Override
	public ImageRadioButton getTrayButton ()
	{
		if (this.trayIcon == null)
		{
			createTrayIcon();
		}
		return this.trayIcon;
	}

	class AccionesCombo implements ActionListener{

		JComboBox cb ;
		JComboBox fec;
		JComboBox part;
		JButton ing;
		JButton vol;
		JLabel loc;
		JLabel vis;
		JLabel lfe;
		JLabel lpa;

		public AccionesCombo(JComboBox torneo,JComboBox fecha,JComboBox partido,JButton ingresar,JButton volver,JLabel local,JLabel visitante) {
			this.cb = torneo;
			this.fec = fecha;
			this.part = partido;
			this.ing = ingresar;
			this.vol = volver;
			this.loc = local;
			this.vis = visitante;

		}

		public void actionPerformed(ActionEvent e) {


			if(this.cb.isEnabled())
			{
				this.CargarFiltroFecha(this.cb.getSelectedItem().toString(),this.fec);
				this.fec.setEnabled(true);
				this.cb.setEnabled(false);

			}else{
				if(this.fec.isEnabled()){
					this.CargarFiltroPartido(this.cb.getSelectedItem().toString(),Integer.parseInt(this.fec.getSelectedItem().toString()),this.part);
					this.fec.setEnabled(false);
					this.part.setEnabled(true);
				}else{
					if(this.part.isEnabled()){
						this.part.setEnabled(false);
						this.CargarLabelsOcultos(Integer.parseInt(this.part.getSelectedItem().toString()),this.loc,this.vis);
						this.ing.setEnabled(true);
					}
				}
			}

		}

		public void CargarLabelsOcultos(int partido,JLabel local,JLabel visitante){

			Connection conexion;
			String URL = "jdbc:mysql://localhost:3306/torneo";
			String driver = "com.mysql.jdbc.Driver";
			String user = "root";
			String pass = "root";

			CallableStatement cs;
			ResultSet rs = null;

			try {
				Class.forName(driver).newInstance();
				conexion = (Connection) DriverManager.getConnection(URL, user, pass);
				cs = (CallableStatement) conexion.prepareCall("{call torneo.MostrarEquiposDePartido(?)}");
				cs.setInt(1,partido);
				cs.executeQuery();
				rs = cs.getResultSet();
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

			try {
				while (rs.next()) {
					local.setText(rs.getString(1));
					visitante.setText(rs.getString(2));
					log.debug(local.getText());
					log.debug(visitante.getText());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void CargarFiltroPartido(String torneo,int fecha,JComboBox partido)
		{
			String add;
			JComboBox part = partido;

			Session s = HibernateUtil.getSession();
			s.beginTransaction();

			String[] items = new String[] {"item1"};
			DefaultComboBoxModel model = new DefaultComboBoxModel(items);

			model.removeAllElements();

			Query q = s.getNamedQuery("getMostrarPartidosPorFechayTorneo");
			q.setInteger(0, fecha);
			q.setString(1, torneo);

			List<?> result = q.list();

			s.getTransaction().commit();
			s.close();

			Iterator<?> i = result.iterator();

			try{
				while (i.hasNext())
				{
					MostrarPartidosPorFechayTorneo mostrarpartidosporfechaytorneo = (MostrarPartidosPorFechayTorneo) i.next();
					add = mostrarpartidosporfechaytorneo.getPartidos().toString();
					model.addElement(add);

				}

				part.setModel(model);
			}catch(Exception e){
				e.printStackTrace();
			}


		}

		public void CargarFiltroFecha(String torneo,JComboBox fecha)
		{
			String add;
			JComboBox fec = fecha;
			String[] items = new String[] {"item1"};
			DefaultComboBoxModel model = new DefaultComboBoxModel(items);

			model.removeAllElements();

			Connection conexion;
			String URL = "jdbc:mysql://localhost:3306/torneo";
			String driver = "com.mysql.jdbc.Driver";
			String user = "root";
			String pass = "root";

			CallableStatement cs;
			ResultSet rs = null;

			try {
				Class.forName(driver).newInstance();
				conexion = (Connection) DriverManager.getConnection(URL, user, pass);
				cs = (CallableStatement) conexion.prepareCall("{call torneo.MostrarFechasDeTorneo(?)}");
				cs.setString(1,torneo);
				cs.executeQuery();
				rs = cs.getResultSet();
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

			try {
				while (rs.next()) {
					add = rs.getString(1);
					log.debug(rs.getObject(1));
					log.debug(add);
					model.addElement(add);
				}
				fec.setModel(model);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}


	}


	class AccionesBoton implements ActionListener{

		JComboBox cb ;
		JComboBox fec;
		JComboBox part;
		JButton act;
		JButton vol;
		JButton ing;
		JButton rein;
		JButton confloc;
		JButton confvis;
		JLabel loc;
		JLabel vis;
		JTable jugloc;
		JTable jugvis;

		public AccionesBoton(JComboBox torneo,JComboBox fecha,JComboBox partido,JButton actualizar,JButton volver,JButton ingresar,JButton reiniciar,JButton conflocal,JButton confvisit,JLabel local,JLabel visitante,JTable juglocal,JTable jugvisit) {
			this.cb = torneo;
			this.fec = fecha;
			this.part = partido;
			this.act = actualizar;
			this.vol = volver;
			this.ing = ingresar;
			this.rein = reiniciar;
			this.loc = local;
			this.vis = visitante;
			this.confloc = conflocal;
			this.confvis = confvisit;
			this.jugloc = juglocal;
			this.jugvis = jugvisit;
		}

		public void actionPerformed(ActionEvent e) {

			this.act.setEnabled(false);

			if(this.fec.isEnabled())
			{
				this.fec.setEnabled(false);
				this.fec.removeAllItems();
				this.cb.setEnabled(true);

			}else{
				if (this.part.isEnabled()){
					this.part.setEnabled(false);
					this.part.removeAllItems();
					this.fec.setEnabled(true);
					this.ing.setEnabled(false);
				}else{
					if(!this.part.isEnabled()){
						this.ing.setEnabled(false);
						this.part.setEnabled(true);
					}
				}
			}
			this.confloc.setEnabled(false);
			this.confvis.setEnabled(false);
			this.LimpiarTablaPlantel(this.jugloc);
			this.LimpiarTablaPlantel(this.jugvis);
			this.jugloc.setVisible(false);
			this.jugvis.setVisible(false);


		}

		public void LimpiarTablaPlantel(JTable plantel){

			int cantcolumnas = plantel.getModel().getColumnCount();
			int cantfilas = plantel.getModel().getRowCount();
			int col;
			int fila;

			for(col=2;col<cantcolumnas;col++){
				for(fila=0 ;fila<cantfilas;fila++){
					plantel.getModel().setValueAt("0", fila, col);
				}
			}
		}

		public void CargarFiltroFecha(String torneo,JComboBox fecha)
		{
			String add;
			JComboBox fec = fecha;
			String[] items = new String[] {"item1"};
			DefaultComboBoxModel model = new DefaultComboBoxModel(items);

			model.removeAllElements();

			Connection conexion;
			String URL = "jdbc:mysql://localhost:3306/torneo";
			String driver = "com.mysql.jdbc.Driver";
			String user = "root";
			String pass = "root";

			CallableStatement cs;
			ResultSet rs = null;

			try {
				Class.forName(driver).newInstance();
				conexion = (Connection) DriverManager.getConnection(URL, user, pass);
				cs = (CallableStatement) conexion.prepareCall("{call torneo.MostrarFechasDeTorneo(?)}");
				cs.setString(1,torneo);
				cs.executeQuery();
				rs = cs.getResultSet();
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

			try {
				while (rs.next()) {
					add = rs.getString(1);
					log.debug(rs.getObject(1));
					log.debug(add);
					model.addElement(add);
				}
				fec.setModel(model);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}


	}


	class AccionesBotonEvento implements ActionListener{

		JComboBox cb ;
		JComboBox fec;
		JComboBox part;
		JButton vol;
		JButton act;
		JButton ing;
		JButton rein;
		JButton confloc;
		JButton confvis;
		JLabel loc;
		JLabel vis;
		JTable jugloc;
		JTable jugvis;

		public AccionesBotonEvento(JComboBox torneo,JComboBox fecha,JComboBox partido,JButton volver,JButton actualizar,JButton ingresar,JButton reiniciar,JButton conflocal,JButton confvisit,JLabel local,JLabel visitante,JTable juglocal,JTable jugvisit) {
			this.cb = torneo;
			this.fec = fecha;
			this.part = partido;
			this.vol = volver;
			this.act = actualizar;
			this.ing = ingresar;
			this.rein = reiniciar;
			this.loc = local;
			this.vis = visitante;
			this.jugloc = juglocal;
			this.jugvis = jugvisit;
			this.confloc = conflocal;
			this.confvis = confvisit;
		}

		public void actionPerformed(ActionEvent e) {

			this.CargarPlantel(Integer.parseInt(this.loc.getText()),this.jugloc);
			this.CargarPlantel(Integer.parseInt(this.vis.getText()),this.jugvis);
			this.confloc.setEnabled(true);
			this.confvis.setEnabled(true);
			this.jugloc.setEnabled(true);
			this.jugvis.setEnabled(true);
			this.jugloc.setVisible(true);
			this.jugvis.setVisible(true);
			this.act.setEnabled(false);

		}

		public void CargarPlantel(int equipo,JTable jugadores){

			Connection conexion;
			String URL = "jdbc:mysql://localhost:3306/torneo";
			String driver = "com.mysql.jdbc.Driver";
			String user = "root";
			String pass = "root";
			int colCount=0;
			int rowCount = 0;

			JComboBox goles = new JComboBox();
			goles.addItem("0");
			goles.addItem("1");
			goles.addItem("2");
			goles.addItem("3");
			goles.addItem("4");
			goles.addItem("5");
			goles.addItem("6");
			goles.addItem("7");
			goles.addItem("8");
			goles.addItem("9");
			goles.addItem("10");

			JComboBox amon = new JComboBox();
			amon.addItem("0");
			amon.addItem("1");
			amon.addItem("2");

			JComboBox expul = new JComboBox();
			expul.addItem("0");
			expul.addItem("1");

			CallableStatement cs;
			ResultSet rs = null;

			String[] columnas = new String [] {"Nombre", "Apellido","Goles","Amon","Expul"};

			try {
				Class.forName(driver).newInstance();
				conexion = (Connection) DriverManager.getConnection(URL, user, pass);
				cs = (CallableStatement) conexion.prepareCall("{call torneo.MostrarJugadores(?)}");
				cs.setInt(1,equipo);
				cs.executeQuery();
				rs = cs.getResultSet();
				colCount = rs.getMetaData().getColumnCount();
				String[] colName = new String[colCount];
				while(rs.next()){
					rowCount++;
				}

				rs.beforeFirst();

				Object [][] datos = new Object [rowCount][colCount];

				for(int col=0;col<colCount;col++){
					colName[col]= rs.getMetaData().getColumnLabel(col+1);
				}

				rs.beforeFirst();

				int row=0;
				while(rs.next()){
					for(int col=0;col<colCount;col++){
						datos[row][col]= rs.getObject(col+1);
					}
					row++;
				}

				jugadores.setModel(new javax.swing.table.DefaultTableModel(datos,columnas) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					boolean[] canEdit = new boolean [] {
							false,false,true,true,true,true
					};

					@Override
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return this.canEdit [columnIndex];
					}
				});

				TableColumn Cgoles = jugadores.getColumnModel().getColumn(2);
				TableColumn Camon = jugadores.getColumnModel().getColumn(3);
				TableColumn Cexpul = jugadores.getColumnModel().getColumn(4);
				Cgoles.setCellEditor(new DefaultCellEditor(goles));
				Camon.setCellEditor(new DefaultCellEditor(amon));
				Cexpul.setCellEditor(new DefaultCellEditor(expul));

				int cantcolumnas = jugadores.getModel().getColumnCount();
				int cantfilas = jugadores.getModel().getRowCount();
				int col;
				int fila;

				for(col=2;col<cantcolumnas;col++){
					for(fila=0 ;fila<cantfilas;fila++){
						jugadores.getModel().setValueAt("0", fila, col);
					}
				}


			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

		}




	}

	class AccionesBotonConfirmar implements ActionListener{

		JTable jug;
		JButton conf;
		JButton confotro;
		JButton act;

		public AccionesBotonConfirmar(JTable plantel,JButton confirmar,JButton confirmarotro,JButton actualizar) {
			this.jug = plantel;
			this.conf = confirmar;
			this.confotro = confirmarotro;
			this.act= actualizar;
		}

		public void actionPerformed(ActionEvent e) {

			this.jug.setEnabled(false);
			this.conf.setEnabled(false);

			if(!this.confotro.isEnabled()){
				this.act.setEnabled(true);
			}

		}


	}


	class AccionesBotonActualizar implements ActionListener{

		JButton confloc;
		JButton confvis;
		JButton actualizar;
		JTable jugloc;
		JTable jugvis;
		JLabel loc;
		JLabel vis;
		JComboBox part;
		JComboBox fec;

		public AccionesBotonActualizar(JComboBox fecha,JComboBox partido,JLabel local,JLabel visitante,JTable juglocal,JTable jugvisit,JButton conflocal,JButton confvisit,JButton actbase) {

			this.jugloc = juglocal;
			this.jugvis = jugvisit;
			this.confloc = conflocal;
			this.confvis = confvisit;
			this.actualizar = actbase;
			this.loc= local;
			this.vis = visitante;
			this.part = partido;
			this.fec = fecha;
		}

		public void actionPerformed(ActionEvent e) {

			//int cantcolumnas = jugloc.getModel().getColumnCount();
			int cantfilas = this.jugloc.getModel().getRowCount();
			int cantfilasvis = this.jugvis.getModel().getRowCount();
			//int col;
			int fila;
			int filavis;
			Integer goles;
			String nombre;
			String apellido;


			for(fila=0 ;fila<cantfilas;fila++){
				nombre = (String) this.jugloc.getModel().getValueAt(fila, 0);
				log.debug(nombre);
				apellido = (String) this.jugloc.getModel().getValueAt(fila, 1);

				goles =  Integer.parseInt((String) this.jugloc.getModel().getValueAt(fila, 2));
				log.debug("GOLES");
				log.debug(goles.toString());
				if(goles > 0){
					this.ActualizarDatosGol(goles,Integer.parseInt(this.loc.getText()),Integer.parseInt(this.part.getSelectedItem().toString()),Integer.parseInt(this.fec.getSelectedItem().toString()),nombre,apellido);
				}
			}

			for(filavis=0 ;filavis<cantfilasvis;filavis++){
				nombre = (String) this.jugvis.getModel().getValueAt(filavis, 0);
				log.debug(nombre);
				apellido = (String) this.jugvis.getModel().getValueAt(filavis, 1);

				goles =  Integer.parseInt((String) this.jugvis.getModel().getValueAt(filavis, 2));
				log.debug("GOLES");
				log.debug(goles.toString());
				if(goles > 0){
					this.ActualizarDatosGol(goles,Integer.parseInt(this.vis.getText()),Integer.parseInt(this.part.getSelectedItem().toString()),Integer.parseInt(this.fec.getSelectedItem().toString()),nombre,apellido);

				}
			}

			this.ActualizarTorneo(Integer.parseInt(this.part.getSelectedItem().toString()),Integer.parseInt(this.loc.getText()),Integer.parseInt(this.vis.getText()));


		}



		private void ActualizarTorneo(int partido, int local, int visitante) {
			Session s3 = HibernateUtil.getSession();
			s3.beginTransaction();

			Query q = s3.getNamedQuery("getActualizarTabla");
			q.setInteger(0, partido);
			q.setInteger(1, local);
			q.setInteger(2, visitante);

			q.executeUpdate();

			s3.getTransaction().commit();
			s3.close();



		}

		public void ActualizarDatosGol(Integer goles,int equipo,int partido,int fecha,String nombre,String apellido){

			Integer id;

			Session s = HibernateUtil.getSession();
			s.beginTransaction();

			Session s2 = HibernateUtil.getSession();
			s2.beginTransaction();

			Query q = s.getNamedQuery("getMostrarIdJugador");
			q.setString(1, nombre);
			log.debug(nombre);
			q.setString(0, apellido);

			List<?> result = q.list();

			log.debug("TAMAÑO");
			log.debug(result.size());

			s.getTransaction().commit();
			s.close();

			MostrarIdJugador mostraridjugador = (MostrarIdJugador) result.get(0);
			id = mostraridjugador.getId();
			log.debug(id.toString());

			q = s2.getNamedQuery("getIngresarEventoGol");
			q.setInteger(0, goles);
			q.setInteger(1, equipo);
			q.setInteger(2, partido);
			q.setInteger(3, fecha);
			q.setInteger(4, id);

			q.executeUpdate();

			s2.getTransaction().commit();
			s2.close();


		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.Editor#getMenuBar()
	 */
	public JMenuBar getMenuBar ()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.Editor#initWorkbench()
	 */
	public void initWorkbench ()
	{

	}

	/* (non-Javadoc)
	 * @see com.deportel.futboltel.editor.common.core.Editor#getToolBar()
	 */
	public ToolBar getToolBar ()
	{
		return null;
	}
}