package com.deportel.editor.template.view.property.editor;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

public class AlignPropertyEditor
{
	private static final long	serialVersionUID	= 1L;

	private final JDialog		dialog;
	private JButton				butDone;
	private List<JRadioButton>	controls;
	private ButtonGroup			buttonGroup;

	public AlignPropertyEditor(List<String> controlsNames)
	{
		this.dialog = new JDialog();
		createControls(controlsNames);
		arrangeControls();
	}

	//	private void arrangeControls ()
	//	{
	//		Container container = this.dialog.getContentPane();
	//		GroupLayout gl = new GroupLayout(container);
	//		gl.setAutocreateContainerGaps(true);
	//		gl.setAutocreateGaps(true);
	//		container.setLayout(gl);
	//		ParallelGroup pg = gl.createParallelGroup();
	//		SequentialGroup sg = gl.createSequentialGroup();
	//		for (JRadioButton control : this.controls)
	//		{
	//			pg.add(control);
	//			sg.add(control);
	//			this.buttonGroup.add(control);
	//		}
	//		gl.setHorizontalGroup(pg);
	//		gl.setVerticalGroup(sg);
	//	}

	private void arrangeControls ()
	{
		Container container = this.dialog.getContentPane();
		GroupLayout gl = new GroupLayout(container);
		container.setLayout(gl);
		gl.setAutocreateContainerGaps(true);
		gl.setAutocreateGaps(true);
		//Ordeno los check boxes
		SequentialGroup sg = gl.createSequentialGroup();
		ParallelGroup pg = gl.createParallelGroup();
		for (JRadioButton control : this.controls)
		{
			pg.add(control);
			sg.add(control);
			this.buttonGroup.add(control);
		}
		//Ordeno los botones
		SequentialGroup sg2 = gl.createSequentialGroup();
		ParallelGroup pg2 = gl.createParallelGroup();
		sg2.add(this.butDone);
		pg2.add(this.butDone);
		//Ordeno los grupos
		ParallelGroup pg3 = gl.createParallelGroup(GroupLayout.CENTER);
		SequentialGroup sg3 = gl.createSequentialGroup();
		pg3.add(pg).add(pg2);
		sg3.add(sg).add(sg2);
		//Seteo los grupos al layout
		gl.setHorizontalGroup(pg3);
		gl.setVerticalGroup(sg3);
	}

	private void createControls (List<String> controlsNames)
	{
		this.controls = new ArrayList<JRadioButton>(controlsNames.size());
		for (String controlName : controlsNames)
		{
			JRadioButton control = new JRadioButton(controlName);
			control.setName(controlName);
			this.controls.add(control);
		}
		this.butDone = new JButton("Listo");
		this.butDone.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent e)
			{
				AlignPropertyEditor.this.dialog.dispose();
			}
		});
		this.buttonGroup = new ButtonGroup();
	}

	private String createResponse ()
	{
		for (JRadioButton control : this.controls)
		{
			if (control.isSelected())
			{
				return control.getName();
			}
		}
		return "";
	}

	public void initialize (String selected)
	{
		for (JRadioButton control : this.controls)
		{
			if (selected.equals(control.getName()))
			{
				control.setSelected(true);
				return;
			}
		}
	}

	public String showDialog (Container parent)
	{
		this.dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		this.dialog.setLocationRelativeTo(parent);
		this.dialog.setResizable(false);
		this.dialog.pack();
		this.dialog.setVisible(true);
		return createResponse();
	}
}
