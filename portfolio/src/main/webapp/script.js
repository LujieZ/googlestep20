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

/**
 * Hide the comments form by default and fetch the login status from the servlet
 * If the user is logged in, unhide the form
 * If the user is not logged in, display a login link
 */
function checkLoginStatus(){
    fetch('/home') // sends a request to /home
    .then(response => response.json()) // parse the response as JSON
    .then((object) => { // reference the status in response

        const status = object.status;
        const url = object.url;

        if (status == 'true') {
            document.getElementById('comment').style.display = 'block';
            var text = 'logouthere';
        }
        else {
            var text = 'loginhere';
        }

        // create the button
        var button = document.createElement('button');
        button.setAttribute('id','logbtn');
        button.innerHTML = String(text);

        // append in login div
        var login = document.getElementById('login');
        login.appendChild(button);

        // add event handler
        button.addEventListener('click', 
        function() {
            window.location.href = url;
        });

    });
}

/**
 * Fetch the JSON string from the server and parse JSON
 */
function parseSomething(){

    fetch('/comment')  // sends a request to /data
    .then(response => response.json()) // parses the response as JSON
    .then((object) => { // reference the fields in Comment

        const commentsListElement = document.getElementById('comments-container');
        commentsListElement.innerHTML = '';

        var numberCommentsDisplay = document.getElementById('number-comment').value;
        numberCommentsDisplay = Math.min(numberCommentsDisplay, object.length);

        for (var i = 0; i < numberCommentsDisplay; i++) {
            commentsListElement.appendChild(createListElement(sortedObjs[i]));
        }
        
    });

}

/**
    Delete a comment by its id 
 */
 function deleteComment(comment){
     const params = new URLSearchParams();
     params.append('id', comment.id);
     fetch('/delete-data', {method: 'POST', body: params});
 }

/** Creates an <li> element containing text. */
function createListElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const contentElement = document.createElement('span');
  contentElement.innerText = comment.content + ' by ' + comment.email;
  
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = "goawayplease";
  deleteButtonElement.addEventListener('click', () => {
      deleteComment(comment);

      // Remove the comment from the DOM.
      commentElement.remove();
      fetch('/comment').then(parseSomething());
  });

  commentElement.appendChild(contentElement);
  commentElement.appendChild(deleteButtonElement);

  return commentElement;
}
