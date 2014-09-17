package com.darkrockstudios.apps.lladmin.api.data;

/**
 * Created by Adam on 9/13/2014.
 */
public class LaunchGetRequest extends LLAdminRequest
{
	public static transient final String MODE_OVERVIEW = "overview";
	public static transient final String MODE_SUMMARY  = "summary";
	public static transient final String MODE_VERBOSE  = "verbose";

	public final String  mode;
	public final int     limit;
	public final Integer id;

	/**
	 * For getting a list of launches
	 *
	 * @param mode
	 * @param limit
	 * @param offset
	 */
	public LaunchGetRequest( final String mode, final int limit, final int offset )
	{
		super();

		this.mode = mode;
		this.limit = limit;

		this.id = null;
	}

	/**
	 * For getting a specific launch
	 *
	 * @param mode
	 * @param id
	 */
	public LaunchGetRequest( final String mode, final int id )
	{
		super();

		this.mode = mode;
		this.limit = 1;

		this.id = id;
	}
}
