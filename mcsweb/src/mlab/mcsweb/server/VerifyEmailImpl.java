/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package mlab.mcsweb.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import mlab.mcsweb.shared.Response;

public class VerifyEmailImpl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String serverRoot = "";
	//static String signupPath = "";

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		serverRoot = config.getServletContext().getInitParameter("serverRoot");
		//signupPath = config.getServletContext().getInitParameter("signupPath");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String baseUrl = "https://koiosplatform.com/mcsweb/";
		System.out.println("base url:"+ baseUrl);
		try {
//			String toWrite = "";
			Response response = verifyEmail(req);
			System.out.println("response from verify email "+ response.getMessage());
//			boolean forward = false;
			resp.setContentType("text/html");
			if (response.getCode() == 0) {
//				forward = true;
//				toWrite = "Email verified. You will be redirected to log in.";
//				req.setAttribute("code", 0);
				resp.sendRedirect(baseUrl + "accountverified.jsp?code=0");
			} else if (response.getCode() == 1) {
//				toWrite = "Invalid verification code. Please sign up.";
				resp.sendRedirect(baseUrl + "accountverified.jsp?code=1");
			} else if (response.getCode() == 2) {
//				forward = true;
//				toWrite = "Email already verified. You will be redirected to log in.";
				resp.sendRedirect(baseUrl + "accountverified.jsp?code=2");
			} else {
//				toWrite = "Service is not available, please try later.";
				resp.sendRedirect(baseUrl + "accountverified.jsp?code=-1");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			resp.sendRedirect(baseUrl + "accountverified.jsp?code=-1");
			e.printStackTrace();
		}

	}

	protected Response verifyEmail(HttpServletRequest request) throws Exception {
		String token = request.getParameter("token");

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		System.out.println("token in the param "+ token);
		String url = serverRoot
				+ "/auth/verification/" + token;
		System.out.println("verification url:" + url);

		WebResource service = client.resource(url);
		String result = service.accept(MediaType.APPLICATION_JSON).get(
				String.class);
		Response response = new Gson().fromJson(result, Response.class);
		return response;

	}

}
