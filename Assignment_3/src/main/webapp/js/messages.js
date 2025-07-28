document.addEventListener("DOMContentLoaded", function() {
    fetchMessages();

    var sendMessageButton = document.getElementById('sendMessageButton');
    if (sendMessageButton) {
        sendMessageButton.addEventListener('click', function() {
            var message = document.getElementById('messageText').value;
            var bookingId = document.getElementById('bookingId').value;
            var sender = document.getElementById('sender').value;
            sendMessage(message, bookingId, sender);
        });
    } else {
        console.error("Send message button not found");
    }
});

function fetchMessages() {
    const bookingId = 1; // Retrieve booking ID from the page context or URL
    console.log('Fetching messages...');

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const messages = JSON.parse(xhr.responseText);
            console.log('Messages fetched successfully:', messages);
            displayMessages(messages);
        } else {
            console.error('Error fetching messages: ' + xhr.status);
        }
    };

    xhr.open('GET', `FetchMessages?bookingId=${bookingId}`, true);
    xhr.send();
}

function sendMessage(message, bookingId, sender) {
    console.log('Sending message...');

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('Message sent successfully.');
            fetchMessages();
        } else {
            console.error('Error sending message: ' + xhr.status);
        }
    };

    xhr.open('POST', 'SendMessage');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    var data = `message=${encodeURIComponent(message)}&bookingId=${encodeURIComponent(bookingId)}&sender=${encodeURIComponent(sender)}`;
    xhr.send(data);
}

function displayMessages(messages) {
    const messagesContainer = document.getElementById('messages-container');
    messagesContainer.innerHTML = ''; 

    messages.forEach(message => {
        const messageElement = document.createElement('div');
        messageElement.className = 'message';
        messageElement.innerHTML = `
            <div class="sender">${message.sender}</div>
            <div class="content">${message.message}</div>
            <div class="datetime">${message.datetime}</div>
        `;
        messagesContainer.appendChild(messageElement);
    });
}
