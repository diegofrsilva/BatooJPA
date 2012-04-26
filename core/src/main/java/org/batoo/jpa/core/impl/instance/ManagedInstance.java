/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.batoo.jpa.core.impl.instance;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.metamodel.PluralAttribute;

import org.batoo.jpa.core.impl.SessionImpl;
import org.batoo.jpa.core.impl.types.EntityTypeImpl;
import org.batoo.jpa.core.impl.types.PluralAttributeImpl;

/**
 * The managed instance of {@link #instance}.
 * 
 * @author hceylan
 * @since $version
 */
public final class ManagedInstance<X> {

	/**
	 * The states for a managed instance
	 * 
	 * @author hceylan
	 * @since $version
	 */
	public enum Status {
		/**
		 * Instance is new
		 */
		NEW,

		/**
		 * Instance is managed
		 */
		MANAGED,

		/**
		 * Instance is removed
		 */
		REMOVED,

		/**
		 * Instance is detached
		 */
		DETACHED,
	}

	private final EntityTypeImpl<X> type;
	private final SessionImpl session;
	private final ManagedId<X> id;

	private Status status;
	private boolean executed;
	private boolean loaded;

	/**
	 * @param type
	 *            the type of the instance
	 * @param instance
	 *            the instance
	 * 
	 * @since $version
	 * @author hceylan
	 * @param session
	 */
	public ManagedInstance(EntityTypeImpl<X> type, SessionImpl session, ManagedId<X> id) {
		super();

		this.type = type;
		this.session = session;
		this.id = id;

		this.status = Status.MANAGED;

		// initialize the collections
		for (final PluralAttribute<? super X, ?, ?> attribute : type.getPluralAttributes()) {
			((PluralAttributeImpl<? super X, ?, ?>) attribute).initialize(this, session);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ManagedInstance<?> other = (ManagedInstance<?>) obj;
		if (!this.id.equals(other.id)) {
			return false;
		}
		if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns if any DML executed for the instance.
	 * 
	 * @return if any DML executed for the instance
	 * 
	 * @since $version
	 * @author hceylan
	 */
	public boolean executed() {
		return this.executed;
	}

	/**
	 * Fills the sequence / table generated values.
	 * 
	 * @since $version
	 * @author hceylan
	 */
	public void fillIdValues() {
		this.id.fillIdValues();
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 * 
	 * @since $version
	 * @author hceylan
	 */
	public ManagedId<? super X> getId() {
		return this.id;
	}

	/**
	 * Returns the instance.
	 * 
	 * @return the instance
	 * @since $version
	 */
	public X getInstance() {
		return this.id.getInstance();
	}

	/**
	 * Returns the session.
	 * 
	 * @return the session
	 * @since $version
	 */
	public SessionImpl getSession() {
		return this.session;
	}

	/**
	 * Returns the status of the managed instance.
	 * 
	 * @return the status
	 * @since $version
	 */
	public Status getStatus() {
		return this.status;
	}

	/**
	 * Returns the type.
	 * 
	 * @return the type
	 * @since $version
	 */
	public EntityTypeImpl<X> getType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + this.id.hashCode();
		result = (prime * result) + this.type.hashCode();
		return result;
	}

	/**
	 * Returns the loaded.
	 * 
	 * @return the loaded
	 * @since $version
	 */
	public boolean isLoaded() {
		return this.loaded;
	}

	/**
	 * Performs insert for the managed instance
	 * 
	 * @param connection
	 * @throws SQLException
	 * 
	 * @since $version
	 * @author hceylan
	 */
	public void performInsert(Connection connection) throws SQLException {
		this.type.performInsert(connection, this);
	}

	/**
	 * Sets the instance as executed DML.
	 * 
	 * @since $version
	 * @author hceylan
	 */
	public void setExecuted() {
		this.executed = true;
	}

	/**
	 * Sets the loaded.
	 * 
	 * @param loaded
	 *            the loaded to set
	 * @since $version
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Sets the instance's managed status.
	 * 
	 * @param managed
	 *            the new managed status
	 * 
	 * @since $version
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public String toString() {
		return "ManagedInstance [type=" + this.type.getName() + ", id=" + this.id + "]";
	}

}
