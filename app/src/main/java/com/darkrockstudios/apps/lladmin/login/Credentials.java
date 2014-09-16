package com.darkrockstudios.apps.lladmin.login;

import se.simbio.encryption.Encryption;

/**
 * Created by Adam on 9/14/2014.
 */
public class Credentials
{
	private static final String ENCRYPTION_KEY = "TOTALLY_INSECURE_KEY_OH_WELL_$3creTQei";

	private final String m_userName;
	private final String m_password;

	public Credentials( final String userName, final String password )
	{
		final Encryption encryption = new Encryption();

		m_userName = encryption.encrypt( ENCRYPTION_KEY, userName );
		m_password = encryption.encrypt( ENCRYPTION_KEY, password );
	}

	public String userName()
	{
		final Encryption encryption = new Encryption();
		return encryption.decrypt( ENCRYPTION_KEY, m_userName );
	}

	public String password()
	{
		final Encryption encryption = new Encryption();
		return encryption.decrypt( ENCRYPTION_KEY, m_password );
	}
}
