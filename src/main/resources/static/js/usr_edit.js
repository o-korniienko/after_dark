$(document).ready(function () {
    document.getElementById("name").value = user.username;
    for (var i = 0; i < roles.length; i++) {
        if (user.roles.indexOf(roles[i]) != -1) {
            $(".rl").append(`<input class="${i}" type="checkbox" value="${roles[i]}" checked="checked">${roles[i]}<Br>`);
        } else {
            $(".rl").append(`<input class="${i}" type="checkbox" value="${roles[i]}">${roles[i]}<Br>`);
        }
    }
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

function save() {
    var username = document.getElementById("name").value;
    var index = 0;
    var rles = [];
    for (var i = 0; i < roles.length; i++) {
        var ch_box = document.getElementsByClassName(i);
        if (ch_box[0].checked === true) {
            rles[index] = ch_box[0].value.toString();
            index++;
        }
    }
    saveUser(username, rles);
}

function saveUser(username, roles) {
    var userId = user.id;

    var url = "http://localhost:8080/users?userId=" + userId + "&form=" + roles + "&username=" + username;

    $.post(url).done(function () {
        document.location = "/users";
    });
}

function back() {
    document.location = "/users";
}

function vk() {
    window.open("http://vk.com/after_dark_wow");
}

function discord() {
    window.open("https://discord.gg/WTGY8K4");
}