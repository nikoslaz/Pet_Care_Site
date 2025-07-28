/* global google, response */

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;
}

function getUser() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#ajaxContent").html(createTableFromJSON(JSON.parse(xhr.responseText)));
          //  $("#ajaxContent").html("Successful Login");
        } else if (xhr.status !== 200) {
             $("#ajaxContent").html("User not exists or incorrect password");
        }
    };
    var data = $('#loginForm').serialize();
    xhr.open('GET', 'GetKeeper?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function initDB() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
              $("#ajaxContent").html("Successful Initialization");
        } else if (xhr.status !== 200) {
             $("#ajaxContent").html("Error Occured");
        }
    };

    xhr.open('GET', 'InitDB');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function deleteDB() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
              $("#ajaxContent").html("Successful Deletion");
        } else if (xhr.status !== 200) {
             $("#ajaxContent").html("Error Occured");
        }
    };

    xhr.open('GET', 'DeleteDB');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function validatePassword() {
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    var errorMessageElement = document.getElementById("error-message");
    var strengthElement = document.getElementById("password-strength");
    var blacklist = ["cat", "dog", "gata", "skulos"];

    for(var i = 0; i < blacklist.length; i++){
        if(password.indexOf(blacklist[i]) >= 0){
            errorMessageElement.textContent = "Password contains a blacklisted word.";
            strengthElement.textContent = "";
            return false;
        }
    }

    if (password !== confirmPassword) {
        errorMessageElement.textContent = "Passwords do not match!";
        strengthElement.textContent = "";
        return false;
    }

    errorMessageElement.textContent = "";
    return true;
}

function togglePasswordStrength() {
    var password = document.getElementById("password").value;
    var strengthElement = document.getElementById("password-strength");

    var numbersCount = (password.match(/\d/g) || []).length;
    var lowercaseCount = (password.match(/[a-z]/g) || []).length;
    var uppercaseCount = (password.match(/[A-Z]/g) || []).length;
    var symbolCount = (password.match(/[^a-zA-Z\d]/g) || []).length;

    if (numbersCount >= password.length / 2) {
        strengthElement.textContent = "Weak Password";
    } else if (symbolCount >= 1 && lowercaseCount >= 1 && uppercaseCount >= 1 && numbersCount >= 1) {
        strengthElement.textContent = "Strong Password";
    } else {
        strengthElement.textContent = "Medium Password";
    }
}


function shows_password() {
    var x = document.getElementById("password");
    if (x.type === "password") {
      x.type = "text";
    } else {
      x.type = "password";
    }
}

function shows_conf_password(){
    var t = document.getElementById("confirmPassword");
    if (t.type === "password") {
        t.type = "text";
    } else {
        t.type = "password";
    }
}

function togglePriceInput_cat(element) {
    var catPriceInput = document.getElementById('catprice');
    catPriceInput.disabled = !element.checked;
}

function togglePriceInput_dog(element) {
    var catPriceInput = document.getElementById('dogprice');
    catPriceInput.disabled = !element.checked;
}

function handleAccommodationChange(){
    var cat = document.getElementById('catprice');
    var cat_r = document.getElementById('cat');
    var dog = document.getElementById('dogprice');

    cat_r.disabled = true;
    cat.disabled = true;
    dog.disabled = false;
}


let formHasBeenCleared = false;

document.querySelectorAll('input[name="petO"]').forEach((radio) => {
    radio.addEventListener('change', handlePetChange);
});

function handlePetChange() {
    const petSelection = document.querySelector('input[name="pet"]:checked').value;

    if (petSelection === "petO" && !formHasBeenCleared) {
        document.getElementById("form").reset();
        formHasBeenCleared = true;
    } else if (petSelection === "petK") {
        formHasBeenCleared = false;
    }
}

var lat = 0;
var lon = 0;

