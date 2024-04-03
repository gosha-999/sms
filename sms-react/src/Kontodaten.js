import React, { useState } from 'react';
import { Form, Button, Alert, Container, Row, Col } from 'react-bootstrap';
import { updateUser } from './DashboardService';
import { checkUsernameAvailable } from "./authService";
import Header from "./Header";

const Kontodaten = () => {
    const [email, setEmail] = useState('');
    const [nutzername, setNutzername] = useState('');
    const [semester, setSemester] = useState(''); // Zustand für das Semester
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleUpdate = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        let updatedFields = {};
        if (email.trim() !== '') updatedFields.email = email;
        if (nutzername.trim() !== '') {
            const isAvailable = await checkUsernameAvailable(nutzername);
            if (!isAvailable) {
                setError('Dieser Nutzername ist bereits vergeben.');
                return;
            }
            updatedFields.nutzername = nutzername;
        }
        if (semester.trim() !== '') updatedFields.semester = semester; // Semester zum Update hinzufügen
        if (password.trim() !== '' && password === confirmPassword) {
            updatedFields.password = password;
        } else if (password.trim() !== '') {
            setError('Die Passwörter stimmen nicht überein.');
            return;
        }

        if (Object.keys(updatedFields).length === 0) {
            setError('Bitte füllen Sie mindestens ein Feld aus, um Ihr Konto zu aktualisieren.');
            return;
        }

        try {
            const sessionId = localStorage.getItem('sessionId');
            await updateUser(sessionId, updatedFields);
            setSuccess('Kontodaten erfolgreich aktualisiert.');
        } catch (error) {
            setError('Fehler beim Aktualisieren der Kontodaten.');
            console.error('Update error:', error);
        }
    };

    return (
        <div>
            <Header />
            <Container className="mt-5">
                <Row className="justify-content-center">
                    <Col xs={12} md={6}>
                        <h2 className="mb-4 text-center">Kontodaten aktualisieren</h2>
                        {error && <Alert variant="danger">{error}</Alert>}
                        {success && <Alert variant="success">{success}</Alert>}
                        <Form onSubmit={handleUpdate}>
                            {/* E-Mail-Adresse */}
                            <Form.Group controlId="formBasicEmail" className="mb-3">
                                <Form.Label>E-Mail-Adresse</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Neue E-Mail-Adresse eingeben"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </Form.Group>

                            {/* Nutzername */}
                            <Form.Group controlId="formBasicNutzername" className="mb-3">
                                <Form.Label>Nutzername</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Neuer Nutzername"
                                    value={nutzername}
                                    onChange={(e) => setNutzername(e.target.value)}
                                />
                            </Form.Group>

                            {/* Semester */}
                            <Form.Group controlId="formBasicSemester" className="mb-3">
                                <Form.Label>Semester</Form.Label>
                                <Form.Control
                                    type="number"
                                    placeholder="Aktuelles Semester eingeben"
                                    value={semester}
                                    onChange={(e) => setSemester(e.target.value)}
                                />
                            </Form.Group>

                            {/* Neues Passwort */}
                            <Form.Group controlId="formBasicPassword" className="mb-3">
                                <Form.Label>Neues Passwort</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Neues Passwort"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Form.Group>

                            {/* Passwort bestätigen */}
                            <Form.Group controlId="formBasicPasswordConfirm" className="mb-3">
                                <Form.Label>Passwort bestätigen</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Passwort bestätigen"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                            </Form.Group>

                            {/* Aktualisierungs-Button */}
                            <Button variant="primary" type="submit">
                                Aktualisieren
                            </Button>
                        </Form>
                    </Col>
                </Row>
            </Container>
        </div>
    );

};

export default Kontodaten;
