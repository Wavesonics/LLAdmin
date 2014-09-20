package com.darkrockstudios.apps.lladmin.launches.viewholders;

import android.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.api.data.launchlibrary.Launch;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adam on 9/16/2014.
 */
public class EditDateTimeViewHolder extends FieldViewHolder
{
	private final FragmentManager m_fragmentManager;

	@InjectView(R.id.edit_row_datetime_label)
	public TextView m_labelView;
	@InjectView(R.id.edit_row_datetime_field)
	public TextView m_fieldView;
	//@InjectView(R.id.edit_row_datetime_set_button)
	//public Button   m_setButton;

	public EditDateTimeViewHolder( final View view, final Field field, final Object object, final FragmentManager fragmentManager )
	{
		super( field, object );
		ButterKnife.inject( this, view );

		m_fragmentManager = fragmentManager;
	}

	@Override
	public void populate()
	{
		final String name = m_field.getName();

		try
		{
			DateTime dateTime = (DateTime) m_field.get( m_object );
			DateTimeFormatter formatter = DateTimeFormat.forPattern( Launch.DATE_FORMAT );

			m_labelView.setText( name );
			m_fieldView.setText( formatter.print( dateTime ) );
			m_fieldView.setOnClickListener( new DateClickListener() );
		}
		catch( final IllegalAccessException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void save()
	{
		try
		{
			DateTimeFormatter formatter = DateTimeFormat.forPattern( Launch.DATE_FORMAT );
			DateTime date = DateTime.parse( m_fieldView.getText().toString(), formatter );
			m_field.set( m_object, date );
		}
		catch( final IllegalAccessException e )
		{
			e.printStackTrace();
		}
	}

	private class DateClickListener implements View.OnClickListener
	{
		@Override
		public void onClick( final View view )
		{
			DatePickerDialog dialog = DatePickerDialog.newInstance( new DateTimeListener(), 0, 0, 0 );
			dialog.show( m_fragmentManager, "date_picker" );
		}
	}

	private class DateTimeListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
	{
		@Override
		public void onDateSet( final DatePickerDialog datePickerDialog, final int year, final int month, final int day )
		{
			TimePickerDialog dialog = TimePickerDialog.newInstance( new DateTimeListener(), 0, 0, true );
			dialog.show( m_fragmentManager, "date_picker" );
		}

		@Override
		public void onTimeSet( final RadialPickerLayout radialPickerLayout, final int hours, final int minutes )
		{

		}
	}
}