function loadDoc(){
    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const obj = JSON.parse(xhr.responseText);
            var addressDetails = obj[0];
            var displayName = addressDetails.display_name;
            lon = addressDetails.lon;
            lat = addressDetails.lat;
            
            if(displayName.includes('Greece'))
            {
                if(displayName.includes('Heraklion'))
                {
                    if(displayName.includes('Μεραμβέλλου'))
                    {
                        map(lat,lon);
                        document.getElementById("demo").innerHTML = "Correct";
                    }
                    else
                    {
                        console.log('Adress belongs in Heraklion but is not the correct one');
                        var mapElement = document.getElementById("Map");
                        if (mapElement) {
                            mapElement.parentNode.removeChild(mapElement);
                        }
                        document.getElementById("demo").innerHTML = "Adress belongs in Heraklion but is not the correct one";
                    }
                }
                else
                {
                    var mapElement = document.getElementById("Map");
                    if (mapElement) {
                        mapElement.parentNode.removeChild(mapElement);
                    }
                    document.getElementById("demo").innerHTML = "It isn't in Heraklion";
                }
            }
            else
            {
                var mapElement = document.getElementById("Map");
                if (mapElement) {
                    mapElement.parentNode.removeChild(mapElement);
                }
                document.getElementById("demo").innerHTML = "It isn't in Greece";
            }

        }
    });

    var addressName = document.getElementById("address").value;
    var city = document.getElementById("town").value;
    var country = document.getElementById("country").value;
    

    var address = addressName + " " + city + " " + country;
    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=" + address + "&acceptlanguage=en&polygon_threshold=0.0");
    xhr.setRequestHeader("x-rapidapi-host", "forward-reverse-geocoding.p.rapidapi.com");
    xhr.setRequestHeader("x-rapidapi-key", '4382229303msh86dba8ee47ad3d0p18b607jsn465d9a62bad8');
    xhr.send(data);

}

function map(lat, lon) {
    var mapObj = new OpenLayers.Map("Map");
    var mapnik = new OpenLayers.Layer.OSM();
    mapObj.addLayer(mapnik);

    // Orismos Thesis
    function setPosition(lat, lon) {
        var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
        var toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
        var position = new OpenLayers.LonLat(lon, lat).transform(fromProjection, toProjection);
        return position;
    }

    // Orismos Handler
    function handler(position, message) {
        var popup = new OpenLayers.Popup.FramedCloud("Popup",
            position,
            null,
            message,
            null,
            true // <-- true if we want a close (X) button, false otherwise
        );
        mapObj.addPopup(popup);
        var div = document.getElementById('divID');
        div.innerHTML += 'Energopoitihike o Handler<br>';
    }

    // Markers
    var markers = new OpenLayers.Layer.Markers("Markers");
    mapObj.addLayer(markers);

    // Protos Marker
    var position = setPosition(lat, lon);
    var mar = new OpenLayers.Marker(position);
    markers.addMarker(mar);
    mar.events.register('mousedown', mar, function (evt) {
        handler(position, 'Spiti');
    });

    // Orismos zoom
    const zoom = 18;
    mapObj.setCenter(position, zoom);
}


function sendAjaxGET() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById('ajaxContent').innerHTML = xhr.responseText;
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    var params = "";
    for (let [name, value] of formData) {
        params += encodeURIComponent(name) + "=" + encodeURIComponent(value) + "&";
    }
    params = params.substring(0, params.length - 1);
    xhr.open('GET', 'Echo?' + params);
    xhr.send();
}

    
function RegisterPOST() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);
    const pet = formData.get('pet');
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                $('#ajaxContent').html("Successful Registration. Now please log in!<br> Your Data");
                $('#ajaxContent').append(createTableFromJSON(responseData));
            } else {
                $('#ajaxContent').html('Request failed. Returned status of ' + xhr.status + "<br>");
                if (xhr.responseText) {
                    const responseData = JSON.parse(xhr.responseText);
                    for (const x in responseData) {
                        $('#ajaxContent').append("<p style='color:red'>" + x + "=" + responseData[x] + "</p>");
                    }
                } else {
                    $('#ajaxContent').append("<p>There was an error processing your request.</p>");
                }
            }
        }
    };

    xhr.onerror = function () {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    data.lon = lon;
    data.lat = lat;
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    if(pet === "petO"){
        xhr.open('POST', 'RegistrationPetOwner');
    }
    else{
        xhr.open('POST', 'RegistrationPetKeeper');
    }
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        html += "<tr><td>" + x + "</td><td>" + data[x] + "</td></tr>";
    }
    html += "</table>";
    return html;
}

function redirect() {
    // Redirect the user to profile.html
    window.location.href = "Login.html";
}

