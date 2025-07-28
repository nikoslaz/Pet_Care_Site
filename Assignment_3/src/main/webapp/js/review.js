document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('submitReview').addEventListener('click', function() {
        var ownerId = document.getElementById('owner_id').value;
        var keeperId = document.getElementById('keeper_id').value;
        var reviewText = document.getElementById('reviewText').value;
        var reviewScore = document.getElementById('reviewScore').value;

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'AddReview', true);

        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function() {
            if (this.status >= 200 && this.status < 300) {
                document.getElementById('responseMessage').textContent = this.responseText;
            } else {
                document.getElementById('responseMessage').textContent = 'Error: ' + this.statusText;
            }
        };

        xhr.onerror = function() {
            document.getElementById('responseMessage').textContent = 'An error occurred during the request.';
        };

        var data = `owner_id=${encodeURIComponent(ownerId)}&keeper_id=${encodeURIComponent(keeperId)}&reviewText=${encodeURIComponent(reviewText)}&reviewScore=${encodeURIComponent(reviewScore)}`;
        xhr.send(data);
    });
});
