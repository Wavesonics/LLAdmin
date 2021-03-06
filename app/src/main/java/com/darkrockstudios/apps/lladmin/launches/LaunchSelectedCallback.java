package com.darkrockstudios.apps.lladmin.launches;

/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item
 * selections.
 */
public interface LaunchSelectedCallback
{
	/**
	 * Callback for when an item has been selected.
	 */
	public void onItemSelected( int id );
}