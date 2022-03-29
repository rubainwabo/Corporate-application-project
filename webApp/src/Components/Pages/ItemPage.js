import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";

import itemImg from '../../img/wheelbarrows-4566619_640.jpg';
const item = `
<section id="item-page">
    <div id="item-img-description">
        <div id="item-img">
            <img src="${itemImg}">
        </div>
        <div id="item-description">
            <p id="item-description-p"></p>
        </div>
    </div>

    <div id="item-info">
        <p>Type d’objet : <span id="item-type"> table <span> </p> 
        <p>Offert par : <span id="offeror"> Daniel_12 <span> </p> 
        <p>Nombres de personnes interessées : <span id="number-interest"> 15 <span> </p> 
        <p>Précedentes dates d’offre : <span id="offeror"> <span> </p>   
    </div>

    <div id="item-show-interest">
        <button id="show-interest">Je suis interessé</button>
    </div>
  
</section>
`;

const ItemPage = async () => {
    let id = getId();

    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = item;

    
    
    let showInterest = document.getElementById("show-interest");

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


    console.log(item);
   
     } catch (error) {
        console.error("LoginPage::error: ", error);
     }


    showInterest.addEventListener("click", async function(e){
        try {
            const options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                availabilities : "2016-03-04 11:30",
                callMe :true,
                phoneNumber : "0471717306"
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
              },
            };
      
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