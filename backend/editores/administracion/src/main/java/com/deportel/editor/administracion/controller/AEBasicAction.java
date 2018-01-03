package com.deportel.editor.administracion.controller;

import com.deportel.editor.administracion.exceptions.AdministracionException;


/**
 * @author Emy
 */
public interface AEBasicAction
{
	public void cancelAction ();

	public void prepareAdd ();

	public void validateAdd () throws AdministracionException;

	public void add ();

	public void prepareDelete ();

	public void validateDelete () throws AdministracionException;

	public void delete ();

	public void prepareModify ();

	public void validateModify () throws AdministracionException;

	public void modify ();
}
