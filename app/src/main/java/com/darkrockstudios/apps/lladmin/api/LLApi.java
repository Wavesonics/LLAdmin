package com.darkrockstudios.apps.lladmin.api;

import com.darkrockstudios.apps.lladmin.api.data.AuthResponse;
import com.darkrockstudios.apps.lladmin.api.data.AuthRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;

import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Adam on 9/13/2014.
 */
public interface LLApi
{
	@POST("/auth")
	public AuthResponse auth( @Body AuthRequest authRequest );

	@POST("/launch/get")
	public LaunchGetResponse launchGet( @Body LaunchGetRequest authRequest );
}
