// Dashboard.js

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from './Header';
import DashboardService from './DashboardService';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faBookmark, faCheck } from '@fortawesome/free-solid-svg-icons';

function Dashboard() {
    const [filter, setFilter] = useState('all');
    const [modules, setModules] = useState([]);
    const [bookedModules, setBookedModules] = useState([]);
    const [merkliste, setMerkliste] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadModules = async () => {
            try {
                const data = await DashboardService.fetchModules();
                setModules(data);
            } catch (error) {
                console.error('Fehler beim Laden der Module:', error);
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
                console.error('Fehler beim Laden der gebuchten Module:', error);
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
                alert(message);
                const updatedBookedModules = await DashboardService.fetchBookedModules(sessionId);
                setBookedModules(updatedBookedModules);
            } catch (error) {
                console.error('Fehler beim Buchen des Moduls:', error);
                alert(error.message);
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
                setBookedModules(updatedBookedModules); // Korrekte Aktualisierung der gebuchten Module
            } catch (error) {
                console.error('Fehler beim Entfernen des Moduls:', error);
                alert(error.message);
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
                // Filter die Module, die in der Merkliste vorhanden sind
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
        navigate('/module');
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
            } catch (error) {
                console.error('Fehler beim Hinzufügen zur Merkliste:', error);
                alert(error.message);
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
            } catch (error) {
                console.error('Fehler beim Entfernen aus der Merkliste:', error);
                alert(error.message);
            }
        }
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
                    <button className="icon-button" onClick={handleAddModule} style={{ background: 'none', border: 'none', cursor: 'pointer' }}>
                        <FontAwesomeIcon icon={faPlus} size="2x"/>
                    </button>
                </div>
                <ul className="list-group">
                    {filteredModules().map(module => (
                        <li key={module.moduleId}
                            className="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <div>{module.name}</div>
                                <div>Ects: {module.ects}</div>
                                <div>Dozent: {module.dozent}</div>
                                <div>Min. Semester: {module.minSemester}</div>
                            </div>
                            <div className="d-flex align-items-center">
                                {
                                    merkliste.some(mlItem => mlItem.moduleId === module.moduleId) ? (
                                        <FontAwesomeIcon icon={faCheck} onClick={() => handleRemoveFromMerkliste(module.moduleId)} className="me-4" />
                                    ) : (
                                        <FontAwesomeIcon icon={faBookmark} onClick={() => handleAddToMerkliste(module.moduleId)} className="me-4" />
                                    )
                                }
                                {filter === 'booked' ? (
                                    <button className="btn btn-danger" onClick={() => handleRemove(module.moduleId)}>Entfernen</button>
                                ) : (
                                    <React.Fragment>
                                        {module.isBooked ? (
                                            <button className="btn btn-danger" onClick={() => handleRemove(module.moduleId)}>Entfernen</button>
                                        ) : (
                                            <button className="btn btn-primary" onClick={() => handleEnroll(module.moduleId)}>Buchen</button>
                                        )}
                                    </React.Fragment>
                                )}
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );

}

export default Dashboard;
