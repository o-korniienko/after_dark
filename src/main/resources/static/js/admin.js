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

});

function isActiveUser() {
    $.get("/active", function (resp) {
        user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
            $(".chat_room").css("display", "none");
            $("#admins").css("display", "none");
        } else {
            $(".cabinet").text(user.username);
            $(".login").css("display", "none");
            $(".registration").css("display", "none");
            var roles = user.roles;
            if (!isSuperAdmin(roles)) {
                $("#usr_admin").css("display", "none");
            }
        }
    });
}

function isSuperAdmin(roles) {
    for (var i = 0; i < roles.length; i++) {
        if (roles[i] === "SUPER_ADMIN") {
            return true;
        }
    }
    return false;
}

function goToUsersOption() {
    location = "/users";
}

function goToUpdateAddCharactersPage() {
    location = "/add_update_characters";
}

function goToListOfRecipients() {
    location = "/list_of_recipients";
}