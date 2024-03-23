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

// Ergänzung in ModuleService.js
export const bucheKlausurTermin = async (klausurTerminId, sessionId) => {
    try {
        const response = await axios.post(`${BASE_URL}/klausur/${klausurTerminId}/buchen`, {}, {
            headers: {
                'sessionId': sessionId
            }
        });
        return response.data;
    } catch (error) {
        console.error('Fehler beim Buchen des Klausurtermins:', error);
        throw error;
    }
};

// Neue Funktion zum Abrufen der gebuchten Klausurtermine eines Nutzers
export const fetchBookedKlausurTermine = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/klausur/nutzerTermine`, {
            headers: {
                'sessionId': sessionId // Stelle sicher, dass die Session-ID korrekt übermittelt wird
            }
        });
        return response.data; // Die gebuchten Klausurtermine
    } catch (error) {
        console.error('Fehler beim Laden der gebuchten Klausurtermine:', error);
        throw error;
    }
};

// Funktion zum Hinzufügen von Noten für Module
export const addNotesForModules = async (sessionId, moduleNotes) => {
    try {
        const response = await axios.post(`${BASE_URL}/enrollment/setnoten`, moduleNotes, {
            headers: {
                'Content-Type': 'application/json',
                'sessionId': sessionId,
            },
        });
        return response.data;
    } catch (error) {
        console.error('Fehler beim Speichern der Noten:', error);
        throw error;
    }
};

// Funktion zum Abrufen des gewichteten Notendurchschnitts
export const getWeightedAverageGrade = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/enrollment/durchschnitt`, {
            headers: {
                'sessionId': sessionId,
            },
        });
        return response.data;
    } catch (error) {
        console.error('Fehler beim Abrufen des Notendurchschnitts:', error);
        throw error;
    }
};

export const getAllNotes = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/enrollment/noten`, {
            headers: {
                'sessionId': sessionId,
            },
        });
        return response.data; // Gibt ein Objekt zurück, wobei der Schlüssel die moduleId und der Wert die Note ist
    } catch (error) {
        console.error('Fehler beim Abrufen der Noten:', error);
        throw error;
    }
};






// Stellen Sie sicher, dass Sie die neuen Funktionen auch im default export einfügen, falls Sie dies nutzen
export default {
    addModule,
    fetchModuleById,
    addKlausurTermin,
    fetchKlausurTermineByModuleId,
    bucheKlausurTermin,
    fetchBookedKlausurTermine,
    addNotesForModules,
    getWeightedAverageGrade,
    getAllNotes
};
