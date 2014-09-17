package com.darkrockstudios.apps.lladmin.api.data.launchlibrary;

import java.util.Collection;

/**
 * Created by Adam on 8/19/13.
 * Dark Rock Studios
 * darkrockstudios.com
 */
public class Location
{
	public int             id;
	public String          name;
	public String          infoURL;
	public String          wikiURL;
	public String          countryCode;
	public Collection<Pad> pads;
}
