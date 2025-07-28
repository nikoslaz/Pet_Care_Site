function loadPetKeepers() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'FindPetKeeper?userLocation=' + encodeURIComponent("35.339766,-25.136596"), true);
    
    xhr.onload = function() {
        if (xhr.status === 200) {
            var petKeepers = JSON.parse(xhr.responseText);
            displayPetKeepers(petKeepers);
        } else {
            console.error('Error loading pet keepers: ' + xhr.status);
        }
    };

    xhr.send();
}

function displayPetKeepers(petKeepers) {
    var displayDiv = document.getElementById('petKeepersDisplay');
    displayDiv.innerHTML = ''; // Clear existing content

    petKeepers.forEach(function(keeper) {
        var keeperDiv = document.createElement('div');
        keeperDiv.classList.add('pet-keeper');

        // Check if distance exists and is a number before calling toFixed()
        var distanceStr = (keeper.distance && !isNaN(keeper.distance)) ? keeper.distance.toFixed(2) + ' km' : 'N/A';
        
        keeperDiv.innerHTML = '<h3>' + (keeper.username || 'Unknown Name') + '</h3>'
        
        displayDiv.appendChild(keeperDiv);
    });
}


// Load pet keepers when the page loads
window.onload = loadPetKeepers;
