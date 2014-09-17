package com.darkrockstudios.apps.lladmin.launches;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.api.LLApiProvider;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;
import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.Launch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LaunchDetailFragment extends Fragment
{
	public static final String ARG_ITEM_ID = "item_id";

	private int m_launchId;

	private Launch m_item;

	@InjectView(R.id.launch_detail)
	TextView m_launchDetailView;

	public LaunchDetailFragment()
	{
	}

	@Override
	public void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		if( getArguments().containsKey( ARG_ITEM_ID ) )
		{
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			m_launchId = getArguments().getInt( ARG_ITEM_ID );
		}
	}

	@Override
	public View onCreateView( final LayoutInflater inflater, final ViewGroup container,
	                          final Bundle savedInstanceState )
	{
		final View rootView = inflater.inflate( R.layout.fragment_launch_detail, container, false );
		ButterKnife.inject( this, rootView );

		updateViews();

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		final LaunchGetRequest request = new LaunchGetRequest( LaunchGetRequest.MODE_VERBOSE, m_launchId );
		final Observable<LaunchGetResponse> observable = LLApiProvider.get().launchGet( request );

		observable.subscribeOn( Schedulers.newThread() )
		          .observeOn( AndroidSchedulers.mainThread() )
		          .subscribe( new LaunchObserver() );
	}

	private void updateViews()
	{
		// Show the dummy content as text in a TextView.
		if( m_item != null )
		{
			m_launchDetailView.setText( m_item.toString() );
		}
		else
		{
			m_launchDetailView.setText( R.string.LAUNCHDETAIL_error );
		}
	}

	private class LaunchObserver implements Observer<LaunchGetResponse>
	{
		@Override
		public void onCompleted()
		{

		}

		@Override
		public void onError( final Throwable e )
		{
			updateViews();
		}

		@Override
		public void onNext( final LaunchGetResponse launchGetResponse )
		{
			if( launchGetResponse != null
			    && launchGetResponse.launches != null
			    && launchGetResponse.launches.size() > 0 )
			{
				m_item = launchGetResponse.launches.get( 0 );
			}

			updateViews();
		}
	}
}
