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

var global_id;
var user;
$(document).ready(function () {
    isActiveUser();
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



    document.getElementById("menu1").onclick = function () {
        document.getElementById("recruiting").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("recruitingEditing").style.display = "none";
        document.getElementById("announcements").style.display = "none";
        document.getElementById("team").style.display = "block";
        $("#menu1").removeClass('vmenu');
        $("#menu1").addClass('cmenu');
        $("#menu2").removeClass('cmenu');
        $("#menu2").addClass('vmenu');
        $("#menu3").removeClass('cmenu');
        $("#menu3").addClass('vmenu');
        $("#menu4").removeClass('cmenu');
        $("#menu4").addClass('vmenu');
        getAllCharacters();
    }
    document.getElementById("menu2").onclick = function () {
        document.getElementById("team").style.display = "none";
        document.getElementById("recruiting").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("recruitingEditing").style.display = "none";
        document.getElementById("announcements").style.display = "none";
        document.getElementById("chart").style.display = "block";
        $("#menu2").removeClass('vmenu');
        $("#menu2").addClass('cmenu');
        $("#menu1").removeClass('cmenu');
        $("#menu1").addClass('vmenu');
        $("#menu3").removeClass('cmenu');
        $("#menu3").addClass('vmenu');
        $("#menu4").removeClass('cmenu');
        $("#menu4").addClass('vmenu');
        getChart();
    }
    document.getElementById("menu3").onclick = function () {
        document.getElementById("team").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("recruitingEditing").style.display = "none";
        document.getElementById("announcements").style.display = "none";
        document.getElementById("recruiting").style.display = "block";
        $("#menu3").removeClass('vmenu');
        $("#menu3").addClass('cmenu');
        $("#menu2").removeClass('cmenu');
        $("#menu2").addClass('vmenu');
        $("#menu1").removeClass('cmenu');
        $("#menu1").addClass('vmenu');
        $("#menu4").removeClass('cmenu');
        $("#menu4").addClass('vmenu');
        getRecruitingText();
    }

    document.getElementById("menu4").onclick = function () {
        document.getElementById("team").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("recruitingEditing").style.display = "none";
        document.getElementById("recruiting").style.display = "none";
        document.getElementById("announcements").style.display = "block";
        $("#menu4").removeClass('vmenu');
        $("#menu4").addClass('cmenu');
        $("#menu2").removeClass('cmenu');
        $("#menu2").addClass('vmenu');
        $("#menu3").removeClass('cmenu');
        $("#menu3").addClass('vmenu');
        $("#menu1").removeClass('cmenu');
        $("#menu1").addClass('vmenu');
        if (!isAdmin(user.roles)) {
            $(".sending").css("display", "none");
        }
        getAllAnnouncements();
    }

});

function isActiveUser() {
    $.get("http://localhost:8080/active", function (resp) {
        user = resp;
        if (user === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
            $(".chat_room").css("display", "none");
            $("#updateC").css("display", "none");
            $("#updateR").css("display", "none");

        } else {
            $(".cabinet").text(user.username);
            $(".login").css("display", "none");
            $(".registration").css("display", "none");
            $("#menu4").css("display", "inline");
            var roles = user.roles;
            if (!isAdmin(roles)) {
                $("#updateC").css("display", "none");
                $("#updateR").css("display", "none");
            }
        }
    });
}

function isAdmin(roles) {
    for (var i = 0; i < roles.length; i++) {
        if ((roles[i] === "SUPER_ADMIN") || (roles[i] === "ADMIN")) {
            return true;
        }
    }
    return false;
}

function getAllCharacters() {
    $.get("http://localhost:8080/characters", function (resp) {
        showCharacters(resp);
    });
}

