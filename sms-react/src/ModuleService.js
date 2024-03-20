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
