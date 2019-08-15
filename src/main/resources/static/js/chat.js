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
getAllMessages();
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
function isAdmin(roles) {
    for (var i = 0; i < roles.length; i++) {
        if (roles[i] === "SUPER_ADMIN") {
            return true;
        }
    }
    return false;
}

function isActiveUser() {
    $.get("http://localhost:8080/active", function (resp) {
        user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
            $(".chat_room").css("display", "none");
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

function getAllMessages() {
    $.get("http://localhost:8080/messages", function (resp) {
        var messages = resp;
        $(".messages").empty();
        for (var i = 0; i < messages.length; i++) {
            var name = messages[i].user.username + ":  ";
            $(".messages").append(`<section  class="msg"
                            >${name}  ${messages[i].text} </section>`)
        }

    });
}

function sendMessage() {
    var text = $("#newMessage").val();
    var objectText = {
        text: text
    }
    var jsonText = JSON.stringify(objectText);
    $.post("http://localhost:8080/msg", jsonText).done(function (data) {
        console.log(data);
        getAllMessages()
    })


}