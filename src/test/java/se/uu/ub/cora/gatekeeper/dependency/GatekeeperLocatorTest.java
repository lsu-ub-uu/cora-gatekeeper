/*
 * Copyright 2016 Olov McKie
 * Copyright 2015 Uppsala University Library
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

package se.uu.ub.cora.gatekeeper.dependency;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.authentication.Gatekeeper;

public class GatekeeperLocatorTest {
	private GatekeeperLocator locator = new GatekeeperLocatorImp();

	@BeforeTest
	public void setUp() {
	}

	@Test
	public void testLocateGatekeeper() {
		Gatekeeper gatekeeper = locator.locateGatekeeper();
		assertTrue(gatekeeper instanceof GatekeeperImp);
		Gatekeeper gatekeeper2 = locator.locateGatekeeper();
		assertEquals(gatekeeper, gatekeeper2);
	}

}
