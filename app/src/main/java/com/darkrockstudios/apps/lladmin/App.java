package com.darkrockstudios.apps.lladmin;

import android.app.Application;

import com.darkrockstudios.apps.lladmin.modules.Networking;

import dagger.ObjectGraph;

/**
 * Created by Adam on 9/18/2014.
 */
public class App extends Application
{
	private static ObjectGraph s_objectGraph;

	@Override
	public void onCreate()
	{
		super.onCreate();

		s_objectGraph = ObjectGraph.create( new Networking() );
	}

	public static ObjectGraph objectGraph()
	{
		return s_objectGraph;
	}
}
