import axios from 'axios';

const BASE_URL = 'http://localhost:8080/tasks';

// Axios-Instanz für wiederholte Header-Definitionen
const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' }
});

// Setzt den Session-Header für jede Anfrage
function setSessionHeader(sessionId) {
    axiosInstance.defaults.headers.common['sessionId'] = sessionId;
}

// Hinzufügen eines individuellen Nutzer-Tasks
export const addIndividualTask = async (task, sessionId) => {
    setSessionHeader(sessionId);
    const response = await axiosInstance.post('/add', task);
    return response.data;
};

// Abrufen aller Tasks eines Nutzers
export const getAllTasksForNutzer = async (sessionId) => {
    setSessionHeader(sessionId);
    const response = await axiosInstance.get('/get');
    return response.data;
};

// Hinzufügen eines Modul-Tasks
export const addModuleTask = async (moduleId, task) => {
    const response = await axiosInstance.post(`/module/${moduleId}`, task);
    return response.data;
};

// Abrufen aller Tasks eines Moduls
export const getTasksByModuleId = async (moduleId) => {
    const response = await axiosInstance.get(`/module/${moduleId}`);
    return response.data;
};

// Aktualisieren eines Nutzer-Tasks
export const updateNutzerTask = async (taskId, task, sessionId) => {
    setSessionHeader(sessionId);
    const response = await axiosInstance.put(`/${taskId}`, task);
    return response.data;
};

// Löschen eines Nutzer-Tasks
export const deleteNutzerTask = async (taskId, sessionId) => {
    setSessionHeader(sessionId);
    const response = await axiosInstance.delete(`/${taskId}`);
    return response.status === 200;
};

export const updateTaskStatus = async (taskId, newStatus, sessionId) => {
    setSessionHeader(sessionId);
    const response = await axiosInstance.patch(`/${taskId}/status`, { status: newStatus });
    return response.data;
};

export default {
    addIndividualTask,
    getAllTasksForNutzer,
    addModuleTask,
    getTasksByModuleId,
    updateNutzerTask,
    deleteNutzerTask,
    updateTaskStatus
};
