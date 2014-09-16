package com.darkrockstudios.apps.lladmin.api.data;

/**
 * Created by Adam on 9/13/2014.
 */
public class Launch
{
	public int id;
	public String name;

	@Override
	public String toString()
	{
		return id + " : " + name;
	}
}
