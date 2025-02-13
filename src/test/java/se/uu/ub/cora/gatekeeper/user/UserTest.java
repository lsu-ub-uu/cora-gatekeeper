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
import static org.testng.Assert.assertFalse;
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
		assertTrue(user.passwordId instanceof Optional);
		assertTrue(user.passwordId.isEmpty());
	}

	@Test
	public void testWithPassword() throws Exception {
		user.passwordId = Optional.of("someTextHashed");

		assertTrue(user.passwordId.isPresent());
		assertTrue(user.passwordId.get() instanceof String);
		assertEquals(user.passwordId.get(), "someTextHashed");

	}

	@Test
	public void testAddAndRemoveAppTokens() {
		user.appTokenIds.add("token1");
		user.appTokenIds.add("token2");

		assertTrue(user.appTokenIds.contains("token1"));
		assertTrue(user.appTokenIds.contains("token2"));

		user.appTokenIds.remove("token1");

		assertEquals(user.appTokenIds.size(), 1);
		assertFalse(user.appTokenIds.contains("token1"));
		assertTrue(user.appTokenIds.contains("token2"));
	}

	@Test
	public void testUserActivation() {
		user.active = true;
		assertTrue(user.active);

		user.active = false;
		assertFalse(user.active);
	}

	@Test
	public void testSetUserNames() {
		user.firstName = "John";
		user.lastName = "Doe";

		assertEquals(user.firstName, "John");
		assertEquals(user.lastName, "Doe");
	}

	@Test
	public void testSetLoginDomain() {
		user.loginDomain = "example.com";
		assertEquals(user.loginDomain, "example.com");
	}
}