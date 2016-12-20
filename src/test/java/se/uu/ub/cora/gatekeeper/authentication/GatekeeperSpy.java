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

package se.uu.ub.cora.gatekeeper.authentication;

import se.uu.ub.cora.gatekeeper.Gatekeeper;
import se.uu.ub.cora.gatekeeper.tokenprovider.AuthToken;
import se.uu.ub.cora.userpicker.User;
import se.uu.ub.cora.userpicker.UserInfo;

public class GatekeeperSpy implements Gatekeeper {

	public boolean getUserForTokenWasCalled = false;
	public boolean getAuthTokenForUserInfoWasCalled = false;

	@Override
	public User getUserForToken(String authToken) {
		getUserForTokenWasCalled = true;
		if (authToken == null) {
			User user = new User("12345");
			user.roles.add("someRole112345");
			user.roles.add("someRole212345");
			return user;

		}
		if (authToken.equals("dummyNonAuthenticatedToken")) {
			throw new AuthenticationException("token not valid");
		}
		User user = new User("someId");
		user.roles.add("someRole1");
		user.roles.add("someRole2");
		return user;
	}

	@Override
	public AuthToken getAuthTokenForUserInfo(UserInfo userInfo) {
		if (userInfo.idFromLogin != null && userInfo.idFromLogin.equals("someLoginIdWithProblem")) {
			throw new AuthenticationException("problem getting authToken for userInfo");
		}
		getAuthTokenForUserInfoWasCalled = true;
		return AuthToken.withIdAndValidForNoSeconds("someAuthToken", 600);
	}

}
