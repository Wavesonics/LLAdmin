package com.darkrockstudios.apps.lladmin.login;

import android.content.Context;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;

/**
 * Created by Adam on 9/14/2014.
 */
public class CredentailStorage
{
	private static final String BUCKET = "CredentailStorage";
	private static final String ID     = "current_user";

	public static void get( final Context context, final RetrievalCallback<Credentials> callback )
	{
		NoSQL.with( context ).using( Credentials.class )
		     .bucketId( BUCKET )
		     .entityId( ID )
		     .retrieve( callback );
	}

	public static void save( final String userName, final String password, final Context context )
	{
		NoSQLEntity<Credentials> entity = new NoSQLEntity<Credentials>( BUCKET, ID );
		Credentials data = new Credentials( userName, password );
		entity.setData( data );

		NoSQL.with( context ).using( Credentials.class ).save( entity );
	}
}
