import Navbar from "../Navbar/Navbar";
import { Redirect } from "../Router/Router";
import { removeSessionObject } from "../../utils/session";

const Logout = () => {
  console.log("Logout");
  // clear the user session data from the localStorage
    removeSessionObject("userPseudo");
    removeSessionObject("remeberMe");
    removeSessionObject("userId");
    removeSessionObject("accessToken");
    removeSessionObject("tokenRefresh");
    removeSessionObject("role");

  // re-render the navbar (for a non-authenticated user)
  Navbar();
  Redirect("/login");
  
};

export default Logout;
