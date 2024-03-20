import axios from 'axios';
const BASE_URL = 'http://localhost:8080';

export const loginUser = async (nutzername, password) => {
    try {
        const response = await axios.post(`${BASE_URL}/login`, { nutzername, password });
        const data = response.data;
        if (data.sessionId) {
            localStorage.setItem('sessionId', data.sessionId);
            alert('Login erfolgreich! Session ID gespeichert.');
            return true;
        } else {
            alert('Login fehlgeschlagen.');
            return false;
        }
    } catch (error) {
        console.error('Fehler:', error);
        alert('Ein Fehler ist aufgetreten.');
        return false;
    }
};

export const registerUser = async (nutzername, password, email) => {
    try {
        const response = await axios.post(`${BASE_URL}/nutzer/add`, { nutzername, password, email });
        return response.data;
    } catch (error) {
        console.error('Registrierungsfehler:', error);
        throw error;
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
