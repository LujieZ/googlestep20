// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.servlets.DataServlet;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        String urlToRedirectTo = "/index.html";
        response.setContentType("application/json;"); 

        if (userService.isUserLoggedIn()) {
            String logoutURL = userService.createLogoutURL(urlToRedirectTo);

            String result = "{";
            result += "\"status\": \"true\", ";
            result += "\"url\": \""+ logoutURL + "\"";
            result += "}";
            
            response.getWriter().println(result);
            return;
        }
        String loginURL = userService.createLoginURL(urlToRedirectTo);

        String result = "{";
        result += "\"status\": \"false\", ";
        result += "\"url\": \""+ loginURL + "\"";
        result += "}";

        response.getWriter().println(result);
    }
}
