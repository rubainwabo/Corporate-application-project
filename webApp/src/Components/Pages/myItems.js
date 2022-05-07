import { Redirect } from "../Router/Router";

import itemImg from "../../img/image_not_available.png";
import { getSessionObject, VerifyUser } from "../../utils/session";

/**
 * Render the LoginPage
 */
const starsForm=`<form class="rating">
<label>
<input id="star1" type="radio" name="stars" value="1" />
<span class="icon">★</span>
</label>
<label>
<input id="star2" type="radio" name="stars" value="2" />
<span class="icon">★</span>
<span class="icon">★</span>
</label>
<label>
<input id="star3" type="radio" name="stars" value="3" />
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>   
</label>
<label>
<input id="star4" type="radio" name="stars" value="4" />
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>
</label>
<label>
<input id="star5" type="radio" name="stars" value="5" />
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>
<span class="icon">★</span>
</label>`
const myItems = `
<div id="triangle"> </div>
<section id="my-items-page">
   <div id="my-items-filtre" >

      <select class="add-item-iputs" name="pets" id="items-type-selectbox" >
         <option value="0" selected disabled hidden>TYPE</option>
         <option value="0">Tous sélectionner </option>
      </select>
     
      <select class="add-item-iputs" name="pets" id="invalid-selectbox" >
         <option value="" id="hidden-invalid-option" selected disabled hidden>Cause de la mise en attente</option>
         <option value="invalid recipient">Receveur indisponible</option>  
         <option value="invalid offeror">offreur indisponible</option>
      </select>
   </div>
   <div id="my-items-page-content">
      <div id="my-item-menu">
         <div class="my-item-link"> <a href="#"  id="get-items-offered" data-uri="/"> Mes offres</a></div>
         <div class="my-item-link"> <a  href="#" id="get-items-cancelled" data-uri="/mesOffres"> Mes offres annulées </a></div>
         <div class="my-item-link" > <a href="#" id="get-items-assigned"  data-uri="/userhandeler"> Mes offres attribuées </a></div>
         <div class="my-item-link" > <a href="#" id="get-items-gifted-by-me"  data-uri="/userhandeler"> Mes offres données </a></div>
         <div class="my-item-link"> <a  href="#" id="get-items-gifted" data-uri="/additem"> Mes offres reçus  </a></div>
         <div class="my-item-link"> <a  href="#" id="get-items-invalid" data-uri="/additem"> Mes offres en attente  </a></div>
      </div>
      <div id="all-recent-item">
      </div>
   </div>
   <div id="my-items-pop-up">
      <div id="cancell-item" class="pop-up-option"> Annuler l'offre </div>
      <div id="offer-again" style="color:green; font-weight: bold" class="pop-up-option"> Offir à nouveau </div>
      <div id="item-gived" style="color:green" class="pop-up-option"> Indiquer objet donné </div>
      <div id="item-not-gived" style="color:red" class="pop-up-option"> Indiquer objet non pris </div>
      <div id="rate-item" class="pop-up-option"> Noter l'offre </div>
      <div id="update-item" class="pop-up-option"> Mettre à jour les informations  </div>
      <div id="show-item" class="pop-up-option"> Accéder à la publication </div>
      <div id="pick-recipient" class="pop-up-option"> Indiquer un membre receveur</div>
   </div>
   <div id="rating-box">
      <div id="error"> </div>
      <form class="rating">
         ${starsForm}
      </form>
      <textarea placeholder="commentaire" id="rate-comment" required></textarea>
      <input type="submit" name="envoyer" id="submit-rate-button" value="envoyer" />
   </div>
</section>
`;
let currentItemId;
let currentState = "offered";
const MyItems = async (id) => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = myItems;

  getItemsTypes();
  changeOptions(currentState);

  document.getElementById("items-type-selectbox").addEventListener("change",function(e){
    getMyItems(currentState, true);
  });
  document.getElementById("invalid-selectbox").addEventListener("change",function(e){
    let invalidState = document.getElementById("invalid-selectbox").value;
    currentState = invalidState;
    getMyItems(currentState, true);
  })


  document
    .getElementById("my-items-page")
    .addEventListener("click", function (e) {
     
      if (
        e.target.id == "my-items-page" ||
        e.target.id == "all-recent-item" ||
        e.target.id == "my-items-page-content" ||
        e.target.id == "my-item-menu"
      ) {
        let popup = document.getElementById("my-items-pop-up");
        popup.style.display = "none";
        document.getElementById("rating-box").style.display = "none";
      }
    });

  document
    .getElementById("get-items-cancelled")
    .addEventListener("click", function (e) {
      e.preventDefault();
      currentState = "cancelled";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, true);
      changeOptions(currentState);
    });
  document
    .getElementById("get-items-assigned")
    .addEventListener("click", function (e) {
      e.preventDefault();
      currentState = "Assigned";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, true);
      changeOptions(currentState);
    });

  document
    .getElementById("get-items-offered")
    .addEventListener("click", function (e) {
      e.preventDefault();
      currentState = "offered";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, true);
      changeOptions(currentState);
    });
  document
    .getElementById("get-items-gifted")
    .addEventListener("click", function (e) {
      e.preventDefault();
      currentState = "gifted";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, false);
      changeOptions(currentState);
    });

  document
    .getElementById("get-items-gifted-by-me")
    .addEventListener("click", function (e) {
      e.preventDefault();
      currentState = "gifted-by-me";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, true);
      changeOptions(currentState);
    });

  document
    .getElementById("get-items-invalid")
    .addEventListener("click",function(e){
      e.preventDefault();
      currentState="invalid recipient";
      document.getElementById("invalid-selectbox").style.display="flex";
      document.getElementById("all-recent-item").innerText = "";
      getMyItems(currentState, true);
      changeOptions(currentState);
    })

  document
    .getElementById("cancell-item")
    .addEventListener("click", function (e) {
      let itemR = document.getElementById(currentItemId);
      if (cancelItem(currentItemId)) {
        document.getElementById("all-recent-item").removeChild(itemR);
      }
      document.getElementById("my-items-pop-up").style.display = "none";
    });

  document.getElementById("item-gived").addEventListener("click", function (e) {
    let itemR = document.getElementById(currentItemId);

    if (itemGived(currentItemId, true)) {
      document.getElementById("all-recent-item").removeChild(itemR);
    }
    document.getElementById("my-items-pop-up").style.display = "none";
  });

  document
    .getElementById("item-not-gived")
    .addEventListener("click", async function (e) {

      await itemGived(currentItemId, false);
      currentState = "item-not-gived";
      changeOptions(currentState);

    });

  document
    .getElementById("offer-again")
    .addEventListener("click", function (e) {
      let itemR = document.getElementById(currentItemId);

      if (offerAgain(currentItemId)) {
        document.getElementById("all-recent-item").removeChild(itemR);
      }
      document.getElementById("my-items-pop-up").style.display = "none";
    });

  document
    .getElementById("update-item")
    .addEventListener("click", function (e) {
      let params = [{ key: "id", value: currentItemId }];
      Redirect("/updateitem", params);
    });

  document
    .getElementById("pick-recipient")
    .addEventListener("click", function (e) {       
        let params = [{ key: "id", value: currentItemId }];
        Redirect("/pickrecipient", params);    
    });

  document.getElementById("show-item").addEventListener("click", function (e) {
    let params = [{ key: "id", value: currentItemId }];
    Redirect("/item", params);
  });

  document.getElementById("rate-item").addEventListener("click", function (e) {
    document.getElementById("my-items-pop-up").style.display = "none";
    document.getElementById("rating-box").style.display = "flex";
  });

  document
    .getElementById("submit-rate-button")
    .addEventListener("click", async function (e) {
      let rating = await getItemDetails(currentItemId);
      console.log(rating);
      if (rating == 0) {
        var radios = document.getElementsByName("stars");
        var found = 1;
        for (var i = 0; i < radios.length; i++) {
          if (radios[i].checked) {
            let nbStars = radios[i].value;
            let comment = document.getElementById("rate-comment").value;
            if (comment != "") {
              if (await rateItem(currentItemId, nbStars, comment)) {
                document.getElementById("rating-box").style.display = "none";
              }
            } else {
              document.getElementById("error").innerText =
                "un commentaire est obligatoire";
            }

            found = 0;
            break;
          }
        }
        if (found == 1) {
          alert("Please Select Radio");
        }
      } else {
        document.getElementById("error").innerText =
          "vous avez déjà évalué  l'offre";
      }
    });

  await getMyItems(currentState, true);
};

