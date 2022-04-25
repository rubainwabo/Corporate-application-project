import {getSessionObject} from "../../../utils/session";
import {Redirect} from "../../Router/Router";
import { VerifyUser } from "../../../utils/session";

const userHandler = `
<section id="home-page">
<div id="home-page-navigation">
    <h2 id="home-page-title"> Listes des personnes en attentes/refusées</h2>
</div>
<div id="switch-list-users">
  <div id="in-toggle-list-user">
  <div id="text-state-list-user">
  <p id="state-user-handler">ATTENTE</p>
  </div>
     <label class="switch">
     <input type="checkbox">
     <span class="slider round" ></span>
     </label>
  </div>
</div>
<section id="user-handler-page">

<form name="formA" style="margin-left:auto;">
   </form>
   <div id="user-handler-list">
   </div>

   <div id="add-reason-refusal">
      <form id="add-iterest-form">
      <span id="error"></span>
        <div>
        <textarea id="reason" placeholder="raison du refus..."></textarea>
        </div>
        <div>
          <input type="submit" value="envoyer" id="add-reason-button"> 
        </div>
        
      </form>
    </div>
    </section>
</section>
`;

const UserHandler = () => {

  let accesToken = getSessionObject("accessToken");
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = userHandler;
  document.getElementById("home-page").style.height="1vh";
  // default request of the page
  gettAllByState(accesToken, "waiting");
  const stateUserList = document.getElementById("state-user-handler")
  document.querySelector(".slider.round").addEventListener("click", () => {
    if (stateUserList.innerHTML == "ATTENTE") {
      stateUserList.innerHTML = "REFUSÉ"
      gettAllByState(accesToken, "denied");
    } else {
        // click out the div and delete it TODO !
        let myPopUpDiv = document.getElementById("add-reason-refusal");
        if (myPopUpDiv){
            myPopUpDiv.style.display="none"
        }
      stateUserList.innerHTML = "ATTENTE"
      gettAllByState(accesToken, "waiting");
    }
  })
};

let currentUser;

async function gettAllByState(accesToken, state) {
  await VerifyUser();
  try {
    const option = {
      headers: {
        "token": accesToken
      }
    }
    const response = await fetch("/api/admins/listByState?state=" + state, option); // fetch return a promise => we wait for the response
    if (!response.ok) {
    }
    const items = await response.json();
    var userHandlerList = document.getElementById("user-handler-list");
    userHandlerList.innerHTML = "";
    items.forEach((e) => {
      const divUserHandler = document.createElement("div");
      const pFirstName = document.createElement("p");
      const pLastName = document.createElement("p");
      const divIsAdminBox = document.createElement("div");
      const spanAdminBox = document.createElement("span");
      const br = document.createElement("br");
      const inputCheckBox = document.createElement("input");
      const divValid = document.createElement("div");
      const divDenied = document.createElement("div");
      const validBtn = document.createElement("button");
      const deniedBtn = document.createElement("button");

      divUserHandler.classList = "user-to-handle";
      divUserHandler.id = e.id;
      // checkbox admin + last name and first name
      divIsAdminBox.classList = "is-admin-box";
      pFirstName.innerHTML = e.firstName
      pLastName.innerHTML = e.lastName
      divUserHandler.appendChild(pFirstName);
      divUserHandler.appendChild(pLastName);
      divUserHandler.appendChild(divIsAdminBox)
      spanAdminBox.innerHTML = "admin"
      divIsAdminBox.appendChild(spanAdminBox);
      divIsAdminBox.appendChild(br);
      inputCheckBox.id = e.id;
      inputCheckBox.type = "checkbox"
      divIsAdminBox.appendChild(inputCheckBox);

      // append div with btnValid
      deniedBtn.innerHTML = "refusé"
      validBtn.innerHTML = "accepté"

      divUserHandler.appendChild(divValid)
      divValid.appendChild(validBtn)
      // append div with btnDenied
      divUserHandler.appendChild(divDenied)
      divDenied.appendChild(deniedBtn)
      deniedBtn.addEventListener("click", () => {
        currentUser = e.id;
        document.getElementById("add-reason-refusal").style.display = "flex";
        document.getElementById("add-reason-button").addEventListener("click",
            function (e) {
              e.preventDefault();
              let reason = document.getElementById("reason").value;
              addOrRefuse(currentUser, "denied", reason, accesToken,
                  inputCheckBox)
            })
           
      });
      validBtn.addEventListener("click", () => {
        addOrRefuse(e.id, "valid", "", accesToken, inputCheckBox)
      });
      divUserHandler.addEventListener("click", () => {
        getUserInformation(e.id, accesToken);
      })
      userHandlerList.appendChild(divUserHandler)
    })
  } catch (error) {

  }
}

async function getUserInformation(id, accesToken) {
  await VerifyUser();
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "token": accesToken
      },
    };
    const response = await fetch("/api/members/details?id=" + id, options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      response.text().then((result) => {
        document.getElementById("error").innerText = result;
      })
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const user = await response.json(); // json() returns a promise => we wait for the data
    console.log(user);
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

async function addOrRefuse(id, state, rsnRefusal, accesToken, admin) {
  await VerifyUser();
  try {
    let body1 = rsnRefusal != "" ? JSON.stringify({
      "change_id": id,
      "state": state,
      "admin": admin.checked,
      "refusalReason": rsnRefusal
    }) : JSON.stringify({
      "change_id": id,
      "state": state,
      "admin": admin.checked
    })
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: body1,
      headers: {
        "token": accesToken,
        "Content-Type": "application/json"
      },
    };
    const response = await fetch("/api/admins/changeState", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      response.text().then((result) => {
        document.getElementById("error").innerText = result;
      })
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    await response.json(); // json() returns a promise => we wait for the data
    document.getElementById("add-reason-refusal").style.display="none";
    document.getElementById("reason").value="";
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

export default UserHandler;