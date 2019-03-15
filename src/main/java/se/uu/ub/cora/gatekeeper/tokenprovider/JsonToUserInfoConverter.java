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

import java.util.HashMap;
import java.util.Map;

import se.uu.ub.cora.gatekeeper.user.UserInfo;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public final class JsonToUserInfoConverter {

	private static final String ID_IN_USER_STORAGE = "idInUserStorage";
	private static final String CHILDREN = "children";
	private String jsonUserInfo;
	private Map<String, String> childValues;

	public JsonToUserInfoConverter(String jsonUserInfo) {
		this.jsonUserInfo = jsonUserInfo;
	}

	public UserInfo parseUserInfoFromJson() {
		JsonParser jsonParser = new OrgJsonParser();
		JsonObject jsonUser = (JsonObject) jsonParser.parseString(jsonUserInfo);
		childValues = extractChildValuesToArray(jsonUser);
		return createUserInfoFromChildValues(childValues);
	}

	private Map<String, String> extractChildValuesToArray(JsonObject jsonUser) {
		JsonArray children = jsonUser.getValueAsJsonArray(CHILDREN);
		childValues = new HashMap<>();
		for (JsonValue child : children) {
			JsonObject childObject = (JsonObject) child;
			childValues.put(childObject.getValueAsJsonString("name").getStringValue(),
					childObject.getValueAsJsonString("value").getStringValue());
		}
		return childValues;
	}

	private UserInfo createUserInfoFromChildValues(Map<String, String> childValues) {
		if (idInUserStorageIsSet(childValues)) {
			return UserInfo.withIdInUserStorage(childValues.get(ID_IN_USER_STORAGE));
		}
		return UserInfo.withLoginIdAndLoginDomain(childValues.get("idFromLogin"),
				childValues.get("domainFromLogin"));
	}

	private boolean idInUserStorageIsSet(Map<String, String> childValues) {
		return childValues.containsKey(ID_IN_USER_STORAGE)
				&& childValues.get(ID_IN_USER_STORAGE).length() > 0;
	}

}
