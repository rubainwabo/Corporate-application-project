import { Redirect } from "../Router/Router";
import { getSessionObject, VerifyUser } from "../../utils/session";

import itemImg from "../../img/wheelbarrows-4566619_640.jpg";

const monProfil = `
<div id="triangle"> </div>
`;

const myProfilePassword = `
<div id="triangle"></div>
<section class="vh-100" style="background-color: white;">
   <div id="main-container">
   <div class="row d-flex justify-content-center align-items-center ">
   <div class="col col-lg-6 mb-4 mb-lg-0">
      <div class="card mb-3" style="border-radius: .5rem;">
         <div class="row g-0 m-10" style="margin : 2rem">
            <div class="col-6" style="text-align : center">
               <h3 class="m-4">
               Enter your new password</h1>
               <input required="required" class="w-auto m-4 mt-0" id="pwd-1" type='password'> </input>
            </div>
            <div class="col-6" style="text-align : center">
               <h3 class="m-4 font-medium">
               Confirm your new password </h1>
               <input required="required" type='password' class="w-auto m-4 mt-0" id="pwd-2"> </input>
               <div>
               </div>
            </div>
         </div>
         <div id="btndiv" class="row m-4 mt-0" style ="margin : 2rem; text-align : center;">
            <button id="confirm" style="border-radius : 5px; padding-right: 20px; padding-left: 20px; background : #FFF59B; border : white; color : black; font-size : 1.3rem; font-weight : 400"> Confirm </button>
            <span id="error-msg"> </span> 
         </div>
      </div>
   </div>
</section>
`;

