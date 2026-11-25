import "./body.css";
import RoomEntry from "../room-entry/room-entry";
import AuthPage from "../auth/auth-page";
import { useAuth } from "../../auth/AuthContext";

const Body = ({ showAuth }) => {
  const { user } = useAuth()
  const username = user?.username;
  return showAuth ? ( <AuthPage /> )
    : ( <main className="main-content">
          <p>{username ? `Welcome back, ${username.charAt(0).toUpperCase()}${username.slice(1)}!` : "Welcome!"}</p>
          <ul>
            <RoomEntry roomName="Chat Room 1" creator="Alice" />
            <RoomEntry roomName="Chat Room 2" creator="Bob" />
            <RoomEntry roomName="Chat Room 3" creator="Charlie" />
          </ul>
        </main>
      )
}

export default Body