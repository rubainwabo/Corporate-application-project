/**
 * Render the LoginPage
 */
const affichage = `
<div>
<input id="pseudoInput" type="text" placeholder="Pseudo">
<input  id="passwordInput" type="text" placeholder="Password">
<button id="btnSubmit">Se connecter</button>
</div>
`;

const LoginPage = () => { 
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = affichage;
  
};


export default LoginPage;
