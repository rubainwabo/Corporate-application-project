import {Redirect} from "../Router/Router";

import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";


/**
 * Render the pick recipient page
 */
const pickRecipient = `
<section id="pick-recipient-page">
    <div id="recipient-page-item-infos">
        <div>
         <p id="recipient-page-item-type"></p>
         <p id="recipient-page-item-description"></p>
         </div>
        <div> <img src=${itemImg} ></div>
    </div>
    <div id="recipient-page-item-users">
        <div class="user-interest">
            <p>Dotche</p>
            <p>Daniel</p>
            <p>Olieslagerstraat 92</p>
            <button> Offrir </button>
        </div>
    </div>
</section>

`;

const PickRecipient = async () => {
    let id = getId();
    let token = getSessionObject("accessToken");
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = pickRecipient;
  

  try {
    var options = { 
    method: 'GET',
    headers: {"token" : token},
    };

    const response = await fetch("/api/members/interest/"+id,options); // fetch return a promise => we wait for the response
    console.log("res", response.body);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
    }
    
    const users = await response.json();

    if(users.length==0){
        console.log("aucun interet pour l'instant")
    }
    users .forEach(user => {
        let userBox = document.createElement("div");
        let lastName = document.createElement("p");
        let firstName= document.createElement("p");
        let adress = document.createElement("p");
        let addButton = document.createElement("button");
        
        lastName.innerText=user.lastName;
        firstName.innerText=user.firstName;
        adress.innerText=user.street+" "+user.buildingNumber+" ("+user.city+")";

        addButton.innerText=" Offrir";
        
        userBox.classList.add("user-interest");

        userBox.appendChild(lastName);
        userBox.appendChild(firstName);
        userBox.appendChild(adress);
        userBox.appendChild(addButton);

        document.getElementById("recipient-page-item-users").appendChild(userBox);
      

    });
    console.log(users);
    

  } catch (error) {
    console.log(error);
  }

  try {
    // hide data to inform if the pizza menu is already printed
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        "token":getSessionObject("accessToken"),
      },
    };

    const response = await fetch("/api/items/itemDetails/" + id,options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
    }

    const item = await response.json();

    document.getElementById("recipient-page-item-type").innerText=item.itemtype;
    document.getElementById("recipient-page-item-description").innerText=item.description;

    console.log(item);

  } catch (error) {
    Redirect('/');
    console.error("LoginPage::error: ", error);
  }


};

function getId() {
    let urlString = window.location.href;
    let paramString = urlString.split('?')[1];
    if (paramString) {
      let params_arr = paramString.split('&');
      let pair = params_arr[0].split('=');
      if (pair[1]) {
        return parseInt(pair[1]);
      } else {
        return -1;
      }
  
    } else {
      return -1;
    }
  
}

export default PickRecipient;
