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

public final class UserInfo {

	public String idFromLogin;
	public String domainFromLogin;
	public String idInUserStorage;

	private UserInfo(String idFromLogin, String domainFromLogin) {
		this.idFromLogin = idFromLogin;
		this.domainFromLogin = domainFromLogin;
	}

	private UserInfo(String idInUserStorage) {
		this.idInUserStorage = idInUserStorage;
	}

	private UserInfo() {
	}

	public static UserInfo withLoginIdAndLoginDomain(String idFromLogin, String domainFromLogin) {
		return new UserInfo(idFromLogin, domainFromLogin);
	}

	public static UserInfo withIdInUserStorage(String idInUserStorage) {
		return new UserInfo(idInUserStorage);
	}

	public static UserInfo withLoginId(String idFromLogin) {
		UserInfo userInfo = new UserInfo();
		userInfo.idFromLogin = idFromLogin;
		return userInfo;
	}
}
