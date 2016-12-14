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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;

public class AuthenticatorEndpointTest {
	private AuthenticatorEndpoint authenticatorEndpoint;
	private Response response;
	private GateKeeperLocatorSpy locator;

	@BeforeMethod
	public void setUp() {
		locator = new GateKeeperLocatorSpy();
		GatekeeperInstanceProvider.setGatekeeperLocator(locator);
		authenticatorEndpoint = new AuthenticatorEndpoint();
	}

	@Test
	public void test() {
		String token = "someToken";
		response = authenticatorEndpoint.getUserForToken(token);
		assertResponseStatusIs(Response.Status.OK);
		assertEntityExists();
		String expected = "{\"children\":[{\"children\":[{\"children\":["
				+ "{\"name\":\"role\",\"value\":\"someRole1\"}],\"name\":\"rolePlus\"}"
				+ ",{\"children\":[{\"name\":\"role\",\"value\":\"someRole2\"}]"
				+ ",\"name\":\"rolePlus\"}],\"name\":\"rolesPlus\"}],\"name\":\"someId\"}";
		assertEquals(response.getEntity(), expected);
	}

	private void assertResponseStatusIs(Status responseStatus) {
		assertEquals(response.getStatusInfo(), responseStatus);
	}

	private void assertEntityExists() {
		assertNotNull(response.getEntity(), "An entity in json format should be returned");
	}

	@Test
	public void testNonAuthenticatedToken() {
		response = authenticatorEndpoint.getUserForToken("dummyNonAuthenticatedToken");
		assertResponseStatusIs(Response.Status.UNAUTHORIZED);
	}

	@Test
	public void testNoTokenShouldBeGuest() {
		response = authenticatorEndpoint.getUserForToken(null);
		assertResponseIsCorrectGuestUser();
	}

	@Test
	public void testGetUserWithoutTokenShouldBeGuest() {
		response = authenticatorEndpoint.getUserWithoutToken();
		assertResponseIsCorrectGuestUser();
	}

	private void assertResponseIsCorrectGuestUser() {
		assertResponseStatusIs(Response.Status.OK);
		assertEntityExists();
		String expected = "{\"children\":[{\"children\":["
				+ "{\"children\":[{\"name\":\"role\",\"value\":\"someRole112345\"}]"
				+ ",\"name\":\"rolePlus\"},{\"children\":["
				+ "{\"name\":\"role\",\"value\":\"someRole212345\"}]"
				+ ",\"name\":\"rolePlus\"}],\"name\":\"rolesPlus\"}]" + ",\"name\":\"12345\"}";
		assertEquals(response.getEntity(), expected);
	}

}
