var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#messages-container").show();
    }
    else {
        $("#messages-container").hide();
    }
    $("#messages").html("");
}

function connect() {
    var socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/chat-updates', function (messageFromServer) {
            data = JSON.parse(messageFromServer.body);
            console.log(data);

            switch(data.type) {
                case "message-event":
                    showMessage("<b>" + data.user + "</b> " + data.time + "<br />"
                            + "<i>" + data.message + "</i><br /><br />"
                        );
                    break;
                case "users-event":
                    $("#users-table").empty();
                    for (i = 0; i < data.usersList.length; i++)
                        showUsers(data.usersList[i]);
                    break;
            }

        }, {'username': username});
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    stompClient.send("/app/send-message", {}, JSON.stringify({'username': username, 'message': $("#message").val()}));
}

function showMessage(message) {
    $("#messages-table").append("<tr><td>" + message + "</td></tr>");
}

function showUsers(user) {
    $("#users-table").append("<tr><td>" + user + "</td></tr>");
}

$(document).ready(function(){
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    connect();

    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send-button" ).click(function() { sendMessage(); });
});