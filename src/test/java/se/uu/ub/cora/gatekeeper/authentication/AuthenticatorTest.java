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

import java.util.Iterator;

import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.spider.authentication.Authenticator;
import se.uu.ub.cora.spider.authorization.AuthorizationException;

public class AuthenticatorTest {
	private Authenticator authenticator;
	private User logedInUser;
	private HttpHandlerSpy httpHandler;

	@BeforeMethod
	public void setUp() {
		httpHandler = new HttpHandlerSpy();
		authenticator = AuthenticatorImp.usingHttpHandler(httpHandler);
		String jsonAnswer = "{\"children\":[{\"children\":[{\"children\":["
				+ "{\"name\":\"role\",\"value\":\"someRole1\"}],\"name\":\"rolePlus\"}"
				+ ",{\"children\":[{\"name\":\"role\",\"value\":\"someRole2\"}]"
				+ ",\"name\":\"rolePlus\"}],\"name\":\"rolesPlus\"}],\"name\":\"someId2\"}";
		httpHandler.setResponseText(jsonAnswer);
	}

	@Test
	public void testHttpHandlerCalledCorrectly() {
		logedInUser = authenticator.getUserForToken("someToken");
		assertEquals(httpHandler.requestMetod, "GET");
		assertEquals(httpHandler.url, "http://localhost:8080/gatekeeper/user/someToken");
	}

	@Test
	public void testHttpAnswerParsedToUser() {
		logedInUser = authenticator.getUserForToken("someToken");
		assertEquals(logedInUser.id, "someId2");
	}

	@Test
	public void testHttpAnswerParsedToUserRoles() {
		logedInUser = authenticator.getUserForToken("someToken");
		assertEquals(logedInUser.roles.size(), 2);
		Iterator<String> iterator = logedInUser.roles.iterator();
		assertEquals(iterator.next(), "someRole2");
		assertEquals(iterator.next(), "someRole1");
	}

	@Test(expectedExceptions = AuthorizationException.class)
	public void testUnauthorizedToken() {
		httpHandler.setResponseCode(Response.Status.UNAUTHORIZED);
		logedInUser = authenticator.getUserForToken("dummyNonAuthenticatedToken");
	}

}
