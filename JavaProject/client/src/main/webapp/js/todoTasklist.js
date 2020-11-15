$(document).ready(function() {
     loadTasks();
});

function loadTasks(){
   var userId = window.location.hash.substring(1)
   $.ajax({
          type: 'get',
          url: "/onlineTodoList/rest/users/"+userId+"/tasks",
          success: function (result) {
             var i = 0;
             var tasks = result.taskList;
             for (i=0; i < tasks.length; i++ ){
                  var task = tasks[i]
                  var li = document.createElement("li");
                  li.id = task.id
				  if (task.description =="")
				  {
					  var descriptionNode = document.createTextNode(task.description);
				  }
				  else 
				  {
					  var descriptionNode = document.createTextNode("\u2022" +" "+task.description);
				  }
                  li.setAttribute('description', task.description);
                  li.appendChild(descriptionNode);

                  var lastUpdate = document.createTextNode(
                          "Updated on: " + parseLastUpdateTime(task.lastUpdate));
                  var tooltipSpan = document.createElement("SPAN");
                  tooltipSpan.className = "tooltip";
                  tooltipSpan.appendChild(lastUpdate);
                  li.appendChild(tooltipSpan);

                  if (task.checked){
                    li.classList.toggle("checked");
                  }

                  // Add a "checked" toggle when clicking on a task
                  li.addEventListener('click', function(ev) {
                     if (ev.target.tagName === 'LI') {
                         ev.target.classList.toggle('checked');
                         var taskId = this.id;
                         var checked = this.classList.contains("checked");
                         var description = this.getAttribute('description');

                         $.ajax({
                           type: 'put',
                           url: "/onlineTodoList/rest/users/"+userId+"/tasks/"+taskId,
                           data: JSON.stringify({"description": description, "checked": checked}),
                           contentType: 'application/json',
                           success: function (result) {},
                           error: function (e) {
                              console.log(e);
                              ev.target.classList.toggle('checked');
                           }
                         })
                     }
                  }, false);

                  document.getElementById("tasklist").appendChild(li);

                  // Create delete icon button
                  var txt = document.createTextNode("Delete");
                  var span = document.createElement("SPAN");
                  span.className = "delete";
                  span.onclick = function(){
                      var taskId = this.parentNode.id
                      $.ajax({
                             type: 'delete',
                             url: "/onlineTodoList/rest/users/"+userId+"/tasks/"+taskId,
                             success: function (result) {
                                 document.getElementById(taskId).remove();
                             },
                             error: function (e) {
                                 console.log(e);
                             }
                      })
                  }
                  span.appendChild(txt);
                  li.appendChild(span);
              }
          },
          error: function (e) {
             console.log(e);
          }
     });
}

// Create a new task
function addTask() {
  var userId = window.location.hash.substring(1)
  var description = document.getElementById("description").value;
  if (description === '') {
       alert("Please mention your task!");
  }
  $.ajax({
         type: 'post',
         url: "/onlineTodoList/rest/users/"+userId+"/tasks",
         data: JSON.stringify({"description": description, "checked": false}),
         contentType: 'application/json',
         success: function (result) {
              location.reload();
         },
         error: function (e) {
              console.log(e);
         }
  })
}

function parseLastUpdateTime(lastUpdate){
   var parsedLastUpdate = new Date(lastUpdate);
   return parsedLastUpdate.toGMTString();;
}
