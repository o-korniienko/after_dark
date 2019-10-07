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
    console.log(id);
    if (errors != null) {
        showErrors(errors);
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

function goToLoginPage() {
    document.location = "/login";
}

function changePassword() {
    var pass1 = $(".user_password").val();
    var pass2 = $(".user_password2").val();
    console.log(id);

    var url = "http://localhost:8080/recover/request?password=" + pass1 + "&password2=" + pass2 + "&id=" + (+id);
    location = url;
}

function showErrors(data) {
    console.log(data[1]);
    var pass1_error = data[0];
    var pass2_error = data[1];
    var passwords_are_different_error = data[2];
    if (pass1_error != null) {
        $("#pas1").text(pass1_error);
    } else {
        $("#pas1").text('');
    }
    if (pass2_error != null) {
        $("#pas2").text(pass2_error);
    } else {
        if (passwords_are_different_error != null) {
            $("#pas2").text(passwords_are_different_error);
        } else {
            $("#pas2").text('');
        }
    }
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