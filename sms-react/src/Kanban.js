import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Card, Form, Modal } from 'react-bootstrap';
import { addTaskToNutzer, getTasksByNutzerId, deleteTask, getTasksByModuleId, updateTaskStatus } from './TaskService';
import { fetchBookedModules } from "./DashboardService";
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
    const [sessionId] = useState(localStorage.getItem('sessionId') || '');

    useEffect(() => {
        const fetchTasksAndModules = async () => {
            const userTasks = await getTasksByNutzerId(sessionId);
            const bookedModules = await fetchBookedModules(sessionId);
            const moduleTasksPromises = bookedModules.map(module => getTasksByModuleId(module.moduleId));
            const moduleTasksArrays = await Promise.all(moduleTasksPromises);
            const moduleTasks = moduleTasksArrays.flat().map(task => ({ ...task, isModuleTask: true }));
            setTasks([...userTasks, ...moduleTasks]);
        };

        fetchTasksAndModules();
    }, [sessionId]);

    const handleShow = () => setShow(true);
    const handleClose = () => setShow(false);

    const handleAddTask = async () => {
        try {
            // Der Aufruf von addTaskToNutzer sollte den vollständigen Task zurückgeben
            // inklusive der vom Backend zugewiesenen ID
            const addedTask = await addTaskToNutzer(sessionId, newTask);

            // Aktualisiere den Zustand, um den neu hinzugefügten Task einzuschließen
            // Stelle sicher dass addedTask die neue Task-ID enthält
            setTasks(prevTasks => [...prevTasks, addedTask]);

            // Setze das Formular zurück um einen neuen Task hinzuzufügen
            setNewTask({ title: '', description: '', deadline: '', priority: 'NIEDRIG', status: 'TODO' });

            // Schließe das Modal
            handleClose();
        } catch (error) {
            console.error("Fehler beim Hinzufügen des Tasks:", error);
            // Behandle den Fehler, z.B. durch Anzeigen einer Benachrichtigung
        }
    };


    const handleChange = (e) => {
        const { name, value } = e.target;
        setNewTask(prev => ({ ...prev, [name]: value }));
    };

    const moveTask = async (taskId, direction) => {
        const taskIndex = tasks.findIndex(task => task.id === taskId);
        if (taskIndex === -1) return; // Task nicht gefunden

        const currentStatus = tasks[taskIndex].status;
        const currentIndex = statusOrder.indexOf(currentStatus);
        const newIndex = currentIndex + direction;

        if (newIndex < 0 || newIndex >= statusOrder.length) return; // Verschiebung außerhalb des gültigen Bereichs

        const newStatus = statusOrder[newIndex];

        try {
            // Aktualisiere den Task-Status im Backend
            const updatedTask = await updateTaskStatus(sessionId, taskId, newStatus);

            // Aktualisiere den Zustand, um den aktualisierten Task widerzuspiegeln
            setTasks(prevTasks => prevTasks.map(task => (task.id === taskId ? updatedTask : task)));

        } catch (error) {
            console.error("Fehler beim Aktualisieren des Task-Status:", error);
            // Behandle den Fehler, z.B. durch Anzeigen einer Benachrichtigung
        }
    };

    const handleDeleteTask = async (taskId) => {
        await deleteTask(sessionId, taskId);
        setTasks(tasks.filter(task => task.id !== taskId));
    };

    return (
        <div><Header />
        <Container className="mt-5">
            <Row>
                <Col className="text-center">
                    <h2>Kanban Board</h2>
                    <Button variant="primary" onClick={handleShow} className="mt-2">Neuen Task hinzufügen</Button>
                </Col>
            </Row>
            <Row className="mt-3 justify-content-center">
                {statusOrder.map((status) => (
                    <Col key={status} md={4} className="mb-3">
                        <h4 className="text-center">{status}</h4>
                        <div className="tasks">
                            {tasks.filter(task => task.status === status).map((task) => (
                                <Card key={task.id} className="mb-2">
                                    <Card.Body>
                                        <Card.Title>{task.title}</Card.Title>
                                        <Card.Text>
                                            {task.description}
                                            <br />
                                            <small>Deadline: {task.deadline} | Priorität: {task.priority}</small>
                                        </Card.Text>
                                        {!task.isModuleTask && (
                                            <div className="d-flex justify-content-between">
                                                <Button variant="outline-secondary" onClick={() => moveTask(task.id, -1)}>←</Button>
                                                <Button variant="danger" onClick={() => handleDeleteTask(task.id)}>Löschen</Button>
                                                <Button variant="outline-secondary" onClick={() => moveTask(task.id, 1)}>→</Button>
                                            </div>
                                        )}
                                    </Card.Body>
                                </Card>
                            ))}
                        </div>
                    </Col>
                ))}
            </Row>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Neuen Task hinzufügen</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Titel</Form.Label>
                            <Form.Control type="text" name="title" value={newTask.title} onChange={handleChange} placeholder="Titel des Tasks" />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Beschreibung</Form.Label>
                            <Form.Control as="textarea" name="description" value={newTask.description} onChange={handleChange} rows={3} placeholder="Beschreibung des Tasks" />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Deadline</Form.Label>
                            <Form.Control type="date" name="deadline" value={newTask.deadline} onChange={handleChange} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Priorität</Form.Label>
                            <Form.Select name="priority" value={newTask.priority} onChange={handleChange}>
                                <option value="NIEDRIG">Niedrig</option>
                                <option value="MITTEL">Mittel</option>
                                <option value="HOCH">Hoch</option>
                            </Form.Select>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Schließen</Button>
                    <Button variant="primary" onClick={handleAddTask}>Speichern</Button>
                </Modal.Footer>
            </Modal>
        </Container>
        </div>
    );

};

export default KanbanBoard;
