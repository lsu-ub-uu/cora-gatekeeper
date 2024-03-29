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

package se.uu.ub.cora.gatekeeper.user;

import java.util.LinkedHashSet;
import java.util.Set;

public class User {

	public final String id;
	public String loginId;
	public String loginDomain;
	public final Set<String> roles = new LinkedHashSet<>();
	public Set<String> appTokenIds = new LinkedHashSet<>();
	public String firstName;
	public String lastName;
	public boolean active;

	public User(String id) {
		this.id = id;
	}
}
