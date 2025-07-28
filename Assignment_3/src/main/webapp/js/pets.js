function submitPet() {
    // Collecting values from the input fields
    var petId = document.getElementById('petId').value;
    var ownerId = document.getElementById('ownerId').value;
    var petName = document.getElementById('petName').value;
    var petType = document.getElementById('petType').value;
    var petBreed = document.getElementById('petBreed').value;
    var petGender = document.getElementById('petGender').value;
    var petBirthyear = document.getElementById('petBirthyear').value;
    var petWeight = document.getElementById('petWeight').value;
    var petDescription = document.getElementById('petDescription').value;
    var petPhoto = document.getElementById('petPhoto').value;

    // Basic Validation
    if (!petId || !ownerId || !petName || !petType || !petBreed || !petGender || !petBirthyear || !petWeight) {
        console.log('Please fill in all required fields.');
        return;
    }

    // Preparing the data to be sent in the POST request
    var data = 'pet_id=' + encodeURIComponent(petId) +
               '&owner_id=' + encodeURIComponent(ownerId) +
               '&name=' + encodeURIComponent(petName) +
               '&type=' + encodeURIComponent(petType) +
               '&breed=' + encodeURIComponent(petBreed) +
               '&gender=' + encodeURIComponent(petGender) +
               '&birthyear=' + encodeURIComponent(petBirthyear) +
               '&weight=' + encodeURIComponent(petWeight) +
               '&description=' + encodeURIComponent(petDescription) +
               '&photo=' + encodeURIComponent(petPhoto);

    // Creating an XMLHttpRequest object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'PetSubmission', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Handling the response from the server
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log('Pet submitted successfully.');
        } else {
            console.log('Error submitting pet: ' + xhr.status);
        }
    };

    // Sending the data
    xhr.send(data);
}



function retrievePets() {
    // Collecting values from the input fields
    var petType = document.getElementById('petType').value;
    var petBreed = document.getElementById('petBreed').value;

    // Creating an XMLHttpRequest object
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'PetRetrieval?type=' + encodeURIComponent(petType) + '&breed=' + encodeURIComponent(petBreed), true);

    // Handling the response from the server
    xhr.onload = function() {
        if (xhr.status === 200) {
            var pets = JSON.parse(xhr.responseText);
            displayPets(pets);
        } else {
            console.log('Error retrieving pets: ' + xhr.status);
        }
    };

    // Sending the request
    xhr.send();
}


function updateWeight() {
    // Collecting values from the input fields
    var petId = document.getElementById('updatePetId').value;
    var newWeight = document.getElementById('newWeight').value;

    // Basic Validation
    if (!petId || !newWeight) {
        console.log('Please enter both Pet ID and the new weight.');
        return;
    }

    // Preparing the data to be sent in the POST request
    var data = 'pet_id=' + encodeURIComponent(petId) +
               '&weight=' + encodeURIComponent(newWeight);

    // Creating an XMLHttpRequest object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'UpdatePetWeight', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Handling the response from the server
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log('Pet weight updated successfully.');
        } else {
            console.log('Error updating pet weight: ' + xhr.status);
        }
    };

    // Sending the data
    xhr.send(data);
}



function deletePet() {
    // Collecting the pet ID from the input field
    var petId = document.getElementById('deletePetId').value;

    // Basic Validation
    if (!petId) {
        console.log('Please enter the Pet ID.');
        return;
    }

    // Preparing the data to be sent in the POST request
    var data = 'pet_id=' + encodeURIComponent(petId);

    // Creating an XMLHttpRequest object
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'DeletePet', true); // Using POST as browsers may not support DELETE method in XMLHttpRequest
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Handling the response from the server
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log('Pet deleted successfully.');
        } else {
            console.log('Error deleting pet: ' + xhr.status);
        }
    };

    // Sending the data
    xhr.send(data);
}
