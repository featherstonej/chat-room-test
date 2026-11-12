import { useState } from 'react'
import './App.css'
import Header from './components/header/header'
import Body from './components/body/body'

function App() {
  const [count, setCount] = useState(0)
  const [authenticated, setAuthenticated] = useState(false)
  
  return (
    <>
      <Header 
        authenticated={authenticated}
        setAuthenticated={setAuthenticated}
      />
      <Body 
        authenticated={authenticated}
        setAuthenticated={setAuthenticated}
      />
    </>
  )
}

export default App
