import { Redirect } from "../../Router/Router";
import Logout from '../../Logout/Logout';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../../utils/session";


/**
 * Render the MemberList
 */
const home = `<section id="home-page">
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
</section>
`;

const MemberList =  () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;
  let token = getSessionObject("accessToken");
  let searchInput = document.getElementById("my-input-member-list")
  autocomplete(searchInput,token);
  document.getElementById("container-i-member-list").addEventListener("click",async() => {
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
    
        if(!response.ok){
        return Redirect("/logout");
      }
      const filtredUserList = await response.json();
      filtredUserList.forEach((user)=>{
        console.log(user)
      });
      
    } catch (error) {

    }
  })
};
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
            if(!response.ok){
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

export default MemberList;
