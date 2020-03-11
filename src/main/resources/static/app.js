var stompClient = null;
var stompHeaders = {};
stompHeaders[csrfHeaderName] = csrfHeaderValue;

function clientWork() {
    var socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect(stompHeaders, function (frame) {
        //setConnected(true);
        console.log('Connected: ' + frame);

        //Common events channel subscription and handling
        roomCommonEvents();
    });
}

function roomCommonEvents() {
    stompClient.subscribe('/user/queue/rooms-common-events', function (messageFromServer) {
        data = JSON.parse(messageFromServer.body);

        switch (data.eventType) {
            case "room_list_full":
                $("#room-buttons").empty();
                $("#users-table").empty();
                for (i = 0; i < data.data.length; i++) {
                    room = data.data[i];
                    room_list_full(room);

                    //Concrete room channel subscription and handling
                    roomConcrete(room);
                }
                break;
        }
    }, stompHeaders);
}

function roomConcrete(room) {
    stompClient.subscribe('/topic/room-concrete/' + room.roomId, function (messageFromServer) {
        data = JSON.parse(messageFromServer.body);

        switch (data.eventType) {
            case "new_message":
                new_message(data.data);
                break;
            case "room_all_messages":
                room_all_messages(data.data);
                break;
        }
    }, stompHeaders);
}

function room_list_full(room) {
    $("#room-buttons").append(
        "<input type='button' class='show-room-button' room_id='" + room.roomId + "' value='" + room.roomName + "' " +
        "style='background-color: #F9F9F9' />")

    $(".show-room-button").on("click", function () {
        selectRoom($(this).attr("room_id"));
    });

    appendRoom(room.roomId)

    if (room.roomType == "DEFAULT_PUBLIC_ROOM") {
        selectRoom(room.roomId);
    }

    for (k = 0; k < room.users.length; k++) {
        showUsers(room.roomId, room.users[k].username);
    }
}

function messageSend(roomId) {
    stompClient.send("/app/message-send/" + roomId, stompHeaders, JSON.stringify({'message': $("#message").val()}));
    $("#message").val("");
    $("#message").focus();
}

function new_message(data) {
    message = "<b>" + data.chatUser.username + "</b> " + data.messageTime + "<br />"
        + "<i>" + data.message + "</i><br /><br />";

    $(".room-block[room_id=" + data.roomId + "] .messages-table").append("<tr><td>" + message + "</td></tr>");

    scrollBlock = $(".room-block[room_id=" + data.roomId + "] .my-custom-scrollbar");
    scrollBlock.stop().animate({
        scrollTop: scrollBlock[0].scrollHeight
    }, 800);
}

function room_all_messages(data) {
    for (i = 0; i < data.length; i++)
        new_message(data[i]);
}

function showUsers(roomId, user) {
    $(".room-block[room_id=" + roomId + "] .users-table").append("<tr><td>" + user + "</td></tr>");
}

function appendRoom(roomId) {
    $("#chat-block").append(
        $('#message-and-users-placeholder')
            .clone()
            .attr('id', 'room-' + roomId)
            .attr('class', 'room-block')
            .attr('room_id', roomId)
    );
}

function selectRoom(roomId) {
    $(".show-room-button").css({"background-color": "#F9F9F9"})
    $(".show-room-button[room_id=" + roomId + "]").css({"background-color": "#93b582"})
    $(".room-block").hide();
    $(".room-block[room_id=" + roomId + "]").show();
}

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     if (connected) {
//         $("#messages-container").show();
//     }
//     else {
//         $("#messages-container").hide();
//     }
//     $("#messages").html("");
// }

function disconnect(client) {
    if (client !== null) {
        client.disconnect();
    }
    //setConnected(false);
    console.log("Disconnected");
}

$(document).ready(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    clientWork();

    //$( "#disconnect" ).click(function() { disconnect(stompClient); });
    $("#send-button").click(function () {
        messageSend($(".room-block:visible").attr("room_id"));
    });
});