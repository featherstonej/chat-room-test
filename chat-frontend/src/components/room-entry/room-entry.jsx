import React from "react";
import "./room-entry.css";

const RoomEntry = ({ roomName, creator }) => {
  return (
    <li className="room-entry">
      {roomName} - Created By: {creator}
    </li>
  );
};

export default RoomEntry;