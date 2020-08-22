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

function registration() {
    var username = $(".user_name").val();
    var password = $(".user_password").val();
    var password2 = $(".user_password2").val();
    var email = $(".email").val();
    var captchaConfirm = grecaptcha.getResponse();


    var url = "/registration/request?username=" + username + "&g-recaptcha-response=" +
        captchaConfirm + "&password=" + password + "&password2=" + password2 + "&email=" + email;
    location = url;

}

function relocation(data) {
    location.href = data;
}

$(document).ready(function () {
    if (errors != null) {
        console.log(errors);
        showErrors(errors)
        errors = null;
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

function showErrors(data) {
    console.log(data[2]);
    var pasError;
    var usrNameError;
    var emailError;

    var usrError = data[2];
    var pas2Error = data[1];
    var pas2Empty = data[3];
    var captchaError = data[4];
    for (var i = 5; i < data.length; i++) {
        if (data[i] === null) {

        } else {
            if (data[i].indexOf("Email") !== -1) {
                emailError = data[i];
            }
            if (data[i].indexOf("Password") !== -1) {
                pasError = data[i];
            }
            if (data[i].indexOf("User") !== -1) {
                usrNameError = data[i];
            }
        }
    }
    if (usrNameError != null) {
        $("#usr").text(usrNameError);
    } else {
        $("#usr").text('');
    }
    if (usrError != null) {
        $("#error").text(usrError);
    } else {
        $("#error").text('');
    }
    if (captchaError != null) {
        $("#capt").text(captchaError);
    } else {
        $("#capt").text('');
    }
    if (pasError != null) {
        $("#pas1").text(pasError);
    } else {
        $("#pas1").text('');
    }
    if (pas2Empty != null) {
        $("#pas2").text(pas2Empty);
    } else {
        if (pas2Error != null) {
            $("#pas2").text(pas2Error);
        } else {
            $("#pas2").text('');
        }
    }
    if (emailError != null) {
        $("#email").text(emailError);
    } else {
        $("#email").text('');
    }


}

function goToLoginPage() {
    document.location = "/login";
}

