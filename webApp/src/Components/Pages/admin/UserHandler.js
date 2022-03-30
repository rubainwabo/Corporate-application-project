import { getSessionObject } from "../../../utils/session";
import { Redirect } from "../../Router/Router";

const userHandler = `
<section id="user-handler-page">
<form name="formA">
   <div id="switch-list-users">
        <label for="on-hold">En attente</label>
        <input value="waiting" type="radio" name="user-state" id="on-hold" checked >
        <label for="refusal">refusé</label>
        <input value="denied" type="radio"  name="user-state" id="refusal" > 
   </div>
   </form>
   <div id="test">
   </div>
</section>
`;

const UserHandler = () => {
    let accesToken = getSessionObject("accessToken");
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = userHandler;
    // first request for waiting user
    request(accesToken,"waiting");
    var radios = document.forms["formA"].elements["user-state"];
    radios.forEach(element => {
        element.addEventListener("click",async () => {
            // call the api on every request
            request(accesToken,element.value);
        });
    })
  };

  async function request (accesToken,state) {
    console.log(accesToken);
    try {
        const option = {
            headers: {
              "token":accesToken
        }
      }
        // hide data to inform if the pizza menu is already printed
        const response = await fetch("/api/auths/list?state="+state,option); // fetch return a promise => we wait for the response   
     if(!response.ok){
        return Redirect("/");
     }
     const items = await response.json();
     var test = document.getElementById("test");
     test.innerHTML="";
     items.forEach((e) => {
         console.log(e)
         const divUserHandler = document.createElement("div");
         const pFirstName = document.createElement("p");
         const pLastName = document.createElement("p");
         const divIsAdminBox = document.createElement("div");
         const spanAdminBox = document.createElement("span");
         const br = document.createElement("br");
         const inputCheckBox = document.createElement("input");
         const divValid = document.createElement("div");
         const divDenied = document.createElement("div");
         const validBtn = document.createElement("button");
         const deniedBtn = document.createElement("button");
    
         divUserHandler.classList="user-to-handle";
         // checkbox admin + last name and first name
         divIsAdminBox.classList="is-admin-box";
         pFirstName.innerHTML=e.firstName
         pLastName.innerHTML = e.lastName
         divUserHandler.appendChild(pFirstName);
         divUserHandler.appendChild(pLastName);
         divUserHandler.appendChild(divIsAdminBox)
         spanAdminBox.innerHTML = "admin"
         divIsAdminBox.appendChild(spanAdminBox);
         divIsAdminBox.appendChild(br);
         inputCheckBox.id="is-admin";
         inputCheckBox.type="checkbox"
         divIsAdminBox.appendChild(inputCheckBox);

         // append div with btnValid
         deniedBtn.innerHTML="refusé"
         validBtn.innerHTML="accepté"

         divUserHandler.appendChild(divValid)
         divValid.appendChild(validBtn)
         // append div with btnDenied
         divUserHandler.appendChild(divDenied)
         divDenied.appendChild(deniedBtn)
         deniedBtn.addEventListener("click",() => {
             addOrRefuse(e.id,"denied","raison de votre refus statique",accesToken)});
         validBtn.addEventListener("click", () => {
             addOrRefuse(e.id,"valid","",accesToken)});
         test.appendChild(divUserHandler)
     })
    } catch (error) {
    } 
  }
  async function  addOrRefuse(id,state,rsnRefusal,accesToken){
    try {
        const admin = document.getElementById("is-admin");
        let body1 = rsnRefusal !=""? JSON.stringify( {
            "change_id" : id,
              "state" : state,
              "admin":admin.checked,
              "refusalReason":rsnRefusal
        }) : JSON.stringify( {
            "change_id" : id,
              "state" : state,
              "admin":admin.checked
        }) 
        const options = {
          method: "POST", // *GET, POST, PUT, DELETE, etc.
          body: body1,
          headers: {
            "token":accesToken,
            "Content-Type": "application/json"
        },
        };
        console.log(options.body)
        const response = await fetch("/api/auths/changeState", options); // fetch return a promise => we wait for the response
  
        if (!response.ok) {
          response.text().then((result)=>{
            document.getElementById("error").innerText=result;
          })
          throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
          );
        }
        const user = await response.json(); // json() returns a promise => we wait for the data
        console.log("voici ce que je cherche : " + user);
        Redirect("/userhandeler");
    } catch (error) {
        console.error("LoginPage::error: ", error);
      }
  }
  export default UserHandler;