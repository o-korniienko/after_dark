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
var tags = tags;
$(document).ready(function () {
    getAllRecipients();
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


function getAllRecipients() {
    var recipients = [];
    $.get("/recipient", function (result) {
        recipients = result;
        console.log(recipients);

        if (recipients.length > 0) {
            var tblBody = document.getElementById("tbl");
            var rows = document.getElementsByTagName("tr");
            var rowCount = rows.length;
            for (var i = rowCount - 1; i > 0; i--) {
                tblBody.removeChild(rows[i]);
            }
            for (var i = 0; i < recipients.length; i++) {
                var row = document.createElement("tr");
                for (var j = 0; j < 5; j++) {
                    var cell = document.createElement("td");
                    var bt;
                    var cellText;
                    var box1;
                    var box2;
                    if (j === 0) {
                        cellText = document.createTextNode(recipients[i].emailAddress);
                    }
                    if (j === 1) {
                        box1 = createCheckBox(tags[0], recipients, i);
                    }
                    if (j === 2) {
                        box2 = createCheckBox(tags[1], recipients, i);
                    }
                    if (j === 3) {
                        bt = document.createElement("button");
                        bt.innerText = "Save";
                        bt.id = recipients[i].id;
                        bt.onclick = function () {
                            var id = this.id;
                            var tgs = []
                            var index = 0;
                            for (var i = 0; i < tags.length; i++) {
                                var ch_box = document.getElementsByClassName(id);
                                if (ch_box[i].checked === true) {
                                    tgs[index] = ch_box[i].value.toString();
                                    index++;
                                }
                            }
                            $.put("/recipient?recipient_id=" + id + "&tags=" + tgs, function (result) {

                            })
                        }
                        console.log(bt)
                    } else {
                        bt = document.createElement("button");
                        bt.innerText = "Delete";
                        bt.id = recipients[i].id;
                        bt.onclick = function () {
                            var id = this.id;
                            $.delete("/recipient/" + id, function (resp) {
                                document.location.reload();
                            });
                        }
                    }
                    if (j === 4 || j === 3) {
                        cell.appendChild(bt);
                    }
                    if (j === 1) {
                        cell.appendChild(box1);
                    }
                    if (j === 2) {
                        cell.appendChild(box2);
                    }
                    if (j === 0) {
                        cell.appendChild(cellText);
                    }
                    row.appendChild(cell);
                    cellText = "";
                    section = null;
                }
                tblBody.appendChild(row);
            }
        }
    })
}

function addRecipient() {
    var email = $("#new_recipient").val();
    $.post("/recipient?email=" + email, function (resp) {
        getAllRecipients();
    })
}

function createCheckBox(tag, recipients, index) {
    var id = recipients[index].id;
    var box;
    if (recipients[index].tags.indexOf(tag) != -1) {
        var box_checked = document.createElement("input");
        box_checked.className = id;
        box_checked.type = "checkbox";
        box_checked.value = tag;
        box_checked.checked = "checked";
        box_checked.text = tag;
        box = box_checked
    } else {
        var box_unchecked = document.createElement("input");
        box_unchecked.className = id;
        box_unchecked.type = "checkbox";
        box_unchecked.value = tag;
        box_unchecked.text = tag;
        box = box_unchecked;
    }
    return box;
}