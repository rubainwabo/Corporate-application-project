import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";

import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
const item = `
<section id="item-page">
  <p id="message"> </p>
    <div id="item-img-description">
        <div id="item-img">
            <img src="${itemImg}">
        </div>
        <div id="item-description">
            <p id="item-description-p"></p>
        </div>
    </div>

    <div id="item-info">
        <p>Type d’objet : <span id="item-type"><span> </p> 
        <p>Offert par : <span id="offeror"><span> </p> 
        <p>Nombres de personnes interessées : <span id="number-interest"><span> </p> 
        <p>Précedentes dates d’offre : <span id="offeror"> <span> </p>   
    </div>

    <div id="item-show-interest">
        <button id="show-interest">Je suis interessé</button>
    </div>

    <div id="add-iterest-pop-up">
      <form id="add-iterest-form">
      <span id="error"></span>
        <div>
          <input type="text" id="availabilities" placeholder="vos heures de disponibilité">
        </div>
        <div>
          <input type="checkbox" id="callMe">
        </div>
        <div id="call-me-box"> 
          <input type="text" id="phone-number" placeholder="numéro" required >
        </div>
        <div>
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
    let showInterest = document.getElementById("show-interest");
    let removePopUp = document.getElementById("cancel-add-iterest");
    let popUp = document.getElementById("add-iterest-pop-up");

    removePopUp.addEventListener("click",function(e){
      e.preventDefault();
        popUp.style.display="none";
    })

    
    showInterest.addEventListener("click",function(e){
      e.preventDefault();
      popUp.style.display="flex";
    })

    try {
        // hide data to inform if the pizza menu is already printed
        const response = await fetch("/api/items/itemDetails/"+id); // fetch return a promise => we wait for the response   
   
     if(!response.ok){
         throw new Error(
             "fetch error : " + response.status + " : " + response.statusText
         )
     }
   
     const item = await response.json();
     
     document.getElementById("item-description-p").innerText=item.description;
     document.getElementById("item-type").innerText=item.itemtype;
     document.getElementById("offeror").innerText=item.offeror;
     document.getElementById("number-interest").innerText=item.numberOfPeopleInterested;
    
     let callMe = document.getElementById("callMe");
     
      callMe.addEventListener("change",function(e){
       e.preventDefault();
       
       if(callMe.checked){
        document.getElementById("call-me-box").style.display="block";
       }else{
        document.getElementById("call-me-box").style.display="none";
       }
       
     });

    console.log(item);
   
     } catch (error) {
        console.error("LoginPage::error: ", error);
     }


     addIterestBtn.addEventListener("click", async function(e){
       e.preventDefault();
        let availabilities = document.getElementById("availabilities").value;
        let callMe = document.getElementById("callMe").checked;
        
        
        try {
          var options;
          if(callMe){
            let phoneNumber = document.getElementById("phone-number");
            options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                availabilities : availabilities,
                callMe :callMe,
                phoneNumber : phoneNumber
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
              },
            };
          }else{
            options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                availabilities : availabilities,
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
              },
            };
          }
              
      
            const response = await fetch("/api/items/addInterest/"+id, options); // fetch return a promise => we wait for the response
      
            if (!response.ok) {
              response.text().then((result)=>{
                document.getElementById("error").innerText=result;
              })
              throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
              );
            }

            console.log("votre interet a été pris en compte")
            document.getElementById("message").innerText="votre interet a été pris en compte";
            popUp.style.display="none";
            
            /*
            const rep  = await response.json(); // json() returns a promise => we wait for the data
      
            console.log(rep);
            */
           
          } catch (error) {
            console.error("LoginPage::error: ", error);
          }
        
    })

    console.log(id);
    
   
    
   
  
};


  function getId() {
    let urlString = window.location.href;
    let paramString = urlString.split('?')[1];
    if(paramString){
        let params_arr = paramString.split('&');
        let pair = params_arr[0].split('=');
        if(pair[1]){
            return parseInt(pair[1]);      
        }else{
            return -1;
        }  
        
    }else{
        return -1;
    }
    
        
    
}
  export default ItemPage;