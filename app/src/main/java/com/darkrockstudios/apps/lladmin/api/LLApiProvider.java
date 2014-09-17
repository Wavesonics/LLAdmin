package com.darkrockstudios.apps.lladmin.api;

import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.LaunchLibraryGson;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Adam on 9/13/2014.
 */
public final class LLApiProvider
{
	private static LLApi m_llApi;

	private LLApiProvider()
	{

	}

	public static synchronized LLApi get()
	{
		if( m_llApi == null )
		{
			RestAdapter restAdapter = new RestAdapter.Builder()
					                          .setEndpoint( "http://launchlibrary.net/lladmin/dev" )
					                          .setConverter( new GsonConverter( LaunchLibraryGson.create() ) )
					                          .build();
			m_llApi = restAdapter.create( LLApi.class );
		}

		return m_llApi;
	}
}
