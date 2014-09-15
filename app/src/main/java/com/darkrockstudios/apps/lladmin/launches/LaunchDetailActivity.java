package com.darkrockstudios.apps.lladmin.launches;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.darkrockstudios.apps.lladmin.R;


/**
 * An activity representing a single Launch detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link LaunchListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link LaunchDetailFragment}.
 */
public class LaunchDetailActivity extends Activity
{
	@Override
	protected void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_launch_detail );

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled( true );

		if( savedInstanceState == null )
		{
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt( LaunchDetailFragment.ARG_ITEM_ID,
			                  getIntent().getIntExtra( LaunchDetailFragment.ARG_ITEM_ID, -1 ) );

			LaunchDetailFragment fragment = new LaunchDetailFragment();
			fragment.setArguments( arguments );
			getFragmentManager().beginTransaction()
			                    .add( R.id.launch_detail_container, fragment )
			                    .commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected( final MenuItem item )
	{
		int id = item.getItemId();
		if( id == android.R.id.home )
		{
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo( this, new Intent( this, LaunchListActivity.class ) );
			return true;
		}
		return super.onOptionsItemSelected( item );
	}
}
