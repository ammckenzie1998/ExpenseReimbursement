window.onload = function(){
    document.getElementById("logout").addEventListener('click', logout);
    document.getElementById("view-tickets").addEventListener('click', viewTickets);
    document.getElementById("submit-ticket").addEventListener('click', openTicketForm);
    document.getElementById("ticket-form-submit").addEventListener('click', submitTicket);
    document.getElementById("ticket-form-cancel").addEventListener('click', closeTicketForm);
    document.getElementById("view-all-tickets").addEventListener('click', viewAllTickets);
}

function logout(){
    window.location.href="http://localhost:8080/ExpenseReimbursementSystem/m/logout";
}

function viewTickets(){

    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 200){
            let tickets = JSON.parse(xhttp.responseText);
            drawTicketTable(tickets, false);
        }else if(xhttp.readyState == 4 && xhttp.status != 200){
            alert(xhttp.status + " - Could not view tickets.");
        }
    }

    let ticketURL = "http://localhost:8080/ExpenseReimbursementSystem/m/portal/ticket"
    xhttp.open("GET", ticketURL);
    xhttp.send();
}


function viewAllTickets(){

    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 200){
            let tickets = JSON.parse(xhttp.responseText);
            drawTicketTable(tickets, true);
        }else if(xhttp.readyState == 4 && xhttp.status != 200){
            alert(xhttp.status + " - Could not view tickets.");
        }
    }

    let ticketURL = "http://localhost:8080/ExpenseReimbursementSystem/m/portal/ticket?show=all"
    xhttp.open("GET", ticketURL);
    xhttp.send();
}



function submitTicket(){

    let xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 201){
            alert("Ticket submitted!")
            closeTicketForm();
            viewTickets();
        } else if(xhttp.readyState == 4 && xhttp.status != 201){
            alert("Ticket could not be submitted.");
        }
    }

    let ticketURL = "http://localhost:8080/ExpenseReimbursementSystem/m/portal/ticket";
    xhttp.open("POST", ticketURL);
    xhttp.setRequestHeader('Content-Type', 'application/json');

    let ticket = {
        ticketType: document.getElementById("ticket-type").value,
        amount: document.getElementById("ticket-amount").value,
        description: document.getElementById("ticket-description").value
    }
    
    xhttp.send(JSON.stringify(ticket));
}

function drawTicketTable(ticketObj, edit){
    //Obtain column headers from JSON keys
    let keys = [];
    for(key in ticketObj[0]){
        keys.push(key);
    }
    
    //Create table header
    let table = document.createElement("table");
    table.className = "table table-dark table-striped table-hover table-responsive";
    let tr = table.insertRow(-1);
    for(let i=0; i < keys.length; i++){
        let th = document.createElement("th");
        th.innerHTML = keys[i];
        tr.appendChild(th);
    }
    
    //Create table rows
    for(let i=0; i< ticketObj.length; i++){
        tr = table.insertRow(-1);
        let editable = false;
        let id;
        for(let j=0; j<keys.length; j++){
            let cell = tr.insertCell(-1);
            if(keys[j] == "timestamp"){
            
                let date = new Date(ticketObj[i][keys[j]]);
                cell.innerHTML = date.toLocaleString();
            } else{
                cell.innerHTML = ticketObj[i][keys[j]];
            }
            if(keys[j] == "status"){
                if(cell.innerText == "PENDING"){
                    editable = true;
                }
            } else if(keys[j] == "id"){
                id = cell.innerHTML;
            }
        }
        if(edit == true){
            if(editable){
                let cell = tr.insertCell(-1);
                let appr = document.createElement("INPUT");
                appr.type = "button";
                appr.className = "btn btn-success";
                appr.value = "✓";
                appr.onclick = function(){approveTicket(id)};
                cell.appendChild(appr);

                cell = tr.insertCell(-1);
                let deny = document.createElement("INPUT");
                deny.type = "button";
                deny.className = "btn btn-danger";
                deny.value = "X";
                deny.onclick = function(){denyTicket(id)};
                cell.appendChild(deny);
            }
        }
    }
    document.getElementById("ticket-table").replaceChild(table, document.getElementById("ticket-table").firstChild);
}

function approveTicket(ticketId){
    let approve = confirm("Are you sure you want to approve ticket #" + ticketId + "?");
    if(approve){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(xhttp.readyState == 4 && xhttp.status == 201){
                alert("Ticket approved!");
                viewAllTickets();
            }else if(xhttp.readyState == 4 && xhttp.status != 201){
                alert(xhttp.status + " - Could not approve ticket");
            }
        }
        let ticketURL = "http://localhost:8080/ExpenseReimbursementSystem/m/portal/ticket";
        xhttp.open("PUT", ticketURL);
        xhttp.setRequestHeader('Content-Type', 'application/json');
        let jsonTicket = {
            id: ticketId,
            status: "APPROVED"
        }
        xhttp.send(JSON.stringify(jsonTicket));
    }
}

function denyTicket(ticketId){
    let deny = confirm("Are you sure you want to deny ticket #" + ticketId + "?");
    if(deny){
        let xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function(){
            if(xhttp.readyState == 4 && xhttp.status == 201){
                alert("Ticket denied.");
                viewAllTickets();
            }else if(xhttp.readyState == 4 && xhttp.status != 201){
                alert(xhttp.status + " - Could not deny ticket");
            }
        }
        let ticketURL = "http://localhost:8080/ExpenseReimbursementSystem/m/portal/ticket";
        xhttp.open("PUT", ticketURL);
        xhttp.setRequestHeader('Content-Type', 'application/json');
        let jsonTicket = {
            id: ticketId,
            status: "DENIED"
        }
        xhttp.send(JSON.stringify(jsonTicket));
    }
}

function openTicketForm(){
    document.getElementById("ticket-form-block").style.display = "block";
}

function closeTicketForm(){
    document.getElementById("ticket-form-block").style.display = "none";
}