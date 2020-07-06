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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
@WebServlet("/comment")
public class DataServlet extends HttpServlet {
    public static String NAME = "Comment";
    public static String CONTENT = "content";
    public static String TIMESTAMP = "timestamp";
    public static String EMAIL = "email";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(NAME);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    List<Comment> newComments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        long newID = entity.getKey().getId();
        String newContent = (String)entity.getProperty(CONTENT);
        long newTimestamp = (long)entity.getProperty(TIMESTAMP);
        String newEmail = (String)entity.getProperty(EMAIL);
        Comment newComment = new Comment(newID, newContent, newTimestamp, newEmail);

        newComments.add(newComment);
    }
    response.setContentType("application/json;"); // Send the JSON as the response
    String json = new Gson().toJson(newComments);
    response.getWriter().println(json);
  }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Post user comment if a comment is submitted.
        String comment = request.getParameter("user-comment");
        long timestamp = System.currentTimeMillis();

        // Get user's email address.
        UserService userService = UserServiceFactory.getUserService();
        String userEmail = userService.getCurrentUser().getEmail();

        // Create comment entity and store it in Datastore.
        Entity commentEntity = new Entity(NAME);
        commentEntity.setProperty(CONTENT, comment);
        commentEntity.setProperty(TIMESTAMP, timestamp);
        commentEntity.setProperty(EMAIL, userEmail);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");
    }
}
