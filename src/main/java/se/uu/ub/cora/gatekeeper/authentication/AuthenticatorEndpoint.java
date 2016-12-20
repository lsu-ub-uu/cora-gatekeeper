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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import se.uu.ub.cora.gatekeeper.Gatekeeper;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;
import se.uu.ub.cora.userpicker.User;
import se.uu.ub.cora.json.builder.JsonArrayBuilder;
import se.uu.ub.cora.json.builder.JsonObjectBuilder;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

@Path("user")
public class AuthenticatorEndpoint {

	private static final String NAME = "name";
	private static final String CHILDREN = "children";
	private Gatekeeper gatekeeper;
	private OrgJsonBuilderFactoryAdapter orgJsonBuilderFactoryAdapter;

	public AuthenticatorEndpoint() {
		gatekeeper = GatekeeperInstanceProvider.getGatekeeper();
		orgJsonBuilderFactoryAdapter = new OrgJsonBuilderFactoryAdapter();
	}

	@GET
	public Response getUserWithoutToken() {
		return tryToGetUserForToken(null);
	}

	@GET
	@Path("{token}")
	public Response getUserForToken(@PathParam("token") String token) {
		try {
			return tryToGetUserForToken(token);
		} catch (AuthenticationException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private Response tryToGetUserForToken(String token) {
		User user = gatekeeper.getUserForToken(token);
		String json = convertUserToJson(user);
		return Response.status(Response.Status.OK).entity(json).build();
	}

	private String convertUserToJson(User user) {
		JsonObjectBuilder userBuilder = createObjectBuilderWithName(user.id);
		JsonArrayBuilder userChildren = returnAndAddChildrenToBuilder(userBuilder);
		addRolesPlusToUser(user, userChildren);
		return userBuilder.toJsonFormattedString();
	}

	private JsonObjectBuilder createObjectBuilderWithName(String name) {
		JsonObjectBuilder roleBuilder = orgJsonBuilderFactoryAdapter.createObjectBuilder();
		roleBuilder.addKeyString(NAME, name);
		return roleBuilder;
	}

	private JsonArrayBuilder returnAndAddChildrenToBuilder(JsonObjectBuilder userBuilder) {
		JsonArrayBuilder userChildren = orgJsonBuilderFactoryAdapter.createArrayBuilder();
		userBuilder.addKeyJsonArrayBuilder(CHILDREN, userChildren);
		return userChildren;
	}

	private void addRolesPlusToUser(User user, JsonArrayBuilder userChildren) {
		JsonArrayBuilder rolesPlusChildren = createRolesPlus(userChildren);

		addRolesToRolesPlus(user, rolesPlusChildren);
	}

	private JsonArrayBuilder createRolesPlus(JsonArrayBuilder userChildren) {
		JsonObjectBuilder rolesPlus = createObjectBuilderWithName("rolesPlus");
		userChildren.addJsonObjectBuilder(rolesPlus);
		return returnAndAddChildrenToBuilder(rolesPlus);
	}

	private void addRolesToRolesPlus(User user, JsonArrayBuilder rolesPlusChildren) {
		for (String role : user.roles) {
			JsonObjectBuilder rolePlusBuilder = createRolePlus(rolesPlusChildren);
			JsonArrayBuilder rolePlusChildren = returnAndAddChildrenToBuilder(rolePlusBuilder);
			addRole(role, rolePlusChildren);
		}
	}

	private JsonObjectBuilder createRolePlus(JsonArrayBuilder rolesPlusChildren) {
		JsonObjectBuilder rolePlusBuilder = createObjectBuilderWithName("rolePlus");

		rolesPlusChildren.addJsonObjectBuilder(rolePlusBuilder);
		return rolePlusBuilder;
	}

	private void addRole(String role, JsonArrayBuilder rolePlusChildren) {
		JsonObjectBuilder roleBuilder = createObjectBuilderWithName("role");
		rolePlusChildren.addJsonObjectBuilder(roleBuilder);
		roleBuilder.addKeyString("value", role);
	}

}
