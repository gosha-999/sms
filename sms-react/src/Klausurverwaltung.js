import React, { useState, useEffect } from 'react';
import ModuleService from './ModuleService';
import DashboardService from './DashboardService';
import Header from "./Header";

function Klausurverwaltung() {
    const [gebuchteModule, setGebuchteModule] = useState([]);
    const [alleKlausurTermine, setAlleKlausurTermine] = useState({});
    const [gebuchteTermine, setGebuchteTermine] = useState([]);
    const sessionId = localStorage.getItem('sessionId');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

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

    useEffect(() => {
        if (!sessionId) return;
        fetchData();
    }, [sessionId]);

    const handleEinschreiben = async (klausurTerminId, modulId) => {
        try {
            await ModuleService.bucheKlausurTermin(klausurTerminId, sessionId);
            setSuccessMessage('Klausurtermin erfolgreich gebucht.');
            setTimeout(() => setSuccessMessage(''), 3000);

            const gebuchterTermin = alleKlausurTermine[modulId].find(termin => termin.klausurTerminId === klausurTerminId);
            setGebuchteTermine(prev => [...prev, gebuchterTermin]);

            const updatedTermine = alleKlausurTermine[modulId].filter(termin => termin.klausurTerminId !== klausurTerminId);
            setAlleKlausurTermine(prev => ({ ...prev, [modulId]: updatedTermine }));

            if (updatedTermine.length === 0) {
                setGebuchteModule(prev => prev.filter(modul => modul.moduleId !== modulId));
            }
        } catch (error) {
            setErrorMessage(error.response.data || 'Ein Fehler ist aufgetreten.');
            setTimeout(() => setErrorMessage(''), 3000);
        }
    };

    // Überprüfen Sie, ob für ein Modul bereits ein Termin gebucht wurde
    const isTerminGebucht = (modulId) => {
        return gebuchteTermine.some(termin => termin.modulId === modulId);
    };

    return (
        <div>
            <Header />
            <div className="container mt-5">
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

                <div className="card bg-light mb-4">
                    <h2 className="card-header text-white bg-primary">Gebuchte Module mit verfügbaren Klausurterminen</h2>
                    <div className="card-body">
                        {gebuchteModule.length === 0 ? (
                            <p>Keine gebuchten Module verfügbar.</p>
                        ) : (
                            gebuchteModule.map((modul) => (
                                <div key={modul.moduleId} className="mt-4">
                                    <h3>{modul.name}</h3>
                                    {alleKlausurTermine[modul.moduleId]?.map((termin) => {
                                        const terminGebucht = isTerminGebucht(termin.klausurTerminId);
                                        return (
                                            <div key={termin.klausurTerminId} className="card mb-3">
                                                <div className="card-body">
                                                    <h5 className="card-title">{termin.klausurName}</h5>
                                                    <p className="card-text">Datum: {termin.datum}</p>
                                                    <p className="card-text">Verbleibende Plätze: {termin.verbleibendePlätze}</p>
                                                    <button
                                                        className={`btn btn-${terminGebucht ? 'secondary' : 'primary'}`}
                                                        onClick={() => handleEinschreiben(termin.klausurTerminId, modul.moduleId)}
                                                        disabled={terminGebucht}
                                                    >
                                                        Einschreiben
                                                    </button>
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            ))
                        )}
                    </div>
                </div>

                <div className="card bg-light">
                    <h2 className="card-header text-white bg-primary">Meine gebuchten Klausurtermine</h2>
                    <div className="card-body">
                        {gebuchteTermine.length === 0 ? (
                            <p>Keine gebuchten Klausurtermine verfügbar.</p>
                        ) : (
                            gebuchteTermine.map((termin) => (
                                <div key={termin.klausurTerminId} className="card mb-3">
                                    <div className="card-body">
                                        <h5 className="card-title">{termin.klausurName}</h5>
                                        <p className="card-text">Datum: {termin.datum}</p>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>

            </div>
        </div>
    );


}

export default Klausurverwaltung;
