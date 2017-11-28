/*
 * Copyright 2016, 2017 Uppsala University Library
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

import se.uu.ub.cora.json.builder.JsonArrayBuilder;
import se.uu.ub.cora.json.builder.JsonObjectBuilder;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

public final class AuthTokenToJsonConverter {

	private static final String VALUE = "value";
	private static final String NAME = "name";
	private static final String CHILDREN = "children";
	private AuthToken authToken;
	private OrgJsonBuilderFactoryAdapter orgJsonBuilderFactoryAdapter = new OrgJsonBuilderFactoryAdapter();

	public AuthTokenToJsonConverter(AuthToken authToken) {
		this.authToken = authToken;
	}

	public String convertAuthTokenToJson() {
		JsonObjectBuilder userBuilder = createObjectBuilderWithName("authToken");
		JsonArrayBuilder userChildren = returnAndAddChildrenToBuilder(userBuilder);

		addIdToJson(authToken, userChildren);

		addValidForNoSecondsToJson(authToken, userChildren);
		addIdInUserStorageToJson(authToken, userChildren);

		return userBuilder.toJsonFormattedString();
	}

	private JsonObjectBuilder createObjectBuilderWithName(String name) {
		JsonObjectBuilder roleBuilder = orgJsonBuilderFactoryAdapter.createObjectBuilder();
		roleBuilder.addKeyString(NAME, name);
		return roleBuilder;
	}

	private void addIdToJson(AuthToken authToken, JsonArrayBuilder userChildren) {
		JsonObjectBuilder id = createObjectBuilderWithName("id");
		id.addKeyString(VALUE, authToken.token);
		userChildren.addJsonObjectBuilder(id);
	}

	private void addValidForNoSecondsToJson(AuthToken authToken, JsonArrayBuilder userChildren) {
		JsonObjectBuilder validForNoSeconds = createObjectBuilderWithName("validForNoSeconds");
		validForNoSeconds.addKeyString(VALUE, String.valueOf(authToken.validForNoSeconds));
		userChildren.addJsonObjectBuilder(validForNoSeconds);
	}

	private void addIdInUserStorageToJson(AuthToken authToken, JsonArrayBuilder userChildren) {
		JsonObjectBuilder idInUserStorage = createObjectBuilderWithName("idInUserStorage");
		idInUserStorage.addKeyString(VALUE, String.valueOf(authToken.idInUserStorage));
		userChildren.addJsonObjectBuilder(idInUserStorage);
	}

	private JsonArrayBuilder returnAndAddChildrenToBuilder(JsonObjectBuilder userBuilder) {
		JsonArrayBuilder userChildren = orgJsonBuilderFactoryAdapter.createArrayBuilder();
		userBuilder.addKeyJsonArrayBuilder(CHILDREN, userChildren);
		return userChildren;
	}

}
