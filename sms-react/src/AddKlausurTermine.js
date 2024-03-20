import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { addKlausurTermin } from './ModuleService';

function AddKlausurTermine() {
    const { moduleId } = useParams();
    const navigate = useNavigate();
    const [klausurTermine, setKlausurTermine] = useState([
        { datum: '', klausurName: '', maxPlätze: '' }
    ]);

    const handleTerminChange = (index, field, value) => {
        const updatedTermine = klausurTermine.map((termin, i) => {
            if (i === index) {
                return { ...termin, [field]: value };
            }
            return termin;
        });
        setKlausurTermine(updatedTermine);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await Promise.all(klausurTermine.map(termin => {
                // Datumskonvertierung hier einfügen, falls notwendig
                const formattedTermin = {
                    datum: termin.datum.split('-').reverse().join('.'), // Beispiel für Datumsformatierung zu "dd.MM.yyyy"
                    klausurName: termin.klausurName,
                    maxPlaetze: termin.maxPlätze, // Achten Sie auf die richtige Schreibweise
                };
                return addKlausurTermin(moduleId, formattedTermin);
            }));
            alert('Klausurtermine erfolgreich hinzugefügt');
            navigate(`/moduledetail/${moduleId}`);
        } catch (error) {
            console.error('Fehler beim Hinzufügen der Klausurtermine:', error);
            alert('Fehler beim Hinzufügen der Klausurtermine');
        }
    };


    return (
        <div className="container mt-5">
            <h2>Klausurtermine hinzufügen</h2>
            <form onSubmit={handleSubmit}>
                {klausurTermine.map((klausurTermin, index) => (
                    <div key={index} className="mb-3">
                        <label>Datum</label>
                        <input
                            type="date"
                            className="form-control"
                            value={klausurTermin.datum}
                            onChange={(e) => handleTerminChange(index, 'datum', e.target.value)}
                            required
                        />
                        <label>Klausurname</label>
                        <input
                            type="text"
                            className="form-control"
                            value={klausurTermin.klausurName}
                            onChange={(e) => handleTerminChange(index, 'klausurName', e.target.value)}
                            required
                        />
                        <label>Maximale Plätze</label>
                        <input
                            type="number"
                            className="form-control"
                            value={klausurTermin.maxPlätze}
                            onChange={(e) => handleTerminChange(index, 'maxPlätze', e.target.value)}
                            required
                        />
                    </div>
                ))}
                <button type="submit" className="btn btn-primary">Termine speichern</button>
            </form>
        </div>
    );
}

export default AddKlausurTermine;
