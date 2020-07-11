//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat?token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiZmFsbWVyIiwiaWF0IjoxNTk0NDc0NzA1LCJzdWIiOiJzZWN1cml0eSIsImlzcyI6InNlY3VyaXR5U2VydmljZSIsImV4cCI6MTU5NTQ3NDcwNX0.XWa8PEnBMNIF2MHLFVlnnV-GyB4R69xd7np-ExjVWhc");
webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function () { alert("WebSocket connection closed") };

//когда жмешь кнопку отправить
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//для enter-a
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        let msg = {
            type: 0,
            msgText: message,
            from: 'bfalmer',
            to: 'smight',
            date: 0
        };
        //todo json кому отправляем
        webSocket.send(JSON.stringify(msg));
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insert("chat", "<b/><span> user " + data.from + " says: " + data.msgText + "</span></p>");
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
