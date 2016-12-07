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

import javax.ws.rs.core.Response;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.gatekeeper.http.HttpHandler;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;
import se.uu.ub.cora.spider.authentication.Authenticator;
import se.uu.ub.cora.spider.authorization.AuthorizationException;

public final class AuthenticatorImp implements Authenticator {
	private static final String CHILDREN = "children";
	private HttpHandler httpHandler;
	private User user;
	private JsonObject jsonUser;
	private String responseText;

	public static AuthenticatorImp usingHttpHandler(HttpHandler httpHandler) {
		return new AuthenticatorImp(httpHandler);
	}

	private AuthenticatorImp(HttpHandler httpHandler) {
		this.httpHandler = httpHandler;
	}

	@Override
	public User getUserForToken(String authToken) {
		getUserForTokenFromGatekeeper(authToken);
		return createUserFromResponseText();
	}

	private void getUserForTokenFromGatekeeper(String authToken) {
		httpHandler.setRequestMethod("GET");
		httpHandler.setURL("http://localhost:8080/gatekeeper/user/" + authToken);
		if (httpHandler.getResponseCode() != Response.Status.OK) {
			throw new AuthorizationException("authToken gives no authorization");
		}
		responseText = httpHandler.getResponseText();
	}

	private User createUserFromResponseText() {
		getJsonUserFromResponseText(responseText);
		setIdInUser();
		parseAndSetRolesInUser();
		return user;
	}

	private void getJsonUserFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		jsonUser = (JsonObject) jsonParser.parseString(responseText);
	}

	private void setIdInUser() {
		String id = getIdFromJsonUser();
		user = new User(id);
	}

	private String getIdFromJsonUser() {
		return jsonUser.getValueAsJsonString("name").getStringValue();
	}

	private void parseAndSetRolesInUser() {
		JsonArray rolesPlusChildren = getRolesPlusChildrenFromJsonUser();
		for (JsonValue child : rolesPlusChildren) {
			String roleName = getRoleNameFromRolePlusChild(child);
			user.roles.add(roleName);
		}
	}

	private JsonArray getRolesPlusChildrenFromJsonUser() {
		JsonArray userChildren = jsonUser.getValueAsJsonArray(CHILDREN);
		JsonObject rolesPlus = userChildren.getValueAsJsonObject(0);
		return rolesPlus.getValueAsJsonArray(CHILDREN);
	}

	private String getRoleNameFromRolePlusChild(JsonValue child) {
		JsonArray rolePlusChildren = ((JsonObject) child).getValueAsJsonArray(CHILDREN);
		JsonObject role = rolePlusChildren.getValueAsJsonObject(0);
		return role.getValueAsJsonString("value").getStringValue();
	}

}
