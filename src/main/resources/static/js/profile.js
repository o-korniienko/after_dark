$(document).ready(function () {
    document.getElementById("username").innerText = username;
    if (useremail != null) {
        document.getElementById("useremail").placeholder = useremail;
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
    var url = "http://localhost:8080/users/profile?password=" + password + "&email=" + email;
    $.post(url).done(function () {
        console.log("updated");
    });
}

function back() {
    document.location = "/main";
}
