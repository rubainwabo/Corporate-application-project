
const users = [{firstName:"mehdi",lastName:"Gobbe",}]
const userHandler = `
<section id="user-handler-page">
   <div id="switch-list-users">
        <label for="on-hold">En attente</label>
        <input type="radio" name="user-state" id="on-hold" checked >
        <label for="refusal">refusé</label>
        <input type="radio" name="user-state" id="refusal" > 
   </div>
   <div>
        <div class="user-to-handle">
            <p>Mehdi</p>
            <p>Gobbe</p>
            <div class="is-admin-box">
                <span> admin </span><br>
                 <input type="checkbox" id="is-admin"> 
            </div>
            <div>
                <button> accepter </button> 
            </div>
            <div>
                <button> refuser </button> 
            </div>
        </div>

        <div class="user-to-handle">
            <p>Mehdi</p>
            <p>Gobbe</p>
            <div class="is-admin-box">
                <span> admin </span><br>
                 <input type="checkbox" id="is-admin"> 
            </div>
            <div>
                <button> accepter </button> 
            </div>
            <div>
                <button> refuser </button> 
            </div>
        </div>


        <div class="user-to-handle">
            <p>Mehdi</p>
            <p>Gobbe</p>
            <div class="is-admin-box">
                <span> admin </span><br>
                 <input type="checkbox" id="is-admin"> 
            </div>
            <div>
                <button> accepter </button> 
            </div>
            <div>
                <button> refuser </button> 
            </div>
        </div>


        <div class="user-to-handle">
            <p>Mehdi</p>
            <p>Gobbe</p>
            <div class="is-admin-box">
                <span> admin </span><br>
                 <input type="checkbox" id="is-admin"> 
            </div>
            <div>
                <button> accepter </button> 
            </div>
            <div>
                <button> refuser </button> 
            </div>
        </div>
   </div>
</section>
`;

const UserHandler = () => {
   
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = userHandler;

    
  
  };
  
  export default UserHandler;