function redirect_back() {
    // Redirect the user to profile.html
    window.location.href = "index.html";
}

function loginPOST() {
    var username = document.getElementById('username_log').value;
    var password = document.getElementById('password_log').value;

    console.log("Username:", username, "Password:", password);  

    // Check for administrator credentials
    if (username === 'admin' && password === 'admin12*') {
        window.location.href = 'admin.html'; // Redirect the administrator
        return; // Terminate the function
    }

    // AJAX request for regular users
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var response = JSON.parse(xhr.responseText);
                if (response.success) {
                    // Assuming 'userId' and 'userType' are keys in your response object
                    sessionStorage.setItem('userId', response.userId);
                    sessionStorage.setItem('userType', response.userType);

                    $("#ajaxContent").html("Successful Login");
                    redirect();
                } else {
                    $("#error").html("Wrong Credentials");
                }
            } else {
                $("#error").html("Login Failed");
            }
        }
    };

    var data = $('#loginForm').serialize();
    xhr.open('POST', 'Login');
    xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    xhr.send(data);
}



function showLogin() {
    $("#ajaxContent").load("Login.html");
}

function logout(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#choices').load("buttons.html");
            $("#ajaxContent").html("Successful Logout");
            redirect_back();
        } else if (xhr.status !== 200) {
            alert('Request failed. Returned status of ' + xhr.status);
        }
    };
    xhr.open('POST', 'Logout');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function UpdatePOST() {
    let myForm = document.getElementById('form');
    let formData = new FormData(myForm);
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                $('#ajaxContent').html("Successful Registration. Now please log in!<br> Your Data");
                $('#ajaxContent').append(createTableFromJSON(responseData));
            } else {
                $('#ajaxContent').html('Request failed. Returned status of ' + xhr.status + "<br>");
                if (xhr.responseText) {
                    const responseData = JSON.parse(xhr.responseText);
                    for (const x in responseData) {
                        $('#ajaxContent').append("<p style='color:red'>" + x + "=" + responseData[x] + "</p>");
                    }
                } else {
                    $('#ajaxContent').append("<p>There was an error processing your request.</p>");
                }
            }
        }
    };

    xhr.onerror = function () {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    const data = {};
    formData.forEach((value, key) => (data[key] = value));
    data.lon = lon;
    data.lat = lat;
    const jsonData = JSON.stringify(data);
    console.log(jsonData);
    xhr.open('POST', 'UpdatePetKeeper');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

function loadKeepers() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = xhr.responseText;
                console.log("Response data:", responseData); // Log the response data

                try {
                    const parsedResponse = JSON.parse(responseData); // Attempt to parse as JSON
                    let tableContent = createTableFromJSON(parsedResponse, 'petkeeper'); // Include 'petkeeper' type
                    document.getElementById('keepersContent').innerHTML = tableContent; // Update 'keepersContent' div
                } catch (error) {
                    console.error("JSON parsing error:", error); // Log JSON parsing error
                    document.getElementById('keepersContent').innerHTML = 'Invalid JSON response.';
                }
            } else {
                // Error handling for non-200 responses
                document.getElementById('keepersContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
            }
        }
    };
    xhr.onerror = function() {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    // Setting the query parameter for pet keepers
    var typeParam = "type=all";
    xhr.open('GET', 'AdminKeepers?' + typeParam);
    xhr.send();
}


function loadOwners() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = xhr.responseText;
                console.log("Response data:", responseData); // Log the response data

                try {
                    const parsedResponse = JSON.parse(responseData); // Attempt to parse as JSON
                    let tableContent = createTableFromJSON(parsedResponse, 'petowner'); // Include 'petowner' type
                    document.getElementById('ownersContent').innerHTML = tableContent; // Update 'ownersContent' div
                } catch (error) {
                    console.error("JSON parsing error:", error); // Log JSON parsing error
                    document.getElementById('ownersContent').innerHTML = 'Invalid JSON response.';
                }
            } else {
                // Error handling for non-200 responses
                document.getElementById('ownersContent').innerHTML = 'Request failed. Returned status of ' + xhr.status;
            }
        }
    };
    xhr.onerror = function() {
        // Handle network errors
        alert("Network Error. Please try again.");
    };

    // Setting the query parameter for pet owners
    var typeParam = "type=petowner";
    xhr.open('GET', 'AdminOwners?' + typeParam);
    xhr.send();
}


