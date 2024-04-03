import React, { useState } from 'react';
import { loginUser, registerUser,checkUsernameAvailable } from './authService'; // Angenommen, beide Funktionen werden aus dem gleichen Modul importiert
import { useNavigate } from 'react-router-dom';

function Register({ onLoginSuccess }) {
    const [nutzername, setNutzername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState(''); // Zustand für das Bestätigungspasswort
    const [email, setEmail] = useState('');
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Überprüfung, ob Passwörter übereinstimmen
        if (password !== confirmPassword) {
            alert('Die Passwörter stimmen nicht überein.');
            return; // Verhindert die Fortsetzung der Registrierung
        }

        try {
            if(checkUsernameAvailable(nutzername)) {
                const response = await registerUser(nutzername, password, email);
                console.log('Registrierung erfolgreich:', response);
                loginUser(nutzername, password);
                navigate('/dashboard');
            }
        } catch (error) {
            setErrorMessage("Nutzername exisitiert bereits.")
            console.error(error)
        }
    };

    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">Registrierung</h5>
                <div className="container mt-4">
                    {errorMessage && (
                        <div className="alert alert-danger" role="alert">
                            {errorMessage}
                        </div>
                    )}
                </div>
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
                        <label htmlFor="confirmPassword" className="form-label">Passwort bestätigen</label>
                        <input
                            type="password"
                            className="form-control"
                            id="confirmPassword"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
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