async function getMyItems(state, mine) {
  if (state == "gifted-by-me") {
    state = "gifted";
  }
  let itemtype = parseInt(document.getElementById("items-type-selectbox").value);
  
 
  let allRecentItem = document.getElementById("all-recent-item");
  allRecentItem.innerText="";
  try {
    var options = {
      method: "GET",
      headers: { token: getSessionObject("accessToken") },
    };
    let response;
    if (mine) {
      response = await fetch("/api/items/member/" + getSessionObject("userId") + "?state="+state+"&type="+itemtype +"&mine="+1, options);
    } else {
      response = await fetch("/api/items/member/" + getSessionObject("userId") + "?state="+state+"&type="+itemtype +"&mine="+0, options);
    }
    // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const items = await response.json();

    

    items.forEach((item) => {
      let itemBox = document.createElement("div");
      let homePageImageBox = document.createElement("div");
      let itemImgDiv = document.createElement("img");
      let descriptionBox = document.createElement("div");
      let itemType = document.createElement("p");
      let itemDescription = document.createElement("p");
      let nbrInterestBox = document.createElement("div");
      let nbrInterestHeart = document.createElement("div");
      let nbrInterest = document.createElement("div");

      itemBox.classList.add("item-box");
      itemBox.id = item.id;
      homePageImageBox.classList.add("home-page-item-image");
      descriptionBox.classList.add("home-page-item-description");

      nbrInterestBox.classList.add("number-interest-box");
      

      itemType.classList.add("item-title");
      itemDescription.classList.add("item-description");

      //itemImgDiv.src = itemImg;
      getPicture(item.id, itemImgDiv);
      itemType.innerText = item.itemtype;
      itemDescription.innerText =  item.description.length >=40 ? item.description.substring(0, 40) + "..." : item.description

      // number of interest
      let color;
      if(item.numberOfPeopleInterested>0){
        color = "#FF3030";
      }
     

      if(item.rating!=0){
        let allStars = `<i class="fa-solid fa-star" style="color:#FFC300"></i>`;
        for(let i= 0; i<item.rating;i++){
          nbrInterestHeart.innerHTML+=allStars;
        }
      }else if(currentState!="gifted-by-me" && currentState!="gifted"){
        console.log(currentState);
        nbrInterestHeart.innerHTML=`<i class="fa-solid fa-heart" style="color:${color};margin:2px"></i>`;
        nbrInterest.innerText=item.numberOfPeopleInterested;

      }


      descriptionBox.appendChild(itemType);
      descriptionBox.appendChild(itemDescription);
      homePageImageBox.appendChild(itemImgDiv);
      nbrInterestBox.appendChild(nbrInterestHeart);
      nbrInterestBox.appendChild(nbrInterest);

      itemBox.appendChild(homePageImageBox);
      itemBox.appendChild(descriptionBox);
      itemBox.appendChild(nbrInterestBox);


      itemBox.addEventListener("click", function () {
        document.getElementById("my-items-pop-up").style.display = "flex";
        currentItemId = item.id;
      });
      
      allRecentItem.appendChild(itemBox);
      console.log(item);
    });
  } catch (error) {
    console.log(error);
  }
}

