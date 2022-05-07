import { getSessionObject } from "../../../utils/session";
import { VerifyUser } from "../../../utils/session";

const userHandler = `
<div id="triangle"> </div>
<section id="home-page">
   <div id="container-user-info">
   </div>
   <div id="home-page-navigation">
      <h2 id="home-page-title"> Liste des personnes en attentes/refusées</h2>
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
  // default request of the page
  gettAllByState(accesToken, "waiting");
  const stateUserList = document.getElementById("state-user-handler");
  document.querySelector(".slider.round").addEventListener("click", () => {
    if (stateUserList.innerHTML == "ATTENTE") {
      stateUserList.innerHTML = "REFUSÉS";
      gettAllByState(accesToken, "denied");
    } else {
      // click out the div and delete it TODO !
      stateUserList.innerHTML = "ATTENTE";
      gettAllByState(accesToken, "waiting");
    }
  });
};

let currentUser;
let currentVersion;

async function gettAllByState(accesToken, state) {
  try {
    const option = {
      headers: {
        token: accesToken,
      },
    };
    const response = await fetch(
      "/api/admins/members?state=" + state,
      option
    ); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    const items = await response.json();
    var userHandlerList = document.getElementById("user-handler-list");
    userHandlerList.innerHTML = "";
    items.forEach((e) => {
      console.log(e);
      const divUserHandler = document.createElement("div");
      const pFirstName = document.createElement("span");
      const pLastName = document.createElement("span");
      const divIsAdminBox = document.createElement("div");
      const spanAdminBox = document.createElement("span");
      const br = document.createElement("br");
      const inputCheckBox = document.createElement("input");
      const divValid = document.createElement("div");
      const validBtn = document.createElement("p");
      let classListCol = state != "denied" ? "col-2" : "col-3";

      pFirstName.classList = classListCol;
      pLastName.classList = classListCol;
      divIsAdminBox.classList = classListCol;
      divValid.classList = classListCol;

      divUserHandler.classList = "user-to-handle";
      divUserHandler.id = e.id;
      divIsAdminBox.id = "is-admin-box";
      validBtn.classList = "user-handler-accepted-btn";
      pFirstName.innerHTML = e.firstName;
      pLastName.innerHTML = e.lastName;
      divUserHandler.appendChild(pFirstName);
      divUserHandler.appendChild(pLastName);
      divUserHandler.appendChild(divIsAdminBox);
      spanAdminBox.innerHTML = "admin";
      divIsAdminBox.appendChild(spanAdminBox);
      divIsAdminBox.appendChild(br);
      inputCheckBox.id = e.id;
      inputCheckBox.type = "checkbox";
      divIsAdminBox.appendChild(inputCheckBox);
      validBtn.innerHTML = "accepter";
      divValid.appendChild(validBtn);
      divUserHandler.appendChild(divValid);
      if (state != "denied") {
        const divDenied = document.createElement("div");
        const deniedBtn = document.createElement("p");
        divDenied.classList = classListCol;
        deniedBtn.classList = "user-handler-denied-btn";
        deniedBtn.innerHTML = "refuser";
        divUserHandler.appendChild(divDenied);
        divDenied.appendChild(deniedBtn);
        deniedBtn.addEventListener("click", () => {
          currentUser = e.id;
          currentVersion = e.version;
          document.getElementById("add-reason-refusal").style.display = "flex";
          document
            .getElementById("add-reason-button")
            .addEventListener("click", async function (e) {
              e.preventDefault();
              let reason = document.getElementById("reason").value;
              await addOrRefuse(
                currentUser,
                "denied",
                reason,
                accesToken,
                inputCheckBox,
                currentVersion
              );
              document.getElementById(currentUser).remove();
            });
        });
      }
      validBtn.addEventListener("click", () => {
        addOrRefuse(e.id, "valid", "", accesToken, inputCheckBox, e.version);
        document.getElementById(e.id).remove();
      });
      divUserHandler.addEventListener("click", (target) => {
        if (
          target.target.innerHTML != "refuser" &&
          target.target.innerHTML != "accepter" &&
          target.target.innerHTML != ""
        ) {
          getUserInformation(e.id, accesToken);
        }
      });
      userHandlerList.appendChild(divUserHandler);
    });
  } catch (error) {}
}

async function getUserInformation(id, accesToken) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        token: accesToken,
      },
    };
    const response = await fetch("/api/members/" + id, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      response.text().then((result) => {
        document.getElementById("error").innerText = result;
      });
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const data = await response.json(); // json() returns a promise => we wait for the data
    const formDiv = document.getElementById("container-user-info");
    formDiv.innerHTML = `
  <div id="main-container" class="py-5">
    <div class="row d-flex justify-content-center align-items-center ">
      <div id="user-handler-info" class="col col-lg-6 mb-4 mb-lg-0">
        <div id="card-mb-3" class="card mb-3" style="border-radius: .5rem;">
          <div class="row g-0">
            <div id="name-container" class="col-md-4 gradient-custom text-center text-black" style="border-top-left-radius: .5rem; border-bottom-left-radius: .5rem;">
              <img
                src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp"
                alt="Avatar"
                class="img-fluid my-5"
                style="width: 80px;"
              />
              <h5 id="name">${data.lastName + " " + data.firstName}</h5>
              <p id="user"> ${data.userName} </p>
            </div>
            <div class="col-md-8">
              <div class="card-body p-4">
                <h6>Information</h6>
                <hr class="mt-0 mb-4">
                <div class="row pt-1">
                  <div id="role-container" class="col-6 mb-3">
                    <h6>Role</h6>
                    <p id="role" class="text-muted"> ${data.role} </p>
                  </div>
                  <div id="phone-container" class="col-6 mb-3">
                    <h6>Phone</h6>
                    <p id="phone" class="text-muted">${
                      data.phoneNumber == null ? "/" : data.phoneNumber
                    }</p>
                  </div>
                </div>
                <hr class="mt-0 mb-4">
                <div class="row pt-1">
                  <div class="col-6 mb-3">  
                  <h6>Adresse</h6>
                    <div class="row">
                        <div class="col-3" style="padding-right : 0px">
                            Rue
                        </div>
                        <div id="street-container" class="col-9">    
                            <p id="street" class="text-muted"> ${
                              data.street
                            } </p>

                        </div>
                        <div class="col-3" style="padding-right : 0px">
                            N°
                        </div>
                        <div id="numero-container" class="col-9">    
                            <p id="numero" class="text-muted"> ${
                              data.buildingNumber
                            } </p>
                        </div>
                        <div class="col-3" style="padding-right : 0px">
                            Ville
                        </div>
                        <div id="city-container" class="col-9">    
                            <p id="city" class="text-muted"> ${data.city}</p>
                        </div>

                    </div>
                </div>
                  <div class="col-6 mb-3">
                  <div class="row pt-4">
                  <div class="col-7" style="padding-right : 0px">
                  Code postal :
                    </div>
                    <div id="postcode-container" class="col-5">    
                    <p id="postcode" class="text-muted"> ${data.postCode}</p>
                    </div>
                    <div class="col-4" style="padding-right : 0px">
                        Boite :
                    </div>
                    <div id="unit-container" class="col-8">
                    <p id="unit" class="text-muted"> ${
                      data.unitNumber == null ? "/" : data.unitNumber
                    }</p>
                    </div>
                    </div>
                    </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
