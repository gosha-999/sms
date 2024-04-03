import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addModule, addKlausurTermin} from './ModuleService';
import {addModuleTask} from './TaskService'
import Header from "./Header";

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
    const [tasks, setTasks] = useState([]);

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

    const handleAddTask = () => {
        setTasks([...tasks, { title: '', description: '', deadline: '', priority: 1, status: 'TODO' }]);
    };

    const handleChangeTask = (index, field, value) => {
        const updatedTasks = tasks.map((task, i) => i === index ? { ...task, [field]: value } : task);
        setTasks(updatedTasks);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await addModule(moduleData);
            const moduleId = response.moduleId;

            await Promise.all(klausurTermine.map(termin => addKlausurTermin(moduleId, termin)));
            await Promise.all(tasks.map(task => addModuleTask(moduleId, task)));

            console.log('Modul, Klausurtermine und Tasks erfolgreich hinzugefügt');
            navigate(`/dashboard`);
        } catch (error) {
            console.error('Fehler beim Hinzufügen:', error);
            alert('Fehler beim Hinzufügen des Moduls, der Klausurtermine oder der Tasks');
        }
    };

    return (
        <div><Header />
        <div className="container mt-5">
            <h2>Neues Modul, Klausurtermine und Tasks hinzufügen</h2>
            <form onSubmit={handleSubmit}>
                {/* Moduldetails */}
                <div className="modul-details-container mb-4">
                    <div className="card">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Moduldetails</h3>
                        </div>
                        <div className="card-body">
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
                        </div>
                    </div>
                </div>

                {/* Klausurtermine */}
                <div className="klausurtermine-container mb-4">
                    <div className="card">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Klausurtermine</h3>
                        </div>
                        <div className="card-body">
                            <button type="button" className="btn btn-secondary mb-3" onClick={handleAddKlausurTermin}>
                                Klausurtermin festlegen
                            </button>
                            {klausurTermine.map((termin, index) => (
                                <div key={index} className="card mb-2">
                                    <div className="card-body">
                                        <h5 className="card-title">Klausurtermin #{index + 1}</h5>
                                        <div className="mb-3">
                                            <label className="form-label">Datum</label>
                                            <input type="date" className="form-control" value={termin.datum} onChange={(e) => handleChangeKlausurTermin(index, 'datum', e.target.value)} required />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Klausurname</label>
                                            <input type="text" className="form-control" placeholder="Klausurname" value={termin.klausurName} onChange={(e) => handleChangeKlausurTermin(index, 'klausurName', e.target.value)} required />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Max Plätze</label>
                                            <input type="number" className="form-control" placeholder="Max Plätze" value={termin.maxPlätze} onChange={(e) => handleChangeKlausurTermin(index, 'maxPlätze', e.target.value)} required />
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Tasks */}
                <div className="tasks-container mb-4">
                    <div className="card">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Tasks</h3>
                        </div>
                        <div className="card-body">
                            <button type="button" className="btn btn-secondary mb-3" onClick={handleAddTask}>
                                Task hinzufügen
                            </button>
                            {tasks.map((task, index) => (
                                <div key={index} className="card mb-2">
                                    <div className="card-body">
                                        <h5 className="card-title">Task #{index + 1}</h5>
                                        <div className="mb-3">
                                            <label className="form-label">Titel</label>
                                            <input type="text" className="form-control" placeholder="Titel" value={task.title} onChange={(e) => handleChangeTask(index, 'title', e.target.value)} required />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Beschreibung</label>
                                            <textarea className="form-control" placeholder="Beschreibung" value={task.description} onChange={(e) => handleChangeTask(index, 'description', e.target.value)} required></textarea>
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Deadline</label>
                                            <input type="date" className="form-control" value={task.deadline} onChange={(e) => handleChangeTask(index, 'deadline', e.target.value)} required />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Priorität</label>
                                            <select className="form-select" value={task.priority} onChange={(e) => handleChangeTask(index, 'priority', e.target.value)} required>
                                                <option value="1">Niedrig</option>
                                                <option value="2">Mittel</option>
                                                <option value="3">Hoch</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Submit Button */}
                <button type="submit" className="btn btn-primary mt-3">Alles speichern</button>
            </form>
        </div>
        </div>
    );
}

export default AddModule;
