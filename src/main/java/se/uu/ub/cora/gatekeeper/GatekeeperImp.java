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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import se.uu.ub.cora.gatekeeper.authentication.Gatekeeper;
import se.uu.ub.cora.gatekeeper.authentication.User;
import se.uu.ub.cora.spider.authentication.AuthenticationException;

public enum GatekeeperImp implements Gatekeeper {
	INSTANCE;
	public UserPickerFactory userPickerFactory;
	private Map<String, User> pickedUsers = new HashMap<>();

	public void setUserPickerFactory(UserPickerFactory userPickerFactory) {
		this.userPickerFactory = userPickerFactory;
		addHardCodedTokensToPickedUsers();
	}

	private void addHardCodedTokensToPickedUsers() {
		UserInfo userInfo = null;
		userInfo = UserInfo.withIdInUserStorage("99999");
		User pickedUser = userPickerFactory.factor().pickUser(userInfo);
		pickedUsers.put("dummySystemAdminAuthenticatedToken", pickedUser);

		userInfo = UserInfo.withIdInUserStorage("121212");
		User pickedUser2 = userPickerFactory.factor().pickUser(userInfo);
		pickedUsers.put("fitnesseUserToken", pickedUser2);

		userInfo = UserInfo.withIdInUserStorage("131313");
		User pickedUser3 = userPickerFactory.factor().pickUser(userInfo);
		pickedUsers.put("fitnesseAdminToken", pickedUser3);
	}

	@Override
	public User getUserForToken(String authToken) {
		if (authToken == null) {
			return returnGuestUser();
		}
		return tryToGetAuthenticatedUser(authToken);
	}

	private User returnGuestUser() {
		UserInfo userInfo = UserInfo.withIdInUserStorage("12345");
		return userPickerFactory.factor().pickUser(userInfo);
	}

	private User tryToGetAuthenticatedUser(String authToken) {
		throwErrorIfInvalidToken(authToken);
		return getAuthenticatedUser(authToken);
	}

	private void throwErrorIfInvalidToken(String authToken) {
		if ("dummyNonAuthenticatedToken".equals(authToken)) {
			throw new AuthenticationException("token not valid");
		}
	}

	private User getAuthenticatedUser(String authToken) {
		return pickedUsers.get(authToken);
	}

	@Override
	public String getAuthTokenForUserInfo(UserInfo userInfo) {
		User pickedUser = userPickerFactory.factor().pickUser(userInfo);
		String generateAuthToken = generateAuthToken();
		pickedUsers.put(generateAuthToken, pickedUser);
		return generateAuthToken;
	}

	private String generateAuthToken() {
		return UUID.randomUUID().toString();
	}

}
