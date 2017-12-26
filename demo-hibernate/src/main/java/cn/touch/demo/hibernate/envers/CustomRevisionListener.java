package cn.touch.demo.hibernate.envers;

import org.hibernate.envers.RevisionListener;


public class CustomRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
		// ex: authenticated user, LDAP, etc.
		customRevisionEntity.setModifiedBy( "brmeyer" );
		customRevisionEntity.setIpAddress( "127.0.0.1" );
	}

}
