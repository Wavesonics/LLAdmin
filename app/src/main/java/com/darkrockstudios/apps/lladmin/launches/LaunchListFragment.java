package com.darkrockstudios.apps.lladmin.launches;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.darkrockstudios.apps.lladmin.api.LLApiProvider;
import com.darkrockstudios.apps.lladmin.api.data.Launch;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;

import java.util.ArrayList;

/**
 * A list fragment representing a list of Launches. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link LaunchDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link LaunchSelectedCallback}
 * interface.
 */
public class LaunchListFragment extends ListFragment
{
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private LaunchSelectedCallback m_launchSelectedCallback = sDummyLaunchSelectedCallback;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int m_activatedPosition = ListView.INVALID_POSITION;

	/**
	 * A dummy implementation of the {@link LaunchSelectedCallback} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static LaunchSelectedCallback sDummyLaunchSelectedCallback = new LaunchSelectedCallback()
	{
		@Override
		public void onItemSelected( final int id )
		{
		}
	};

	private ArrayAdapter<Launch> m_adapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LaunchListFragment()
	{
	}

	@Override
	public void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		m_adapter = new ArrayAdapter<Launch>(
				                                    getActivity(),
				                                    android.R.layout.simple_list_item_activated_1,
				                                    android.R.id.text1,
				                                    new ArrayList<Launch>() );
		setListAdapter( m_adapter );
	}

	@Override
	public void onViewCreated( final View view, final Bundle savedInstanceState )
	{
		super.onViewCreated( view, savedInstanceState );

		// Restore the previously serialized activated item position.
		if( savedInstanceState != null
		    && savedInstanceState.containsKey( STATE_ACTIVATED_POSITION ) )
		{
			setActivatedPosition( savedInstanceState.getInt( STATE_ACTIVATED_POSITION ) );
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		final LoadLaunchListTask loadLaunchListTask = new LoadLaunchListTask();

		final LaunchGetRequest request = new LaunchGetRequest( "overview", 10, 0 );
		loadLaunchListTask.execute( request );
	}

	@Override
	public void onAttach( final Activity activity )
	{
		super.onAttach( activity );

		// Activities containing this fragment must implement its callbacks.
		if( !(activity instanceof LaunchSelectedCallback) )
		{
			throw new IllegalStateException( "Activity must implement fragment's callbacks." );
		}

		m_launchSelectedCallback = (LaunchSelectedCallback) activity;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		m_launchSelectedCallback = sDummyLaunchSelectedCallback;
	}

	@Override
	public void onListItemClick( final ListView listView, final View view, final int position, final long id )
	{
		super.onListItemClick( listView, view, position, id );

		final Launch launch = m_adapter.getItem( position );
		m_launchSelectedCallback.onItemSelected( launch.id );
	}

	@Override
	public void onSaveInstanceState( final Bundle outState )
	{
		super.onSaveInstanceState( outState );
		if( m_activatedPosition != ListView.INVALID_POSITION )
		{
			// Serialize and persist the activated item position.
			outState.putInt( STATE_ACTIVATED_POSITION, m_activatedPosition );
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick( final boolean activateOnItemClick )
	{
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode( activateOnItemClick
		                             ? ListView.CHOICE_MODE_SINGLE
		                             : ListView.CHOICE_MODE_NONE );
	}

	private void setActivatedPosition( final int position )
	{
		if( position == ListView.INVALID_POSITION )
		{
			getListView().setItemChecked( m_activatedPosition, false );
		}
		else
		{
			getListView().setItemChecked( position, true );
		}

		m_activatedPosition = position;
	}

	private class LoadLaunchListTask extends AsyncTask<LaunchGetRequest, Void, LaunchGetResponse>
	{
		@Override
		protected LaunchGetResponse doInBackground( final LaunchGetRequest... launchGetRequests )
		{
			return LLApiProvider.get().launchGet( launchGetRequests[ 0 ] );
		}

		@Override
		protected void onPostExecute( final LaunchGetResponse launchGetResponse )
		{
			m_adapter.clear();
			m_adapter.addAll( launchGetResponse.launches );
			m_adapter.notifyDataSetChanged();
		}
	}
}