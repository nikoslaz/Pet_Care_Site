function sendQuery() {
    var petType = document.getElementById('petType').value;
    var breed = document.getElementById('breed').value;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "ChatGPT", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            document.getElementById('response').innerHTML = this.responseText;
        }
    }

    xhr.send("petType=" + encodeURIComponent(petType) + "&breed=" + encodeURIComponent(breed));
}
