import { createContext, useContext, useEffect, useState, useCallback } from 'react'
import { api } from '../api/client'
const AuthContext = createContext(null)

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null)
    const [loading, setLoading] = useState(true) // true until we know whether a session exists
    useEffect(() => {
        api('/auth/csrf')
            // makes sure the XSRF-TOKEN cookie exists
            .then(() => api('/auth/me'))
            .then(setUser)
            .catch(() => setUser(null))
            .finally(() => setLoading(false))
    }, [])
    const login = useCallback(async (userName, password) => {
        const u = await api('/auth/login', {
            method: 'POST', body: { userName, password }
        })
        setUser(u)
    }, [])
    const register = useCallback(async (userName, password) => {
        await api('/auth/register', { method: 'POST', body: { userName, password } })
        await login(userName, password)
    }, [login])
    const logout = useCallback(async () => {
        await api('/auth/logout', { method: 'POST' })
        setUser(null)
    }, [])
    return (
        <AuthContext.Provider value={{
            user, authenticated: !!user, loading, login,
            register, logout
        }}>
            {children}
        </AuthContext.Provider>
    )
}
export const useAuth = () => useContext(AuthContext)