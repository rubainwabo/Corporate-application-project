import { Redirect } from "../Router/Router";

import itemImg from "../../img/wheelbarrows-4566619_640.jpg";
import {
  getSessionObject,
  VerifyUser,
} from "../../utils/session";

/**
 * Render the pick recipient page
 * 
 */


const pickRecipient = `
<div id="triangle"> </div>
<section id="pick-recipient-page">
   <div id="recipient-page-container">
      <div id="pick-recipient-page-left">
              <div class="recipent-page-desc">
              <h6 id="recipent-page-item-type-title"></h6>
               <p id="recipient-page-item-type"></p>
               </div>
               <div class="recipent-page-desc">
               <h6 id="recipent-page-item-description-title"></h6>
               <p id="recipient-page-item-description"></p>
              </div>
            <img id="recipient-page-item-img">
         </div>
      <div id="pick-recipient-page-right">
         <div id="recipient-page-item-users">
         <h6 id="recipient-page-item-users-no-interest"> </h6>
         </div>
      </div>
   </div>
</section>
`;
let version;
const PickRecipient = async () => {
  let id = getId();
  let token = getSessionObject("accessToken");
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = pickRecipient;
  getPicture(id,document.getElementById("recipient-page-item-img"));

  try {
    var options = {
      method: "GET",
      headers: { token: token },
    };

    const response = await fetch("/api/members/interest/" + id, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const users = await response.json();
    if (users.length == 0) {
      let myItems = document.getElementById("recipient-page-item-users-no-interest");
      myItems.innerText =
      "Aucun interet pour l'instant";
    }
    // list of user
    users.forEach((user) => {
      let userBox = document.createElement("div");
      let lastName = document.createElement("span");
      let firstName = document.createElement("span");
      let adress = document.createElement("span");
      const divBtn = document.createElement("div");
      let addButton = document.createElement("p");

      lastName.classList="col-2"
      firstName.classList="col-2"
      adress.classList="col-3"
      divBtn.classList="col-2"
      addButton.classList="user-handler-denied-btn"
      divBtn.classList="col-2"

      lastName.innerText = user.lastName;
      firstName.innerText = user.firstName;
      adress.innerText =
        user.street + " " + user.buildingNumber + " (" + user.city + ")";

      addButton.innerText = " Offrir";
      addButton.addEventListener("click", async function (e) {
        await addRecipient(id, user.id);
        Redirect("/myitems");
      });
      userBox.classList.add("user-list-pick-recipient");
      userBox.style.cursor="auto"
      
      let userInBox = document.getElementById("recipient-page-item-users");
      if (users.length == 1){
        userInBox.style.height="12vh";
      }else if (users.length == 2){
        userInBox.style.height="24vh";
      }else if (users.length==3 ){
        userInBox.style.height="274px"
      }else {
        userInBox.style.height="363px"
      }

      userBox.appendChild(lastName);
      userBox.appendChild(firstName);
      userBox.appendChild(adress);
      divBtn.appendChild(addButton)
      userBox.appendChild(divBtn);
      document.getElementById("recipient-page-item-users").appendChild(userBox);
    });
  } catch (error) {
    console.log(error);
  }

  try {
    const options = {
      headers: {
        token: getSessionObject("accessToken"),
      },
    };

    const response = await fetch("/api/items/" + id, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    // get the item
    const item = await response.json();
    version = item.version;
    document.getElementById("recipient-page-item-type").innerText =
     item.itemtype;
    document.getElementById("recipient-page-item-description").innerText =
 item.description;

      document.getElementById("recipent-page-item-type-title").innerHTML="Type de l'objet : ";
      document.getElementById("recipent-page-item-description-title").innerHTML="Description de l'objet : ";

  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
};

async function addRecipient(idItem, idRecipient) {
  try {
    var options = {
      method: "PUT",
      body: JSON.stringify({
        idItem : idItem ,
        idRecipient: idRecipient,
        version:version
      }),
      headers: { 
      token: getSessionObject("accessToken"), 
      "Content-Type": "application/json" },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch(
      "/api/items/addRecipient",
      options
    ); // fetch return a promise => we wait for the response   changeCondition/{id}/{condition}
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (response.ok) {
      return true;
    }
  } catch (error) {
    console.log(error);
    return false;
  }
}

function getId() {
  let urlString = window.location.href;
  let paramString = urlString.split("?")[1];
  if (paramString) {
    let params_arr = paramString.split("&");
    let pair = params_arr[0].split("=");
    if (pair[1]) {
      return parseInt(pair[1]);
    } else {
      return -1;
    }
  } else {
    return -1;
  }
}
async function getPicture(itemId, imgDiv) {
  try {
    const response = await fetch("/api/items/picture/" + itemId); // fetch return a promise => we wait for the response

    if (!response.ok) {
      imgDiv.src = itemImg;
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    if (response.ok) {
      const imageBlob = await response.blob();
      const imageObjectURL = URL.createObjectURL(imageBlob);
      
      imgDiv.src = imageObjectURL;
    }
  } catch (error) {
    console.log(error);
  }
}

export default PickRecipient;
