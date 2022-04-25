import "bootstrap/dist/css/bootstrap.min.css";
import "./stylesheets/style.css"; // If you prefer to style your app with vanilla CSS rather than with Bootstrap
import { VerifyUser } from "./utils/session";
import Navbar from "./Components/Navbar/Navbar";
import { Router } from "./Components/Router/Router";

VerifyUser();

Navbar();

Router(); // The router will automatically load the root page