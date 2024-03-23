import React, { useState, useEffect } from 'react';
import ModuleService from './ModuleService';
import DashboardService from './DashboardService';
import Header from "./Header";

function Klausurverwaltung() {
    const [gebuchteModule, setGebuchteModule] = useState([]);
    const [alleKlausurTermine, setAlleKlausurTermine] = useState({});
    const [gebuchteTermine, setGebuchteTermine] = useState([]);
    const sessionId = localStorage.getItem('sessionId');

    useEffect(() => {
        if (!sessionId) return;

        const fetchData = async () => {
            const gebuchteModuleData = await DashboardService.fetchBookedModules(sessionId);
            const gebuchteKlausurTermine = await ModuleService.fetchBookedKlausurTermine(sessionId);
            const moduleTerminePromises = gebuchteModuleData.map(modul =>
                ModuleService.fetchKlausurTermineByModuleId(modul.moduleId)
            );
            const moduleTermineResults = await Promise.all(moduleTerminePromises);

            let tempAlleKlausurTermine = {};
            moduleTermineResults.forEach((termine, index) => {
                const filteredTermine = termine.filter(termin =>
                    !gebuchteKlausurTermine.some(gebuchterTermin => gebuchterTermin.klausurTerminId === termin.klausurTerminId)
                );
                if (filteredTermine.length > 0) {
                    tempAlleKlausurTermine[gebuchteModuleData[index].moduleId] = filteredTermine;
                }
            });

            setAlleKlausurTermine(tempAlleKlausurTermine);
            setGebuchteModule(gebuchteModuleData.filter(modul => tempAlleKlausurTermine[modul.moduleId]));
            setGebuchteTermine(gebuchteKlausurTermine);
        };

        fetchData();
    }, [sessionId]);


    const handleEinschreiben = async (klausurTerminId, modulId) => {
        try {
            await ModuleService.bucheKlausurTermin(klausurTerminId, sessionId);
            alert('Klausurtermin erfolgreich gebucht.');

            const gebuchterTermin = alleKlausurTermine[modulId].find(termin => termin.klausurTerminId === klausurTerminId);
            setGebuchteTermine(prev => [...prev, gebuchterTermin]);

            const updatedTermine = alleKlausurTermine[modulId].filter(termin => termin.klausurTerminId !== klausurTerminId);
            setAlleKlausurTermine(prev => ({ ...prev, [modulId]: updatedTermine }));

            if (updatedTermine.length === 0) {
                setGebuchteModule(prev => prev.filter(modul => modul.moduleId !== modulId));
            }
        } catch (error) {
            console.error('Fehler beim Buchen des Klausurtermins:', error);
            alert('Fehler beim Buchen des Klausurtermins.');
        }
    };

    return (
        <div>
            <Header />
            <div className="container mt-5">
                <h2 className="card header text-white bg-primary p-2">Gebuchte Module mit verfügbaren Klausurterminen</h2>
                {gebuchteModule.length === 0 ? <p>Keine gebuchten Module verfügbar.</p> : gebuchteModule.map(modul => (
                    <div key={modul.moduleId} className="mt-4">
                        <h3>{modul.name}</h3>
                        {alleKlausurTermine[modul.moduleId]?.map(termin => (
                            <div key={termin.klausurTerminId} className="card mb-3">
                                <div className="card-body">
                                    <h5 className="card-title">{termin.klausurName}</h5>
                                    <p className="card-text">Datum: {termin.datum}</p>
                                    <p className="card-text">Verbleibende Plätze: {termin.verbleibendePlätze}</p>
                                    <button className="btn btn-primary" onClick={() => handleEinschreiben(termin.klausurTerminId, modul.moduleId)}>Einschreiben</button>
                                </div>
                            </div>
                        ))}
                    </div>
                ))}

                <h2 className="card header text-white bg-primary p-2 mt-4">Meine gebuchten Klausurtermine</h2>
                {gebuchteTermine.length === 0 ? <p>Keine gebuchten Klausurtermine verfügbar.</p> : gebuchteTermine.map(termin => (
                    <div key={termin.klausurTerminId} className="card mb-3">
                        <div className="card-body">
                            <h5 className="card-title">{termin.klausurName}</h5>
                            <p className="card-text">Datum: {termin.datum}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Klausurverwaltung;
