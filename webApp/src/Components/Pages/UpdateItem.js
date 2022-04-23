import { getSessionObject } from '../../utils/session';
import {Redirect} from '../Router/Router';

const updateItem = `
<section id="add-item-page">
  <p id="message"></p>
    <form id="add-item-form">
     
            <div>
                <label for="item-type">Type d’objet</label><br>
                <input type="text" value="" id="item-type" disabled>
            </div>
            <div>
                <label for="item-date">Date de l'offre </label><br>
                <input type="text" value="" id="item-date" disabled>
            </div>
            <div>
                <label for="availability">Plage horaire</label><br>
                <input  class="add-item-iputs" type="text" id="availability" name="availability" required="required">
            </div>
  
            <div id="add-item-description">
                <label for="itemDescription">Description</label><br>
                <input  class="" type="text" id="itemDescription" name="itemDescription" >
            </div>

            <div id="cancel-add">
                <div>
                    <button>annuler</button>           
                </div>
                <div>
                    <button id="addItemButton">publier</button>
                </div>
            </div>
    </form>
    
</div>
   
</section>
`;

const UpdateItem = async () => {
    let id = getId();
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = updateItem;
  
  let addItemForm = document.getElementById("add-item-form");
  let updateButton = document.getElementById("addItemButton");



  
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
    document.getElementById("item-type").value=item.itemtype;
    document.getElementById("availability").value = item.timeSlot;
    document.getElementById("itemDescription").value = item.description;
    
    console.log(item);

  } catch (error) {
    Redirect('/');
    console.error("LoginPage::error: ", error);
  }

  updateButton.addEventListener("click", async function (e) {
    e.preventDefault();
    let description = document.getElementById("itemDescription").value;
    let urlPicture = "none";
    let timeSlot = document.getElementById("availability").value;

    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          id:id,
          description: description,
          urlPicture: urlPicture,
          timeSlot: timeSlot
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          "token":getSessionObject("accessToken"),
        },
      };

      const response = await fetch("/api/items/update", options); // fetch return a promise => we wait for the response

      if (!response.ok) {
        response.text().then((result) => {
          document.getElementById("error").innerText = result;
        })
      }

      const itemType = await response.json(); // json() returns a promise => we wait for the data

      console.log(itemType);
      let params = [{key: "id", value: id}];
      Redirect("/item", params);

     
     

    } catch (error) {
      console.error("LoginPage::error: ", error);
    }

  })

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
  
export default UpdateItem;
