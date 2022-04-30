// When using Bootstrap to style components, the CSS is imported in index.js
// However, the JS has still to be loaded for each Bootstrap's component that needs it.
// Here, because our JS component 'Navbar' has the same name as Navbar Bootstrap's component
// we change the name of the imported Bootstrap's 'Navbar' component
import { getSessionObject, VerifyUser } from "../../utils/session";
import defaultItemImg from "../../img/image_not_available.png";
import logo from "../../img/logo.svg";
import { Redirect } from "../Router/Router";
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

  let navbar = "";
  if (accesToken && isAdmin == "admin") {
    navbar = `
    <nav>
    <div id="navigation" >
       <div id="menu">
          <img src =" ${logo}" style = "height : 90px; position : relative;" id = "logoImg"> </img>
          <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/monProfil"> Profil </a></div>
          <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/myitems"> Mes offres </a></div>
          <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/additem"> Offre + </a></div>
          <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/userhandeler"> Utilisateurs </a></div>
          <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/memberList"> Membres </a></div>
       </div>
       <div id="nav-connection">
          <div id="icon-bell"><i class="fa-solid fa-bell"></i></div>
          <div class="notifications" id="box"></div>
          <div id="deconnection"> <a class="nav-item" href="#" data-uri="/logout"> Se deconnecter </a>  </div>
       </div>
    </div>
 </nav>
  `;
  } else {
    if (accesToken) {
      navbar = `
      <nav>
      <div id="navigation">
         <div id="menu">
            <img src =" ${logo}" id = "logoImg"> </img>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/monProfil"> Mon profil </a></div>
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
    } else {
      navbar = `
    <nav>
      <div id="navigation">
         <img src =" ${logo}" style = "left: 50px;"id = "logoImg"> </img>
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
  if (bellIconDiv) {
    bellIconDiv.addEventListener("click", async () => {
      if (!isClicked) {
        let notificationsList = await getNotificationList(accesToken, false);
        await createNotification(notificationsList, boxDiv, false);
        if (notificationsList.length > 0) {
          await updateNotifNotViewed(accesToken);
        }
        isClicked = true;
      } else {
        boxDiv.innerHTML = "";
        isClicked = false;
      }
    });
  }
  const image = document.getElementById("logoImg");
  image.onclick = () => {
    Redirect("/");
  };
};
async function getNotificationList(accesToken, isAll) {
  try {
    var options = {
      method: "GET",
      headers: {
        token: accesToken,
      },
    };
    const response = await fetch(
      "/api/members/notifications/" +
        getSessionObject("userId") +
        "?all=" +
        isAll,
      options
    );
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    return await response.json();
  } catch (error) {}
}
async function createNotification(notificationsList, boxDiv, isALl) {
  notificationsList.forEach(async (item) => {
    let notificationItemDiv = document.createElement("div");
    let textDiv = document.createElement("div");
    let h4ItemType = document.createElement("h4");
    let pDescription = document.createElement("p");
    let itemImg = document.createElement("img");

    notificationItemDiv.classList = "notifications-item";
    textDiv.classList = "text";

    getImg(item.itemId, itemImg);
    h4ItemType.innerHTML = item.itemType;
    pDescription.innerHTML = item.txt;

    notificationItemDiv.appendChild(itemImg);
    textDiv.appendChild(h4ItemType);
    textDiv.appendChild(pDescription);
    notificationItemDiv.appendChild(textDiv);
    boxDiv.appendChild(notificationItemDiv);
  });

  let getAllNotifDiv = document.createElement("div");
  let txtAllNotifDiv = document.createElement("div");
  let h4GetAll = document.createElement("h4");

  h4GetAll.id = "h4GetAllOrNotNotif";
  getAllNotifDiv.id = "all-notif";
  txtAllNotifDiv.id = "txt-all-notif-div";
  h4GetAll.innerHTML = !isALl
    ? "Afficher toutes les notifications"
    : "Afficher les notifications non vues";
  txtAllNotifDiv.appendChild(h4GetAll);
  getAllNotifDiv.appendChild(txtAllNotifDiv);
  boxDiv.appendChild(getAllNotifDiv);
  boxDiv.style.opacity = "1";

  document.getElementById("all-notif").addEventListener("click", async () => {
    // need to do all this getSessionObjet bcs in other case, it is null ...
    if (
      document.getElementById("h4GetAllOrNotNotif").innerHTML ==
      "Afficher toutes les notifications"
    ) {
      let token = getSessionObject("accessToken");
      boxDiv.innerHTML = "";
      let notificationsList = await getNotificationList(token, true);

      await createNotification(notificationsList, boxDiv, true);
      document.getElementById("h4GetAllOrNotNotif").innerHTML =
        "Afficher les notifications non vues";
    } else {
      let token = getSessionObject("accessToken");
      boxDiv.innerHTML = "";
      let notificationsList = await getNotificationList(token, false);
      await createNotification(notificationsList, boxDiv, false);
      if (notificationsList.length > 0) {
        await updateNotifNotViewed(token);
      }
      document.getElementById("h4GetAllOrNotNotif").innerHTML =
        "Afficher toutes les notifications";
    }
  });
}
async function updateNotifNotViewed(accesToken) {
  try {
    var options = {
      method: "PUT",
      headers: {
        token: accesToken,
      },
    };
    const response = await fetch(
      "/api/members/notifications/update/notViewed/" +
        getSessionObject("userId"),
      options
    );
    if (response.status == 307) {
      await VerifyUser();
      document.location.reload();
    }
    await response.json();
  } catch (error) {}
}

async function getImg(itemId, itemImg) {
  try {
    const response = await fetch("/api/items/picture/" + itemId);
    if (!response.ok) {
      itemImg.src = defaultItemImg;
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    if (response.ok) {
      const imageBlob = await response.blob();
      const imageObjectURL = URL.createObjectURL(imageBlob);
      itemImg.src = imageObjectURL;
    }
  } catch (error) {
    console.log(error);
  }
}
export default Navbar;