const MonProfil = async () => {
  const pageDiv = document.querySelector("#page");

  try {
    // hide data to inform if the pizza menu is already printed
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        token: getSessionObject("accessToken"),
      },
    };

    const response = await fetch("/api/members/"+getSessionObject("userId"), options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    const data = await response.json();

    pageDiv.innerHTML = monProfil;

    const formDiv = document.createElement("div");
    formDiv.innerHTML = `
    <div id="triangle"> </div>
    <div id="main-container" >
       <div class="row d-flex justify-content-center align-items-center ">
          <div class="col col-lg-6 mb-4 mb-lg-0">
             <div class="card mb-3" style="border-radius: .5rem;">
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
                      <i id="edit-button" class="far fa-edit mb-5"></i>
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
                                  }
                               </p>
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
                                        } 
                                     </p>
                                  </div>
                                  <div class="col-3" style="padding-right : 0px">
                                     N°
                                  </div>
                                  <div id="numero-container" class="col-9">
                                     <p id="numero" class="text-muted"> ${
                                        data.buildingNumber
                                        } 
                                     </p>
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
                                        }
                                     </p>
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
    pageDiv.appendChild(formDiv);

    const editButton = document.getElementById("edit-button");
    let edit = false;
    editButton.onclick = () => {
      if (!edit) {
        edit = true;

        let name = document.getElementById("name");
        let username = document.getElementById("user");
        let phone = document.getElementById("phone");
        let street = document.getElementById("street");
        let postcode = document.getElementById("postcode");
        let city = document.getElementById("city");
        let unit = document.getElementById("unit");
        let buildNumber = document.getElementById("numero");

        const nameInput = document.createElement("input");
        const usernameInput = document.createElement("input");
        const phoneInput = document.createElement("input");
        const streetInput = document.createElement("input");
        const postcodeInput = document.createElement("input");
        const cityInput = document.createElement("input");
        const unitInput = document.createElement("input");
        const buildingNumberInput = document.createElement("input");

        nameInput.value = data.lastName + " " + data.firstName;
        usernameInput.value = data.userName;
        phoneInput.value = data.phoneNumber;
        streetInput.value = data.street;
        postcodeInput.value = data.postCode;
        cityInput.value = data.city;
        unitInput.value = data.unitNumber;
        buildingNumberInput.value = data.buildingNumber;

        let widthHeight = "width : 100%; height : 100%;";
        let wauto = "width : auto";

        nameInput.style = wauto;
        usernameInput.style = "width : auto";
        streetInput.style = widthHeight;
        postcodeInput.style = widthHeight;
        cityInput.style = widthHeight;
        unitInput.style = widthHeight;
        buildingNumberInput.style = widthHeight;

        nameInput.className = "text-muted effect-1 rounded";
        usernameInput.className = "text-muted effect-1 rounded";
        phoneInput.className = "text-muted effect-1";
        streetInput.className = "text-muted effect-1";
        postcodeInput.className = "text-muted effect-1";
        cityInput.className = "text-muted effect-1";
        unitInput.className = "text-muted effect-1";
        buildingNumberInput.className = " text-muted effect-1";

        const nameContainer = document.getElementById("name-container");
        const phoneContainer = document.getElementById("phone-container");
        const streetContainer = document.getElementById("street-container");
        const postcodeContainer = document.getElementById("postcode-container");
        const cityContainer = document.getElementById("city-container");
        const unitContainer = document.getElementById("unit-container");
        const buildContainer = document.getElementById("numero-container");

        nameContainer.removeChild(username);
        nameContainer.removeChild(name);
        nameContainer.removeChild(document.getElementById("edit-button"));
        if (phoneInput.value.length > 0 || phone.phoneInput == "/") {
          phoneContainer.removeChild(phone);
        }
        streetContainer.removeChild(street);
        postcodeContainer.removeChild(postcode);
        cityContainer.removeChild(city);
        unitContainer.removeChild(unit);
        buildContainer.removeChild(buildNumber);

        phoneContainer.style = "position : relative";
        nameContainer.style = "position : relative";
        streetContainer.style = "position : relative";
        postcodeContainer.style = "position : relative";
        cityContainer.style = "position : relative";
        buildContainer.style = "position : relative";
        unitContainer.style = "position : relative";

        nameContainer.appendChild(nameInput);
        nameContainer.appendChild(usernameInput);

        if (phoneInput.value.length > 0 || phone.phoneInput == "/") {
          phoneContainer.appendChild(phoneInput);
        }
        streetContainer.appendChild(streetInput);
        postcodeContainer.appendChild(postcodeInput);
        cityContainer.appendChild(cityInput);
        unitContainer.appendChild(unitInput);
        buildContainer.appendChild(buildingNumberInput);

        let main = document.getElementById("main-container");
        const saveDiv = document.createElement("div");
        saveDiv.className =
          "row d-flex justify-content-center align-items-center position-relative";
        let saveButton = document.createElement("button");
        let changePassword = document.createElement("button");

        changePassword.innerHTML = "Change password";
        changePassword.className = "w-auto btn btn-primary";
        changePassword.style =
          "margin-left: 10px; background : #FFF59B; border : white; color : black; font-size : 1rem; font-weight : 500";

        changePassword.onclick = () => {
          pageDiv.innerHTML = myProfilePassword;
          const confirmButton = document.getElementById("confirm");

          confirmButton.onclick = async () => {
            const pwd = document.getElementById("pwd-1");
            const msg = document.getElementById("error-msg");
            let options = {
              method: "PUT", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                newPassword: pwd.value,
              }),
              // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
                token: getSessionObject("accessToken"),
              },
            };

            if (pwd.value.length > 5) {
              const res = await fetch("/api/members/update/password/"+getSessionObject("userId"), options); // fetch return a promise => we wait for the response
              if (response.status == 307) {
                await VerifyUser();
                document.location.reload();
              }
              if (res.ok) {
                setTimeout(() => {
                  Redirect("/monProfil");
                }, 2000);
                msg.innerHTML = "Votre mot de passe a bien été changé";
              }
            } else {
              msg.innerHTML =
                "vous devez remplir les champs pour le mot de passe";
            }
          };
        };

        saveButton.innerHTML = "Save changes";
        saveButton.className = "w-auto btn btn-primary";
        saveButton.style =
          "background : #FFF59B; border : white; color : black; font-size : 1rem; font-weight : 500;";

        saveDiv.appendChild(saveButton);
        saveDiv.appendChild(changePassword);
        main.appendChild(saveDiv);

        saveButton.onclick = async () => {
          let char = nameInput.value.split(" ");
          let lName = char[0];
          let fName = char[1];

          let options = {
            method: "PUT", // *GET, POST, PUT, DELETE, etc.
            body: JSON.stringify({
              username: usernameInput.value,
              firstName: fName,
              lastName: lName,
              street: streetInput.value,
              unitNumber: buildingNumberInput.value,
              postcode: postcodeInput.value,
              box: unitInput.value,
              city: cityInput.value,
              phone: phoneInput.value,
            }),
            // body data type must match "Content-Type" header
            headers: {
              "Content-Type": "application/json",
              token: getSessionObject("accessToken"),
            },
          };

          const res = await fetch("/api/members/update/"+getSessionObject("userId"), options); // fetch return a promise => we wait for the response
          if (response.status == 307) {
            await VerifyUser();
            document.location.reload();
          }
          Redirect("/monProfil");

          if (!res.ok) {
            res.text().then((result) => {
              Redirect("/logout");
              console.log("error", result);
            });
            throw new Error(
              "fetch error : " + response.status + " : " + response.statusText
            );
          }
        };
      }
    };
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
  } catch (error) {
    console.error("MyProfile::error: ", error);
  }
};
export default MonProfil;
