import { Redirect } from "../../Router/Router";
import { getSessionObject,setSessionObject,removeSessionObject } from "../../../utils/session";
import itemImg from '../../../img/wheelbarrows-4566619_640.jpg';
import { VerifyUser } from "../../../utils/session";
import defaultItemImg from '../../../img/image_not_available.png';

/**
 * Render the MemberList
 */
const pageContaint = `
<section id="home-page">
<div id="home-page-navigation">
    <h2 id="home-page-title"> Listes des membres</h2>
</div>
<div id="member-list-checkbox-container">
    <div id="member-list-checkbox-in">
        <div class="checkbox-member-list">
            <label for="name">Name</label>
            <input type="checkbox" id="user-name-member-list" name="name" >
        </div>
        <div class="checkbox-member-list">
            <label for="city">Ville</label>
            <input type="checkbox" id="city-member-list" name="city">
        </div>
        <div class="checkbox-member-list">
            <label for="post-code">Code postal</label>
            <input type="checkbox" id="post-code-member-list" name="post-code">
        </div>
        <form autocomplete="off">
        <div class="autocomplete" id="container-research-member-list">
            <input type="text" id="my-input-member-list">
                <div id="container-i-member-list">
                    <i id="fa-research-member-list" class="fas fa-search"></i>
                </div>
        </div>
        </form>
    </div>
</div>
  <section id="member-list-page">
    <div id="member-list">
    
    </div>
    </section>
  </section>
`;

const MemberList =  async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = pageContaint;
  
  document.getElementById("home-page").style.height="";
  let token = getSessionObject("accessToken");
  let searchInput = document.getElementById("my-input-member-list")
  getAllMemberByFilter(searchInput,token);
  autocomplete(searchInput,token);
  document.addEventListener("click",(e) => {
    // click out the div and delete it TODO !
    let myPopUpDiv = document.getElementById("member-list-all-my-item");
    if (myPopUpDiv){
      if (!myPopUpDiv.contains(e.target)) {
        myPopUpDiv.remove();
      }
    }
  })
  document.getElementById("container-i-member-list").addEventListener("click",async() => {
    getAllMemberByFilter(searchInput,token)
  })
};

