import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Card, Form, Modal } from 'react-bootstrap';
import { addIndividualTask, getAllTasksForNutzer, deleteNutzerTask, updateTaskStatus } from './TaskService';
import Header from "./Header";

const statusOrder = ["TODO", "IN_PROGRESS", "DONE"]; // Reihenfolge der Status für die Verschiebung

const KanbanBoard = () => {
    const [tasks, setTasks] = useState([]);
    const [show, setShow] = useState(false);
    const [sortField, setSortField] = useState('deadline'); // Default zu 'deadline' setzen
    const [sortOrder, setSortOrder] = useState('asc'); // Default zu 'asc' setzen
    const [newTask, setNewTask] = useState({
        title: '',
        description: '',
        deadline: '',
        priority: 'NIEDRIG',
        status: 'TODO'
    });
    const sessionId = localStorage.getItem('sessionId') || '';

    const fetchTasks = async () => {
        const tasksFromServer = await getAllTasksForNutzer(sessionId);
        setTasks(tasksFromServer);
    };

    useEffect(() => {
        fetchTasks();
    }, [sessionId]);

    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);

    const handleAddTask = async () => {
        try {
            const addedTask = await addIndividualTask(newTask, sessionId);
            setTasks(prevTasks => [...prevTasks, addedTask]);
            handleClose(); // Schließe das Modal nach dem Hinzufügen
        } catch (error) {
            console.error("Fehler beim Hinzufügen des Tasks:", error);
        }
    };

    const handleDeleteTask = async (taskId) => {
        try {
            await deleteNutzerTask(taskId, sessionId);
            setTasks(tasks.filter(task => task.id !== taskId));
        } catch (error) {
            console.error("Fehler beim Löschen des Tasks:", error);
        }
    };

    const moveTask = async (taskId, direction) => {
        try {
            // Finde den aktuellen Task und berechne den neuen Status basierend auf der Richtung
            const task = tasks.find(task => task.id === taskId);
            if (!task) {
                console.error("Task nicht gefunden");
                return;
            }
            const currentStatusIndex = statusOrder.indexOf(task.status);
            const newStatusIndex = currentStatusIndex + direction;
            if (newStatusIndex < 0 || newStatusIndex >= statusOrder.length) {
                console.error("Ungültige Statusänderung");
                return;
            }
            const newStatus = statusOrder[newStatusIndex];

            // Aktualisiere den Task-Status im Backend
            await updateTaskStatus(taskId, newStatus, sessionId);
            await fetchTasks();

            // Aktualisiere den Zustand, um den aktualisierten Task widerzuspiegeln
            setTasks(currentTasks => currentTasks.map(task => {
                if (task.id === taskId) {
                    return { ...task, status: newStatus };
                }
                return task;
            }));
        } catch (error) {
            console.error("Fehler beim Aktualisieren des Task-Status:", error);
        }
    };

    const sortTasks = (a, b) => {
        if (!a[sortField] || !b[sortField]) return 0; // Falls eines der Felder fehlt, nicht sortieren
        if (sortOrder === 'asc') {
            return a[sortField].localeCompare(b[sortField]);
        } else {
            return b[sortField].localeCompare(a[sortField]);
        }
    };

    return (
        <div>
            <Header />
            <div className="container mt-5">
                <div className="card bg-light mb-4">
                    <h2 className="card-header text-white bg-primary">Kanban Board</h2>
                    <div className="card-body">
                        <div className="mb-3 text-center">
                            <Button variant="primary" onClick={handleShow}>Neuen Task hinzufügen</Button>
                        </div>
                        <div className="d-flex" style={{ width: '100%' }}>
                            {statusOrder.map((status, index) => (
                                <div key={status} className={`d-flex flex-column p-3 ${index < statusOrder.length - 1 ? 'border-right' : ''}`} style={{ flex: 1 }}>
                                    <h4 className="text-center">{status}</h4>
                                    {tasks.filter(task => task.status === status).sort(sortTasks).map(task => (
                                        <Card key={task.id} className="mb-2">
                                            <Card.Body>
                                                <Card.Title>{task.title}</Card.Title>
                                                <Card.Text>
                                                    {task.description}<br />
                                                    <small>Deadline: {task.deadline}</small><br />
                                                    <small>Erstellungsdatum: {task.erstellungsDatum}</small><br />
                                                    <small>Erfüllungsdatum: {task.erfuellungsDatum || 'N/A'}</small>
                                                </Card.Text>
                                                <div className="d-flex justify-content-between">
                                                    {status !== 'TODO' && (
                                                        <Button variant="outline-secondary" onClick={() => moveTask(task.id, -1)}>←</Button>
                                                    )}
                                                    <Button variant="danger" onClick={() => handleDeleteTask(task.id)}>Löschen</Button>
                                                    {status !== 'DONE' && (
                                                        <Button variant="outline-secondary" onClick={() => moveTask(task.id, 1)}>→</Button>
                                                    )}
                                                </div>
                                            </Card.Body>
                                        </Card>
                                    ))}
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
                {/* Modal für das Hinzufügen von Tasks */}
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Neuen Task erstellen</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Form.Group className="mb-3">
                                <Form.Label>Titel</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="title"
                                    value={newTask.title}
                                    onChange={(e) => setNewTask({ ...newTask, title: e.target.value })}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Beschreibung</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    name="description"
                                    value={newTask.description}
                                    onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Deadline</Form.Label>
                                <Form.Control
                                    type="date"
                                    name="deadline"
                                    value={newTask.deadline}
                                    onChange={(e) => setNewTask({ ...newTask, deadline: e.target.value })}
                                />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Priorität</Form.Label>
                                <Form.Select
                                    name="priority"
                                    value={newTask.priority}
                                    onChange={(e) => setNewTask({ ...newTask, priority: e.target.value })}
                                >
                                    <option value="NIEDRIG">Niedrig</option>
                                    <option value="MITTEL">Mittel</option>
                                    <option value="HOCH">Hoch</option>
                                </Form.Select>
                            </Form.Group>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>Abbrechen</Button>
                        <Button variant="primary" onClick={handleAddTask}>Speichern</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        </div>
    );

};

export default KanbanBoard;
