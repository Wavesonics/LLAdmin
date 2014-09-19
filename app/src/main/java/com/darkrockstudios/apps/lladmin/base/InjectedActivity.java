package com.darkrockstudios.apps.lladmin.base;

import android.app.Activity;
import android.os.Bundle;

import com.darkrockstudios.apps.lladmin.App;

import butterknife.ButterKnife;

/**
 * Created by Adam on 9/18/2014.
 */
public abstract class InjectedActivity extends Activity
{
	@Override
	protected void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( getLayoutId() );
		App.objectGraph().inject( this );
		ButterKnife.inject( this );
	}

	/**
	 * Return the ID of the layout to be inflated during {@link Activity#onCreate}
	 * @return
	 */
	protected abstract int getLayoutId();
}
