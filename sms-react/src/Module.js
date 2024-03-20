import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addModule } from './ModuleService';

function Module() {
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

    const formFields = [
        { label: 'Modulname', id: 'name', type: 'text' },
        { label: 'Beschreibung', id: 'beschreibung', type: 'textarea' },
        { label: 'ECTS', id: 'ects', type: 'number' },
        { label: 'Dozent', id: 'dozent', type: 'text' },
        { label: 'Lehrstuhl', id: 'lehrstuhl', type: 'text' },
        { label: 'Regeltermin', id: 'regeltermin', type: 'text' },
        { label: 'Literaturempfehlung', id: 'literaturempfehlung', type: 'text' },
        { label: 'Mindestsemester', id: 'minSemester', type: 'number' }
    ];

    const handleChange = (e) => {
        const { id, value } = e.target;
        setModuleData({ ...moduleData, [id]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const newModule = await addModule(moduleData);
            console.log('Modul erfolgreich hinzugefügt:', newModule);
            navigate('/dashboard');
        } catch (error) {
            console.error('Fehler beim Hinzufügen des Moduls:', error);
            alert('Fehler beim Hinzufügen des Moduls');
        }
    };

    return (
        <div className="container mt-5">
            <h2>Neues Modul hinzufügen</h2>
            <form onSubmit={handleSubmit}>
                {formFields.map(({ label, id, type }) => (
                    <div className="mb-3" key={id}>
                        <label htmlFor={id} className="form-label">{label}</label>
                        {type === 'textarea' ? (
                            <textarea className="form-control" id={id} value={moduleData[id]} onChange={handleChange} required></textarea>
                        ) : (
                            <input type={type} className="form-control" id={id} value={moduleData[id]} onChange={handleChange} required />
                        )}
                    </div>
                ))}
                <button type="submit" className="btn btn-primary">Modul speichern</button>
            </form>
        </div>
    );
}

export default Module;
