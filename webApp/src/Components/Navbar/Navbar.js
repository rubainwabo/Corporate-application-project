// When using Bootstrap to style components, the CSS is imported in index.js
// However, the JS has still to be loaded for each Bootstrap's component that needs it.
// Here, because our JS component 'Navbar' has the same name as Navbar Bootstrap's component
// we change the name of the imported Bootstrap's 'Navbar' component
import { Navbar as BootstrapNavbar} from "bootstrap";
import { getSessionObject } from "../../utils/session";

/**
 * Render the Navbar which is styled by using Bootstrap
 * Each item in the Navbar is tightly coupled with the Router configuration :
 * - the URI associated to a page shall be given in the attribute "data-uri" of the Navbar
 * - the router will show the Page associated to this URI when the user click on a nav-link
 */

const Navbar = () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let accesToken = getSessionObject("accessToken");
  let username = getSessionObject("userPseudo");
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
if(accesToken){
  navbar = `
      <nav>
        <div id="navigation">
          <div id="menu">
            <div id="logo"> <a class="nav-item menu-item" href="#"  data-uri="/"> Donnamis </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/"> Mon profile </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/"> Mes offres </a></div>
            <div id=""> <a class="nav-item menu-item" href="#"  data-uri="/additem"> Nouvelles offre + </a></div>
          </div>
         

          <div id="username"> bonjour ${username}</div>

          <div id="nav-connection"> 
            <div id="deconnection"> <a class="nav-item" href="#" data-uri="/logout"> Se deconnecter </a>  </div>
          </div>
        </div>
      </nav>
  `;  
}else{
   navbar = `
      <nav>
        <div id="navigation">
          <div id="logo"> <a class="nav-item" href="#"  data-uri="/"> Donnamis </a></div>

          <div id="nav-connection"> 
            <div id="connection"> <a class="nav-item" href="#" data-uri="/login"> Se connecter </a> </div>
            <div id="deconnection"> <a class="nav-item" href="#" data-uri="/register"> S'inscrire </a>  </div>
          </div>
        </div>
      </nav>
  `; 
}
 


  navbarWrapper.innerHTML = navbar;
};

export default Navbar;
