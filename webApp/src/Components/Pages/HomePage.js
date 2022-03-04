import { isJwtExpired } from 'jwt-check-expiration';
import { Redirect } from "../Router/Router";
import Logout from '../Logout/Logout';
import { getSessionObject,setSessionObject,removeSessionObject } from "../../utils/session";
/**
 * Render the LoginPage
 */
const home = `
<div>
  <p> Hello world</p>
</div>
`;

const HomePage = async () => { 
    let accessToken = getSessionObject("accessToken");
    if (!accessToken){
      return Redirect("/login");
    }
    let remeberMe = getSessionObject("remeberMe");
    // si son refreshToken a expiré, il faut le déconnecté (implémenter une page de deconnexion)
    if(isJwtExpired(accessToken)){
      let refreshToken = getSessionObject("tokenRefresh");

        if(remeberMe && !isJwtExpired(refreshToken)){
          
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

              const tokenRefresh = await response.text(); // json() returns a promise => we wait for the data
              console.log(tokenRefresh);
              setSessionObject("accessToken", tokenRefresh);
              // call the HomePage via the Router
            } catch (error) {
              console.error("LoginPage::error: ", error);
            }
          }else {
            return Redirect("/logout")
          }
    }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;
  
  
  
};


export default HomePage;
