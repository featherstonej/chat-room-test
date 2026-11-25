import { useEffect, useState } from "react";
import { useAuth } from "../../auth/AuthContext";
import "./main-menu.css";

const MainMenu = ({ onLogin, onLogout }) => {
  const { authenticated, logout } = useAuth();
  const [error, setError] = useState("");
  const [loggingOut, setLoggingOut] = useState(false);
  const [loggedOut, setLoggedOut] = useState(false);

  useEffect(() => {
    if (!loggedOut) return;
    const timer = setTimeout(() => setLoggedOut(false), 5000);
    return () => clearTimeout(timer);
  }, [loggedOut]);

  async function handleAuth() {
    setError("");
    setLoggedOut(false);
    if (!authenticated) {
      onLogin();
      return;
    }

    setLoggingOut(true);
    try {
      await logout();
      onLogout();
      setLoggedOut(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoggingOut(false);
    }
  }

  return (
    <div className="main-menu">
        <ul className="test">
            <li><button className="menu-button">Home</button></li>
            <li><button className="menu-button">About</button></li>
            <li><button className="menu-button">Contact</button></li>
            <li><button className="menu-button">Help</button></li>
            <li><button type="button" className="menu-button login-button" onClick={handleAuth} disabled={loggingOut}>{authenticated ? "Logout" : "Login"}</button></li>
        </ul>
        <p role="status">{loggedOut ? "You've been logged out." : ""}</p>
        {error && <p role="alert">{error}</p>}
    </div>
  );
};

export default MainMenu;