/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.itba.pod.tp.interfaces;

import java.rmi.RemoteException;

/**
 *
 * @author mariano
 */
public class PlayerLooserException extends RemoteException
{

	public PlayerLooserException(String s)
	{
		super(s);
	}
	
}
