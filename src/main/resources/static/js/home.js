$(document).ready(function () {
    getActiveuser();


});

function getActiveuser() {
    $.get("http://localhost:8080/active", function (resp) {
        user = resp;
        if (resp === "") {
            $(".cabinet").css("display", "none");
            $(".logout").css("display", "none");
        } else {
            console.log(user.username);
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
        for (var i = 0; i < resp.length; i++) {
            console.log(resp[i]);
        }
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