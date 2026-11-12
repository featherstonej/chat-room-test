import React from 'react'
import './header.css'
import MainMenu from '../menu/main-menu'

const Header = ({ authenticated, setAuthenticated }) => {
  return (
    <header className="header">
      <h1>Chat Room</h1>
      <MainMenu 
        authenticated={authenticated}
        setAuthenticated={setAuthenticated}
      />
    </header>
  )
}

export default Header