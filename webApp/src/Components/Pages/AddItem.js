import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
const addItem = `
<section id="add-item-page">
    <form id="add-item-form">
     
            <div >
                <label for="pet-select">Type d’objet</label><br>
                <select class="add-item-iputs" name="pets" id="items-type-selectbox">
                    <option value="">--veuillez choisir un type--</option>
                    <option value="dog">Dog</option>
                    <option value="cat">Cat</option>
                    <option value="hamster">Hamster</option>
                    <option value="parrot">Parrot</option>
                    <option value="spider">Spider</option>
                    <option value="goldfish">Goldfish</option>
                </select>
                <button id="add-new-item-type-btn"> <i class="fa-solid fa-plus"></i> </button>
            </div>
            
            <div>
                <label for="availability">Plage horaire</label><br>
                <input  class="add-item-iputs" type="text" id="availability" name="availability">
            </div>

            
            <div id="add-item-description">
                <label for="itemDescription">Description</label><br>
                <input  class="" type="text" id="itemDescription" name="itemDescription">
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
   
</section>
`;

const AddItemPage = () => {
   
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = addItem;

    let addItemButton = document.getElementById("addItemButton");
    let addItemType = document.getElementById("add-item-type");
    let addItemTypeBtn = document.getElementById("add-new-item-type-btn");
    let popUp = document.getElementById("add-item-pop-up");
    let removePopUp = document.getElementById("cancel-add-item-type");

    removePopUp.addEventListener("click",function(e){
    
        popUp.style.display="none";
      
    })
    addItemTypeBtn.addEventListener("click",function(e){
      e.preventDefault();
      
      popUp.style.display="flex";
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
        
        const rep  = await response.json(); // json() returns a promise => we wait for the data
  
        console.log(rep);
        popUp.style.display="none";
        
       
      } catch (error) {
        console.error("LoginPage::error: ", error);
      }

    })

    addItemButton.addEventListener("click",async function(e){
      
        try {
            const options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                description: "objet 6",
                urlPicture: "www.object.com",
                itemtype: "test",
                timeSlot: "12345"
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
            
            const rep  = await response.json(); // json() returns a promise => we wait for the data
      
            console.log(rep);
            
           
          } catch (error) {
            console.error("LoginPage::error: ", error);
          }
        
    })
  
  };
  
  export default AddItemPage;