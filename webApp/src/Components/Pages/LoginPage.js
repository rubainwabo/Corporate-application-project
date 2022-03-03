import { Redirect } from "../Router/Router";

import { setSessionObject } from "../../utils/session";
/**
 * Render the LoginPage
 */
const affichage = `
<div>
  <form action="#" id="form">
    <input id="pseudo" type="text" placeholder="Pseudo">
    <input  id="password" type="text" placeholder="Password">
    <input type="checkbox" id="rememberMe" name="rememberMe">
    <input type="submit" id="btnSubmit">
  </form>
</div>
`;

const LoginPage = () => { 
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = affichage;
  
  const form = document.getElementById("form");

  form.addEventListener("submit",onSubmit);

  async function onSubmit(e) {
    e.preventDefault();
    const username = document.getElementById("pseudo");
    const password = document.getElementById("password");
    const rememberMe = document.getElementById("rememberMe");
    
    console.log(rememberMe.checked);

    
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          pseudo: username.value,
          password: password.value,
          rememberMe:rememberMe.checked
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch("/api/auths/login", options); // fetch return a promise => we wait for the response

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const user = await response.json(); // json() returns a promise => we wait for the data
      console.log("user authenticated", user);
      // save the user into the localStorage
      setSessionObject("user", user);


      // call the HomePage via the Router
      Redirect("/");
    } catch (error) {
      console.error("LoginPage::error: ", error);
    }
    
  }
};


export default LoginPage;
