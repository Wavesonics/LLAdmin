package com.darkrockstudios.apps.lladmin.launches.viewholders;

import java.lang.reflect.Field;

/**
 * Created by adam on 9/17/2014.
 */
public abstract class FieldViewHolder
{
	public final Field  m_field;
	public final Object m_object;

	public FieldViewHolder( final Field field, final Object object )
	{
		m_field = field;
		m_object = object;
	}

	public abstract void populate();

	public abstract void save();
}
