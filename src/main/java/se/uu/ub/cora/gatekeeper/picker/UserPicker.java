/*
 * Copyright 2016 Uppsala University Library
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

package se.uu.ub.cora.gatekeeper.picker;

import se.uu.ub.cora.gatekeeper.user.User;

/**
 * UserPicker is used to be able to have different strategies for how to pick and load information
 * about a user, based on known information about that user.
 * </p>
 * One implementation could only load specific users from storage, but another could load a one
 * generic user and add extra settings based on the provided information.
 *
 */
public interface UserPicker {
	/**
	 * pickGuest returns the systems configured guest User.
	 * 
	 * @return A {@link User} with loaded information for the systems guest User.
	 */
	User pickGuest();

	/**
	 * pickUser returns a populated User, how this user is populated is up to the implementation.
	 * 
	 * @param userInfo
	 *            A UserInfo with information to be able to pick and load a sutible user.
	 * @return A {@link User} with loaded information for the requested user.
	 */
	User pickUser(UserInfo userInfo);

}
