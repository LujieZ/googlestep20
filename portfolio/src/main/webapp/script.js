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
 * Hide the comments form by default and fetch the login status from the servlet.
 * If the user is logged in, unhide the form.
 * If the user is not logged in, display a login link.
 */
function checkLoginStatus(){
    fetch('/home') 
    .then(response => response.json())
    .then((object) => { 
        const status = object.status;
        const url = object.url;
        if (status == 'true') {
            document.getElementById('comment').style.display = 'block';
            var text = 'LOG OUT';
        } else {
            var text = 'LOG IN';
        }

        var button = document.createElement('button');
        button.setAttribute('id','logbtn');
        button.innerHTML = String(text);
        var login = document.getElementById('login');
        login.appendChild(button);

        // Access the URL that is feteched from /home.
        button.addEventListener('click', 
            function() {
                window.location.href = url;
            });
    });
}

/**
 * Fetch the comments in JSON format from the server and populates the DOM.
 */
function parseSomething(){
    fetch('/comment')  
    .then(response => response.json()) 
    .then((object) => { 
        object.sort(function(object1, object2) {
            return object2.timestamp - object1.timestamp;
        });
        const commentsListElement = document.getElementById('comments-container');
        commentsListElement.innerHTML = '';

        var numberCommentsDisplay = document.getElementById('number-comment').value;
        numberCommentsDisplay = Math.min(numberCommentsDisplay, object.length);

        for (var i = 0; i < numberCommentsDisplay; i++) {
            commentsListElement.appendChild(createListElement(object[i]));
        }
    });
}

/** 
 * Creates an <li> element containing text. 
 */
function createListElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const contentElement = document.createElement('span');
  contentElement.innerText = comment.content + ' by ' + comment.email;
  
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = "GO AWAY PLEASE";
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

/**
 * Delete a comment by its ID.
 */
 function deleteComment(comment){
     const params = new URLSearchParams();
     params.append('id', comment.id);
     fetch('/delete-data', {method: 'POST', body: params});
 }
 