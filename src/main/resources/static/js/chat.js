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
var global_messages = [];

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
    setInterval("isChatChanged()", 1000);
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
        //user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
            $(".chat_room").css("display", "none");
        } else {
            $(".cabinet").text(user.username);
            $(".login").css("display", "none");
            $(".registration").css("display", "none");
            var roles = user.roles;
            if (!isAdmin(roles)) {
                $("#updateC").css("display", "none");
            }
        }
    });
}

function getAllMessages() {
    $.get("http://localhost:8080/messages", function (resp) {
        normalizationMessages(resp);
        $(".messages").empty();
        fillMessages(resp);
    });

}

function isChatChanged() {
    var messagesJson = JSON.stringify(global_messages);
    var size = global_messages.length;
    //console.log(global_messages);
    $.put("http://localhost:8080/messages", messagesJson).done(function (resp) {
        if (resp.length > 0) {
            normalizationMessages(resp);
            $(".messages").empty();
            fillMessages(resp);
        }
    })
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

function editMsg(id) {
    var text = document.getElementById(id).innerText;
    $("#newMessage").val(text);
    $("#sendMessage").css("display", "none");
    $("#changeMessage").css("display", "block")
    global_id = id;
    isChatChanged()
}

function changeMessage() {
    var text = $("#newMessage").val();
    var msgObject = {
        text: text
    }
    var msgJson = JSON.stringify(msgObject);

    $.put("http://localhost:8080/msg?id=" + global_id, msgJson).done(function (resp) {
        getAllMessages();
    })

}

function deleteMsg(id) {
    $.delete("http://localhost:8080/msg?id=" + id, function (resp) {
        getAllMessages();
    })
}

function sendMessage() {
    var text = $("#newMessage").val();
    var objectText = {
        text: text
    }

    var jsonText = JSON.stringify(objectText);
    $.post("http://localhost:8080/msg", jsonText).done(function (data) {
        $("#newMessage").val("");
        getAllMessages();
    })


}

function fillMessages(messages) {
    for (var i = 0; i < messages.length; i++) {
        var name = messages[i].user.username;
        var usrname = name + ":  ";
        var id = messages[i].id;
        var text = messages[i].text;
        $(".messages").append(`<section  class="msg">${usrname} &nbsp;<a id="${id}">${text}</a>
                            <button onclick="deleteMsg(${id})" class="controllersD">delete</button>
                            <button onclick="editMsg(${id})" class="${name}" style="display: none">edit</button>
                            </section>`)
        showEdit(name);
        showDelete();
    }
}

function normalizationMessages(messages) {
    for (var i = 0; i < messages.length; i++) {
        var id = messages[i].id;
        var text = messages[i].text;
        var tag = messages[i].tag;
        var user_id = messages[i].user.id;
        var username = messages[i].user.username;
        var password = messages[i].user.password;
        var active = messages[i].user.active;
        var email = messages[i].user.email;
        var activationCode = messages[i].user.activationCode;
        var roles = messages[i].user.roles;
        var user = {
            id: user_id,
            username: username,
            password: password,
            active: active,
            email: email,
            activationCode: activationCode,
            roles: roles
        }
        var normMessage = {
            id: id,
            text: text,
            tag: tag,
            user: user
        }
        global_messages[i] = normMessage;
    }
}