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

$(document).ready(function () {
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


function addCharactersInDB() {

var informWindow = window.open("/info/adding", "Adding Characters","width=400,height=150");


    $.post("http://localhost:8080/characters", function (resp) {
        console.log(resp);
        informWindow.close();
        window.alert("added!!!");
    })
}

function delAllCharactersFromDB() {

var informWindow = window.open("/info/deleting", "Adding Characters","width=400,height=150");
    $.delete("http://localhost:8080/chars", function (resp) {
        console.log("deleted by: " + resp.username);
        informWindow.close();
        window.alert("deleted!!!");
    })
}

function updateCharactersInDB() {
var informWindow = window.open("/info/updating", "Adding Characters","width=400,height=150");
    $.put("http://localhost:8080/characters", function (resp) {
        console.log("updated by: " + resp.username);
        informWindow.close();
        window.alert("updated!!!");
    })
}