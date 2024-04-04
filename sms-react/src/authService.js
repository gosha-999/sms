import axios from 'axios';
const BASE_URL = 'http://localhost:8080';

export const loginUser = async (nutzername, password) => {
    try {
        const response = await axios.post(`${BASE_URL}/login`, { nutzername, password });
        if (response.status === 200) {
            const data = response.data;
            localStorage.setItem('sessionId', data.sessionId);
            return true; // Rückgabe true bei erfolgreicher Anmeldung
        } else {
            return false; // Rückgabe false bei fehlgeschlagener Anmeldung
        }
    } catch (error) {
        console.error('Fehler:', error);
        return false; // Rückgabe false bei Fehler
    }
};

export const registerUser = async (nutzername, password, confirmPassword, email, semester) => {
    try {
        if (!(await checkUsernameAvailable(nutzername))) {
            console.error('Nutzername ist bereits vergeben.');
            return false; // Nutzername nicht verfügbar
        }

        if (password !== confirmPassword) {
            console.error('Die Passwörter stimmen nicht überein.');
            return false; // Passwörter stimmen nicht überein
        }

        const response = await axios.post(`${BASE_URL}/nutzer/add`, { nutzername, password, email, semester });
        if (response.status === 200 || response.status === 201) {
            return true; // Registrierung erfolgreich
        } else {
            return false; // Registrierung fehlgeschlagen
        }
    } catch (error) {
        console.error('Registrierungsfehler:', error);
        return false; // Fehler bei der Registrierung
    }
};


export const logout = async (sessionId) => {
    try {
        await axios.post(`${BASE_URL}/logout`, null, {
            headers: { sessionId: sessionId }
        });
        localStorage.removeItem('sessionId');
        return true;
    } catch (error) {
        console.error('Fehler beim Logout:', error);
        throw error;
    }
};

export const fetchNutzerInfo = async (sessionId) => {
    try {
        const response = await axios.get(`${BASE_URL}/nutzer/info`, {
            headers: { sessionId: sessionId }
        });
        return response.data;
    } catch (error) {
        console.error('Fehler:', error);
        throw error;
    }
};

export const checkUsernameAvailable = async (nutzername) => {
    try {
        const response = await axios.post(`${BASE_URL}/checkUsername`, { nutzername });
        return response.data.available;
    } catch (error) {
        console.error('Fehler beim Überprüfen des Nutzernamens:', error);
        throw error;
    }
};