function changeOptions(state) {
  let cancel = document.getElementById("cancell-item");
  let offerAgain = document.getElementById("offer-again");
  let itemGived = document.getElementById("item-gived");
  let itemNotGived = document.getElementById("item-not-gived");

  let update = document.getElementById("update-item");
  let show = document.getElementById("show-item");
  let pickRecipient = document.getElementById("pick-recipient");

  let rating = document.getElementById("rate-item");

  let divs = document.querySelectorAll("#my-items-pop-up div");

  for (let i = 0; i < divs.length; i++) {
    divs[i].style.display = "none";
  }
  let links = document.querySelectorAll("#my-item-menu div a");
 
  for (let i = 0; i < links.length; i++) {
    links[i].style.fontWeight = "lighter";
  }
  if(state!="invalid offeror" && state!="invalid recipient"){
    document.getElementById("hidden-invalid-option").selected=true;
    document.getElementById("invalid-selectbox").style.display="none";
  }

  document.getElementById("my-items-pop-up").style.display = "none";
  document.getElementById("rating-box").style.display = "none";
  if (state == "offered") {
    cancel.style.display = "flex";
    update.style.display = "flex";
    show.style.display = "flex";
    pickRecipient.style = "flex";
    document.getElementById("get-items-offered").style.fontWeight = "normal";
  } else if (state == "cancelled") {
    offerAgain.style.display = "flex";
    show.style.display = "flex";
    document.getElementById("get-items-cancelled").style.fontWeight = "normal";
  } else if (state == "Assigned") {
    itemGived.style.display = "flex";
    itemNotGived.style.display = "flex";

    itemGived.style.display = "flex";
    show.style.display = "flex";
    document.getElementById("get-items-assigned").style.fontWeight = "normal";
  } else if (state == "gifted-by-me") {
    show.style.display = "flex";
    document.getElementById("get-items-gifted-by-me").style.fontWeight = "normal";
  } else if (state == "item-not-gived") {
    offerAgain.style.display = "flex";
    cancel.style.display = "flex";
    pickRecipient.style.display = "flex";
    document.getElementById("my-items-pop-up").style.display = "flex";
    document.getElementById("get-items-assigned").style.fontWeight = "normal";
  } else if(state=="invalid offeror" || state=="invalid recipient"){
    offerAgain.style.display = "flex";
    cancel.style.display = "flex";
    pickRecipient.style.display = "flex";
    document.getElementById("get-items-invalid").style.fontWeight = "normal";
  }else {
    rating.style.display = "flex";
    show.style.display = "flex";
    document.getElementById("get-items-gifted").style.fontWeight = "normal";
  }
}
async function cancelItem(idItem) {
  try {
    var options = {
      method: "PUT",
      headers: { token: getSessionObject("accessToken") },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch(
      "/api/items/update/" + idItem + "?condition=cancelled",
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
async function offerAgain(idItem) {
  try {
    var options = {
      method: "POST",
      headers: { token: getSessionObject("accessToken") },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch("/api/items/offerAgain/" + idItem, options); // fetch return a promise => we wait for the response
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

async function itemGived(itemId, collected) {
  try {
    var options = {
      method: "POST",
      body: JSON.stringify({
        itemId: itemId,
        itemCollected: collected,
      }),
      headers: {
        "Content-Type": "application/json",
        token: getSessionObject("accessToken"),
      },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch("/api/items/itemCollected", options); // fetch return a promise => we wait for the response
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
async function rateItem(itemId, nbStars, comment) {
  try {
    var options = {
      method: "POST",
      body: JSON.stringify({
        itemId: itemId,
        nbStars: nbStars,
        comment: comment,
      }),
      headers: {
        "Content-Type": "application/json",
        token: getSessionObject("accessToken"),
      },
      mode: "cors",
      cache: "default",
    };
    const response = await fetch("/api/items/rate", options); // fetch return a promise => we wait for the response
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

async function getItemDetails(itemId) {
  try {
    // hide data to inform if the pizza menu is already printed
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        token: getSessionObject("accessToken"),
      },
    };

    const response = await fetch("/api/items/" + itemId, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const item = await response.json();
    console.log(item);
    return item.rating;
  } catch (error) {
    console.log(error);
  }
}
async function getItemsTypes() {
  let selectBox = document.getElementById("items-type-selectbox");
  try {
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        token: getSessionObject("accessToken"),
      },
    };
    const response = await fetch("/api/itemsType", options); // fetch return a promise => we wait for the response

    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    const itemsTypes = await response.json();

    itemsTypes.forEach((itemType) => {
      let option = document.createElement("option");
      option.value = itemType.idItemType;
      option.innerText = itemType.itemTypeName;

      selectBox.appendChild(option);
    });
  } catch (error) {
    console.error("addItemPage::error: ", error);
  }
}

export default MyItems;
