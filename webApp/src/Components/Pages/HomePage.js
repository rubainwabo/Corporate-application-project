import { isJwtExpired } from 'jwt-check-expiration';
import { Redirect } from "../Router/Router";
import Logout from '../Logout/Logout';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";


import itemImg from '../../img/wheelbarrows-4566619_640.jpg';

const data = [{type:"meuble",description:"une table tres belle"},{type:"meuble",description:"une table tres belle"},{type:"meuble",description:"une table tres belle"}]
/**
 * Render the LoginPage
 */
const home = `
<section id="home-page">
    <div id="home-page-navigation">
        <h2 id="home-page-title"> Dernières offres</h2>
        
    </div>
      

    <div id="all-recent-item">
    
       <div class="item-box" id="hello" style = "position : relative">
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

const HomePage = async (id) => {

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

  
  let allRecentItem = document.getElementById("all-recent-item");
  var hello = document.getElementById("hello");

  hello.addEventListener("click",function(e){
    e.preventDefault();

    var params = [{key:"id",value:"1"}]   
    Redirect("/item", params)
  })
  try {

    const response = await fetch("/api/items/lastItemsOfferedNotConnected"); // fetch return a promise => we wait for the response   
    console.log("res", response);
  if(!response.ok){
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
      itemBox.appendChild(homePageImageBox);
      itemBox.appendChild(descriptionBox);
      
      itemBox.addEventListener("click",function(){
        let params = [{key:"id",value:item.id}];
        Redirect("/item",params);
      })
      allRecentItem.appendChild(itemBox);
    })

  } catch (error) {
    
  }
  
};


export default HomePage;
