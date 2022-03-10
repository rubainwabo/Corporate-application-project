import { isJwtExpired } from 'jwt-check-expiration';
import { Redirect } from "../Router/Router";
import Logout from '../Logout/Logout';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";
/**
 * Render the LoginPage
 */
const home = `
<div>
  <p> Bienvue sur Donnamis </p>
</div>
`;

const HomePage = async () => {
    let accessToken = getSessionObject("accessToken");

    // if the user has a accesToken and the token is expired
    if(((accessToken) && isJwtExpired(accessToken))){
      let refreshToken = getSessionObject("tokenRefresh");
      // if his not in possession of a refresh
      if (!refreshToken) {
       return  Redirect("/logout");
      }
      // try to get the user new tokens if his refresh token is not expired
        if(!isJwtExpired(refreshToken)){
          console.log("voici le refrehs bg : " +refreshToken)
            try {
              const options = {
                method: "POST", // *GET, POST, PUT, DELETE, etc.
                body: JSON.stringify({
                  refreshToken: refreshToken,
                }), // body data type must match "Content-Type" header
                headers: {
                  "Content-Type": "application/json",
                },
              };
              const response = await fetch("/api/auths/refreshToken", options); // fetch return a promise => we wait for the response

              if (!response.ok) {
                throw new Error(
                  "fetch error : " + response.status + " : " + response.statusText
                );
              }

              const tokens = await response.json(); // json() returns a promise => we wait for the data
              console.log(tokens);
              setSessionObject("accessToken", tokens.accessToken);
              setSessionObject("tokenRefresh",tokens.tokenRefresh);
              // call the HomePage via the Router
            } catch (error) {
              console.error("LoginPage::error: ", error);
            }
          }
    }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

};


export default HomePage;
