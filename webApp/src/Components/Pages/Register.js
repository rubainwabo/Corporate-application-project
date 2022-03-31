
import { Redirect } from "../Router/Router";
import Navbar from "../Navbar/Navbar";
import { setSessionObject,getSessionObject } from "../../utils/session";

const register = `
<section id="register-page">
    
    <h2> Inscrivez-vous !</h2>
        <form action="#" id="register-form">
        <span id="error"></span>
            <div>
                <input id ='username-rgst' type='text' placeholder='Username' required="required">
            </div>
            <div>
                <input id ='password-rgst' type='password' placeholder='Password' required="required">
                <input id ='confirm-pass-rgst' type='password' placeholder='Confirm password'>
            </div>
            <div>
                <input id ='last-name-rgst' type='text' placeholder='Last name'>
                <input id ='first-name-rgst' type='text' placeholder='First name'>
            </div>
            <div>
                <input id ='city-rgst' type='text' placeholder='City'>
            </div>
            <div>
                <input id ='street-rgst' type='text' placeholder='Street'>
                <input id ='number-of-building-rgst' type='number' placeholder='Number of building'>
                <input id ='unit-number' type='number' placeholder='Unit number'>
            </div>
            <div>
                <input id ='post-code-rgst' type='number' placeholder='Post code'>
            </div>

            <div id="user-decision">
                <div id="form-button-box">
                    <input type="submit" value="Envoyer"> 
                </div>
                
            </div>         
        </form>
   

    <div id="register-pop-up"> 
        <p>Votre incription a été réalisé</p>
        <button id="register-done"> Terminer </button>
    </div>

</section>
`;

const Register = () => {
   
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = register;

    let registerForm = document.getElementById("register-form");

    registerForm.addEventListener("submit", async function(e){
        e.preventDefault();

        let username = document.getElementById("username-rgst").value;
        let lastName = document.getElementById("last-name-rgst").value;
        let firstName= document.getElementById("first-name-rgst").value;
        let password= document.getElementById("password-rgst").value;
        let street = document.getElementById("street-rgst").value;
        let buildingNumber = document.getElementById("number-of-building-rgst").value;
        let unitNumber = document.getElementById("unit-number").value;
        let postCode = document.getElementById("post-code-rgst").value;
        let city = document.getElementById("city-rgst").value;
        let urlPhoto = "url_photo";
        let registerDone = document.getElementById("register-done");

        

        registerDone.addEventListener("click",function(e){
          e.preventDefault();
          Redirect("/");
        })
       
        try {
            const options = {
              method: "POST", // *GET, POST, PUT, DELETE, etc.
              body: JSON.stringify({
                userName: username,
                lastName: lastName,
                firstName: firstName,
                password: password,
                street: street,
                buildingNumber: buildingNumber,
                unitNumber: unitNumber,
                postCode: postCode,
                city: city,
                urlPhoto: urlPhoto
              }), // body data type must match "Content-Type" header
              headers: {
                "Content-Type": "application/json",
              },
            };
      
            const response = await fetch("/api/auths/register", options); // fetch return a promise => we wait for the response
      
            if (!response.ok) {
              response.text().then((result)=>{
                document.getElementById("error").innerText=result;
              })
              throw new Error(
                "fetch error : " + response.status + " : " + response.statusText
              );
            }
            const userId = await response.json(); // json() returns a promise => we wait for the data
            
            document.getElementById("register-pop-up").style.display="flex";  
            // call the HomePage via the Router

            
          } catch (error) {
            console.error("LoginPage::error: ", error);
          }
    })

  };
  
export default Register;