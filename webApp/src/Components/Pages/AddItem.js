const addItem = `
<section id="add-item-page">
    <form id="add-item-form">
     
            <div >
                <label for="pet-select">Type d’objet</label><br>
                <select class="add-item-iputs" name="pets" id="items-type-selectbox">
                    <option value="">--veuillez choisir un type--</option>
                    
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
    getItemsTypes();
    let addItemButton = document.getElementById("addItemButton");
    let addItemType = document.getElementById("add-item-type");
    let addItemTypeBtn = document.getElementById("add-new-item-type-btn");
    let popUp = document.getElementById("add-item-pop-up");
    let removePopUp = document.getElementById("cancel-add-item-type");

    removePopUp.addEventListener("click",function(){
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

    addItemButton.addEventListener("click",async function(e){
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
            }
            
            const itemType  = await response.json(); // json() returns a promise => we wait for the data
      
            console.log(itemType);

           
          } catch (error) {
            console.error("LoginPage::error: ", error);
          }
        
    })

    async function getItemsTypes(){

      let selectBox = document.getElementById("items-type-selectbox");

      try{
        const response = await fetch("/api/itemsType/getAll"); // fetch return a promise => we wait for the response   

        if(!response.ok){
            
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

      let option = document.createElement("option");
      option.value="table";
      option.innerText="table en boite";
     
      option.value="table";
      selectBox.appendChild(option);
      
    }
    /*
    const getItemsTypes = () => {
     
      
      
       
    }
     */
  
  };
  
  export default AddItemPage;