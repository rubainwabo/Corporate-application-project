import { isJwtExpired } from 'jwt-check-expiration';
import { Redirect } from "../Router/Router";
import Logout from '../Logout/Logout';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";


import itemImg from '../../img/wheelbarrows-4566619_640.jpg';

/**
 * Render the LoginPage
 */
const home = `<section id="home-page">
<div id="home-page-navigation">
    <h2 id="home-page-title"> Dernières offres</h2>
</div>
  
<div id="all-recent-item">

   <div class="item-box" id="hello" style = "position : relative">
      <div class="delete-cross" id ="delete" 
      style = "position : absolute; right : 3px; color : white; text-shadow: 0 0 2px black, 0 0 2px black, 0 0 2px black, 0 0 2px black;"
      ">
      X 
      </div>
      <div class="home-page-item-image">
        <img src="${itemImg}">
      </div>
      <div class="home-page-item-description">
          <p class="item-title"> Meuble</p>
          <p class="item-description"> "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was tesfing pokqs qplsk ... </p>
      </div>
   </div>
</div>

</section>
`;

const MesOffres = async (id) => {

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

  let allRecentItem = document.getElementById("all-recent-item");
  
  try {
    var options = { method: 'GET',
               headers: {"token" : localStorage.getItem("accessToken")},
               mode: 'cors',
               cache: 'default'};   
    const response = await fetch("/api/items/mesOffres", options); // fetch return a promise => we wait for the response   
    console.log("res", response.body);
  if(!response.ok){
    return Redirect("/logout");

      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
  }

  const items = await response.json();

 console.log("here", items);

 items.forEach((item)=>{
    console.log("my item" , item);
    
    let itemBox = document.createElement("div");
    let homePageImageBox = document.createElement("div");
    let itemImgDiv = document.createElement("img");
    let descriptionBox = document.createElement("div");
    let itemType = document.createElement("p")
    let itemDescription = document.createElement("p");

    itemBox.style = `position : relative`

    //delete cross 
    let cross = document.createElement("div");
    cross.innerHTML = "X";
    cross.id = "delete"
    cross.style = `position : absolute; right : 3px; 
    color : white; text-shadow: 0 0 2px black, 0 0 2px black, 0 0 2px black, 0 0 2px black;
    `

    itemBox.classList.add("item-box");
    homePageImageBox.classList.add("home-page-item-image");
    descriptionBox.classList.add("home-page-item-description")
    
    itemType.classList.add("item-title");
    itemDescription.classList.add("item-description");
    
    itemImgDiv.src=itemImg;
    itemType.innerText=item.itemtype;
    itemDescription.innerText=item.description;

    descriptionBox.appendChild(itemType);
    descriptionBox.appendChild(itemDescription);
    homePageImageBox.appendChild(itemImgDiv);
    itemBox.appendChild(cross);
    itemBox.appendChild(homePageImageBox);
    itemBox.appendChild(descriptionBox);

    homePageImageBox.addEventListener("click",function(){
      let params = [{key:"id",value:item.id}];
      Redirect("/item",params);
    })
    allRecentItem.appendChild(itemBox);
    
    cross.onclick = async () => {
        var options = { method: 'POST',
        headers: {"token" : localStorage.getItem("accessToken")},
        mode: 'cors',
        cache: 'default',
    }; 
    const response = await fetch("/api/items/cancelOffer/" + item.id, options); // fetch return a promise => we wait for the response   

    if (response.ok) {
        allRecentItem.removeChild(itemBox);
    } 
    }
})

  } catch (error) {
    
  }
  
};


export default MesOffres;
