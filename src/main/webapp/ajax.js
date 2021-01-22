let username = "";
let userpassword = "";

window.onload = function(){
    document.getElementById("login-submit").addEventListener('click', login);
    console.log("Attempting to log in");
}

function login(){
    
    username = document.getElementById("username").value;
    userpassword = document.getElementById("userpassword").value;
    
    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function(){

        console.log("New ready state: " + xhttp.readyState);

        if(xhttp.readyState == 4 && xhttp.status == 401){
            console.log("Login was not accepted by the server.");
            username = "";
            userpassword = "";
        } else if(xhttp.readyState == 4 && xhttp.status == 200){
            console.log("Login accepted!");
        }

    }

    let loginURL = "http://localhost:8080/ExpenseReimbursementSystem/m/login";

    xhttp.open("POST", loginURL);
    xhttp.setRequestHeader("Content-Type", "application/json");
    let loginData = JSON.stringify({"username": username, "password": userpassword});
    xhttp.send(loginData);

}