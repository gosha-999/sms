import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addModule, addKlausurTermin } from './ModuleService';

function AddModule() {
    const navigate = useNavigate();
    const [moduleData, setModuleData] = useState({
        name: '',
        beschreibung: '',
        ects: '',
        dozent: '',
        lehrstuhl: '',
        regeltermin: '',
        literaturempfehlung: '',
        minSemester: ''
    });
    const [klausurTermine, setKlausurTermine] = useState([]);

    const moduleFormFields = [
        { label: 'Modulname', id: 'name', type: 'text' },
        { label: 'Beschreibung', id: 'beschreibung', type: 'textarea' },
        { label: 'ECTS', id: 'ects', type: 'number' },
        { label: 'Dozent', id: 'dozent', type: 'text' },
        { label: 'Lehrstuhl', id: 'lehrstuhl', type: 'text' },
        { label: 'Regeltermin', id: 'regeltermin', type: 'text' },
        { label: 'Literaturempfehlung', id: 'literaturempfehlung', type: 'text' },
        { label: 'Mindestsemester', id: 'minSemester', type: 'number' }
    ];

    const handleChangeModule = (e) => {
        const { id, value } = e.target;
        setModuleData({ ...moduleData, [id]: value });
    };

    const handleAddKlausurTermin = () => {
        setKlausurTermine([...klausurTermine, { datum: '', klausurName: '', maxPlätze: '' }]);
    };

    const handleChangeKlausurTermin = (index, field, value) => {
        const updatedKlausurTermine = klausurTermine.map((termin, i) => i === index ? { ...termin, [field]: value } : termin);
        setKlausurTermine(updatedKlausurTermine);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await addModule(moduleData);
            const moduleId = response.moduleId;

            await Promise.all(klausurTermine.map(termin => addKlausurTermin(moduleId, termin)));

            console.log('Modul und Klausurtermine erfolgreich hinzugefügt');
            navigate(`/dashboard`);
        } catch (error) {
            console.error('Fehler beim Hinzufügen:', error);
            alert('Fehler beim Hinzufügen des Moduls oder der Klausurtermine');
        }
    };

    return (
        <div className="container mt-5">
            <h2>Neues Modul und Klausurtermine hinzufügen</h2>
            <form onSubmit={handleSubmit}>
                {moduleFormFields.map(({ label, id, type }) => (
                    <div className="mb-3" key={id}>
                        <label htmlFor={id} className="form-label">{label}</label>
                        {type === 'textarea' ? (
                            <textarea className="form-control" id={id} value={moduleData[id]} onChange={handleChangeModule} required />
                        ) : (
                            <input type={type} className="form-control" id={id} value={moduleData[id]} onChange={handleChangeModule} required />
                        )}
                    </div>
                ))}
                <button type="button" className="btn btn-secondary" onClick={handleAddKlausurTermin}>Klausurtermin festlegen</button>
                {klausurTermine.map((termin, index) => (
                    <div key={index}>
                        <input type="date" value={termin.datum} onChange={(e) => handleChangeKlausurTermin(index, 'datum', e.target.value)} required />
                        <input type="text" placeholder="Klausurname" value={termin.klausurName} onChange={(e) => handleChangeKlausurTermin(index, 'klausurName', e.target.value)} required />
                        <input type="number" placeholder="Max Plätze" value={termin.maxPlätze} onChange={(e) => handleChangeKlausurTermin(index, 'maxPlätze', e.target.value)} required />
                    </div>
                ))}
                <button type="submit" className="btn btn-primary mt-3">Alles speichern</button>
            </form>
        </div>
    );
}

export default AddModule;
