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

package se.uu.ub.cora.gatekeeper;

import se.uu.ub.cora.gatekeeper.user.User;
import se.uu.ub.cora.gatekeeper.user.UserInfo;
import se.uu.ub.cora.gatekeeper.user.UserPicker;

public class UserPickerSpy implements UserPicker {

	public boolean userPickerWasCalled = false;
	public boolean pickGuestWasCalled = false;
	public UserInfo usedUserInfo = null;
	public User returnedUser = null;

	@Override
	public User pickUser(UserInfo userInfo) {
		if (null != userInfo.idInUserStorage) {
			return fakeUserFromStorage(userInfo);
		}
		if (userInfo.idFromLogin != null && userInfo.idFromLogin.equals("someLoginIdWithProblem")) {
			throw new RuntimeException("problem finding user");
		}
		usedUserInfo = userInfo;
		userPickerWasCalled = true;
		User user = new User("12345");
		user.loginId = userInfo.idFromLogin;
		user.loginDomain = userInfo.domainFromLogin;
		returnedUser = user;
		return user;
	}

	private User fakeUserFromStorage(UserInfo userInfo) {
		usedUserInfo = userInfo;
		userPickerWasCalled = true;
		User user = new User(userInfo.idInUserStorage);
		user.loginId = "loginIdFromUserPickerSpy";
		user.loginDomain = "loginDomainFromUserPickerSpy";
		if (userInfo.idInUserStorage.equals("someIdInStorageReturningName")) {
			user.firstName = "firstNameFromUserPickerSpy";
			user.lastName = "lastNameFromUserPickerSpy";
		}

		returnedUser = user;
		return user;
	}

	@Override
	public User pickGuest() {
		pickGuestWasCalled = true;
		User user = new User("guestUserIdDecidedByStorage");
		user.loginId = "someLoginId";
		user.loginDomain = "someLoginDomain";
		returnedUser = user;
		return user;
	}

}
