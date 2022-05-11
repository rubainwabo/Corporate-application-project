import { getSessionObject, VerifyUser } from "../../utils/session";

import itemImg from "../../img/image_not_available.png";

let currentVersion;
const item = `
<div id="triangle"> </div>
<section id="item-page">
   <p id="message"> </p>
   <div id="item-img-description">
      <div id="item-img">
         <img id="img-id" alt="">
      </div>
      <div id="item-description">
         <p id="item-description-p"></p>
      </div>
   </div>
   <div id="item-info">
      <p style ="font-size : 0px" id="type-objet"> Type d’objet : <span id="item-type"><span> </p>
      <p style ="font-size : 0px" id="Offert"> Offert par : <span id="offeror"><span> </p>
      <p style ="font-size : 0px" id="nb-personnes"> Nombres de personnes interessées : <span id="number-interest"><span> </p>
      <p style ="font-size : 0px" id="slot"> Plages horaires : <span id="timeSlot"></span></p>
      <p style ="font-size : 0px" id="preced"> Précedentes dates d’offre : <span id="last-dates"> <span> </p>
      
   </div>
   <div id="item-show-interest">
   </div>
   <div id="add-iterest-pop-up">
      <div id="close-pop-up">
      <i style="font-size:25px;color:#CACACA"class="fa-regular fa-circle-xmark"></i>
      </div>
      <form id="add-iterest-form">
         <span id="error"></span>
         <div id="availabilitiesBox">
            <input type="text" required id="availabilities" placeholder="vos heures de disponibilité">
         </div>
         <div>
            <span> Je souhaite discuter des modalités pratiques du don </span><input type="checkbox" id="callMe"> 
         </div>
         <div id="call-me-box"> 
            <input type="text" id="phone-number" placeholder="numéro" required >
         </div>
         <div id="add-iterest-form-submits">
            <input type="submit" id="cancel-add-iterest" value="annuler">
            <input type="submit" id="add-interest-btn" value="envoyer">
         </div>
      </form>
   </div>
</section>
`;
const ItemPage = async () => {
  let id = getId();
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = item;

  let addIterestBtn = document.getElementById("add-interest-btn");

  let removePopUp = document.getElementById("cancel-add-iterest");
  let popUp = document.getElementById("add-iterest-pop-up");

  removePopUp.addEventListener("click", function (e) {
    e.preventDefault();
    popUp.style.display = "none";
  });
  //close pop-up
  document.getElementById("close-pop-up").addEventListener("click",function(e){
    popUp.style.display = "none";
  })

  try {
    // prepare the header of the request
    const options = {
      // body data type must match "Content-Type" header
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

    const item = await response.json();
    
    currentVersion = item.version;
    document.getElementById("item-description-p").innerText = item.description;
    document.getElementById("item-type").innerText = item.itemtype;
    document.getElementById("offeror").innerText = item.offeror;
    document.getElementById("number-interest").innerText =
      item.numberOfPeopleInterested;
    document.getElementById("timeSlot").innerText=item.timeSlot;
    console.log(item.timeSlot);
    let addInterestBox = document.getElementById("item-show-interest");


    if (item.offerorId != getSessionObject("userId")) {
      let button = document.createElement("button");
      button.id = "show-interest";
      button.innerText = "Je suis interessé";
      button.addEventListener("click", function (e) {
        e.preventDefault();
        popUp.style.display = "flex";
      });
      addInterestBox.appendChild(button);
    }

    getPicture(id, document.getElementById("img-id"));

    document.getElementById("type-objet").style.fontSize = "12px";
    document.getElementById("Offert").style.fontSize = "12px";
    document.getElementById("nb-personnes").style.fontSize = "12px";
    document.getElementById("preced").style.fontSize = "12px";
    document.getElementById("slot").style.fontSize="12px"
    document.getElementById("item-show-interest").style = "display : inline";

    let callMe = document.getElementById("callMe");

    callMe.addEventListener("change", function (e) {
      e.preventDefault();

      if (callMe.checked) {
        document.getElementById("call-me-box").style.display = "block";
      } else {
        document.getElementById("call-me-box").style.display = "none";
      }
    });

    console.log(item);
  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
  console.log("hello");
  await getDates(id);

  addIterestBtn.addEventListener("click", async function (e) {
    e.preventDefault();
    e.preventDefault();
    let availabilities = document.getElementById("availabilities").value;
    let callMe = document.getElementById("callMe").checked;

    try {
      let options;
      if (callMe) {
        let oldPhone = await getPhoneNumber(getSessionObject("userId"));

        let phoneNumber = document.getElementById("phone-number").value;

        let updateNumer = oldPhone != phoneNumber;

        options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            availabilities: availabilities,
            updateNumber: updateNumer,
            callMe: callMe,
            phoneNumber: phoneNumber,
            version:currentVersion,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            token: getSessionObject("accessToken"),
          },
        };
      } else {
        options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            availabilities: availabilities,
            updateNumber: "false",
            callMe: callMe,
            phoneNumber: "",
            version:currentVersion,
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
            token: getSessionObject("accessToken"),
          },
        };
      }

      const response = await fetch("/api/items/addInterest/" + id, options); // fetch return a promise => we wait for the response
      if (response.status == 307) {
        await VerifyUser();
        document.location.reload();
      }
      if (!response.ok) {
        response.text().then((result) => {
          document.getElementById("error").innerText = result;
        });
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }

      console.log("votre interet a été pris en compte");
      document.getElementById("message").innerText =
        "votre interet a été pris en compte";
      popUp.style.display = "none";
      document.getElementById("show-interest").style.display = "none";

      /*
      const rep  = await response.json(); // json() returns a promise => we wait for the data

      console.log(rep);
      */
    } catch (error) {
      console.error("LoginPage::error: ", error);
    }
  });
};

async function getPhoneNumber(idUser) {
  console.log("START OF ");
  try {
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        token: getSessionObject("accessToken"),
      },
    };
    const response = await fetch("/api/members/details?id=" + idUser, options); // fetch return a promise => we wait for the response
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }

    let user = await response.json();
    console.log(user);
    return user.phoneNumber;
  } catch (error) {
    console.log(error);
  }
}

async function getDates(itemId) {
  try {
    const options = {
      // body data type must match "Content-Type" header
      headers: {
        token: getSessionObject("accessToken"),
      },
    };
    const response = await fetch("/api/dates/" + itemId, options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    let datesToString = "";
    let dates = await response.json();
    dates.forEach((element) => {
      let date = new Date(element.date);
      datesToString +=
        ",  " +
        date.getDay() +
        "/" +
        date.getMonth() +
        "/" +
        date.getFullYear();
    });
    datesToString = datesToString.substr(1);
    document.getElementById("last-dates").innerText = datesToString;
  } catch (error) {
    console.log(error);
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
    console.log(imgDiv);
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
      console.log(imageObjectURL);
      imgDiv.src = imageObjectURL;
    }
  } catch (error) {
    console.log(error);
  }
}

export default ItemPage;
