import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Card, Form, Modal } from 'react-bootstrap';
import { addIndividualTask, getAllTasksForNutzer, deleteNutzerTask, updateTaskStatus } from './TaskService';
import Header from "./Header";

const statusOrder = ["TODO", "IN_PROGRESS", "DONE"]; // Reihenfolge der Status für die Verschiebung

const KanbanBoard = () => {
    const [tasks, setTasks] = useState([]);
    const [show, setShow] = useState(false);
    const [newTask, setNewTask] = useState({
        title: '',
        description: '',
        deadline: '',
        priority: 'NIEDRIG',
        status: 'TODO'
    });
    const sessionId = localStorage.getItem('sessionId') || '';

    useEffect(() => {
        const fetchTasks = async () => {
            const tasksFromServer = await getAllTasksForNutzer(sessionId);
            setTasks(tasksFromServer);
        };

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
            // Aktualisiere den Task-Status im Backend
            await updateTaskStatus(taskId, { direction });

            // Aktualisiere den Zustand, um den aktualisierten Task widerzuspiegeln
            const updatedTasks = [...tasks];
            const taskIndex = updatedTasks.findIndex(task => task.id === taskId);
            if (taskIndex !== -1) {
                const currentStatus = updatedTasks[taskIndex].status;
                const currentIndex = statusOrder.indexOf(currentStatus);
                const newIndex = currentIndex + direction;
                if (newIndex >= 0 && newIndex < statusOrder.length) {
                    const newStatus = statusOrder[newIndex];
                    updatedTasks[taskIndex].status = newStatus;
                    setTasks(updatedTasks);
                }
            }
        } catch (error) {
            console.error("Fehler beim Aktualisieren des Task-Status:", error);
        }
    };

    return (
        <div>
            <Header />
            <Container className="mt-5">
                <Row>
                    <Col className="text-center">
                        <h2>Kanban Board</h2>
                        <Button variant="primary" onClick={handleShow}>Neuen Task hinzufügen</Button>
                    </Col>
                </Row>
                <Row className="mt-3 justify-content-center">
                    {statusOrder.map(status => (
                        <Col key={status} md={4} className="mb-3">
                            <h4 className="text-center">{status}</h4>
                            {tasks.filter(task => task.status === status).map(task => (
                                <Card key={task.id} className="mb-2">
                                    <Card.Body>
                                        <Card.Title>{task.title}</Card.Title>
                                        <Card.Text>
                                            {task.description}<br />
                                            <small>Deadline: {task.deadline} | Priorität: {task.priority}</small>
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
                        </Col>
                    ))}
                </Row>
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
            </Container>
        </div>
    );
};

export default KanbanBoard;
