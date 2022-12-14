import { Redirect } from "../Router/Router";
import { getSessionObject, VerifyUser } from "../../utils/session";

import itemImg from "../../img/wheelbarrows-4566619_640.jpg";

/**
 * Render the LoginPage
 */
const home = `
<div id="triangle"> </div>
<section id="home-page">
   <div id="home-page-navigation">
      <h2 id="home-page-title"> Dernières offres</h2>
   </div>
   <div id="all-recent-item"></div>
</section>
`;

const MesOffres = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

  let allRecentItem = document.getElementById("all-recent-item");
  let token = getSessionObject("accessToken");

  try {
    var options = {
      method: "GET",
      headers: { token: token },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch("/api/items/mesOffres", options); // fetch return a promise => we wait for the response

    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }

    const items = await response.json();

    console.log("here", items);

    items.forEach((item) => {
      console.log("my item", item);

      let itemBox = document.createElement("div");
      let homePageImageBox = document.createElement("div");
      let itemImgDiv = document.createElement("img");
      let descriptionBox = document.createElement("div");
      let itemType = document.createElement("p");
      let itemDescription = document.createElement("p");

      itemBox.style = `position : relative; width : 17vw`;

      //delete cross
      let cross = document.createElement("div");
      cross.innerHTML = "X";
      cross.id = "delete";
      cross.style = `position : absolute; right : 3px; 
    color : white; text-shadow: 0 0 2px black, 0 0 2px black, 0 0 2px black, 0 0 2px black;
    `;

      itemBox.classList.add("item-box");
      homePageImageBox.classList.add("home-page-item-image");
      descriptionBox.classList.add("home-page-item-description");

      itemType.classList.add("item-title");
      itemDescription.classList.add("item-description");

      itemImgDiv.src = itemImg;
      itemType.innerText = item.itemtype;
      itemDescription.innerText = item.description;

      descriptionBox.appendChild(itemType);
      descriptionBox.appendChild(itemDescription);
      homePageImageBox.appendChild(itemImgDiv);
      itemBox.appendChild(cross);
      itemBox.appendChild(homePageImageBox);
      itemBox.appendChild(descriptionBox);

      homePageImageBox.addEventListener("click", function () {
        let params = [{ key: "id", value: item.id }];
        Redirect("/item", params);
      });
      allRecentItem.appendChild(itemBox);

      cross.onclick = async () => {
        var options = {
          method: "POST",
          headers: { token: getSessionObject("accessToken") },
          mode: "cors",
          cache: "default",
        };
        const response = await fetch(
          "/api/items/cancelOffer/" + item.id,
          options
        ); // fetch return a promise => we wait for the response
        if (response.status == 307) {
          await VerifyUser();
          document.location.reload();
        }
        if (response.ok) {
          allRecentItem.removeChild(itemBox);
        }
      };
    });
  } catch (error) {}
};

export default MesOffres;
