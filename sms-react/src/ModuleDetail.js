import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Header from './Header';
import { getTasksByModuleId } from "./TaskService"; // Verwende getTasksByModuleId aus TaskService
import ModuleService from "./ModuleService";
import ReactStars from "react-rating-stars-component/dist/react-stars";

function ModuleDetail() {
    const [moduleDetails, setModuleDetails] = useState(null);
    const [klausurTermine, setKlausurTermine] = useState([]);
    const [tasks, setTasks] = useState([]);
    const { moduleId } = useParams();
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

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

    useEffect(() => {

        fetchModuleDetails();
        fetchKlausurTermine();
        fetchTasksByModuleId();
    }, [moduleId]);

    const handleRating = async (newRating) => {
        try {
            const sessionId = localStorage.getItem("sessionId"); // Annahme, dass die Session ID im localStorage gespeichert ist
            await ModuleService.rateModule(moduleId, sessionId, newRating);
            await fetchModuleDetails();
            setSuccessMessage("Modul erfolgreich bewertet")
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (error) {
            console.error('Fehler beim Hinzufügen der Bewertung:', error);
            setErrorMessage("Es können nur benotete Module bewertet werden.")
            setTimeout(() => setErrorMessage(''), 3000);
        }
    };

    return (
        <div>
            <Header />

            <div className="container py-5">
                {/* Moduldetails Karte */}
                <div className="card mb-3 shadow">

                    <div className="card-header bg-primary text-white">
                        <h3 className="mb-0">{moduleDetails?.name}</h3>
                    </div>
                    <div className="card-body bg-light">
                        {/* Aufteilung der Moduldetails in zwei Spalten */}
                        <div className="row mb-4">
                            <div className="col-md-6">
                                <p className="card-text"><strong>Dozent:</strong> {moduleDetails?.dozent}</p>
                                <p className="card-text"><strong>ECTS:</strong> {moduleDetails?.ects}</p>
                                <p className="card-text"><strong>Mindestsemester:</strong> {moduleDetails?.minSemester}
                                </p>
                            </div>
                            <div className="col-md-6">
                                <p className="card-text"><strong>Lehrstuhl:</strong> {moduleDetails?.lehrstuhl}</p>
                                <p className="card-text"><strong>Regeltermin:</strong> {moduleDetails?.regeltermin}</p>
                                <p className="card-text">
                                    <strong>Literaturempfehlung:</strong> {moduleDetails?.literaturempfehlung}</p>
                            </div>
                        </div>

                        {/* Trennlinie und Titel "Beschreibung" */}
                        <div className="row">
                            <div className="col">
                                <hr/>
                                <h5 className="text-center">Beschreibung</h5>
                            </div>
                        </div>

                        {/* Beschreibung */}
                        <div className="row mb-4">
                            <div className="col">
                                <p className="card-text">{moduleDetails?.beschreibung}</p>
                            </div>
                        </div>

                        <div className="container mt-4">
                            {successMessage && (
                                <div className="alert alert-success" role="alert">
                                    {successMessage}
                                </div>
                            )}
                            {errorMessage && (
                                <div className="alert alert-danger" role="alert">
                                    {errorMessage}
                                </div>
                            )}
                        </div>

                        {/* Durchschnittliche Bewertung und Möglichkeit zur Bewertung */}
                        <div className="row">
                            <div className="col">
                                <div className="d-flex flex-column align-items-start">
                                    <div className="mb-2">
                                        <span className="fw-bold text-primary">Durchschnittliche Bewertung:</span>
                                        <ReactStars
                                            key={moduleDetails?.durchschnittlicheBewertung}
                                            count={5}
                                            value={moduleDetails?.durchschnittlicheBewertung}
                                            edit={false}
                                            size={24}
                                            isHalf={true}
                                            activeColor="#ffd700"
                                        />
                                        <span className="ms-2">({moduleDetails?.anzahlBewertungen} Bewertungen)</span>
                                    </div>
                                    <div>
                                        <span className="fw-bold text-primary">Bewerte dieses Modul:</span>
                                        <ReactStars
                                            count={5}
                                            onChange={handleRating}
                                            size={24}
                                            isHalf={false} // Keine halben Sterne erlauben
                                            activeColor="#ffd700"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Klausurtermine Karte */}
                {klausurTermine.length > 0 && (
                    <div className="card mb-4 shadow">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Klausurtermine</h3>
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

                {/* Tasks Karte */}
                {tasks.length > 0 && (
                    <div className="card shadow">
                        <div className="card-header bg-primary text-white">
                            <h3 className="mb-0">Tasks</h3>
                        </div>
                        <ul className="list-group list-group-flush">
                            {tasks.map((task, index) => (
                                <li key={index} className="list-group-item">
                                    <span className="fw-bold">Titel:</span> {task.title} <br />
                                    <span className="fw-bold">Beschreibung:</span> {task.description} <br />
                                    <span className="fw-bold">Deadline:</span> {task.deadline} <br />
                                    <span className="badge rounded-pill bg-info text-dark">Priorität: {task.priority}</span>
                                    <span className="badge rounded-pill bg-secondary ms-2">Status: {task.status}</span>
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