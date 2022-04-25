const STORE_NAME = "user";
import {Redirect} from "../Components/Router/Router";

/**
 * Get the Object that is in the localStorage under the storeName key
 * @param {string} storeName
 * @returns
 */
const getSessionObject = (storeName) => {
  const retrievedObject = localStorage.getItem(storeName);
  if (!retrievedObject) return;
  return JSON.parse(retrievedObject);
};

/**
 * Set the object in the localStorage under the storeName key
 * @param {string} storeName
 * @param {Object} object
 */
const setSessionObject = (storeName, object) => {
  const storageValue = JSON.stringify(object);
  localStorage.setItem(storeName, storageValue);
};

/**
 * Remove the object in the localStorage under the storeName key
 * @param {String} storeName
 */
const removeSessionObject = (storeName) => {
  localStorage.removeItem(storeName);
};

const VerifyUser = async () => {
  let accesToken = getSessionObject("accessToken");
  let refreshToken = getSessionObject("tokenRefresh");
  if (accesToken || refreshToken){
    let header = refreshToken ? 
    {
      "token":accesToken,
      "refreshToken":refreshToken
    }:
    {
      "token":accesToken,
    }
  try {
  const options = {
    method: "GET", // *GET, POST, PUT, DELETE, etc.
    headers: header
  };
    const response = await fetch("/api/members/me", options); // fetch return a promise => we wait for the response
    if (!response.ok) {
      Redirect("/logout");
    }
    if (response.status == 200 ){
      const myTokens = await response.json();
      setSessionObject("accessToken", myTokens.accessToken);
      setSessionObject("tokenRefresh", myTokens.tokenRefresh);
    }
  } catch (error) {
    console.error("checkUser::error: ", error);
  }
}
}

export { getSessionObject, setSessionObject, removeSessionObject,VerifyUser};
