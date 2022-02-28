import { isJwtExpired } from 'jwt-check-expiration';
import { Redirect } from "../Router/Router";


import { getSessionObject } from "../../utils/session";
/**
 * Render the LoginPage
 */
const home = `
<div>
  <p> Hello world</p>
</div>
`;

const HomePage = () => { 
    let user = getSessionObject("user");
    if( isJwtExpired(user.token)){
        if(user.rememberMe){
            
        }
        
    }
  if (!user) return Redirect("/login");
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;
  
  
  
};


export default HomePage;