function showCharacters(resp) {
    var characters = resp;
    if (characters.length <= 0) {
        $("#titles").css("display", "none");
    } else {
        var tblBody = document.getElementById("body");
        var rows = tblBody.getElementsByTagName("tr");
        var rowCount = rows.length;
        for (var i = rowCount - 1; i > 0; i--) {
            tblBody.removeChild(rows[i]);
        }
        var cellText;
        var text;
        for (var i = 0; i < characters.length; i++) {
            var row = document.createElement("tr");
            for (var j = 0; j < 5; j++) {
                if (j === 0) {
                    text = characters[i].name;
                    cellText = document.createTextNode(text);
                }
                if (j === 3) {
                    text = characters[i].classRu;
                    cellText = document.createTextNode(text);
                }
                if (j === 2) {
                    text = characters[i].level;
                    cellText = document.createTextNode(text);
                }
                if (j === 1) {
                    text = characters[i].rank;
                    cellText = document.createTextNode(text);
                }
                if (j === 4) {
                    var name = characters[i].name;
                    var sect = document.createElement("section");
                    sect.className = name;
                    var bt = document.createElement("section");
                    bt.className = "characters";
                    bt.id = name;
                    bt.innerText = "battle.net";
                    bt.addEventListener("click", goToBT);
                    var wl = document.createElement("section");
                    wl.className = "characters";
                    wl.id = name;
                    wl.innerText = "wow.logs";
                    wl.addEventListener("click", goToWL);
                    var wp = document.createElement("section");
                    wp.className = "characters";
                    wp.id = name;
                    wp.innerText = "wow.progress";
                    wp.addEventListener("click", goToWP);
                    sect.appendChild(bt);
                    sect.appendChild(wl);
                    sect.appendChild(wp);

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
    $('#myTable').DataTable();
}

function goToBT() {
    var target = event.target;
    var name = target.id;
    window.open("https://worldofwarcraft.com/ru-ru/character/eu/borean-tundra/" + name);
}

function goToWL() {
    var target = event.target;
    var name = target.id;
    window.open("https://ru.warcraftlogs.com/character/eu/бореиская-тундра/" + name);
}

function goToWP() {
    var target = event.target;
    var name = target.id;
    window.open("https://www.wowprogress.com/character/eu/борейская-тундра/" + name);
}

function getChart() {
    $.get("http://localhost:8080/charter", function (resp) {
        var text = resp.text;
        var id = resp.id;
        $("#chartText").text(text);
        $("#chartID").text(id);
    })
}

function changeChart() {
    var text = $("#changedChart").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.put("http://localhost:8080/charter", jsonText).done(function (data) {
        document.getElementById("chart").style.display = "block";
        document.getElementById("chartEditing").style.display = "none";
        getChart();
    })
}

function updateChart() {
    document.getElementById("chart").style.display = "none";
    document.getElementById("chartEditing").style.display = "block";
    var text = $("#chartText").text();
    $("#changedChart").val(text);
}

function getRecruitingText() {
    $.get("http://localhost:8080/recruiting", function (resp) {
        var text = resp.text;
        var id = resp.id;
        $("#recruitingText").text(text);
        $("#recruitingID").text(id);
    })
}

function updateRecruitingText() {
    document.getElementById("recruiting").style.display = "none";
    document.getElementById("recruitingEditing").style.display = "block";
    var text = $("#recruitingText").text();
    $("#changedRecruitingText").val(text);
}

function changeRecruitingText() {
    var text = $("#changedRecruitingText").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.put("http://localhost:8080/recruiting", jsonText).done(function (data) {
        document.getElementById("recruiting").style.display = "block";
        document.getElementById("recruitingEditing").style.display = "none";
        getRecruitingText();
    })
}

function goBackToCharter() {
    document.getElementById("chart").style.display = "block";
    document.getElementById("chartEditing").style.display = "none";
}

function goBackToRecruiting() {
    document.getElementById("recruiting").style.display = "block";
    document.getElementById("recruitingEditing").style.display = "none";
}

function sendRequest() {
    var text = $("#requestText").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.post("http://localhost:8080/send_request", jsonText, function (resp) {
        console.log(resp);
    })
}

function createAnnouncement() {
    var text = $("#new_announcement").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.post("http://localhost:8080/announcements", jsonText).done(function (data) {
        getAllAnnouncements();
        $("#new_announcement").val("");
    })
}

function getAllAnnouncements() {
    $.get("http://localhost:8080/announcements", function (resp) {
        $(".messages").empty();
        fillAnnouncements(resp);
    });

}

function fillAnnouncements(messages) {
    for (var i = 0; i < messages.length; i++) {
        var name = messages[i].user.username;
        var usrname = name + ":  ";
        var id = messages[i].id;
        var text = messages[i].text;
        var timeInSeconds = messages[i].epochSecond;
        var date = new Date(timeInSeconds * 1000);
        var day = date.getDate();
        day = day < 10 ? "0" + day : day;
        var month = date.getMonth() + 1;
        month = month < 10 ? "0" + month : month;
        var year = date.getFullYear();
        var hours = date.getHours();
        hours = hours < 10 ? "0" + hours : hours;
        var minutes = date.getMinutes();
        minutes = minutes < 10 ? "0" + minutes : minutes;
        var correctDate = day + "." + month + "." + year + " " + hours + ":" + minutes;;
        $(".messages").append(`<section  class="msg" style="color: indigo; line-height: 25px;">${usrname} &nbsp;
                            <a style="color: orangered; font-family: 'Comic Sans MS'" id="${id}">${text}</a>
                            <a class="message_time">${correctDate}</a> 
                            <button onclick="deleteAnnouncement(${id})" class="controllersD">&#10008</button>
                            <button onclick="editAnnouncement(${id})" class="${name}" style="display: none;float: right;">&#9998</button>
                            </section>`)
        showEdit(name);
        showDelete();
    }
}

function showEdit(name) {
    var usrname = user.username;
    if (usrname == name) {
        $("." + name).css("display", "inline");

    }

}

function showDelete() {
    var roles = user.roles;
    if (isAdmin(roles)) {
        $(".controllersD").css("display", "inline");
    }
}

function editAnnouncement(id) {
    var text = document.getElementById(id).innerText;
    $("#new_announcement").val(text);
    $("#sendAnnouncement").css("display", "none");
    $("#changeAnnouncement").css("display", "block")
    global_id = id;
}

function deleteAnnouncement(id) {
    $.delete("http://localhost:8080/msg?id=" + id, function (resp) {
        getAllAnnouncements();
    })
}

function changeAnnouncement() {
    var text = $("#new_announcement").val();
    var msgObject = {
        text: text
    }
    var msgJson = JSON.stringify(msgObject);

    $.put("http://localhost:8080/announcements?id=" + global_id, msgJson).done(function (resp) {
        getAllAnnouncements();
        $("#new_announcement").val("");
        $("#sendAnnouncement").css("display", "block");
        $("#changeAnnouncement").css("display", "none")
    })

}


