import {Redirect} from "../Router/Router";

import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";

const data = [{type: "meuble", description: "une table tres belle"},
  {type: "meuble", description: "une table tres belle"},
  {type: "meuble", description: "une table tres belle"}]
/**
 * Render the LoginPage
 */
const myItems = `
<section id="my-items-page">

    <div id="home-page-navigation">
        <h2 id="home-page-title"> Dernières offres</h2>
    </div>
    <div id="my-items-page-content">
        <div id="my-item-menu">
            <div class="my-item-link"> <a href="#"  data-uri="/"> mes offres</a></div>
            <div class="my-item-link"> <a  href="#"  data-uri="/mesOffres"> mes offres annulées </a></div>
            <div class="my-item-link"> <a  href="#"  data-uri="/additem"> mes objet recu  </a></div>
            <div class="my-item-link" > <a href="#"  data-uri="/userhandeler"> mes objet attribués </a></div>
        </div>
        <div id="all-recent-item">
            
        </div>
    </div>
    <div id="my-items-pop-up">
        <div id="cancell-item"> Annuler l'offre </div>
        
        <div id="update-item"> Mettre à jour les informations  </div>
        
        <div id="show-item"> Accéder à la publication </div>
        
        <div id="pick-recipient"> Indiquer un membre receveur</div>     
    </div>
</section>
<hr>
`;

const MyItems = async (id) => {
    let token = getSessionObject("accessToken");
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = myItems;
    let currentItemId;
    let myItemsMenu = document.querySelector("#my-item-menu");
  
    myItemsMenu.addEventListener("click",(e)=>{
        // To get a data attribute through the dataset object, get the property by the part of the attribute name after data- (note that dashes are converted to camelCase).
        let uri = e.target.dataset.uri;
        console.log(uri);
        if (uri) {
        Redirect(uri);
    }
    });

    document.getElementById("cancell-item").addEventListener("click",function(e){
        let itemR = document.getElementById(currentItemId);
        document.getElementById("all-recent-item").removeChild(itemR);
        document.getElementById("my-items-pop-up").style.display="none";
    })

    document.getElementById("update-item").addEventListener("click",function(e){
        let params = [{key: "id", value: currentItemId}];
        Redirect("/updateitem", params);
    });

    document.getElementById("pick-recipient").addEventListener("click",function(e){
        let params = [{key: "id", value: currentItemId}];
        Redirect("/pickrecipient", params);
    })
  
  
  let allRecentItem = document.getElementById("all-recent-item");

  try {
    var options = {
            method: 'GET',
            headers: {"token" : token},
            };   
    const response = await fetch("/api/items/mesOffres", options); // fetch return a promise => we wait for the response   
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
    }
    

    const items = await response.json();

    console.log("here", items);

    items.forEach((item) => {
      console.log("my item", item);

      let itemBox = document.createElement("div");
      let homePageImageBox = document.createElement("div");
      let itemImgDiv = document.createElement("img");
      let descriptionBox = document.createElement("div");
      let itemType = document.createElement("p")
      let itemDescription = document.createElement("p");

      itemBox.classList.add("item-box");
      itemBox.id=item.id;
      homePageImageBox.classList.add("home-page-item-image");
      descriptionBox.classList.add("home-page-item-description")

      itemType.classList.add("item-title");
      itemDescription.classList.add("item-description");

      itemImgDiv.src = itemImg;
      itemType.innerText = item.itemtype;
      itemDescription.innerText = item.description;

      descriptionBox.appendChild(itemType);
      descriptionBox.appendChild(itemDescription);
      homePageImageBox.appendChild(itemImgDiv);
      itemBox.appendChild(homePageImageBox);
      itemBox.appendChild(descriptionBox);

      itemBox.addEventListener("click", function () {
        /*
        let params = [{key: "id", value: item.id}];
        Redirect("/item", params);
        */
       document.getElementById("my-items-pop-up").style.display="flex";
       currentItemId = item.id;
      
      })
      allRecentItem.appendChild(itemBox);
    })

  } catch (error) {

  }

};



export default MyItems;
