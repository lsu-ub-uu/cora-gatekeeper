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

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.spider.authentication.Authenticator;

public class AuthenticatorImp implements Authenticator {
	@Override
	public User getUserForToken(String authToken) {
		// the idea is to in the future change this to an https call to a
		// running gatekeeper server
		// return GatekeeperImp.INSTANCE.getUserForToken(authToken);
		// TODO: this should be read from the json
		User user = new User("someId");
		user.roles.add("someRole");
		return user;

	}
}
