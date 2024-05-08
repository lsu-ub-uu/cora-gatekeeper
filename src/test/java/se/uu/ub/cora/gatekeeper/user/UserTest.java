/*
 * Copyright 2016, 2024 Uppsala University Library
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

package se.uu.ub.cora.gatekeeper.user;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserTest {
	private static final String SOME_USER_ID = "someUserId";
	private static final String SOME_USER_NAME = "someUserName";
	private User user;

	@BeforeMethod
	private void beforeMethod() {
		user = new User(SOME_USER_ID);

	}

	@Test
	public void testLoginInfo() {
		user.loginId = SOME_USER_NAME;
		user.loginDomain = "someDomain";

		assertEquals(user.id, SOME_USER_ID);
		assertEquals(user.loginId, SOME_USER_NAME);
		assertEquals(user.loginDomain, "someDomain");
	}

	@Test
	public void testRoleSet() {
		user.roles.add("admin");
		user.roles.add("guest");
		Set<String> roles = user.roles;

		assertEquals(roles.size(), 2);
		assertTrue(roles.contains("guest"));
		assertTrue(roles.contains("admin"));
	}

	@Test
	public void testUserWithoutPassword() throws Exception {
		assertTrue(user.password instanceof Optional);
		assertTrue(user.password.isEmpty());
	}

	@Test
	public void testWithPassword() throws Exception {
		user.password = Optional.of("someTextHashed");

		assertTrue(user.password.isPresent());
		assertTrue(user.password.get() instanceof String);
		assertEquals(user.password.get(), "someTextHashed");

	}
}