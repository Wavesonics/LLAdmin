package com.darkrockstudios.apps.lladmin.api.data;

/**
 * Created by Adam on 9/13/2014.
 */
public class AuthRequest
{
	public final String user;
	public final String pass;

	public AuthRequest( final String user, final String pass )
	{
		this.user = user;
		this.pass = pass;
	}
}
