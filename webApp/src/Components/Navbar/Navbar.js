// When using Bootstrap to style components, the CSS is imported in index.js
// However, the JS has still to be loaded for each Bootstrap's component that needs it.
// Here, because our JS component 'Navbar' has the same name as Navbar Bootstrap's component
// we change the name of the imported Bootstrap's 'Navbar' component
import { Navbar as BootstrapNavbar} from "bootstrap";
import { getSessionObject, VerifyUser } from "../../utils/session";

import logo from "../../img/logo.svg"

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let accesToken = getSessionObject("accessToken");
  let isAdmin = getSessionObject("role");
  /*
  let navbar = `
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
          <a class="navbar-brand" href="#">Add your brand here</a>
          <button
            class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <a class="nav-link" aria-current="page" href="#" data-uri="/">Home</a>
              </li>                   
            </ul>
          </div>
        </div>
      </nav>
  `;  
*/
let navbar = "";
if(accesToken && isAdmin == "admin"){
  navbar = `
      <nav>
        <div id="navigation" >
          <div id="menu">
          <img src =" ${logo}" style = "height : 90px; position : relative;" id = "logoImg"> </img>
            <div id="logo"> <a class="nav-item menu-item" href="#"  data-uri="/"> Donnamis </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/monProfile"> Mon profile </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/myitems"> Mes offres </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/additem"> Nouvelles offre + </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/userhandeler"> liste des utilisateurs </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/memberList"> liste des membres </a></div>
          </div>
        
          <div id="icon-bell"><i class="fa-solid fa-bell"></i></div>
          <div class="notifications" id="box"></div>

          <div id="nav-connection"> 
            <div id="deconnection"> <a class="nav-item" href="#" data-uri="/logout"> Se deconnecter </a>  </div>
          </div>
        </div>
      </nav>
  `;  
}else {
  if (accesToken){
   navbar = `
   <nav>
   <div id="navigation">
     <div id="menu">
     <img src =" ${logo}" style = "height : 90px; position : relative;" id = "logoImg"> </img>
     <div id="logo"> <a class="nav-item menu-item" href="#"  data-uri="/"> Donnamis </a></div>
       <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/monProfile"> Mon profile </a></div>
       <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/myitems"> Mes offres </a></div>
       <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/additem"> Nouvelles offre + </a></div>
     </div>
  
     <div id="icon-bell"><i class="fa-solid fa-bell"></i></div>
     <div class="notifications" id="box"></div>

     <div id="nav-connection"> 
       <div id="deconnection"> <a class="nav-item" href="#" data-uri="/logout"> Se deconnecter </a>  </div>
     </div>
   </div>
 </nav>
  `; 
}else {
  navbar=
  ` <nav>
        <div id="navigation">
          <img src =" ${logo}" style = "height : 90px; position : relative;"id = "logoImg"> </img>

          <div id="nav-connection"> 
            <div id="connection"> <a class="nav-item" href="#" data-uri="/login"> Se connecter </a> </div>
            <div id="deconnection"> <a class="nav-item" href="#" data-uri="/register"> S'inscrire </a>  </div>
          </div>
        </div>
      </nav>
  `; 
}
}
  navbarWrapper.innerHTML = navbar;
  const bellIconDiv = document.getElementById("icon-bell");
  const boxDiv = document.getElementById("box");
  let isClicked = false;
  if (bellIconDiv){
    bellIconDiv.addEventListener("click", async () => {
      await VerifyUser();
      if (!isClicked){
      let notificationsList = await getNotificationList(accesToken,false)
      await createNotification(notificationsList,boxDiv,false);
      if (notificationsList.length > 0){
        updateNotifNotViewed(accesToken);
      }
      isClicked=true;
    }else {
      boxDiv.innerHTML=""
      isClicked=false;
    }
  });
  };
};
async function getNotificationList(accesToken,isAll){
  try {
    var options = {
       method: 'GET',
      headers: {
        "token" : accesToken
      }
  };   
  const response = await fetch("/api/members/notifications/"+getSessionObject("userId")+"?all="+isAll, options);
  if(!response.ok){
  }
  return await response.json();
}
  catch (error) {
  }
}
async function createNotification(notificationsList,boxDiv,isALl){
  
  notificationsList.forEach((item) => {
    let notificationItemDiv = document.createElement("div");
    let textDiv = document.createElement("div");
    let h4ItemType = document.createElement("h4");
    let pDescription = document.createElement("p")
    let itemImg = document.createElement("img");
  
    notificationItemDiv.classList="notifications-item";
    textDiv.classList="text";
    itemImg.src=logo;
    
    h4ItemType.innerHTML=item.itemType
    pDescription.innerHTML=item.txt
  
    notificationItemDiv.appendChild(itemImg);
    textDiv.appendChild(h4ItemType);
    textDiv.appendChild(pDescription);
    notificationItemDiv.appendChild(textDiv);
    boxDiv.appendChild(notificationItemDiv);
  });

let getAllNotifDiv = document.createElement("div");
let txtAllNotifDiv = document.createElement("div");
let h4GetAll = document.createElement("h4");

h4GetAll.id="h4GetAllOrNotNotif";
getAllNotifDiv.id="all-notif";
txtAllNotifDiv.id="txt-all-notif-div"
h4GetAll.innerHTML= !isALl ? "Afficher toutes les notifications" : "Afficher les notifications non lus"
txtAllNotifDiv.appendChild(h4GetAll);
getAllNotifDiv.appendChild(txtAllNotifDiv);
boxDiv.appendChild(getAllNotifDiv)
boxDiv.style.opacity="1";
document.getElementById("all-notif").addEventListener("click",async  () => {
  // need to do all this getSessionObjet bcs in other case, it is null ...
  if (document.getElementById("h4GetAllOrNotNotif").innerHTML=="Afficher toutes les notifications"){
    let token = getSessionObject("accessToken");
    boxDiv.innerHTML="";
    let notificationsList = await getNotificationList(token,true);
    await createNotification(notificationsList,boxDiv,true);
    document.getElementById("h4GetAllOrNotNotif").innerHTML="Afficher les notifications non lus"
  }else {
    let token = getSessionObject("accessToken");
    boxDiv.innerHTML="";
    let notificationsList = await getNotificationList(token,false);
    await createNotification(notificationsList,boxDiv,false);
    if (notificationsList.length > 0){
      console.log("fdp")
      updateNotifNotViewed(token);
    }
    document.getElementById("h4GetAllOrNotNotif").innerHTML="Afficher toutes les notifications"
  }
})
}
async function updateNotifNotViewed(accesToken) {
  try {
    var options = {
       method: 'PUT',
      headers: {
        "token" : accesToken
      }
  };   
  const response = await fetch("/api/members/notifications/update/notViewed/"+getSessionObject("userId"), options);
  if(!response.ok){
  }
  await response.json();
}
  catch (error) {
  }
}
export default Navbar;
