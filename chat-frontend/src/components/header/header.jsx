import './header.css'
import MainMenu from '../menu/main-menu'

const Header = ({ onLogin, onLogout }) => {
  return (
    <header className="header">
      <h1>Chat Room</h1>
      <MainMenu onLogin={onLogin} onLogout={onLogout} />
    </header>
  )
}

export default Header