import { useState } from 'react'
import { useAuth } from '../../auth/AuthContext'
import PasswordInput from './password-input'
export default function LoginForm() {
    const { login } = useAuth()
    const [userName, setUserName] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [submitting, setSubmitting] = useState(false)
    async function handleSubmit(e) {
        e.preventDefault()
        setError('')
        setSubmitting(true)
        try {
            await login(userName.trim(), password)
        } catch (err) {
            setError(err.status === 401 ? 'Invalid username or password' : err.message)
        } finally {
            setSubmitting(false)
        }
    }
    return (
        <form className="auth-form" onSubmit={handleSubmit} noValidate>
            <label htmlFor="username">Username</label>
            <input id="username" autoComplete="username" autoFocus required
                value={userName} onChange={(e) => setUserName(e.target.value)} />
            <label htmlFor="password">Password</label>
            <PasswordInput id="password" autoComplete="current-password"
                value={password} onChange={setPassword} />
            {error && <div className="auth-error" role="alert">{error}</div>}
            <button className="auth-submit" type="submit"
                disabled={submitting || !userName || !password}>
                {submitting ? 'Signing in…' : 'Sign in'}
            </button>
        </form>
    )
}