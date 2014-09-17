package com.darkrockstudios.apps.lladmin.api.data.launchlibrary;

import java.io.Serializable;

/**
 * Created by Adam on 6/23/13.
 * Dark Rock Studios
 * darkrockstudios.com
 */

public class Rocket implements Serializable
{
	public int          id;
	public String       name;
	public String       wikiURL;
	public String       infoURL;
	public String       configuration;
	public RocketFamily family;
}
