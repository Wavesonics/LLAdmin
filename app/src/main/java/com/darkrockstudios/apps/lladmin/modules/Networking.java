package com.darkrockstudios.apps.lladmin.modules;

import com.darkrockstudios.apps.lladmin.api.LLApi;
import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.LaunchLibraryGson;
import com.darkrockstudios.apps.lladmin.launches.LaunchDetailFragment;
import com.darkrockstudios.apps.lladmin.launches.LaunchListFragment;
import com.darkrockstudios.apps.lladmin.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Adam on 9/18/2014.
 */
@Module(injects = {
		                  LoginActivity.class,
		                  LaunchListFragment.class,
		                  LaunchDetailFragment.class
})
public class Networking
{
	@Provides
	@Singleton
	public LLApi provideLLApi()
	{
		RestAdapter restAdapter = new RestAdapter.Builder()
				                          .setEndpoint( "http://launchlibrary.net/lladmin/dev" )
				                          .setConverter( new GsonConverter( LaunchLibraryGson.create() ) )
				                          .build();

		return restAdapter.create( LLApi.class );
	}
}
