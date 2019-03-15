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

package se.uu.ub.cora.gatekeeper.tokenprovider;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import se.uu.ub.cora.gatekeeper.Gatekeeper;
import se.uu.ub.cora.gatekeeper.authentication.AuthenticationException;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;
import se.uu.ub.cora.gatekeeper.user.UserInfo;

@Path("authToken")
public final class TokenProviderEndpoint {

	@POST
	public Response getAuthTokenForUserInfo(String jsonUserInfo) {
		try {
			return tryToGetAuthTokenForUserInfo(jsonUserInfo);
		} catch (AuthenticationException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private Response tryToGetAuthTokenForUserInfo(String jsonUserInfo) {
		Gatekeeper gatekeeper = GatekeeperInstanceProvider.getGatekeeper();
		UserInfo userInfo = convertJsonToUserInfo(jsonUserInfo);

		AuthToken authTokenForUserInfo = gatekeeper.getAuthTokenForUserInfo(userInfo);

		String json = convertAuthTokenToJson(authTokenForUserInfo);
		return Response.status(Response.Status.OK).entity(json).build();
	}

	private UserInfo convertJsonToUserInfo(String jsonUserInfo) {
		JsonToUserInfoConverter converter = new JsonToUserInfoConverter(jsonUserInfo);
		return converter.parseUserInfoFromJson();
	}

	private String convertAuthTokenToJson(AuthToken authTokenForUserInfo) {
		AuthTokenToJsonConverter converter = new AuthTokenToJsonConverter(authTokenForUserInfo);
		return converter.convertAuthTokenToJson();
	}

	@DELETE
	@Path("{userid}")
	public Response removeAuthTokenForUser(String authToken,
			@PathParam("userid") String idInUserStorage) {
		try {
			return tryToRemoveAuthTokenForUser(authToken, idInUserStorage);
		} catch (AuthenticationException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	private Response tryToRemoveAuthTokenForUser(String authToken, String idInUserStorage) {
		Gatekeeper gatekeeper = GatekeeperInstanceProvider.getGatekeeper();
		gatekeeper.removeAuthTokenForUser(authToken, idInUserStorage);
		return Response.status(Response.Status.OK).build();
	}
}
