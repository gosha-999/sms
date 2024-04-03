import React, { useState, useEffect } from 'react';
import DashboardService from './DashboardService';
import ModuleService from "./ModuleService";
import Header from "./Header";

function Notenverwaltung() {
    const [bookedModules, setBookedModules] = useState([]);
    const [moduleGrades, setModuleGrades] = useState([]);
    const [averageGrade, setAverageGrade] = useState(0);
    const [allNotes, setAllNotes] = useState({});
    const [filterModus, setFilterModus] = useState('alle');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

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

        // Nachdem die initialen Daten geladen wurden, berechnen Sie die Durchschnittsnote
        await calculateAverage();
    };

    useEffect(() => {
        fetchData();
        calculateAverage();
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
            setSuccessMessage('Noten erfolgreich gespeichert!');
            setTimeout(() => setSuccessMessage(''), 3000);

            // Neu laden der Daten und erneutes Berechnen der Durchschnittsnote
            await fetchData();
            await calculateAverage();
        } catch (error) {
            console.error('Fehler beim Speichern der Noten:', error);
            setErrorMessage("Fehler beim Speichern der Note");
            setTimeout(() => setErrorMessage(''), 3000);
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

    let filteredModules = [];
    if (filterModus === 'alle') {
        filteredModules = bookedModules;
    } else if (filterModus === 'benotet') {
        filteredModules = bookedModules.filter(module => allNotes[module.moduleId] !== undefined);
    } else if (filterModus === 'nicht benotet') {
        filteredModules = bookedModules.filter(module => allNotes[module.moduleId] === undefined);
    }

    return (
        <div>
            <Header />
            <div className="container mt-5">
                <div className="card bg-light mb-4">
                    <h2 className="card-header text-white bg-primary">Notenübersicht</h2>
                    <div className="card-body">
                        <div className="btn-group mb-3" role="group" aria-label="Filter">
                            <button
                                type="button"
                                className={`btn ${filterModus === 'alle' ? 'btn-primary' : 'btn-outline-primary'}`}
                                onClick={() => setFilterModus('alle')}
                            >
                                Alle Module
                            </button>
                            <button
                                type="button"
                                className={`btn ${filterModus === 'benotet' ? 'btn-primary' : 'btn-outline-primary'}`}
                                onClick={() => setFilterModus('benotet')}
                            >
                                Benotete Module
                            </button>
                            <button
                                type="button"
                                className={`btn ${filterModus === 'nicht benotet' ? 'btn-primary' : 'btn-outline-primary'}`}
                                onClick={() => setFilterModus('nicht benotet')}
                            >
                                Nicht benotete Module
                            </button>
                        </div>

                        {successMessage && <div className="alert alert-success" role="alert">{successMessage}</div>}
                        {errorMessage && <div className="alert alert-danger" role="alert">{errorMessage}</div>}

                        {filteredModules.length === 0 ? (
                            <p>Keine gebuchten Module verfügbar.</p>
                        ) : (
                            filteredModules.map((module, index) => (
                                <div key={module.moduleId} className="card mb-2">
                                    <div className="card-header">
                                        <strong>{module.name}</strong> - ECTS: {module.ects}
                                    </div>
                                    <div className="card-body">
                                        <input
                                            type="number"
                                            className="form-control form-control-sm d-inline-block w-auto"
                                            id={`grade-${module.moduleId}`}
                                            placeholder="Note"
                                            value={moduleGrades.find(item => item.moduleId === module.moduleId)?.grade || ''}
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
                        <div className="mt-3">
                            <button type="button" className="btn btn-primary mr-2" onClick={saveGrades}>Noten speichern</button>
                            <div>Durchschnittsnote: {averageGrade > 0 ? averageGrade.toFixed(2) : "N/A"}</div>
                        </div>
                    </div>
            </div>
        </div>
        </div>
    );

}

export default Notenverwaltung;
