// DashboardService.js

import axios from 'axios';

const BASE_URL = 'http://localhost:8080';

export const fetchModules = async () => {
    try {
        const response = await axios.get(`${BASE_URL}/modules/all`);
        return response.data;
    } catch (error) {
        throw new Error('Fehler beim Laden der AddModule');
    }
};

export const fetchBookedModules = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/enrollment/get`, {
            headers: {
                'sessionId': sessionId
            }
        });
        return response.data;
    } catch (error) {
        throw new Error('Fehler beim Laden der gebuchten AddModule');
    }
};

export const fetchMerkliste = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/merkliste`, {
            headers: {
                'sessionId': sessionId
            }
        });
        return response.data; // Nimmt an, dass die Antwort die Liste der AddModule in der Merkliste ist
    } catch (error) {
        throw new Error('Fehler beim Laden der Merkliste');
    }
};

export const enrollInModule = async (sessionId, moduleId) => {
    try {
        const response = await axios.post(`${BASE_URL}/enrollment/${moduleId}/add`, {}, {
            headers: { 'sessionId': sessionId }
        });
        return response.data; // Die API-Antwort, z.B. "Modul erfolgreich gebucht"
    } catch (error) {
        throw new Error('Fehler beim Buchen des Moduls');
    }
};

export const removeBookedModule = async (sessionId, moduleId) => {
    try {
        const response = await axios.delete(`${BASE_URL}/enrollment/${moduleId}/delete`, {
            headers: {
                'Content-Type': 'application/json',
                'sessionId': sessionId
            }
        });
        return response.data;
    } catch (error) {
        console.error('Fehler beim Entfernen des gebuchten Moduls:', error);
        throw error;
    }
};

// Fügt ein Modul zur Merkliste hinzu
export const addToMerkliste = async (sessionId, moduleId) => {
    try {
        const response = await axios.post(`${BASE_URL}/merkliste/${moduleId}/add`, {}, {
            headers: { 'sessionId': sessionId }
        });
        return response.data; // z.B. "Modul wurde zur Merkliste hinzugefügt"
    } catch (error) {
        throw new Error('Fehler beim Hinzufügen zur Merkliste');
    }
};

// Entfernt ein Modul aus der Merkliste
export const removeFromMerkliste = async (sessionId, moduleId) => {
    try {
        const response = await axios.delete(`${BASE_URL}/merkliste/${moduleId}/delete`, {
            headers: {
                'Content-Type': 'application/json',
                'sessionId': sessionId
            }
        });
        return response.data; // z.B. "Modul wurde aus der Merkliste entfernt"
    } catch (error) {
        throw new Error('Fehler beim Entfernen aus der Merkliste');
    }
};

// Aktualisiert Nutzerdaten
export const updateUser = async (sessionId, userData) => {
    try {
        const response = await axios.patch(`${BASE_URL}/nutzer/update`, userData, {
            headers: {
                'Content-Type': 'application/json',
                'sessionId': sessionId,
            }
        });
        return response.data; // Der aktualisierte Nutzer
    } catch (error) {
        console.error('Fehler beim Aktualisieren der Nutzerdaten:', error);
        throw error;
    }
};




export default { fetchModules, fetchBookedModules,
    fetchMerkliste, enrollInModule, removeBookedModule,
    addToMerkliste, removeFromMerkliste, updateUser};

