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

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * User is an object that holds information of a user. It is the object representation of the User
 * recordtype.
 *
 */
public class User {

	public final String id;
	public String loginId;
	public String firstName;
	public String lastName;
	public boolean active;
	public String loginDomain;
	public Set<String> appTokenIds = new LinkedHashSet<>();
	public final Set<String> roles = new LinkedHashSet<>();
	public Optional<String> passwordId = Optional.empty();

	public User(String id) {
		this.id = id;
	}
}
