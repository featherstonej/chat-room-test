import { useState } from 'react'
import './App.css'
import Header from './components/header/header'
import Body from './components/body/body'
import { useAuth } from './auth/AuthContext'

// function App() {
//   const [count, setCount] = useState(0)
//   const [authenticated, setAuthenticated] = useState(false)
  
//   return (
//     <>
//       <Header 
//         authenticated={authenticated}
//         setAuthenticated={setAuthenticated}
//       />
//       <Body 
//         authenticated={authenticated}
//         setAuthenticated={setAuthenticated}
//       />
//     </>
//   )
// }

function App() {
  const { authenticated, loading } = useAuth()
  const [showAuth, setShowAuth] = useState(false)
  if (loading) return <div className="app-loading">Loading...</div>
  return (
    <>
      <Header onLogin={() => setShowAuth(true)} onLogout={() => setShowAuth(false)} />
      <Body showAuth={showAuth && !authenticated} />
    </>
  )
}
export default App
