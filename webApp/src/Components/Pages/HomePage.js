import {Redirect} from "../Router/Router";

import itemImg from '../../img/wheelbarrows-4566619_640.jpg';

/**
 * Render the LoginPage
 */
const home = `
<div style="width : 100%; width : 100%, 
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

  try {
    const response = await fetch("/api/items/lastItemsOfferedNotConnected"); // fetch return a promise => we wait for the response
    console.log("res", response.body);
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
      homePageImageBox.classList.add("home-page-item-image");
      descriptionBox.classList.add("home-page-item-description")

      itemType.classList.add("item-title");
      itemDescription.classList.add("item-description");
      
      itemImgDiv.src = item.urlPicture;
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

export default HomePage;
