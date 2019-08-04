$(document).ready(function () {
    document.getElementById("username").innerText = username;
    if (useremail != null) {
        document.getElementById("useremail").placeholder = useremail;
    }
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
