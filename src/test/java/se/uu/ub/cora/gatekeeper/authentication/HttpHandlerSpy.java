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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.uu.ub.cora.gatekeeper.http.HttpHandler;

public class HttpHandlerSpy implements HttpHandler {

	public String requestMetod;
	public String url;
	private String jsonAnswer;
	private Status responseCode = Response.Status.OK;

	@Override
	public void setRequestMethod(String requestMetod) {
		this.requestMetod = requestMetod;
	}

	@Override
	public void setURL(String url) {
		this.url = url;

	}

	public void setResponseText(String jsonAnswer) {
		this.jsonAnswer = jsonAnswer;

	}

	@Override
	public String getResponseText() {
		return jsonAnswer;
	}

	public void setResponseCode(Status responseStatus) {
		this.responseCode = responseStatus;
	}

	@Override
	public Status getResponseCode() {
		return responseCode;
	}

}
