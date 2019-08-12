var usr = users;

$(document).ready(function () {
    getUsers();
    (function () {
        var date = new Date();
        var h = date.getHours();
        h = h < 10 ? "0" + h : h;
        var m = date.getMinutes();
        m = m < 10 ? "0" + m : m;
        var s = date.getSeconds();
        s = s < 10 ? "0" + s : s;
        var time = h + ":" + m + ":" + s;
        $("#aT").text(time);
        window.setTimeout(arguments.callee, 1000);

    })();
});

function getUsers() {
    var tblBody = document.getElementById("tb_body");

    for (var i = 0; i < usr.length; i++) {
        var row = document.createElement("tr");
        for (var j = 0; j < 3; j++) {
            var cell = document.createElement("td");
            var bt;
            var cellText;
            if (j === 0) {
                cellText = document.createTextNode(usr[i].username);
            }
            if (j === 1) {
                for (var k = 0; k < usr[i].roles.length; k++) {
                    if (k === 0) {
                        cellText = usr[i].roles[k];
                    } else {
                        cellText = cellText + ", " + usr[i].roles[k];
                    }
                }
                cellText = document.createTextNode(cellText);
            } else {
                bt = document.createElement("button");
                bt.innerText = "Edit";
                bt.id = i;
                bt.onclick = function () {
                    var idk = this.id;
                    var user_id = usr[idk].id;
                    var url = "/users/" + user_id;
                    document.location = url;
                }
            }
            if (j === 2) {
                cell.appendChild(bt);
            } else {
                cell.appendChild(cellText);
            }
            row.appendChild(cell);
            cellText = "";
        }
        tblBody.appendChild(row);
    }
}
function back() {
    document.location = "/";
}