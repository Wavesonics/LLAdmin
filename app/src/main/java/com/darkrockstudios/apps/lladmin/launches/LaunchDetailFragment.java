package com.darkrockstudios.apps.lladmin.launches;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.api.LLApi;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;
import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.Launch;
import com.darkrockstudios.apps.lladmin.base.InjectedFragment;
import com.darkrockstudios.apps.lladmin.launches.viewholders.EditDateTimeViewHolder;
import com.darkrockstudios.apps.lladmin.launches.viewholders.EditTextViewHolder;
import com.darkrockstudios.apps.lladmin.launches.viewholders.FieldViewHolder;

import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LaunchDetailFragment extends InjectedFragment
{
	private static final String TAG = LaunchDetailFragment.class.getSimpleName();

	public static final String ARG_ITEM_ID = "item_id";

	private int      m_launchId;
	private MenuItem m_saveActionItem;

	private Launch m_item;

	private final List<FieldViewHolder> m_viewHolders;

	@Inject
	LLApi m_llApi;

	@InjectView(R.id.LAUNCH_DETAIL_edit_container)
	TableLayout m_launchDetailView;

	public LaunchDetailFragment()
	{
		m_viewHolders = new ArrayList<>();
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
	public void onViewCreated( final View view, final Bundle savedInstanceState )
	{
		updateViews();
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.fragment_launch_detail;
	}

	@Override
	public void onCreateOptionsMenu( final Menu menu, final MenuInflater inflater )
	{
		super.onCreateOptionsMenu( menu, inflater );

		inflater.inflate( R.menu.edit, menu );

		m_saveActionItem = menu.findItem( R.id.MENU_edit_save );
		//m_saveActionItem.setEnabled( false );
	}

	@Override
	public boolean onOptionsItemSelected( final MenuItem item )
	{
		final boolean handled;

		switch( item.getItemId() )
		{
			case R.id.MENU_edit_save:
				Toast.makeText( getActivity(), R.string.EDIT_saving_toast, Toast.LENGTH_SHORT ).show();
				handled = true;
				break;
			default:
				handled = super.onOptionsItemSelected( item );
				break;
		}

		return handled;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		final LaunchGetRequest request = new LaunchGetRequest( LaunchGetRequest.MODE_VERBOSE, m_launchId );
		final Observable<LaunchGetResponse> observable = m_llApi.launchGet( request );

		observable.subscribeOn( Schedulers.newThread() )
		          .observeOn( AndroidSchedulers.mainThread() )
		          .subscribe( new LaunchObserver() );

		/*
		// Hacky hardcoded bull
		m_item = new Launch();
		m_item.id = 12;
		m_item.inhold = false;
		m_item.name = "Test Launch";
		m_item.net = DateTime.now();
		m_item.status = 1;
		m_item.windowstart = DateTime.now();
		m_item.windowend = DateTime.now();
		//m_item.location
		*/

		updateViews();
	}

	private void updateViews()
	{
		m_viewHolders.clear();
		m_launchDetailView.removeAllViews();

		// Show the dummy content as text in a TextView.
		if( m_item != null )
		{
			final LayoutInflater inflater = LayoutInflater.from( getActivity() );

			final Field[] fields = Launch.class.getDeclaredFields();
			for( final Field field : fields )
			{
				final String name = field.getName();
				final Type type = field.getType();

				try
				{
					final int fieldModifiers = field.getModifiers();
					if( !Modifier.isTransient( fieldModifiers ) && !Modifier.isStatic( fieldModifiers ) )
					{
						if( type.equals( String.class ) )
						{
							final View view = inflater.inflate( R.layout.edit_row_text, m_launchDetailView, false );
							EditTextViewHolder holder = new EditTextViewHolder( view, field, m_item );
							view.setTag( holder );

							holder.m_labelView.setText( name );
							holder.m_fieldView.setText( field.get( m_item ).toString() );

							m_viewHolders.add( holder );
							m_launchDetailView.addView( view );
						}
						else if( type.equals( DateTime.class ) )
						{
							final View view = inflater.inflate( R.layout.edit_row_datetime, m_launchDetailView, false );
							EditDateTimeViewHolder holder = new EditDateTimeViewHolder( view, field, m_item );
							view.setTag( holder );

							holder.m_labelView.setText( name );
							holder.m_fieldView.setText( field.get( m_item ).toString() );
							holder.m_fieldView.setOnClickListener( new DateClickListener() );

							m_viewHolders.add( holder );
							m_launchDetailView.addView( view );
						}
					}
				}
				catch( final IllegalAccessException e )
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			//m_launchDetailView.setText( R.string.LAUNCHDETAIL_error );
		}
	}

	private class DateClickListener implements View.OnClickListener
	{
		@Override
		public void onClick( final View view )
		{
			DatePickerDialog dialog = DatePickerDialog.newInstance( new DateTimeListener(), 0, 0, 0 );
			dialog.show( getFragmentManager(), "date_picker" );
		}
	}

	private class DateTimeListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
	{
		@Override
		public void onDateSet( final DatePickerDialog datePickerDialog, final int year, final int month, final int day )
		{
			TimePickerDialog dialog = TimePickerDialog.newInstance( new DateTimeListener(), 0, 0, true );
			dialog.show( getFragmentManager(), "date_picker" );
		}

		@Override
		public void onTimeSet( final RadialPickerLayout radialPickerLayout, final int hours, final int minutes )
		{

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
			Log.d( TAG, "ERROR: " + e.toString() );

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

