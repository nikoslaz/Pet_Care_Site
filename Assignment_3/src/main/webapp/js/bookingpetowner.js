document.addEventListener('DOMContentLoaded', function() {
    fetchAvailableKeepers();
});

function fetchAvailableKeepers() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'BookingPetOwner', true);
    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log("Response received: " + this.responseText);
            var keepers = JSON.parse(this.responseText);
            console.log("Parsed keepers: ", keepers);
            displayKeepers(keepers);
        } else if (this.readyState === 4) {
            console.log("Error: ", this.statusText);
        }
    };
    xhr.send();
}



function displayKeepers(keepers) {
    var keepersListDiv = document.getElementById("keepersList");
    keepersListDiv.innerHTML = ''; // Clear previous list

    keepers.forEach(function(keeper) {
        var keeperDiv = document.createElement('div');
        keeperDiv.innerHTML = 'Keeper: ' + keeper.firstname + ' - <button onclick="selectKeeper(' + keeper.id + ')">Select</button>';
        keepersListDiv.appendChild(keeperDiv);
    });
}

function selectKeeper(keeperId) {
    document.getElementById("selectedKeeperId").value = keeperId;
    document.getElementById("makeReservation").style.display = 'block'; // Show booking form
}

function createBooking() {
    var keeperId = document.getElementById("selectedKeeperId").value;
    var ownerId = document.getElementById("ownerId").value;
    var petId = document.getElementById("petId").value;
    var fromDate = document.getElementById("fromDate").value;
    var toDate = document.getElementById("toDate").value;
    var status = document.getElementById("status").value;
    var price = document.getElementById("price").value;

    var bookingData = {
        keeper_id: parseInt(keeperId),
        owner_id: parseInt(ownerId),
        pet_id: parseInt(petId),
        fromDate: fromDate,
        toDate: toDate,
        status: status,
        price: parseInt(price)
    };

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'CreateBooking', true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log("Booking created successfully.");
        }
    };

    xhr.send("booking=" + encodeURIComponent(JSON.stringify(bookingData)));
}


function updateBooking() {
    var bookingID = document.getElementById("updateBookingID").value;
    var status = document.getElementById("newStatus").value;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'BookingPetOwner', true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log("Booking status updated successfully.");
        }
    };

    xhr.send('action=update&bookingID=' + bookingID + '&status=' + status);
}

function markAsFinished() {
    var bookingID = document.getElementById("finishedBookingID").value;
    var status = "finished"; // Setting the status to 'finished'

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'UpdateBookingServlet', true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log("Reservation marked as finished successfully.");
            // Additional actions upon successful update, e.g., updating the UI
        }
    };

    xhr.send('bookingID=' + encodeURIComponent(bookingID) + '&status=' + encodeURIComponent(status));
}
