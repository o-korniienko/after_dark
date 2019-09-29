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

var BNET_ID = "9125eaa751bf43f5aee173f0d8d50efd";
var BNET_SECRET = "AHfMwyqoBADo244amT1jRfTg1Ll0JrMw";

function fillCharacters() {
    /*var url = "https://eu.api.blizzard.com/wow/guild/borean-tundra/" +
        "После Тьмы?fields=members&locale=ru_RU&access_token=USZqsm0klEPQ5f3GEvl5Vte77p9r28Sl5R";

    $.get(url, function (resp) {
        console.log(resp);
    });
    */
    $.post("http://localhost:8080/characters", function (resp) {
        console.log(resp);
    })
}

function delAll() {
    $.delete("http://localhost:8080/chars", function () {
        console.log("done");
    })
}