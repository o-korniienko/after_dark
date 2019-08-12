$(document).ready(function () {
    isActiveUser();

    getYourCharacters();
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

function isActiveUser() {
    $.get("http://localhost:8080/active", function (resp) {
        user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
            $(".chat").css("display", "none");
        } else {
            $(".cabinet").text(user.username);
            $(".login").css("display", "none");
            $(".registration").css("display", "none");
        }
    });
}


function addCharToUser() {
    var charName = $("#new_char").val();
    $.post("http://localhost:8080/chartouser?charName=" + charName, function (data) {
        if (data[0] != null) {
            alert(data[0]);
        }
        getYourCharacters();
    })

}

function getYourCharacters() {
    $.get("http://localhost:8080/chars", function (resp) {
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

function vk() {
    window.open("http://vk.com/after_dark_wow");
}

function discord() {
    window.open("https://discord.gg/WTGY8K4");
}
