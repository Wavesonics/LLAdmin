package com.darkrockstudios.apps.lladmin.api.data.launchlibrary;

import java.io.Serializable;

/**
 * Created by Adam on 6/23/13.
 * Dark Rock Studios
 * darkrockstudios.com
 */

public class Mission implements Serializable
{
	public int    id;
	public Launch launch;
	public int    type;
	public String description;
	public String name;
	public String wikiURL;
	public String infoURL;
}
