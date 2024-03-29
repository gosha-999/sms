import React, { useState } from 'react';
import { loginUser } from './authService';
import {registerUser} from "./authService";
import { useNavigate } from 'react-router-dom';

function Register({ onLoginSuccess }) {
    const [nutzername, setNutzername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await registerUser(nutzername, password, email);
            console.log('Registrierung erfolgreich:', response);
            loginUser(nutzername, password);
            navigate('/dashboard');
        } catch (error) {
            alert('Registrierung fehlgeschlagen: ' + error.message);
        }
    };

    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">Registrierung</h5>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="registerUsername" className="form-label">Benutzername</label>
                        <input
                            type="text"
                            className="form-control"
                            id="registerUsername"
                            value={nutzername}
                            onChange={(e) => setNutzername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="registerPassword" className="form-label">Passwort</label>
                        <input
                            type="password"
                            className="form-control"
                            id="registerPassword"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="registerEmail" className="form-label">E-Mail</label>
                        <input
                            type="email"
                            className="form-control"
                            id="registerEmail"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-success">Registrieren</button>
                </form>
            </div>
        </div>
    );
}

export default Register;
