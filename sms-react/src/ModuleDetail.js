import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Header from './Header';
import { getTasksByModuleId } from "./TaskService"; // Verwende getTasksByModuleId aus TaskService
import ModuleService from "./ModuleService";

function ModuleDetail() {
    const [moduleDetails, setModuleDetails] = useState(null);
    const [klausurTermine, setKlausurTermine] = useState([]);
    const [tasks, setTasks] = useState([]);
    const { moduleId } = useParams();

    useEffect(() => {
        const fetchModuleDetails = async () => {
            try {
                const data = await ModuleService.fetchModuleById(moduleId);
                setModuleDetails(data);
            } catch (error) {
                console.error('Fehler beim Laden der Moduldetails:', error);
            }
        };

        const fetchKlausurTermine = async () => {
            try {
                const termine = await ModuleService.fetchKlausurTermineByModuleId(moduleId);
                setKlausurTermine(termine);
            } catch (error) {
                console.error('Fehler beim Laden der Klausurtermine:', error);
            }
        };

        // Verwendung der aktualisierten Funktion aus TaskService
        const fetchTasksByModuleId = async () => {
            try {
                const tasksData = await getTasksByModuleId(moduleId);
                setTasks(tasksData);
            } catch (error) {
                console.error('Fehler beim Laden der Tasks:', error);
            }
        };

        fetchModuleDetails();
        fetchKlausurTermine();
        fetchTasksByModuleId();
    }, [moduleId]);

    return (
        <div>
            <Header />
            <div className="container py-5">
                {/* Moduldetails */}
                <div className="card shadow mb-4">
                    {/* Header */}
                    <div className="card-header text-white bg-primary">
                        <h2 className="mb-0">{moduleDetails?.name}</h2>
                    </div>
                    {/* Body */}
                    <div className="card-body">
                        <p className="card-text"><strong>Beschreibung:</strong> {moduleDetails?.beschreibung}</p>
                        <p className="card-text"><strong>ECTS:</strong> {moduleDetails?.ects}</p>
                        <p className="card-text"><strong>Dozent:</strong> {moduleDetails?.dozent}</p>
                        <p className="card-text"><strong>Lehrstuhl:</strong> {moduleDetails?.lehrstuhl}</p>
                        <p className="card-text"><strong>Regeltermin:</strong> {moduleDetails?.regeltermin}</p>
                        <p className="card-text"><strong>Literaturempfehlung:</strong> {moduleDetails?.literaturempfehlung}</p>
                        <p className="card-text"><strong>Mindestsemester:</strong> {moduleDetails?.minSemester}</p>
                        <p className="card-text"><strong>Durchschnittliche Bewertung:</strong> {moduleDetails?.durchschnittlicheBewertung}</p>
                        <p className="card-text"><strong>Anzahl Bewertungen:</strong> {moduleDetails?.anzahlBewertungen}</p>
                    </div>
                </div>

                {/* Klausurtermine Container */}
                {klausurTermine.length > 0 && (
                    <div className="card shadow mb-4">
                        <div className="card-header text-white bg-secondary">
                            Klausurtermine
                        </div>
                        <ul className="list-group list-group-flush">
                            {klausurTermine.map((termin, index) => (
                                <li key={index} className="list-group-item">
                                    Datum: {termin.datum}, Name: {termin.klausurName}, Verbleibende Plätze: {termin.verbleibendePlätze}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {/* Tasks Container */}
                {tasks.length > 0 && (
                    <div className="card shadow">
                        <div className="card-header text-white bg-info">
                            Tasks
                        </div>
                        <ul className="list-group list-group-flush">
                            {tasks.map((task, index) => (
                                <li key={index} className="list-group-item">
                                    Titel: {task.title}, Beschreibung: {task.description}, Deadline: {task.deadline}, Priorität: {task.priority}, Status: {task.status}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );

}

export default ModuleDetail;