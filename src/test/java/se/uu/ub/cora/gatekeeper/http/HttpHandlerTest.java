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

package se.uu.ub.cora.gatekeeper.http;

import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

public class HttpHandlerTest {

	@Test
	public void testSetRequestMethod() throws MalformedURLException {
		URL url = new URL("http://google.se");
		HttpURLConnectionSpy urlConnection = new HttpURLConnectionSpy(url);
		HttpHandler httpHandler = HttpHandlerImp.usingURLConnection(urlConnection);
		httpHandler.setRequestMethod("GET");
		assertEquals(urlConnection.requestMethod, "GET");
	}

	@Test
	public void testGetResponseCode() throws MalformedURLException {
		URL url = new URL("http://google.se");
		HttpURLConnectionSpy urlConnection = new HttpURLConnectionSpy(url);
		HttpHandler httpHandler = HttpHandlerImp.usingURLConnection(urlConnection);

		urlConnection.setResponseCode(200);
		assertEquals(httpHandler.getResponseCode(), Response.Status.OK);
	}

	@Test
	public void testGetResponseText() throws MalformedURLException {
		URL url = new URL("http://google.se");
		HttpURLConnectionSpy urlConnection = new HttpURLConnectionSpy(url);
		HttpHandler httpHandler = HttpHandlerImp.usingURLConnection(urlConnection);

		urlConnection.setResponseText("some text");
		assertEquals(httpHandler.getResponseText(), "some text");
	}

}
