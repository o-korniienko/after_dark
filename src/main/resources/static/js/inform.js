jQuery.each(["put", "delete", "post"], function (i, method) {
    jQuery[method] = function (url, data, callback) {
        if (jQuery.isFunction(data)) {
            type = type || callback;
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
        document.getElementById("reversStr").style.display = "none";
        document.getElementById("registration").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("game").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("team").style.display = "block";
        getAllcharacters();

    }
    document.getElementById("menu2").onclick = function () {
        document.getElementById("reversStr").style.display = "none";
        document.getElementById("team").style.display = "none";
        document.getElementById("registration").style.display = "none";
        document.getElementById("game").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("chart").style.display = "block";
        getChart();
    }
    document.getElementById("menu3").onclick = function () {
        document.getElementById("reversStr").style.display = "none";
        document.getElementById("team").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("game").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("registration").style.display = "block";
    }


    document.getElementById("menu4").onclick = function () {
        document.getElementById("reversStr").style.display = "none";
        document.getElementById("team").style.display = "none";
        document.getElementById("registration").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("game").style.display = "block";
    }


    document.getElementById("menu5").onclick = function () {
        document.getElementById("team").style.display = "none";
        document.getElementById("chart").style.display = "none";
        document.getElementById("registration").style.display = "none";
        document.getElementById("game").style.display = "none";
        document.getElementById("chartEditing").style.display = "none";
        document.getElementById("reversStr").style.display = "block";
        getChart();
    }
});

function updateChart() {
    document.getElementById("chart").style.display = "none";
    document.getElementById("chartEditing").style.display = "block";
    var text = $("#chartText").text();
    console.log(text);
    $("#changedChart").val(text);
}

function vk() {
    window.open("http://vk.com/after_dark_wow");
}

function discord() {
    window.open("https://discord.gg/WTGY8K4");
}

function isActiveUser() {
    $.get("http://localhost:8080/active", function (resp) {
        user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
        } else {
            $(".cabinet").text(user.username);
            $(".login").css("display", "none");
            $(".registration").css("display", "none");
            var roles = user.roles;
            if (!isAdmin(roles)){
                $("#updateC").css("display", "none");
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

function getAllcharacters() {
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
                    bt.className = name;
                    bt.innerText = "battle.net";
                    bt.addEventListener("click", goToBT);
                    var wl = document.createElement("section");
                    wl.className = name;
                    wl.innerText = "wow.logs";
                    wl.addEventListener("click", goToWL);
                    var wp = document.createElement("section");
                    wp.className = name;
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
    var name = target.classList[0];
    window.open("https://worldofwarcraft.com/ru-ru/character/eu/borean-tundra/" + name);
}

function goToWL() {
    var target = event.target;
    var name = target.classList[0];
    window.open("https://ru.warcraftlogs.com/character/eu/бореиская-тундра/" + name);
}

function goToWP() {
    var target = event.target;
    var name = target.classList[0];
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
    var id = $("#chartID").text();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.put("http://localhost:8080/charter?id=" + id, jsonText).done(function (data) {
        document.getElementById("chart").style.display = "block";
        document.getElementById("chartEditing").style.display = "none";
        getChart();
    })
}

function createChart() {
    var text = $("#changedChart").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.post("http://localhost:8080/msg", jsonText).done(function (data) {
        console.log(data);
    })
}

function goBack() {
    document.getElementById("chart").style.display = "block";
    document.getElementById("chartEditing").style.display = "none";
}


