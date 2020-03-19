var stompClient = null;
var stompHeaders = {};
stompHeaders[csrfHeaderName] = csrfHeaderValue;
var roomCommonSubscription;


function clientWork() {
    var socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect(stompHeaders, function (frame) {
        console.log('Connected: ' + frame);

        //Common events channel subscription and handling
        publicEvents();
    });
}

function publicEvents() {
    stompClient.subscribe('/topic/events-for-all', function (messageFromServer) {}, stompHeaders);
    userCommonEvents();
}

function userCommonEvents() {
    roomCommonSubscription = stompClient.subscribe('/user/queue/rooms-common-events', function (messageFromServer) {
        data = JSON.parse(messageFromServer.body);
        switch (data.eventType) {
            case "room_list_full": {
                $("#room-buttons").empty();
                $("#users-table").empty();
                for (i = 0; i < data.data.length; i++) {
                    room = data.data[i];
                    room_list_full(room);

                    //Concrete room channel subscription and handling
                    roomConcreteEvents(room);
                }
            }
                break;
        }
    }, stompHeaders);
}

function roomConcreteEvents(room) {
    stompClient.subscribe('/topic/room-concrete/' + room.roomId, function (messageFromServer) {
        data = JSON.parse(messageFromServer.body);
        switch (data.eventType) {
            case "new_message":
                new_message(data.data);
                break;
            case "room_all_messages":
                room_all_messages(data.data);
                break;
            case "system_command":
                new_message(data.data);
                break;
            case "room_create":
                // Если комната уже загружена, не грузим
                if (!$("#room-buttons .show-room-button[room_id='" + data.roomId + "']").length) {
                    room_list_full(data.data);
                    roomConcreteEvents(data.data);
                }
                break;
            case "room_remove":
                room_remove(data.data);
                break;
            case "room_rename":
                room_rename(data.data);
                break;
            case "room_connect":
                room_connect(data.data);
                break;
            case "user_rename":
                user_rename(data.data);
                break;
        }
    }, stompHeaders);
}

function room_list_full(room) {
    $("#room-buttons").append(
        "<input type='button' class='show-room-button' room_id='" + room.roomId + "' value='" + room.roomName + "' " +
        "style='background-color: #F9F9F9' />");

    $(".show-room-button").on("click", function () {
        selectRoom($(this).attr("room_id"));
    });

    appendRoom(room.roomId);

    if (room.roomType == "DEFAULT_PUBLIC_ROOM") {
        selectRoom(room.roomId);
    }

    for (k = 0; k < room.users.length; k++) {
        addUser(room.roomId, room.users[k]);
    }
}

function messageSend(roomId) {

    message = $("#message").val();

    if ((message.length >= 2 && message.substring(0, 2) != "//") || message.length < 2)
        message = "//msg -m \"" + message + "\"";

    stompClient.send("/app/message-send/" + roomId, stompHeaders,
        JSON.stringify({'message': message}));
    $("#message").val("");
    $("#message").focus();
}

function new_message(data) {
    message = "<b user_id='" + data.chatUser.userId + "'>" + data.chatUser.username + "</b> "
        + data.messageTime + "<br /><i>" + data.message + "</i><br /><br />";

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

function room_remove(data) {
    $("#room-buttons .show-room-button").each(function () {
        val = $(this).val().toLowerCase();
        if (val == data.toLowerCase()) {
            roomId = $(this).attr('room_id');
            $("#room-" + roomId).remove();
            $(this).remove();
            $("#room-buttons .show-room-button").first().click();
        }
    });
}

function room_rename(data) {
    roomButton = $("#room-buttons .show-room-button[room_id='" + data.roomId + "']").val(data.roomName);
}

function room_connect(data) {
    addUser(data.roomId, data.chatUser);
}

function user_rename(data) {
    $("*[user_id='" + data.userId + "']").html(data.new_username);
    if(data.userId == userId) {
        location.reload(); //не разобрался как перезапустить клиента нормально
    }
}

function addUser(roomId, user) {
    $(".room-block[room_id=" + roomId + "] .users-table").append("<tr id='user-cell'><td user_id='" + user.userId
        + "'>" + user.username + "</td></tr>");
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
    $(".show-room-button").css({"background-color": "#F9F9F9"});
    $(".show-room-button[room_id=" + roomId + "]").css({"background-color": "#93b582"});
    $(".room-block").hide();
    $(".room-block[room_id=" + roomId + "]").show();
}

function disconnect(client) {
    if (client !== null) {
        client.disconnect();
    }
    console.log("Disconnected");
}

$(document).ready(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    clientWork();

    $("#send-button").click(function () {
        messageSend($(".room-block:visible").attr("room_id"));
    });
});