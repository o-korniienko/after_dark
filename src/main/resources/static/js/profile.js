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
        });
    };
});

$(document).ready(function () {
    document.getElementById("username").innerText = username;
    if (useremail != null) {
        document.getElementById("email").placeholder = useremail;

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

function updateProfile() {
    var password = $("#password").val();
    var email = $("#email").val();
    var url = "http://localhost:8080/users/profile_update?password=" + password + "&email=" + email;
    $.get(url, function (resp) {
            $("#email_msg").text(resp);

    });
}

function back() {
    document.location = "/main";
}

function vk() {
    window.open("http://vk.com/after_dark_wow");
}

function discord() {
    window.open("https://discord.gg/WTGY8K4");
}

function goToSupport() {
    location = "/support";
}