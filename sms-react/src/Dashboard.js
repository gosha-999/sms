// Dashboard.js

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from './Header';
import DashboardService from './DashboardService';
import ModuleService from "./ModuleService";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faBookmark, faCheck, faTrash } from '@fortawesome/free-solid-svg-icons';


function Dashboard() {
    const [filter, setFilter] = useState('all');
    const [modules, setModules] = useState([]);
    const [bookedModules, setBookedModules] = useState([]);
    const [merkliste, setMerkliste] = useState([]);
    const navigate = useNavigate();
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const loadModules = async () => {
            try {
                const data = await DashboardService.fetchModules();
                setModules(data);
            } catch (error) {
                console.error('Fehler beim Laden der AddModule:', error);
            }
        };

        const loadBookedModules = async () => {
            try {
                const sessionId = localStorage.getItem('sessionId');
                if (sessionId) {
                    const data = await DashboardService.fetchBookedModules(sessionId);
                    setBookedModules(data);
                }
            } catch (error) {
                console.error('Fehler beim Laden der gebuchten AddModule:', error);
            }
        };

        const loadMerkliste = async () => {
            try {
                const sessionId = localStorage.getItem('sessionId');
                if (sessionId) {
                    const data = await DashboardService.fetchMerkliste(sessionId);
                    setMerkliste(data);
                }
            } catch (error) {
                console.error('Fehler beim Laden der Merkliste:', error);
            }
        };

        loadModules();
        loadBookedModules();
        loadMerkliste();
    }, []);

    const handleEnroll = async (moduleId) => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            try {
                const message = await DashboardService.enrollInModule(sessionId, moduleId);
                setSuccessMessage("Module erfolgreich gebucht")
                setTimeout(() => setSuccessMessage(''), 3000);
                const updatedBookedModules = await DashboardService.fetchBookedModules(sessionId);
                setBookedModules(updatedBookedModules);

            } catch (error) {
                console.error('Fehler beim Buchen des Moduls:', error);
                setErrorMessage("Fehler beim Buchen des Moduls")
                setTimeout(() => setErrorMessage(''), 3000);
            }
        }
    };

    const handleRemove = async (moduleId) => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            try {
                console.log(sessionId);
                console.log(moduleId);
                await DashboardService.removeBookedModule(sessionId, moduleId);
                const updatedBookedModules = await DashboardService.fetchBookedModules(sessionId);
                setBookedModules(updatedBookedModules); // Korrekte Aktualisierung der gebuchten AddModule
                setSuccessMessage("Module wurde erfolgreich entfernt")
                setTimeout(() => setSuccessMessage(''), 3000);
            } catch (error) {
                console.error('Fehler beim Entfernen des Moduls:', error);
                setErrorMessage("Fehler beim Enfernen des Moduls")
                setTimeout(() => setErrorMessage(''), 3000);
            }
        }
    };





    const filteredModules = () => {
        switch (filter) {
            case 'booked':
                return modules.filter(module =>
                    bookedModules.some(bookedModule => bookedModule.moduleId === module.moduleId)
                ).map(module => {
                    const bookedModule = bookedModules.find(bookedModule => bookedModule.moduleId === module.moduleId);
                    return { ...module, bookedModuleId: bookedModule.id };
                });
            case 'wishlist':
                // Filter die AddModule, die in der Merkliste vorhanden sind
                return modules.filter(module =>
                    merkliste.some(merklisteItem => merklisteItem.moduleId === module.moduleId)
                ).map(module => {
                    // Hier kannst du zusätzliche Informationen oder Anpassungen hinzufügen,
                    // ähnlich wie im 'booked' Fall, wenn nötig
                    const isBooked = bookedModules.some(bookedModule => bookedModule.moduleId === module.moduleId);
                    return { ...module, isBooked, isInMerkliste: true };
                });
            default:
                return modules.map(module => {
                    const isBooked = bookedModules.some(bookedModule => bookedModule.moduleId === module.moduleId);
                    return { ...module, isBooked };
                });
        }
    };

    const handleAddModule = () => {
        navigate('/addmodule');
        console.log("Öffnen des Formulars zum Hinzufügen eines neuen Moduls");
    };

    const handleAddToMerkliste = async (moduleId) => {
        const sessionId = localStorage.getItem('sessionId');
        console.log(`Adding to wishlist: ${moduleId} for session: ${sessionId}`); // Zum Debuggen hinzugefügt
        if (sessionId) {
            try {
                await DashboardService.addToMerkliste(sessionId, moduleId);
                const updatedMerkliste = await DashboardService.fetchMerkliste(sessionId);
                setMerkliste(updatedMerkliste);
                setSuccessMessage("Module wurde zur Merkliste hinzugefügt")
                setTimeout(() => setSuccessMessage(''), 3000);
            } catch (error) {
                console.error('Fehler beim Hinzufügen zur Merkliste:', error);
                setErrorMessage("Fehler beim Hinzufügen zur Merkliste")
                setTimeout(() => setErrorMessage(''), 3000);
            }
        }
    };

    const handleRemoveFromMerkliste = async (moduleId) => {
        const sessionId = localStorage.getItem('sessionId');
        console.log(`Removing from wishlist: ${moduleId} for session: ${sessionId}`); // Zum Debuggen hinzugefügt
        if (sessionId) {
            try {
                await DashboardService.removeFromMerkliste(sessionId, moduleId);
                const updatedMerkliste = await DashboardService.fetchMerkliste(sessionId);
                setMerkliste(updatedMerkliste);
                setSuccessMessage("Module von Merkliste entfernt")
                setTimeout(() => setSuccessMessage(''), 3000);
            } catch (error) {
                console.error('Fehler beim Entfernen aus der Merkliste:', error);
                setSuccessMessage("Fehler beim Entfernen aus der Merkliste")
                setTimeout(() => setSuccessMessage(''), 3000);
            }
        }
    };

    const handleDeleteModule = async (moduleId) => {
        const sessionId = localStorage.getItem('sessionId');
        if (!sessionId) {
            alert('Sie sind nicht angemeldet.');
            return;
        }

        try {
            const confirmationMessage = await DashboardService.deleteModule(sessionId, moduleId);
            const updatedModules = await DashboardService.fetchModules();
            setModules(updatedModules);
            setSuccessMessage("Module erfolgreich gelöscht")
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (error) {
            console.error('Fehler beim Löschen des Moduls:', error);
            setErrorMessage("Modul ist belegt.")
            setTimeout(() => setErrorMessage(''), 3000);
        }
    };


    // Funktion zur Navigation zur Detailansicht eines Moduls
    const navigateToModuleDetail = (moduleId) => {
        navigate(`/moduledetail/${moduleId}`);
    };


    return (
        <div>
            <Header />
            <div className="container mt-5">
                <h2>Module</h2>
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div className="btn-group" role="group" aria-label="Filter">
                        <button type="button" className={`btn btn-secondary ${filter === 'all' ? 'active' : ''}`}
                                onClick={() => setFilter('all')}>Alle Module
                        </button>
                        <button type="button" className={`btn btn-secondary ${filter === 'booked' ? 'active' : ''}`}
                                onClick={() => setFilter('booked')}>Gebuchte Module
                        </button>
                        <button type="button" className={`btn btn-secondary ${filter === 'wishlist' ? 'active' : ''}`}
                                onClick={() => setFilter('wishlist')}>Merkliste
                        </button>
                    </div>

                    <button className="icon-button" onClick={handleAddModule}
                            style={{background: 'none', border: 'none', cursor: 'pointer'}}>
                        <FontAwesomeIcon icon={faPlus} size="2x"/>
                    </button>

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
                {filteredModules().length === 0 && <p>Keine Module verfügbar.</p>}
                <ul className="list-group">
                    {filteredModules().map(module => (
                        <li key={module.moduleId}
                            className="list-group-item d-flex justify-content-between align-items-center">
                            <div onClick={() => navigateToModuleDetail(module.moduleId)}
                                 style={{cursor: 'pointer', flexGrow: 1}}>
                                <div style={{fontWeight: 'bold'}}>
                                    {module.name}
                                </div>
                                <div>Ects: {module.ects}</div>
                                <div>Dozent: {module.dozent}</div>
                                <div>Min. Semester: {module.minSemester}</div>
                            </div>
                            <div className="d-flex align-items-center">
                                {
                                    merkliste.some(mlItem => mlItem.moduleId === module.moduleId) ? (
                                        <FontAwesomeIcon icon={faCheck}
                                                         onClick={() => handleRemoveFromMerkliste(module.moduleId)}
                                                         className="me-4"/>
                                    ) : (
                                        <FontAwesomeIcon icon={faBookmark}
                                                         onClick={() => handleAddToMerkliste(module.moduleId)}
                                                         className="me-4"/>
                                    )
                                }
                                {filter === 'booked' ? (
                                    <button className="btn btn-danger me-2"
                                            onClick={() => handleRemove(module.moduleId)}>Entfernen</button>
                                ) : (
                                    <>
                                        {module.isBooked ? (
                                            <button className="btn btn-danger me-2"
                                                    onClick={() => handleRemove(module.moduleId)}>Entfernen</button>
                                        ) : (
                                            <button className="btn btn-primary me-2"
                                                    onClick={() => handleEnroll(module.moduleId)}>Buchen</button>
                                        )}
                                    </>
                                )}
                                <FontAwesomeIcon icon={faTrash} className="text-danger ms-2" style={{cursor: 'pointer'}}
                                                 onClick={() => handleDeleteModule(module.moduleId)}/>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );


}

export default Dashboard;
