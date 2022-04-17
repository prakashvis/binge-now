function setFormMessage(formElement, type, message) {
    const messageElement = formElement.querySelector(".form__message");

    messageElement.textContent = message;
    messageElement.classList.remove("form__message--success", "form__message--error");
    messageElement.classList.add(`form__message--${type}`);
}

function setInputError(inputElement, message) {
    inputElement.classList.add("form__input--error");
    inputElement.parentElement.querySelector(".form__input-error-message").textContent = message;
}

function clearInputError(inputElement) {
    inputElement.classList.remove("form__input--error");
    inputElement.parentElement.querySelector(".form__input-error-message").textContent = "";
}

function fetchShow () {
    let params = {};
    params.username = localStorage.getItem('bingenow.username');
    params.operation = 'fetch-show';
    let dataObj = JSON.stringify(params);
    $.ajax({
        type: 'POST',
        url: 'show',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('show')) {
                let show = response.show;
                console.log('show-name : ' + show.name);
                document.getElementById('show-name').innerHTML = show.name;
                document.getElementById('show-details').innerHTML = show.details;
                document.getElementById('show-cast').innerHTML = show.castdetails;
                document.getElementById("show-url").href = show.showurl;
                document.getElementById("show-summary").innerHTML = show.description;
                document.getElementById('poster').src = show.image;

                document.getElementById('show').style.display = 'block';
                document.getElementById('show-error').style.display = 'none';
            }
            else {
                document.getElementById('show').style.display = 'none';
                document.getElementById('show-error').style.display = 'block';
            }
        },
        error: function(error) {
            console.log(error);
            document.getElementById('show').style.display = 'none';
            document.getElementById('show-error').style.display = 'block';
        }
    });
}

function addToWatchList () {
    let params = {};
    params.show = document.getElementById('show-name').innerText;
    params.username = localStorage.getItem('bingenow.username');
    params.operation = 'add-to-watchlist';
    let dataObj = JSON.stringify(params);
    $.ajax({
        type: 'POST',
        url: 'show',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('success')) {
                console.log('added to watchlist = ' + show);
            }
            else {
                console.log('Failed to add to watchlist');
            }
        },
        error: function(error) {
            console.log(error);
            console.log('Failed to add to watchlist');
        }
    });
}

function getWatchlist () {
    let params = {};
    let result = false;
    params.username = localStorage.getItem('bingenow.username');
    params.operation = 'get-watchlist';
    let dataObj = JSON.stringify(params);
    $.ajax({
        type: 'POST',
        url: 'show',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('watchlist')) {
                let watchlist = response.watchlist;
                let innerHtml = '';
                Object.values(watchlist).forEach(show => {
                    innerHtml = innerHtml + '<li>' + show + '</li>';
                    console.log(innerHtml);
                });
                document.getElementById('watchlist-list').innerHTML = innerHtml;
                result = true;
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
    if(result) {
        document.getElementById('watchlist').style.display = 'block';
        // document.getElementById('watchlist-error').style.display = 'none';
    }
    else {
        document.getElementById('watchlist').style.display = 'block';
        // document.getElementById('watchlist-error').style.display = 'block';
    }
}

function login(loginForm) {
    let params = {};
    let username =  document.getElementById('loginusername').value;
    let password = document.getElementById('loginpassword').value;
    params.username = username;
    params.password = password;
    params.operation = 'login';
    let dataObj = JSON.stringify(params);
    console.log('Ajax request  - ' + dataObj);
    $.ajax({
        type: 'POST',
        url: 'user',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('success')) {
                localStorage.setItem('bingenow.username', username);
                localStorage.setItem('bingenow.cookie', response.cookie);
                setFormMessage(loginForm, "success", response.success);
                window.location = "./";
            }
            else{
                setFormMessage(loginForm, "error", "Invalid username/password combination");
            }
        },
        error: function(error) {
            console.log(error);
            setFormMessage(loginForm, "error", "Invalid username/password combination");
        }
    });
}

function createAccount(createAccountForm) {
    let params = {};
    let username =  document.getElementById('signupusername').value;
    let password = document.getElementById('signuppassword').value;
    params.username = username;
    params.password = password;
    params.operation = 'signup';
    let dataObj = JSON.stringify(params);
    console.log('Ajax request  - ' + dataObj);
    $.ajax({
        type: 'POST',
        url: 'user',
        data: dataObj,
        // contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('success')) {
                localStorage.setItem('bingenow.username', username);
                localStorage.setItem('bingenow.cookie', response.cookie);
                setFormMessage(createAccountForm, "success", response.success);
                window.location = "./";
            }
            else{
                setFormMessage(createAccountForm, "error", "Username not available");
            }
        },
        error: function(error) {
            console.log(error);
            setFormMessage(createAccountForm, "error", "Something went wrong");
        }
    });
}

function logout() {
    let params = {};
    params.username = localStorage.getItem('bingenow.username');
    params.cookie = localStorage.getItem('bingenow.cookie');
    params.operation = 'logout';
    let dataObj = JSON.stringify(params);
    $.ajax({
        type: 'POST',
        url: 'user',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('success')) {
                console.log('logout successful');
            }
            else {
                console.log('Failed to remove session from server');
            }
        },
        error: function(error) {
            console.log(error);
            console.log('Failure!');
        }
    });

    localStorage.removeItem('bingenow.username');
    localStorage.removeItem('bingenow.cookie');
    window.location = 'login.html'
}

function reset() {

    let params = {};
    params.username = localStorage.getItem('bingenow.username');
    params.operation = 'reset';
    let dataObj = JSON.stringify(params);
    $.ajax({
        type: 'POST',
        url: 'show',
        data: dataObj,
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            console.log(response);
            if(response.hasOwnProperty('success')) {
                console.log('recommendation reset successful');
            }
            else {
                console.log('Failed to reset');
            }
        },
        error: function(error) {
            console.log(error);
            console.log('Failed to reset');
        }
    });

}