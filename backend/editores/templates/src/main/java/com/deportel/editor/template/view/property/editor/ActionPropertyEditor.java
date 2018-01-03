package com.deportel.editor.template.view.property.editor;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

public class ActionPropertyEditor
{
	private static final long	serialVersionUID	= 1L;

	private final JDialog		dialog;
	private JButton				butDone;
	private List<JCheckBox>		controls;

	public ActionPropertyEditor(List<String> actions)
	{
		this.dialog = new JDialog();
		createControls(actions);
		arrangeControls();
	}

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
		for (JCheckBox control : this.controls)
		{
			pg.add(control);
			sg.add(control);
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

	private void createControls (List<String> actions)
	{
		this.controls = new ArrayList<JCheckBox>(actions.size());
		for (String action : actions)
		{
			JCheckBox check = new JCheckBox(action);
			check.setName(action);
			this.controls.add(check);
		}
		this.butDone = new JButton("Listo");
		this.butDone.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent e)
			{
				ActionPropertyEditor.this.dialog.dispose();
			}
		});
	}

	private String createResponse ()
	{
		StringBuilder sb = new StringBuilder();
		for (Iterator<JCheckBox> iterator = this.controls.iterator(); iterator.hasNext();)
		{
			JCheckBox control = iterator.next();
			sb.append(control.isSelected() ? control.getText() : "");
			sb.append(control.isSelected() && iterator.hasNext() ? "," : "");
		}
		if (sb.toString().endsWith(","))
		{
			return sb.toString().substring(0, sb.toString().length() - 1);
		}
		return sb.toString();
	}

	public void initialize (List<String> selectedControls)
	{
		for (String controlName : selectedControls)
		{
			for (JCheckBox control : this.controls)
			{
				if (controlName.equals(control.getName()))
				{
					control.setSelected(true);
				}
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
