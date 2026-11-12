import React from "react";
import "./main-menu.css";

const MainMenu = ({ authenticated, setAuthenticated }) => {

  function handleAuth() {
    console.log(typeof setAuthenticated)
    if(typeof setAuthenticated === "function"){
      setAuthenticated(!authenticated);
    }
  }

  return (
    <div className="main-menu">
        <ul className="test">
            <li><button className="menu-button">Home</button></li>
            <li><button className="menu-button">About</button></li>
            <li><button className="menu-button">Contact</button></li>
            <li><button className="menu-button">Help</button></li>
            <li><button className="menu-button login-button" onClick={handleAuth}>{authenticated ? "Logout" : "Login"}</button></li>
        </ul>
    </div>
  );
};

export default MainMenu;