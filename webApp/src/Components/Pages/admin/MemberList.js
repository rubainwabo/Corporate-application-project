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
        <div id="container-research-member-list">
            <input type="text">
                <div id="container-i-member-list">
                    <i id="fa-research-member-list" class="fas fa-search"></i>
                </div>
        </div>
    </div>
</div>
</section>
`;

const MemberList =  () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;

  let token = getSessionObject("accessToken");

};
export default MemberList;
