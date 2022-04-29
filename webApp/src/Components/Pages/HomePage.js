import {Redirect} from "../Router/Router";

import itemImg from '../../img/image_not_available.png';
import { getSessionObject, VerifyUser } from "../../utils/session";

/**
 * Render the LoginPage
 */
const home = `
<div style="width : 100%; width : 100%; 
width : 100%; 
height : 100%; position : absolute; 
left : 0px; right : 0px;
clip-path: polygon(75% 0, 0 0, 0 25%);
position : absolute; 
top : 0px;
left : 0px;
z-index: -5;
background-color: #FFF59B;
"> </div>
<div id="polygon-bottom-right">
</div>
</div> 
<section id="home-page">
    <div id="home-page-navigation">
        <h2 id="home-page-title"> Dernières offres</h2>

    </div>
    
    <div id="all-recent-item">

    </div>
</section>
`;

const HomePage = async (id) => {

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

  let allRecentItem = document.getElementById("all-recent-item");
  let fetchMethodName = getSessionObject("accessToken") ? "lastItemsOfferedConnected" : "lastItemsOfferedNotConnected";
  let token = getSessionObject("accessToken");
  try {
    var options = {
        method: 'GET',
        headers: {
          "token" : token}
    };
    const response = await fetch("/api/items/"+fetchMethodName,options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser(); 
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
    }

    const items = await response.json();


    items.forEach((item) => {

      let itemBox = document.createElement("div");
      let homePageImageBox = document.createElement("div");
      let itemImgDiv = document.createElement("img");
      let descriptionBox = document.createElement("div");
      let itemType = document.createElement("p")
      let itemDescription = document.createElement("p");

      itemBox.classList.add("item-box");
      homePageImageBox.classList.add("home-page-item-image");
      descriptionBox.classList.add("home-page-item-description")

      itemType.classList.add("item-title");
      itemDescription.classList.add("item-description");

      //itemImgDiv.src = itemImg;
      getPicture(item.id,itemImgDiv);
      itemType.innerText = item.itemtype;
      itemDescription.innerText = item.description;

      descriptionBox.appendChild(itemType);
      descriptionBox.appendChild(itemDescription);
      homePageImageBox.appendChild(itemImgDiv);
      itemBox.appendChild(homePageImageBox);
      itemBox.appendChild(descriptionBox);

      itemBox.addEventListener("click", function () {
        let params = [{key: "id", value: item.id}];
        Redirect("/item", params);
      })
      allRecentItem.appendChild(itemBox);
    })

  } catch (error) {

  }

};
async function getPicture(itemId,imgDiv){
  try{
  const response = await fetch("/api/items/picture/"+itemId); // fetch return a promise => we wait for the response
  
  if (!response.ok) {
    imgDiv.src=itemImg;
    throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
    )
  }
  if(response.ok){
    const imageBlob = await response.blob();
        const imageObjectURL = URL.createObjectURL(imageBlob);
        imgDiv.src=imageObjectURL
        
  }
  
  }catch(error){
  }
}

export default HomePage;
