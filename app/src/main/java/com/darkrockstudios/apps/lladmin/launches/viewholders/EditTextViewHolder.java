package com.darkrockstudios.apps.lladmin.launches.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.darkrockstudios.apps.lladmin.R;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adam on 9/16/2014.
 */
public class EditTextViewHolder extends FieldViewHolder
{
	@InjectView(R.id.edit_row_text_label)
	public TextView m_labelView;
	@InjectView(R.id.edit_row_text_field)
	public EditText m_fieldView;

	public EditTextViewHolder( final View view, final Field field, final Object object )
	{
		super( field, object );
		ButterKnife.inject( this, view );
	}

	@Override
	public void save()
	{
		try
		{
			m_field.set( m_object, m_fieldView.getText().toString() );
		}
		catch( IllegalAccessException e )
		{
			e.printStackTrace();
		}
	}
}