`;
    document.querySelector("#page").appendChild(formDiv);
    document.addEventListener("click", (e) => {
      // click out the div and delete it TODO !
      let myPopUpDiv = document.getElementById("card-mb-3");
      if (myPopUpDiv) {
        if (!myPopUpDiv.contains(e.target)) {
          myPopUpDiv.remove();
        }
      }
    });
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

async function addOrRefuse(id, state, rsnRefusal, accesToken, admin, version) {
  try {
    let body1 =
      rsnRefusal != ""
        ? JSON.stringify({
            version: version,
            change_id: id,
            state: state,
            admin: admin.checked,
            refusalReason: rsnRefusal,
          })
        : JSON.stringify({
            version: version,
            change_id: id,
            state: state,
            admin: admin.checked,
          });
    const options = {
      method: "PUT", // *GET, POST, PUT, DELETE, etc.
      body: body1,
      headers: {
        token: accesToken,
        "Content-Type": "application/json",
      },
    };
    const response = await fetch("/api/admins/update/state/"+id, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      response.text().then((result) => {
        document.getElementById("error").innerText = result;
      });
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    await response.json(); // json() returns a promise => we wait for the data
    document.getElementById("add-reason-refusal").style.display = "none";
    document.getElementById("reason").value = "";
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

export default UserHandler;
