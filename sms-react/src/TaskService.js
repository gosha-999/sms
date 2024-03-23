import axios from "axios";

const BASE_URL = 'http://localhost:8080';
export const addTaskToModule = async (moduleId, taskData) => {
    try {
        const response = await axios.post(`${BASE_URL}/tasks/${moduleId}/add`, taskData, {
            headers: {
                'Content-Type': 'application/json',
                // Eventuell weitere Headers für Authentifizierung
            }
        });
        return response.data; // Der hinzugefügte Task
    } catch (error) {
        console.error('Fehler beim Hinzufügen eines Tasks:', error);
        throw error;
    }
};

export const getTasksByModuleId = async (moduleId) => {
    try {
        const response = await axios.get(`${BASE_URL}/tasks/${moduleId}`);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Abrufen der Tasks für das Modul:', error);
        throw error; // Sie können eine Behandlung entsprechend Ihrer Anwendungslogik hinzufügen
    }
};

// Neue Funktion, um einen Task zu einem Nutzer hinzuzufügen
export const addTaskToNutzer = async (sessionId, taskData) => {
    try {
        const response = await axios.post(`${BASE_URL}/tasks/add`, taskData, {
            headers: {
                'Content-Type': 'application/json',
                'sessionId': sessionId,
            }
        });
        return response.data; // Der hinzugefügte Task
    } catch (error) {
        console.error('Fehler beim Hinzufügen eines Tasks zu einem Nutzer:', error);
        throw error;
    }
};

// Neue Funktion, um alle Tasks eines Nutzers abzurufen
export const getTasksByNutzerId = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/tasks/tasks`, {
            headers: {
                'sessionId': sessionId,
            }
        });
        return response.data; // Die Tasks des Nutzers
    } catch (error) {
        console.error('Fehler beim Abrufen der Tasks eines Nutzers:', error);
        throw error;
    }
};

// Neue Funktion, um einen Task eines Nutzers zu löschen
export const deleteTask = async (sessionId, taskId) => {
    try {
        const response = await axios.delete(`${BASE_URL}/tasks/${taskId}`, {
            headers: {
                'sessionId': sessionId,
            }
        });
        return response.data; // Bestätigung des gelöschten Tasks
    } catch (error) {
        console.error('Fehler beim Löschen eines Tasks:', error);
        throw error;
    }
};

// Funktion, um den Status eines Tasks zu aktualisieren
export const updateTaskStatus = async (sessionId, taskId, newStatus) => {
    try {
        const response = await axios.patch(`${BASE_URL}/tasks/${taskId}/status`, newStatus, {
            headers: {
                'Content-Type': 'text/plain', // Überprüfen, ob der Backend-Server diesen Content-Type für den Body akzeptiert
                'sessionId': sessionId,
            }
        });
        return response.data; // Der aktualisierte Task
    } catch (error) {
        console.error('Fehler beim Aktualisieren des Task-Status:', error);
        throw error;
    }
};


export default {addTaskToModule, addTaskToNutzer,getTasksByModuleId, getTasksByNutzerId, deleteTask, updateTaskStatus}