function createTableFromJSON(data, type) {
    console.log("Creating table for type:", type); // Debugging

    var tableContent = "<table border='1'><tr><th>Select</th><th>Username</th><th>First Name</th><th>Last Name</th></tr>";
    data.forEach(function(user) {
        let userId = user.id || user.keeper_id || user.owner_id; // Adjust based on your data structure
        console.log("User ID:", userId); // Debugging
        tableContent += "<tr><td><input type='checkbox' name='userSelect' value='" + userId + "'></td><td>" + user.username + "</td><td>" + user.firstname + "</td><td>" + user.lastname + "</td></tr>";
    });
    tableContent += "</table>";
    tableContent += "<button onclick='deleteSelected(\"" + type + "\")'>Delete Selected</button>";
    return tableContent;
}



function deleteSelected(type) {
    console.log("Type passed to deleteSelected:", type); // Debugging

    var selectedUsers = document.querySelectorAll('input[name="userSelect"]:checked');
    var idsToDelete = Array.from(selectedUsers).map(function(checkbox) {
        console.log("Checkbox value:", checkbox.value); // Debugging
        return parseInt(checkbox.value); // Ensure values are integers
    });

    console.log("Deleting users of type:", type, "with IDs:", idsToDelete); // Debugging

    if (idsToDelete.length === 0 || idsToDelete.includes(NaN)) {
        console.log("No valid users selected for deletion."); // Debugging
        return;
    }

    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.status === 200) {
            console.log("Selected users deleted successfully."); // Debugging
            // Refresh the list
            if (type === "petkeeper") {
                loadKeepers();
            } else {
                loadOwners();
            }
        } else {
            console.error("Error deleting users:", xhr.status); // Debugging
        }
    };
    xhr.onerror = function() {
        console.error("Network Error. Please try again."); // Debugging
    };

    xhr.open('POST', 'DeleteUsers');
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify({ type: type, ids: idsToDelete }));
}

function showPieChart() {
    fetch('PieChart')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        var dataTable = new google.visualization.DataTable();
        dataTable.addColumn('string', 'Pet Type');
        dataTable.addColumn('number', 'Count');
        dataTable.addRows([
            ['Cats', data.cats],
            ['Dogs', data.dogs]
        ]);

        var options = {'title':'Cats vs Dogs', 'width':400, 'height':300};
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(dataTable, options);
    })
    .catch(error => {
        console.error('Error:', error);
        // Handle the error, e.g., display an error message on the page.
    });
}

function showEarningsChart(){
    fetch('Earnings')
    .then(response => response.json())
    .then(data => {
        var dataTable = new google.visualization.DataTable();
        dataTable.addColumn('string', 'Entity');
        dataTable.addColumn('number', 'Earnings');
        dataTable.addRows([
            ['Pet Keepers', data.keeperEarnings],
            ['Application', data.appEarnings]
        ]);

        var options = {'title':'Earnings Distribution', 'width':400, 'height':300};
        var chart = new google.visualization.PieChart(document.getElementById('earnings_chart_div'));
        chart.draw(dataTable, options);
    })
    .catch(error => console.error('Error:', error));
}

function loadPetOwnerKeeperCount() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                const responseData = JSON.parse(xhr.responseText);
                drawPetOwnerKeeperChart(responseData);
            } else {
                console.error('Error fetching pet owner and keeper count:', xhr.statusText);
            }
        }
    };
    xhr.onerror = function() {
        console.error('Network error occurred while fetching pet owner and keeper count.');
    };
    xhr.open('GET', 'CounterPie', true);
    xhr.send();
}

function drawPetOwnerKeeperChart(data) {
    var dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Category');
    dataTable.addColumn('number', 'Count');
    dataTable.addRows([
        ['Pet Owners', data.owners],
        ['Pet Keepers', data.keepers]
    ]);

    var options = {
        title: 'Pet Owner and Keeper Count',
        width: 400,
        height: 300
    };

    var chart = new google.visualization.PieChart(document.getElementById('petOwnerKeeperChart'));
    chart.draw(dataTable, options);
}



















