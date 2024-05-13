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

package se.uu.ub.cora.gatekeeper.storage;

import se.uu.ub.cora.gatekeeper.user.AppToken;
import se.uu.ub.cora.gatekeeper.user.User;

/**
 * UserStorage is an interface that is used to get users from storage. It is mainly intended to be
 * used from loginsystems to check if a user is allowed to log into the system.
 * </p>
 * Instances of UserStorageView SHOULD be obtained through
 * {@link UserStorageProvider#getStorageView()} for each thread that needs access to
 * UserStorageView.
 */
public interface UserStorageView {

	/**
	 * getUserById is used to retreive a {@link User} from storage if the userid is known.
	 * <p>
	 * If no user for the specified id is found MUST a {@link UserStorageViewException} be thrown,
	 * indicating that the requested user can not be found.
	 * </p>
	 * 
	 * @param userId
	 *            A String with the users id
	 * @return A User populated with info from storage
	 */
	User getUserById(String userId);

	/**
	 * getUserById is used to retreive a {@link User} from storage using idFromLogin, this is
	 * normally a username from a common login system such as Swamid or LDAP.
	 * <p>
	 * If no user for the specified id is found MUST a {@link UserStorageViewException} be thrown,
	 * indicating that the requested user can not be found.
	 * </p>
	 * 
	 * @param idFromLogin
	 *            A String with the users uniquiely identifying username from an external login
	 *            system.
	 * @return A User populated with info from storage
	 */
	User getUserByIdFromLogin(String idFromLogin);

	/**
	 * getTokenById is used to retreive a {@link AppToken} from storage if the tokenId is known.
	 * <p>
	 * If no AppToken for the specified id is found MUST a {@link UserStorageViewException} be
	 * thrown, indicating that the requested AppToken can not be found.
	 * </p>
	 * 
	 * @param userId
	 *            A String with the appToken id
	 * @return A AppToken populated with info from storage
	 */
	AppToken getAppTokenById(String tokenId);

	/**
	 * doesPasswordMatchForUser method is intended to find out if the password of a User matches the
	 * one provided.
	 * 
	 * 
	 * @param user
	 *            A User. The user must have been read previouly using getUserById or
	 *            getUserByIdFromLogin
	 * @param password
	 *            A string containing the password to match
	 * @return A boolean that indicates wether the passwords match or not. If User has no password
	 *         then false is returned.
	 */
	boolean doesPasswordMatchForUser(User user, String password);
}
