/**
 * Render the RegisterPage
 */
const home = `
<section id="section-register">
   <div id="register-box">
   <form action="#" id="register-form">
      <h2> Inscrivez-vous !</h2>
      <div>
         <input id ='username-rgst' type='text' placeholder='Username'>
      </div>
      <div>
         <input id ='password-rgst' type='text' placeholder='Password'>
         <input id ='confirm-pass-rgst' type='text' placeholder='Confirm password'>
      </div>
      <div>
         <input id ='last-name-rgst' type='text' placeholder='Last name'>
         <input id ='first-name-rgst' type='text' placeholder='First name'>
      </div>
      <div>
         <input id ='city-rgst' type='text' placeholder='City'>
      </div>
      <div>
         <input id ='street-rgst' type='text' placeholder='Street'>
         <input id ='number-of-building-rgst' type='text' placeholder='Number of building'>
         <input id ='unit-number' type='text' placeholder='Unit number'>
      </div>
      <div>
         <input id ='post-code-rgst' type='text' placeholder='Post code'>
      </div>
      <input type="submit" id="btnSubmit" value="S'inscrire">
      <a style="text-decoration:none" href='login'>
         <p>Déjà inscrit ? Connectez-vous !</p>
      </a>
   </form>
   <div>
</section>
`;

const RegisterPage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = home;
};


export default RegisterPage;
