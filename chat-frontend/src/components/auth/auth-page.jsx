import { useState } from 'react'
import LoginForm from './login-form'
import RegisterForm from './register-form'
import './auth.css'
export default function AuthPage() {
    const [mode, setMode] = useState('login')
    const isLogin = mode === 'login'
    return (
        <section className="auth-card" aria-labelledby="auth-title">
            <div className="auth-brand" aria-hidden="true">💬</div>
            <h2 id="auth-title">{isLogin ? 'Welcome back' : 'Create your account'}</h2>
            <p className="auth-subtitle">
                {isLogin ? 'Sign in to continue to Chat Room' : 'Join the conversation in seconds'}
            </p>

            {isLogin ? <LoginForm /> : <RegisterForm />}
            
            <p className="auth-switch">
                {isLogin ? 'New here?' : 'Already have an account?'}{' '}
                <button type="button" className="link-button"
                    onClick={() => setMode(isLogin ? 'register' : 'login')}>
                    {isLogin ? 'Create an account' : 'Sign in'}
                </button>
            </p>
        </section>
    )
}