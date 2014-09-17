package com.darkrockstudios.apps.lladmin.api.data.launchlibrary;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adam on 6/23/13.
 * Dark Rock Studios
 * darkrockstudios.com
 */

public class Pad implements Serializable
{
	public int          id;
	public String       name;
	public boolean      retired;
	public Location     location;
	public Double       longitude;
	public Double       latitude;
	public String       wikiURL;
	public String       infoURL;
	public String       mapURL;
	public List<Agency> agencies;
}
