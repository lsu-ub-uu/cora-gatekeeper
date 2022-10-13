/*
 * Copyright 2017 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.gatekeeper.user;

/**
 * UserStorage is an interface that is used to get users from storage. It is mainly intended to be
 * used from loginsystems to check if a user is allowed to log into the system.
 */
public interface UserStorageView {

	/**
	 * getUserById is used to retreive a user from storage if the userid is known.
	 * 
	 * @param userId
	 *            A String with the users id
	 * @return A user represented as a DataGroup
	 */
	User getUserById(String userId);

	/**
	 * getUserById is used to retreive a user from storage using idFromLogin, this is normally a
	 * username from a common login system such as Swamid or LDAP.
	 * 
	 * @param idFromLogin
	 *            A String with the users uniquiely identifying username from an external login
	 *            system.
	 * @return A user represented as a DataGroup
	 */
	User getUserByIdFromLogin(String idFromLogin);
}
