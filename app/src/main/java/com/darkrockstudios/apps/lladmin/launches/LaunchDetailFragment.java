package com.darkrockstudios.apps.lladmin.launches;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.api.LLApiProvider;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetRequest;
import com.darkrockstudios.apps.lladmin.api.data.LaunchGetResponse;
import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.Launch;
import com.darkrockstudios.apps.lladmin.launches.viewholders.EditDateTimeViewHolder;
import com.darkrockstudios.apps.lladmin.launches.viewholders.EditTextViewHolder;

import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;

public class LaunchDetailFragment extends Fragment
{
	public static final String ARG_ITEM_ID = "item_id";

	private int m_launchId;

	private Launch m_item;

	@InjectView(R.id.LAUNCH_DETAIL_edit_container)
	TableLayout m_launchDetailView;

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

		/*
		Hack to get around the broken API
		observable.subscribeOn( Schedulers.newThread() )
		          .observeOn( AndroidSchedulers.mainThread() )
		          .subscribe( new LaunchObserver() );
		          */

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

		updateViews();
	}

	private void updateViews()
	{
		m_launchDetailView.removeAllViews();

		// Show the dummy content as text in a TextView.
		if( m_item != null )
		{
			final LayoutInflater inflater = LayoutInflater.from( getActivity() );
			//m_launchDetailView.setText( m_item.toString() );

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
							EditTextViewHolder holder = new EditTextViewHolder( view );
							view.setTag( holder );

							holder.m_label.setText( name );
							holder.m_field.setText( field.get( m_item ).toString() );

							m_launchDetailView.addView( view );
						}
						else if( type.equals( DateTime.class ) )
						{
							final View view = inflater.inflate( R.layout.edit_row_datetime, m_launchDetailView, false );
							EditDateTimeViewHolder holder = new EditDateTimeViewHolder( view );
							view.setTag( holder );

							holder.m_label.setText( name );
							holder.m_field.setText( field.get( m_item ).toString() );
							holder.m_field.setOnClickListener( new DateClickListener() );

							m_launchDetailView.addView( view );
						}
					}
				}
				catch( final IllegalAccessException e )
				{
					e.printStackTrace();
				}
			}

			//m_launchDetailView.setText( test );
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

