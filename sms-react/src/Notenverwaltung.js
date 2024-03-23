import React, { useState, useEffect } from 'react';
import DashboardService from './DashboardService';
import ModuleService from "./ModuleService";
import Header from "./Header";

function Notenverwaltung() {
    const [bookedModules, setBookedModules] = useState([]);
    const [moduleGrades, setModuleGrades] = useState([]);
    const [averageGrade, setAverageGrade] = useState(0);
    const [allNotes, setAllNotes] = useState({});

    useEffect(() => {
        const fetchData = async () => {
            const sessionId = localStorage.getItem('sessionId');
            const modules = await DashboardService.fetchBookedModules(sessionId);
            const notes = await ModuleService.getAllNotes(sessionId);
            setBookedModules(modules);
            setAllNotes(notes);
            const initialGrades = modules.map(module => ({
                moduleId: module.moduleId,
                grade: notes[module.moduleId] || ''
            }));
            setModuleGrades(initialGrades);
        };

        fetchData();
    }, []);

    const handleGradeChange = (index, grade) => {
        const newModuleGrades = [...moduleGrades];
        newModuleGrades[index].grade = parseFloat(grade);
        setModuleGrades(newModuleGrades);
    };

    const saveGrades = async () => {
        const sessionId = localStorage.getItem('sessionId');
        const formattedGrades = moduleGrades.reduce((acc, {moduleId, grade}) => {
            if (grade !== '') {
                acc[moduleId] = parseFloat(grade);
            }
            return acc;
        }, {});

        try {
            await ModuleService.addNotesForModules(sessionId, formattedGrades);
            alert('Noten gespeichert!');
        } catch (error) {
            console.error('Fehler beim Speichern der Noten:', error);
            alert('Fehler beim Speichern der Noten.');
        }
    };

    const calculateAverage = async () => {
        const sessionId = localStorage.getItem('sessionId');
        try {
            const average = await ModuleService.getWeightedAverageGrade(sessionId);
            setAverageGrade(average);
        } catch (error) {
            console.error('Fehler beim Berechnen des Durchschnitts:', error);
        }
    };

    return (
        <div><Header />
            <div className="container mt-4">
                <div className="text-white bg-primary mb-3 p-3 rounded" style={{backgroundColor: "blue"}}>
                    <h1>Notenübersicht</h1>
                </div>
                {bookedModules.length === 0 ? (
                    <p>Keine gebuchten Module verfügbar.</p>
                ) : (
                    bookedModules.map((module, index) => (
                        <div key={module.id} className="card mb-2">
                            <div className="card-header">
                                <strong>{module.name}</strong> - ECTS: {module.ects}
                            </div>
                            <div className="card-body">
                                <input
                                    type="number"
                                    className="form-control form-control-sm d-inline-block w-auto"
                                    id={`grade-${module.id}`}
                                    placeholder="Note"
                                    value={moduleGrades[index].grade}
                                    onChange={(e) => handleGradeChange(index, e.target.value)}
                                    step="0.1"
                                    min="1.0"
                                    max="5.0"
                                    disabled={allNotes[module.moduleId] !== undefined}
                                />
                            </div>
                        </div>
                    ))
                )}
                <button type="button" className="btn btn-primary" onClick={saveGrades}>Noten speichern</button>
                {averageGrade > 0 && <h2 className="mt-3">Durchschnittsnote: {averageGrade}</h2>}
            </div>
        </div>
    );
}

export default Notenverwaltung;
