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
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import se.uu.ub.cora.userpicker.User;

public class UserTest {
	@Test
	public void init() {
		String id = "someUserId";
		User user = new User(id);
		assertEquals(user.id, "someUserId");
	}

	@Test
	public void testLoginInfo() {
		String id = "122345";
		User user = new User(id);
		user.loginId = "someUserId";
		user.loginDomain = "someDomain";
		assertEquals(user.id, "122345");
		assertEquals(user.loginId, "someUserId");
		assertEquals(user.loginDomain, "someDomain");
	}

	@Test
	public void testRoleSet() {
		String id = "122345";
		User user = new User(id);
		user.roles.add("admin");
		user.roles.add("guest");
		Set<String> roles = user.roles;
		assertEquals(roles.size(), 2);
		assertTrue(roles.contains("guest"));
		assertTrue(roles.contains("admin"));
	}
}
