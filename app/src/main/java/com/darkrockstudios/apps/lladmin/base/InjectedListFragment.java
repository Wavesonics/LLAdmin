package com.darkrockstudios.apps.lladmin.base;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;

import com.darkrockstudios.apps.lladmin.App;

import butterknife.ButterKnife;

/**
 * Created by Adam on 9/18/2014.
 */
public abstract class InjectedListFragment extends ListFragment
{
	@Override
	public void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		App.objectGraph().inject( this );
	}

	@Override
	public void onViewCreated( final View view, final Bundle savedInstanceState )
	{
		super.onViewCreated( view, savedInstanceState );
		ButterKnife.inject( this, view );
	}
}
