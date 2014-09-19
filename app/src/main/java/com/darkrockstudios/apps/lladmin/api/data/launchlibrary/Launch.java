package com.darkrockstudios.apps.lladmin.api.data.launchlibrary;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Adam on 6/23/13.
 * Dark Rock Studios
 * darkrockstudios.com
 */

public class Launch implements Serializable
{
	public transient static final String DATE_FORMAT = "MMM dd, yyyy HH:mm:ss zzz";

	public int                 id;
	public int                 status;
	public DateTime            windowstart;
	public DateTime            windowend;
	public String              name;
	public int                 inhold;
	public DateTime            net;
	public Collection<Mission> missions;
	public Rocket              rocket;
	public Location            location;

	public String toString()
	{
		return name;
	}
}
