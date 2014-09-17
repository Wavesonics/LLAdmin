package com.darkrockstudios.apps.lladmin.launches.viewholders;

import android.view.View;
import android.widget.TextView;

import com.darkrockstudios.apps.lladmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adam on 9/16/2014.
 */
public class EditDateTimeViewHolder
{
	@InjectView(R.id.edit_row_datetime_label)
	public TextView m_label;
	@InjectView(R.id.edit_row_datetime_field)
	public TextView m_field;
	//@InjectView(R.id.edit_row_datetime_set_button)
	//public Button   m_setButton;

	public EditDateTimeViewHolder( final View view )
	{
		ButterKnife.inject( this, view );
	}
}
