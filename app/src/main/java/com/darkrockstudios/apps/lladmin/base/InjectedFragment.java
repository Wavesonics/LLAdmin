package com.darkrockstudios.apps.lladmin.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darkrockstudios.apps.lladmin.App;

import butterknife.ButterKnife;

/**
 * Created by Adam on 9/18/2014.
 * Injects Dagger and ButterKnife dependencies
 */
public abstract class InjectedFragment extends Fragment
{
	@Override
	public void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		App.objectGraph().inject( this );
	}

	@Nullable
	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState )
	{
		final View view = inflater.inflate( getLayoutId(), container, false );
		ButterKnife.inject( this, view );

		return view;
	}

	/**
	 * Return the ID of the layout to be inflated by {@link Fragment#onCreateView}
	 * @return
	 */
	protected abstract int getLayoutId();
}
