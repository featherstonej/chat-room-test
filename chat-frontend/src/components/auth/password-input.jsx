import { useState } from 'react'
export default function PasswordInput({ id, value, onChange, ...rest }) {
    const [visible, setVisible] = useState(false)
    return (
        <div className="password-field">
            <input id={id} type={visible ? 'text' : 'password'} required
                value={value} onChange={(e) => onChange(e.target.value)} {...rest} />
            <button type="button" className="password-toggle"
                aria-label={visible ? 'Hide password' : 'Show password'}
                onClick={() => setVisible((v) => !v)}>
                {visible ? 'Hide' : 'Show'}
            </button>
        </div>
    )
}