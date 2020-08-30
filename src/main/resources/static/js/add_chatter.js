jQuery.each(["put", "delete", "post"], function (i, method) {
    jQuery[method] = function (url, data, callback) {
        if (jQuery.isFunction(data)) {
            callback = data;
            data = undefined;
        }

        return jQuery.ajax({
            url: url,
            type: method,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: data,
            success: callback,
            error: function (data) {
                console.log("error");
                console.log(data);
            }
        });
    };
});

$(document).ready(function () {
    getUsers();
});

function getUsers(){
    $.get("/get_other_users").done(function(resp){
        displayUsers(resp);
    })

}

function displayUsers(users){
console.log(users);
    if (users.length <= 0) {
            $("#titles").css("display", "none");
            return;
        } else {
            var tblBody = document.getElementById("body");
            var rows = document.getElementsByTagName("tr");
            var rowCount = rows.length;
            for (var i = rowCount - 1; i > 0; i--) {
                tblBody.removeChild(rows[i]);
            }
            var cellText;
            var text;
            for (var i = 0; i < users.length; i++) {
                if(users[i].id===user.id){
                    continue;
                }else{
                    var row = document.createElement("tr");
                    for (var j = 0; j < 2; j++) {
                        if (j === 0) {
                            text = users[i].username;
                            cellText = document.createTextNode(text);
                        }
                        if (j === 1) {
                            var id = users[i].id;
                            var sect = document.createElement("section");
                            var bt = document.createElement("button");
                            bt.className = "add_chatter";
                            bt.id = id;
                            bt.innerText = "ADD";
                            bt.addEventListener("click", addInterlocutor);
                            sect.appendChild(bt);

                            cellText = sect;
                        }
                        var cell = document.createElement("td");
                        cell.style.backgroundColor = "orange";
                        cell.style.textAlign = "center";
                        cell.appendChild(cellText);
                        row.appendChild(cell);

                    }
                    tblBody.appendChild(row);
                }
            }
        }
        $('#myTable').DataTable();

}

function addInterlocutor(){
    var target = event.target;
    var id = target.id;

    $.post("/add_chatter?id="+id).done(function(resp){

        location = "/chat_room";
    })

}