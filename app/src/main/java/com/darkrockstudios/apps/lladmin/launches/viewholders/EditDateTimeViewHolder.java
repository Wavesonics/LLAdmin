package com.darkrockstudios.apps.lladmin.launches.viewholders;

import android.view.View;
import android.widget.TextView;

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
	@InjectView(R.id.edit_row_datetime_label)
	public TextView m_labelView;
	@InjectView(R.id.edit_row_datetime_field)
	public TextView m_fieldView;
	//@InjectView(R.id.edit_row_datetime_set_button)
	//public Button   m_setButton;

	public EditDateTimeViewHolder( final View view, final Field field, final Object object )
	{
		super( field, object );
		ButterKnife.inject( this, view );
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
}
