import axios from 'axios';

const BASE_URL = 'http://localhost:8080'; // Ersetze 'http://deineapi.com' mit der Basis-URL deines Backends

// Funktion zum Hinzufügen eines neuen Moduls
export const addModule = async (moduleData) => {
    try {
        const response = await axios.post(`${BASE_URL}/modules/add`, moduleData, {
            headers: {
                'Content-Type': 'application/json',
                // Füge weitere benötigte Headers hinzu, z.B. Authorization für Authentifizierung
            }
        });
        return response.data; // Das hinzugefügte Modul
    } catch (error) {
        console.error('Fehler beim Hinzufügen eines Moduls:', error);
        throw error; // Weitergeben des Fehlers an die aufrufende Komponente
    }
};

export const fetchModuleById = async (moduleId) => {
    try {
        const response = await axios.get(`${BASE_URL}/modules/${moduleId}`);
        return response.data;
    } catch (error) {
        throw new Error('Fehler beim Laden der Moduldetails: ' + error);
    }
};

export const addKlausurTermin = async (moduleId, klausurTermin) => {
    try {
        const response = await axios.post(`${BASE_URL}/klausur/${moduleId}/add`, klausurTermin, {
            headers: {
                'Content-Type': 'application/json',
            }
        });
        return response.data;
    } catch (error) {
        console.error('Fehler beim Hinzufügen eines Klausurtermins:', error);
        throw error;
    }
};

// In ModuleService.js

export const fetchKlausurTermineByModuleId = async (moduleId) => {
    try {
        const response = await axios.get(`${BASE_URL}/klausur/${moduleId}`);
        return response.data;
    } catch (error) {
        console.error('Fehler beim Laden der Klausurtermine:', error);
        throw error;
    }
};


export default {addModule, fetchModuleById, fetchKlausurTermineByModuleId};
