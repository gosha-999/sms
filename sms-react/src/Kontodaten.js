import React, { useState } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';
import { updateUser } from './DashboardService'; // Stellen Sie sicher, dass der Pfad zu Ihrer DashboardService.js korrekt ist

const Kontodaten = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleUpdate = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        if (password !== confirmPassword) {
            setError('Die Passwörter stimmen nicht überein.');
            return;
        }

        try {
            // Die Session-ID sollte entsprechend gehandhabt werden, z.B. aus dem localStorage
            const sessionId = localStorage.getItem('sessionId');
            await updateUser(sessionId, { email, password });
            setSuccess('Kontodaten erfolgreich aktualisiert.');
        } catch (error) {
            setError('Fehler beim Aktualisieren der Kontodaten.');
            console.error('Update error:', error);
        }
    };

    return (
        <div className="kontodaten-form">
            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}
            <Form onSubmit={handleUpdate}>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>E-Mail-Adresse</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Neue E-Mail-Adresse eingeben"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}

                    />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                    <Form.Label>Neues Passwort</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Neues Passwort"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}

                    />
                </Form.Group>

                <Form.Group controlId="formBasicPasswordConfirm">
                    <Form.Label>Passwort bestätigen</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Passwort bestätigen"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}

                    />
                </Form.Group>

                <Button variant="primary" type="submit">
                    Aktualisieren
                </Button>
            </Form>
        </div>
    );
};

export default Kontodaten;
