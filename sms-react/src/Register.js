import React, { useState } from 'react';
import { loginUser, registerUser,} from './authService'; // Angenommen, beide Funktionen werden aus dem gleichen Modul importiert
import { useNavigate } from 'react-router-dom';

function Register({ onLoginSuccess }) {
    const [nutzername, setNutzername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState(''); // Zustand für das Bestätigungspasswort
    const [email, setEmail] = useState('');
    const [semester, setSemester] = useState('');
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const registerSuccess = await registerUser(nutzername, password, confirmPassword, email, semester);
            if (registerSuccess) {
                await loginUser(nutzername, password); // Stellen Sie sicher, dass loginUser auch async aufgerufen wird
                navigate('/dashboard');
            } else {
                setErrorMessage("Registrierung fehlgeschlagen. Bitte überprüfen Sie Ihre Eingaben.");
            }
        } catch (error) {

            console.error('Unbehandelter Fehler:', error);
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
                    <div className="mb-3">
                        <label htmlFor="registerSemester" className="form-label">Semester</label>
                        <input
                            type="number"
                            className="form-control"
                            id="registerSemester"
                            value={semester}
                            onChange={(e) => setSemester(e.target.value)}
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
