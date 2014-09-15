package com.darkrockstudios.apps.lladmin.api.data;

import com.darkrockstudios.apps.lladmin.TemporaryAuthToken;

/**
 * Created by Adam on 9/13/2014.
 */
public class LLAdminRequest
{
	public final String token;

	public LLAdminRequest()
	{
		this.token = TemporaryAuthToken.authToken;
	}
}
