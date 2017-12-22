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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.authentication.GateKeeperLocatorSpy;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;

public class TokenProviderEndpointTest {
	private Response response;
	private GateKeeperLocatorSpy locator;
	private TokenProviderEndpoint tokenProviderEndpoint;
	private String jsonUserInfo;

	@BeforeMethod
	public void setUp() {
		locator = new GateKeeperLocatorSpy();
		GatekeeperInstanceProvider.setGatekeeperLocator(locator);
		tokenProviderEndpoint = new TokenProviderEndpoint();
		jsonUserInfo = "{\"children\":[" + "{\"name\":\"idFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"domainFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"idInUserStorage\",\"value\":\"131313\"}" + "],\"name\":\"userInfo\"}";
	}

	@Test
	public void testDependenciesAreCalled() {
		response = tokenProviderEndpoint.getAuthTokenForUserInfo(jsonUserInfo);
		assertTrue(locator.gatekeeperLocated);
		assertTrue(locator.gatekeeperSpy.getAuthTokenForUserInfoWasCalled);
	}

	@Test
	public void testGetToken() {
		response = tokenProviderEndpoint.getAuthTokenForUserInfo(jsonUserInfo);

		assertResponseStatusIs(Response.Status.OK);
		assertEntityExists();
		String expected = "{\"children\":[" + "{\"name\":\"id\",\"value\":\"someAuthToken\"},"
				+ "{\"name\":\"validForNoSeconds\",\"value\":\"600\"},"
				+ "{\"name\":\"idInUserStorage\",\"value\":\"someIdFromStorage\"},"
				+ "{\"name\":\"idFromLogin\",\"value\":\"someIdFromLogin\"}"
				+ "],\"name\":\"authToken\"}";
		assertEquals(response.getEntity(), expected);
	}

	private void assertResponseStatusIs(Status responseStatus) {
		assertEquals(response.getStatusInfo(), responseStatus);
	}

	private void assertEntityExists() {
		assertNotNull(response.getEntity(), "An entity in json format should be returned");
	}

	@Test
	public void testNonUserInfoWithProblem() {
		// someLoginIdWithProblem
		String jsonUserInfo = "{\"children\":["
				+ "{\"name\":\"idFromLogin\",\"value\":\"someLoginIdWithProblem\"},"
				+ "{\"name\":\"domainFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"idInUserStorage\",\"value\":\"\"}" + "],\"name\":\"userInfo\"}";
		response = tokenProviderEndpoint.getAuthTokenForUserInfo(jsonUserInfo);
		assertResponseStatusIs(Response.Status.UNAUTHORIZED);
	}

	@Test
	public void testRemoveAuthTokenForUser() {
		String authToken = "someAuthToken";
		String idInUserStorage = "someId";
		response = tokenProviderEndpoint.removeAuthTokenForUser(authToken, idInUserStorage);
		assertResponseStatusIs(Response.Status.OK);
	}

	@Test
	public void testRemoveAuthTokenForUserWithProblem() {
		String authToken = "someNonExistingAuthToken";
		String idInUserStorage = "someId";
		response = tokenProviderEndpoint.removeAuthTokenForUser(authToken, idInUserStorage);
		assertResponseStatusIs(Response.Status.NOT_FOUND);
	}
}
