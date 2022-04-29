import itemImg from "../../img/wheelbarrows-4566619_640.jpg";
import search from "../../img/search.svg";
import { Redirect } from "../Router/Router";
import {getSessionObject, VerifyUser} from "../../utils/session";


const updateCards = (items) => {
  console.log("items update");
  let allRecentItem = document.getElementById("all-recent-item");
  allRecentItem.innerHTML = "";
  items.forEach((item) => {
    console.log("my item", item);

    let itemBox = document.createElement("div");
    let homePageImageBox = document.createElement("div");
    let itemImgDiv = document.createElement("img");
    let descriptionBox = document.createElement("div");
    let itemType = document.createElement("p");
    let itemDescription = document.createElement("p");

    itemBox.classList.add("item-box");
    homePageImageBox.classList.add("home-page-item-image");
    descriptionBox.classList.add("home-page-item-description");

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
      let params = [{ key: "id", value: item.id }];
      Redirect("/item", params);
    });
    allRecentItem.appendChild(itemBox);
  });
};

const handleSearchButton = async () => {
  const searchButton = document.getElementById("search-btn");
  searchButton.addEventListener("click", async () => {
    console.log("We clicked");
    const value = document.getElementById("search").value;
    const filter = document.getElementById("type").checked
        ? "type"
        : document.getElementById("state").checked
            ? "state"
            : document.getElementById("name").checked
                ? "name"
                : "";

    try {
      const response = await fetch(
          "/api/items/filtered?filter=" + filter + "&input=" + value
      ); // fetch return a promise => we wait for the response
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const items = await response.json();
      updateCards(items);
    } catch (error) {}
  });
};

const input2 = `
  <input id="search" class="col-10 mt-1" style="border-radius : 4px; border : solid grey;"> </input>
    <img id="search-btn" src="${search}" class="col-2 px-0 mt-1" 
      style="width: 30px; border : solid grey; transform: translateX(-8px); border-radius : 5px;"/>
  </div>
      `;

const searchBtn = document.createElement("img");
searchBtn.id = "search-date";
searchBtn.style =
    "width: 30px; border : solid grey; border-radius : 5px;margin-left: 45%;margin-right: 45%;margin-top: 7px;";
searchBtn.src = search;


const home = `
<div id="triangle"> </div>

<section id="home-page">
    <div id="home-page-navigation">
        <div class="row">
        <h2 class="col-4" id="home-page-title"> Dernières offres</h2>
        <div class="col-8">
          <div class="row">  
            <div class="col-4">
              <div class="row" id="input-div">
                <input id="search" class="col-10 mt-1" style="border-radius : 4px; border : solid grey;"> </input>
                <img id="search-btn" src="${search}" class="col-2 px-0 mt-1" 
                style="width: 30px; border : solid grey; transform: translateX(-8px); border-radius : 5px;"/>
              </div>
            </div>
            <div id="radio-cont" class="col-8">
            <div id="radio"class="col-8 pt-2" style="display : flex;">
              <span class="col-2 filter" style ="padding-right : 10px;"> Trier par : </span> 
              <label class="filter col-2 container"> Nom de la personne
                <input id="name" type="radio" name="radio">
                <span class="checkmark"></span>
              </label>
              <label class="filter col-2 container"> Type d'objet
                <input id="type" type="radio" name="radio">
                <span class="checkmark"></span>
              </label>
              <label class="filter col-2 container"> Etat de l'objet
                <input  id="state" type="radio" name="radio">
                <span class="checkmark"></span>
              </label>
              <label class="filter col-2 container"> Date
                <input id="date" type="radio" name="radio">
                <span class="checkmark"></span>
              </label>
            </div>
            </div>
          </div>
          </div>
        </div>
    </div>
      <div id="all-recent-item">
    </div>
</section>
`;


const HomePage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;
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
    updateCards(items);
  } catch (error) {}

  const date = document.getElementById("date");
  const state = document.getElementById("state");
  const type = document.getElementById("type");
  const name = document.getElementById("name");

  const radio = document.getElementById("input-div");
  const dateDebut = document.createElement("input");
  const dateFin = document.createElement("input");

  state.onclick = () => {
    {
      radio.innerHTML = input2;
      handleSearchButton();
    }
  };
  type.onclick = () => {
    radio.innerHTML = input2;
    handleSearchButton();
  };
  name.onclick = () => {
    radio.innerHTML = input2;
    handleSearchButton();
  };

  date.onclick = () => {
    const dateInputDiv = document.createElement("div");
    console.log("test");
    radio.innerHTML = "";
    const deb = document.createElement("span");
    deb.innerHTML = "Date début";
    const fin = document.createElement("span");
    fin.innerHTML = "Date fin";

    dateDebut.value = "yyyy/mm/jj";
    dateDebut.id = "date-d";
    dateFin.value = "yyyy/mm/jj";
    dateFin.id = "date-f";

    dateInputDiv.appendChild(deb);
    dateInputDiv.appendChild(dateDebut);
    dateInputDiv.appendChild(fin);
    dateInputDiv.appendChild(dateFin);
    dateInputDiv.appendChild(searchBtn);
    radio.appendChild(dateInputDiv);
    radio.style = "display : content; font-color : gray";
  };

  searchBtn.onclick = async () => {
    let options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };
    const date1 = document.getElementById("date-d").value;
    const date2 = document.getElementById("date-f").value;
    try {
      const response = await fetch(
          "api/items/filtered?filter=date&input=" + date1 + "-" + date2,
          options
      );
      console.log(response);

      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const items = await response.json();
      updateCards(items);
    } catch (error) {}
  };
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
