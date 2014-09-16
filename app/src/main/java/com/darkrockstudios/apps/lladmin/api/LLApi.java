package com.darkrockstudios.apps.lladmin.api;

import com.darkrockstudios.apps.lladmin.api.data.AuthRequest;
import com.darkrockstudios.apps.lladmin.api.data.AuthResponse;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Adam on 9/13/2014.
 */
public interface LLApi
{
	@POST("/auth")
	public Observable<AuthResponse> auth( @Body AuthRequest authRequest );

	@POST("/launch/get")
	public Observable<LaunchGetResponse> launchGet( @Body LaunchGetRequest authRequest );
}