async function getAllMemberByFilter(searchInput,token){
  let inputVal = searchInput.value;
  let usernameCheck = document.getElementById("user-name-member-list").checked;
  let cityCheck = document.getElementById("city-member-list").checked;
  let postCodeCheck = document.getElementById("post-code-member-list").checked;
  let username,city,postCode;
  username = usernameCheck ? inputVal : "";
  city = cityCheck ? inputVal : "";
  postCode = postCodeCheck ? inputVal : "";
  try { 

      var options = { method: 'GET',
                      headers: {"token" : token}
                  };   
      const response = await fetch("/api/admins/list/filtred?name="+username+"&"+"city="+city+"&"+"postCode="+postCode, options);    
  
      if (response.status == 307) {
        await VerifyUser(); 
        document.location.reload();
    }
    const filtredUserList = await response.json();
    var memberList = document.getElementById("member-list");
    memberList.innerHTML="";
    filtredUserList.forEach((e)=>{
    const divUserHandler = document.createElement("div");
    const pFirstName = document.createElement("span");
    const pLastName = document.createElement("span");
    const username = document.createElement("span");
    const state = document.createElement("span");
    const role = document.createElement("span");
    const phoneNumber = document.createElement("span");
    const nb_of_item_not_taken = document.createElement("span");
    const nb_of_item_offered = document.createElement("span");
    const nb_of_item_gifted = document.createElement("span");
    const nb_of_item_received = document.createElement("span");
    const divUserHandlerIn = document.createElement("div")


    divUserHandler.classList = "user-to-handle";
    pFirstName.classList="col-1"
    pLastName.classList="col-1"
    username.classList="col-1"
    state.classList="col-1"
    role.classList="col-1"
    phoneNumber.classList="col-1"
    nb_of_item_not_taken.classList="col-1"
    nb_of_item_offered.classList="col-1"
    nb_of_item_gifted.classList="col-1"
    nb_of_item_received.classList="col-1"
    
    divUserHandler.id = e.id;
    pFirstName.innerHTML = e.firstName
    pLastName.innerHTML = e.lastName
    username.innerHTML = e.userName
    state.innerHTML=e.state
    role.innerHTML=e.role
    phoneNumber.innerHTML= e.phoneNumber == null ? "/" : e.phoneNumber
    nb_of_item_not_taken.innerHTML = e.nbrItemNotTaken + " object non pris"
    nb_of_item_offered.innerHTML = e.nbrItemOffered  + " offres"
    nb_of_item_gifted.innerHTML = e.nbrGiftenItems + " donnés"
    nb_of_item_received.innerHTML = e.nbrItemReceived + " réceptions"

    divUserHandler.appendChild(pFirstName);
    divUserHandler.appendChild(pLastName);
    divUserHandler.appendChild(username);
    divUserHandler.appendChild(state);
    divUserHandler.appendChild(role);
    divUserHandler.appendChild(phoneNumber);
    divUserHandler.appendChild(nb_of_item_not_taken);
    divUserHandler.appendChild(nb_of_item_offered);
    divUserHandler.appendChild(nb_of_item_gifted);
    divUserHandler.appendChild(nb_of_item_received);
    
    divUserHandler.addEventListener("click",async  () => {
      const divMyItems = document.createElement("div"); 
      const left = document.createElement("div");
      const right = document.createElement("div");
      const divLeftTitle = document.createElement("div");
      const divRightTitle = document.createElement("div");
      const pLTitle = document.createElement("H3");
      const pRTitle = document.createElement("H3");

      divMyItems.id="member-list-all-my-item";
      divLeftTitle.classList="member-list-pop-up-items";
      divRightTitle.classList="member-list-pop-up-items";
      left.id="member-list-left"
      right.id="member-list-right"

      pLTitle.innerHTML="Objects offerts"
      pRTitle.innerHTML="Objects reçus"

      // fetch offered items of the user we clicked on him
      let offeredItems = await getAllItemsByItemCondition('offered',token,divUserHandler.id,true);
      let receptedItems =  await getAllItemsByItemCondition('gifted',token,divUserHandler.id,false);
      divLeftTitle.appendChild(pLTitle);
      divRightTitle.appendChild(pRTitle);
      left.appendChild(divLeftTitle)
      right.appendChild(divRightTitle)
      let divOfferedItem = document.createElement("div");
      let divReceptedItem = document.createElement("div");
      divReceptedItem.classList="member-list-offered-items"
      divOfferedItem.classList="member-list-offered-items";
      offeredItems.forEach((item) => {
        membersItemsList(item,left,divOfferedItem)
      })
      receptedItems.forEach((item) => {
        membersItemsList(item,right,divReceptedItem)
      })
      divMyItems.appendChild(left)
      divMyItems.appendChild(right)
      document.querySelector("#page").appendChild(divMyItems);
      
    })
    memberList.appendChild(divUserHandler)
  })    
    
  } catch (error) {
  }
}
function membersItemsList(item,container,divItems){
        
        let divSingleItem = document.createElement("div");
        let pItemTypeTitle = document.createElement("h6");
        let pDescriptionTitle = document.createElement("h6");
        let pItemType= document.createElement("p");
        let pDescription = document.createElement("p");
        let leftDiv = document.createElement("div");
        let rightDiv = document.createElement("div");
        let insideRightDiv = document.createElement("div")


        pItemTypeTitle.innerHTML="Type de l'objet :"
        pDescriptionTitle.innerHTML="Description :" 
        item.description = item.description.substring(0, 70) 
        item.description += item.description.length >= 70 ? " ..." : ""; 
        pDescription.innerHTML=item.description
        pItemType.innerHTML=item.itemtype;
        pItemTypeTitle.style.paddingTop="10px"
        let img = document.createElement("img");
        img.classList="member-list-item-img";
        
        getImg(item.id,img);
        leftDiv.classList="member-list-left-items";
        rightDiv.classList="member-list-right-items";
        divSingleItem.classList="member-list-container-item";

        insideRightDiv.appendChild(pItemTypeTitle);
        insideRightDiv.appendChild(pItemType);
        insideRightDiv.appendChild(pDescriptionTitle);
        insideRightDiv.appendChild(pDescription);
        rightDiv.appendChild(insideRightDiv)
        leftDiv.appendChild(img);
        divSingleItem.appendChild(leftDiv);
        divSingleItem.appendChild(rightDiv);
        divItems.appendChild(divSingleItem);
        container.appendChild(divItems);
}
function autocomplete(inp,token) {
    /*the autocomplete function takes two arguments,
    the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", async function(e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) { return false;}
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        try {
            var options = { method: 'GET',
                            headers: {"token" : token}
                        };   

            const response = await fetch("/api/admins/autocompleteList?value="+val.toUpperCase(), options);    
            if (response.status == 307) {
              await VerifyUser(); 
              document.location.reload();
            }
          const autocompleteList = await response.json();
          autocompleteList.forEach((item)=>{
            /*create a DIV element for each matching element:*/
            b = document.createElement("DIV");
            /*make the matching letters bold:*/
            b.innerHTML = "<strong>" + item.substr(0, val.length) + "</strong>";
            b.innerHTML += item.substr(val.length);
            /*insert a input field that will hold the current array item's value:*/
            b.innerHTML += "<input type='hidden' value='" + item + "'>";
            /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function(e) {
                /*insert the value for the autocomplete text field:*/
                inp.value = this.getElementsByTagName("input")[0].value;
                /*close the list of autocompleted values,
                (or any other open lists of autocompleted values:*/
                closeAllLists();
            });
            a.appendChild(b);
          
          });
        } catch (error) {

        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function(e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
          /*If the arrow DOWN key is pressed,
          increase the currentFocus variable:*/
          currentFocus++;
          /*and and make the current item more visible:*/
          addActive(x);
        } else if (e.keyCode == 38) { //up
          /*If the arrow UP key is pressed,
          decrease the currentFocus variable:*/
          currentFocus--;
          /*and and make the current item more visible:*/
          addActive(x);
        } else if (e.keyCode == 13) {
          /*If the ENTER key is pressed, prevent the form from being submitted,*/
          e.preventDefault();
          if (currentFocus > -1) {
            /*and simulate a click on the "active" item:*/
            if (x) x[currentFocus].click();
          }
        }
    });
    function addActive(x) {
      /*a function to classify an item as "active":*/
      if (!x) return false;
      /*start by removing the "active" class on all items:*/
      removeActive(x);
      if (currentFocus >= x.length) currentFocus = 0;
      if (currentFocus < 0) currentFocus = (x.length - 1);
      /*add class "autocomplete-active":*/
      x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
      /*a function to remove the "active" class from all autocomplete items:*/
      for (var i = 0; i < x.length; i++) {
        x[i].classList.remove("autocomplete-active");
      }
    }
    function closeAllLists(elmnt) {
      /*close all autocomplete lists in the document,
      except the one passed as an argument:*/
      var x = document.getElementsByClassName("autocomplete-items");
      for (var i = 0; i < x.length; i++) {
        if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
}
async function getAllItemsByItemCondition(itemCondition,token,id,isOfferor) {
  try { 
    var options = { method: 'GET',
                    headers: {"token" : token}
                };   
    const response = await fetch("/api/admins/memberListItems/"+id+"?itemCondition="+itemCondition+"&isOfferor="+isOfferor, options);    

    if (response.status == 307) {
      await VerifyUser(); 
      document.location.reload();
    }
  return await response.json();

  } catch (error) {
}   
}
async function getImg(itemId,itemImg){
  try{
    const response = await fetch("/api/items/picture/"+itemId);
    if (!response.ok) {
      itemImg.src=defaultItemImg;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      )
    }
    if(response.ok){
      const imageBlob = await response.blob();
      const imageObjectURL = URL.createObjectURL(imageBlob);
      itemImg.src= imageObjectURL
    }
    }catch(error){
      console.log(error)
    }
}
export default MemberList;
