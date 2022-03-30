import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
import { Redirect } from '../Router/Router';
const addItem = `
<section id="add-item-page">
  <p id="message"></p>
    <form id="add-item-form">
     
            <div >
                <label for="pet-select">Type d’objet</label><br>
                <select class="add-item-iputs" name="pets" id="items-type-selectbox" required="required">
                    <option value="">--veuillez choisir un type--</option>
                    
                </select>
                <button id="add-new-item-type-btn"> <i class="fa-solid fa-plus"></i> </button>
            </div>
            
            <div>
                <label for="availability">Plage horaire</label><br>
                <input  class="add-item-iputs" type="text" id="availability" name="availability" required="required">
            </div>
  
            <div id="add-item-description">
                <label for="itemDescription">Description</label><br>
                <input  class="" type="text" id="itemDescription" name="itemDescription" required="required">
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
  
    <div id="add-item-pop-up">
      
        <form id="add-item-type-form">
        <span id="error"></span>
          <div>
            <input type="text" id="item-type-name" placeholder="nom du type de l'objet">
          </div>
          <div>
            <input type="submit" id="cancel-add-item-type" value="annuler">
            <input type="submit" id="add-item-type" value="envoyer">
          </div>
        </form>
    </div>

    <div id="add-item-pop-up-confim">
        <div>
          Votre objet a été ajouté
        </div>
       
        <button id="add-new">continuer</button>
        <button id="done">OK</button>
        </div>
        
        
    </div>


   
</section>
`;

const AddItemPage = () => {
   
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = addItem;
    getItemsTypes();
    let addItemForm = document.getElementById("add-item-form");
    let addItemType = document.getElementById("add-item-type");
    let addItemTypeBtn = document.getElementById("add-new-item-type-btn");
    let popUp = document.getElementById("add-item-pop-up");
    let removePopUp = document.getElementById("cancel-add-item-type");
    let addNew = document.getElementById("add-new");
    let confirmPop = document.getElementById("add-item-pop-up-confim");
    let done = document.getElementById("done");
    

    removePopUp.addEventListener("click",function(e){
        popUp.style.display="none";
    })
    addItemTypeBtn.addEventListener("click",function(e){
      e.preventDefault();
      popUp.style.display="flex";
    })

    addNew.addEventListener("click",function(e){
      document.getElementById("items-type-selectbox").value="";      
      document.getElementById("availability").value="";
      document.getElementById("itemDescription").value="";
      confirmPop.style.display="none";

    })
    done.addEventListener("click",function(e){
      Redirect('/');
    })
    

   

    addItemType.addEventListener("click", async function(e){
      e.preventDefault();
     
      let itemTypeName = document.getElementById("item-type-name").value;
      try {
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: JSON.stringify({
            itemType: itemTypeName
          }), // body data type must match "Content-Type" header
          headers: {
            "Content-Type": "application/json",
          },
        };
        
        const response = await fetch("/api/itemsType/addItemType", options); // fetch return a promise => we wait for the response
  
        if (!response.ok) {
          response.text().then((result)=>{
            document.getElementById("error").innerText=result;
          })
          throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
          );
        }
        
        
        const itemType  = await response.json(); // json() returns a promise => we wait for the data
      
        console.log(itemType);

        let selectBox = document.getElementById("items-type-selectbox");
        console.log(selectBox);
        let option = document.createElement("option");
        option.value=itemType.itemTypeName;
        option.innerText=itemType.itemTypeName;
        option.selected=true;
        selectBox.appendChild(option);

        
        popUp.style.display="none";
        
       
      } catch (error) {
        console.error("LoginPage::error: ", error);
      }

    })

    addItemForm.addEventListener("submit",async function(e){
      e.preventDefault();
      let description = document.getElementById("itemDescription").value;
      let urlPicture = "none";
      let itemtype = document.getElementById("items-type-selectbox").value;
      
      let timeSlot = document.getElementById("availability").value;
      
        try {
            const options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                description: description,
                urlPicture: urlPicture,
                itemtype: itemtype,
                timeSlot: timeSlot
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
              },
            };
      
            const response = await fetch("/api/items/add", options); // fetch return a promise => we wait for the response
      
            if (!response.ok) {
              response.text().then((result)=>{
                document.getElementById("error").innerText=result;
              })
              throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
              );
            }
            
            const itemType  = await response.json(); // json() returns a promise => we wait for the data
      
            console.log(itemType);
            document.getElementById("add-item-pop-up-confim").style.display="flex";
            
           
          } catch (error) {
            console.error("LoginPage::error: ", error);
          }
        
    })

    async function getItemsTypes(){

      let selectBox = document.getElementById("items-type-selectbox");

      try{
        const response = await fetch("/api/itemsType/getAll"); // fetch return a promise => we wait for the response   

        if(!response.ok){
            throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
            )
        }
        const itemsTypes = await response.json();
        
        itemsTypes.forEach(itemType => {
          let option = document.createElement("option");
          option.value=itemType.itemTypeName;
          option.innerText=itemType.itemTypeName;

          selectBox.appendChild(option);
        });

      }catch(error){
        console.error("addItemPage::error: ", error);
      }

      
      
    }
    /*
    const getItemsTypes = () => {
     
      
      
       
    }
     */
  
  };
  
  export default AddItemPage;