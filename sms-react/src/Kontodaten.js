import React, { useState, useEffect } from 'react';
import {Form, Button, Alert, Container, Row, Col, Card} from 'react-bootstrap';
import { updateUser } from './DashboardService';
import {checkUsernameAvailable, fetchNutzerInfo} from "./authService";
import Header from "./Header";

const Kontodaten = () => {
    const [email, setEmail] = useState('');
    const [nutzername, setNutzername] = useState('');
    const [semester, setSemester] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [currentUser, setCurrentUser] = useState({ email: '', nutzername: '', semester: '' });

    useEffect(() => {
        const fetchUserData = async () => {
            const sessionId = localStorage.getItem('sessionId');
            const userData = await fetchNutzerInfo(sessionId);
            setCurrentUser(userData);
            setEmail(userData.email);
            setNutzername(userData.nutzername);
            setSemester(userData.semester);
        };
        fetchUserData();
    }, []);

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
        if (semester<= 10) {
            updatedFields.semester = semester;
        } else {
            setError('Das Semester muss zwischen 1 und 10 liegen.');
            return;
        }
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
                    <Col xs={12} lg={10}>
                        <Row>
                            <Col md={6}>
                                <Card className="mb-4 shadow-sm">
                                    <h2 className="card-header text-white bg-primary">Aktuelle Daten</h2>
                                    <Card.Body>
                                    <p><strong>E-Mail:</strong> {currentUser.email || 'Nicht verfügbar'}</p>
                                        <p><strong>Nutzername:</strong> {currentUser.nutzername || 'Nicht verfügbar'}</p>
                                        <p><strong>Semester:</strong> {currentUser.semester || 'Nicht verfügbar'}</p>
                                    </Card.Body>
                                </Card>
                            </Col>
                            <Col md={6}>
                                <Card className="mb-4 shadow-sm">
                                    <h2 className="card-header text-white bg-primary">Kontodaten ändern</h2>
                                    <Card.Body>
                                        {error && <Alert variant="danger">{error}</Alert>}
                                        {success && <Alert variant="success">{success}</Alert>}
                                    <Form onSubmit={handleUpdate}>
                                            <Form.Group controlId="formBasicEmail" className="mb-3">
                                                <Form.Label>E-Mail-Adresse</Form.Label>
                                                <Form.Control
                                                    type="email"
                                                    placeholder="Neue E-Mail-Adresse eingeben"
                                                    value={email}
                                                    onChange={(e) => setEmail(e.target.value)}
                                                />
                                            </Form.Group>
                                            <Form.Group controlId="formBasicNutzername" className="mb-3">
                                                <Form.Label>Nutzername</Form.Label>
                                                <Form.Control
                                                    type="text"
                                                    placeholder="Neuer Nutzername"
                                                    onChange={(e) => setNutzername(e.target.value)}
                                                />
                                            </Form.Group>
                                            <Form.Group controlId="formBasicSemester" className="mb-3">
                                                <Form.Label>Semester</Form.Label>
                                                <Form.Control
                                                    type="number"
                                                    placeholder="Aktuelles Semester eingeben"
                                                    value={semester}
                                                    onChange={(e) => setSemester(e.target.value)}
                                                />
                                            </Form.Group>
                                            <Form.Group controlId="formBasicPassword" className="mb-3">
                                                <Form.Label>Neues Passwort</Form.Label>
                                                <Form.Control
                                                    type="password"
                                                    placeholder="Neues Passwort"
                                                    value={password}
                                                    onChange={(e) => setPassword(e.target.value)}
                                                />
                                            </Form.Group>
                                            <Form.Group controlId="formBasicPasswordConfirm" className="mb-3">
                                                <Form.Label>Passwort bestätigen</Form.Label>
                                                <Form.Control
                                                    type="password"
                                                    placeholder="Passwort bestätigen"
                                                    value={confirmPassword}
                                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                                />
                                            </Form.Group>
                                            <Button variant="primary" type="submit" onClick={handleUpdate}>
                                                Aktualisieren
                                            </Button>
                                        </Form>
                                    </Card.Body>
                                </Card>
                            </Col>
                        </Row>
                    </Col>
                </Row>
            </Container>
        </div>
    );

};

export default Kontodaten;
