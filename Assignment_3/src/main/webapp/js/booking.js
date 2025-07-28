function fetchBookings() {
    var userId = sessionStorage.getItem('userId');
    var userType = sessionStorage.getItem('userType');
    

    if (!userId || !userType) {
        console.error('User ID or User Type not found in session storage');
        return; // Exit the function if userId or userType is not found
    }

    console.log('Fetching bookings for user ID:', userId, 'and user type:', userType);

    fetch('FetchReservation?userId=' + userId + '&userType=' + userType)
    .then(response => response.json())
    .then(data => {
        console.log('Bookings data received:', data);
        displayBookings(data, userType);
    })
    .catch(error => console.error('Error fetching bookings:', error));
}


function displayBookings(bookings, userType) {
    console.log('Displaying bookings for user type:', userType);
    let bookingsHTML = '';

    if (bookings.length === 0) {
        console.log('No bookings available.');
        bookingsHTML = '<p>No bookings available.</p>';
    } else {
        bookings.forEach(booking => {
            console.log('Booking ID:', booking.booking_id);
            bookingsHTML += `<div class="booking">
                <p>Booking ID: ${booking.booking_id}</p>
                <p>From: ${booking.fromdate}</p>
                <p>To: ${booking.todate}</p>
                <p>Status: ${booking.status}</p>
                <p>Price: ${booking.price}</p>`;

            if (userType === 'petkeeper' && booking.status === 'requested') {
                console.log(`Booking ${booking.booking_id} is in requested status. Allowing actions.`);
                bookingsHTML += `<button onclick="updateReservationStatus(${booking.booking_id}, 'accepted')">Accept</button>
                                 <button onclick="updateReservationStatus(${booking.booking_id}, 'rejected')">Reject</button>`;
            }

            bookingsHTML += `</div>`;
        });
    }

    // Check if element exists before trying to modify it
    const bookingSection = document.getElementById('bookingSection');
    if (bookingSection) {
        bookingSection.style.display = 'block';
        document.getElementById('bookings').innerHTML = bookingsHTML;
    } else {
        console.error('Booking section element not found');
    }
    
}


function updateReservationStatus(bookingId, newStatus) {
    console.log('Updating reservation status for booking ID:', bookingId, 'to:', newStatus); // Debugging

    fetch('UpdateReservationStatus', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ bookingId: bookingId, status: newStatus })
    })
    .then(response => {
        if (response.ok) {
            console.log('Reservation status updated successfully.');
            // Refresh the bookings list
            fetchBookings(loggedInUserId, loggedInUserType);
        } else {
            console.log('Error updating reservation status.');
        }
    })
    .catch(error => console.error('Error updating reservation status:', error));
}

document.addEventListener("DOMContentLoaded", function(){
    var userId = sessionStorage.getItem('userId');
    var userType = sessionStorage.getItem('userType');
    if(userId && userType){
        fetchBookings(userId, userType);
    }
});

