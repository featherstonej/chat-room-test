import React from "react";
import "./body.css";
import RoomEntry from "../room-entry/room-entry";
import Login from "../login/login";

const Body = ({ authenticated, setAuthenticated }) => {
  return (
    <main className="main-content">
      <p>Welcome!</p>
      <ul>
        <RoomEntry roomName="Chat Room 1" creator="Alice" />
        <RoomEntry roomName="Chat Room 2" creator="Bob" />
        <RoomEntry roomName="Chat Room 3" creator="Charlie" />
      </ul>
      {!authenticated && <Login authenticated={authenticated} setAuthenticated={setAuthenticated} />}
    </main>
  )
}

export default Body