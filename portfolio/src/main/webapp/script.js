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
 * Fetch the JSON string from the server and parse JSON
 */
function parseSomething(){

    fetch('/comment')  // sends a request to /data
    .then(response => response.json()) // parses the response as JSON
    .then((object) => { // now we can reference the fields in myObject!

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
  contentElement.innerText = comment.content;
  
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = "goawayplease";
  deleteButtonElement.addEventListener('click', () => {
      deleteComment(comment);

      // Remove the comment from the DOM.
      commentElement.remove();
      fetch('/data').then(parseSomething());
  });

  commentElement.appendChild(contentElement);
  commentElement.appendChild(deleteButtonElement);

  return commentElement;
}
