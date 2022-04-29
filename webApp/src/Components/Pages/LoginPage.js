import { Redirect } from "../Router/Router";
import Navbar from "../Navbar/Navbar";

import { setSessionObject,getSessionObject } from "../../utils/session";
/** 
 * Render the LoginPage
 */
const affichage = `
<div id="triangle"> </div>

<section id="section-connection">
  <div id="connection-box">
    <form action="#" id="form">
      <h2> Connectez-vous !</h2>
      <span id="error"></span>
      <p><input id="pseudo" type="text" placeholder="Pseudo"></p>
      <p><input  id="password" type="password" placeholder="Password"></p>
      <p> se souvenir de moi <input type="checkbox" id="rememberMe" name="rememberMe"></p>
      <input type="submit" id="btnSubmit" value="se connecter">
    </form>
  <div>
</section>
`;

const LoginPage = () => {
  let userToken = getSessionObject("accessToken");
  // if the user is already connect we redirect him to the homepage
  if (userToken){
     return Redirect("/");
    }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = affichage;

  const form = document.getElementById("form");

  form.addEventListener("submit",onSubmit);
  // try to log the user
  async function onSubmit(e) {
    e.preventDefault();
    const username = document.getElementById("pseudo");
    const password = document.getElementById("password");
    const rememberMe = document.getElementById("rememberMe");


    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          username: username.value,
          password: password.value,
          rememberMe:rememberMe.checked
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch("/api/auths/login", options); // fetch return a promise => we wait for the response
      
       if (!response.ok) {
        response.text().then((result)=>{
          document.getElementById("error").innerText=result;
        })
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const user = await response.json(); // json() returns a promise => we wait for the data

      // save the user into the localStorage
      setSessionObject("userId", user.id );
      setSessionObject("userPseudo", user.username);
      setSessionObject("remeberMe", user.rememberMe);
      setSessionObject("accessToken", user.accessToken);
      setSessionObject("role", user.role);
      if(rememberMe.checked){
        setSessionObject("tokenRefresh", user.tokenRefresh);
      }

      // call the HomePage via the Router
      Navbar();
      Redirect("/");
    } catch (error) {
      
      console.error("LoginPage::error: ", error);
    }

  }
};


export default LoginPage;
