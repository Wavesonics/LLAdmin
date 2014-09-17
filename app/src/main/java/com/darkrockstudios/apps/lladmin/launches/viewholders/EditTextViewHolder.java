package com.darkrockstudios.apps.lladmin.launches.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.darkrockstudios.apps.lladmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by adam on 9/16/2014.
 */
public class EditTextViewHolder
{
	@InjectView(R.id.edit_row_text_label)
	public TextView m_label;
	@InjectView(R.id.edit_row_text_field)
	public EditText m_field;

	public EditTextViewHolder( final View view )
	{
		ButterKnife.inject( this, view );
	}
}
