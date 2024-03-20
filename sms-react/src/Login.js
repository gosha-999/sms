import React, { useState } from 'react';
import { loginUser } from './authService';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [nutzername, setNutzername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        loginUser(nutzername, password);
        navigate('/dashboard');
    };

    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">Login</h5>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="loginUsername" className="form-label">Benutzername</label>
                        <input
                            type="text"
                            className="form-control"
                            id="loginUsername"
                            value={nutzername}
                            onChange={(e) => setNutzername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="loginPassword" className="form-label">Passwort</label>
                        <input
                            type="password"
                            className="form-control"
                            id="loginPassword"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">Einloggen</button>
                </form>
            </div>
        </div>
    );
}

export default Login;
