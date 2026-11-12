import React from "react";
import "./login.css";

const Login = ({ authenticated, setAuthenticated }) => {
    console.log("Authenticated:", authenticated);
    
    function handleLogin(event) {
        event.preventDefault();
        console.log("Logging in...");
        console.log("Authenticated:", authenticated);
        // Add login logic here
        if (typeof setAuthenticated === "function") {
            setAuthenticated(!authenticated);
        }
    }

    return (
        <div className="login-container">
            <h2>Login</h2>
            <form className="login-form">
                <input type="text" placeholder="Username" />
                <input type="password" placeholder="Password" />
                <button type="submit" onClick={handleLogin}>Login</button>
            </form>
        </div>
    );
};



export default Login;