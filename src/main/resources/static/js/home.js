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
            if (!isAdmin(roles)) {
                $(".admins").css("display", "none");
            }
        }
    });
}

function addAllCharactersInDB() {
    $("user").text(user);
    $.get("http://localhost:8080/fillCharacters", function (resp) {
    })
}
function isAdmin(roles) {
    for (var i = 0; i < roles.length; i++) {
        if (roles[i] === "SUPER_ADMIN") {
            return true;
        }
    }
    return false;
}