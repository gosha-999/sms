import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Header from './Header';
import ModuleService from "./ModuleService";

function ModuleDetail() {
    const [moduleDetails, setModuleDetails] = useState(null);
    const [klausurTermine, setKlausurTermine] = useState([]);
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
                console.log(termine); // Füge dies hinzu, um die abgerufenen Daten zu überprüfen
                setKlausurTermine(termine);
            } catch (error) {
                console.error('Fehler beim Laden der Klausurtermine:', error);
            }
        };


        fetchModuleDetails();
        fetchKlausurTermine();
    }, [moduleId]);

    return (
        <div>
            <Header />
            <div className="container py-5">
                <div className="card shadow">
                    <div className="card-header text-white bg-primary">
                        <h2 className="mb-0">{moduleDetails?.name}</h2>
                    </div>
                    <div className="card-body">
                        {/* Der Rest der Modulinformationen */}
                        <p className="card-text"><strong>Beschreibung:</strong> {moduleDetails?.beschreibung}</p>
                        <p className="card-text"><strong>ECTS:</strong> {moduleDetails?.ects}</p>
                        <p className="card-text"><strong>Dozent:</strong> {moduleDetails?.dozent}</p>
                        <p className="card-text"><strong>Lehrstuhl:</strong> {moduleDetails?.lehrstuhl}</p>
                        <p className="card-text"><strong>Regeltermin:</strong> {moduleDetails?.regeltermin}</p>
                        <p className="card-text"><strong>Literaturempfehlung:</strong> {moduleDetails?.literaturempfehlung}</p>
                        <p className="card-text"><strong>Mindestsemester:</strong> {moduleDetails?.minSemester}</p>
                        <p className="card-text"><strong>Durchschnittliche Bewertung:</strong> {moduleDetails?.durchschnittlicheBewertung}</p>
                        <p className="card-text"><strong>Anzahl Bewertungen:</strong> {moduleDetails?.anzahlBewertungen}</p>
                        {/* Weitere Details können hier angezeigt werden */}
                    </div>
                </div>
                {/* Klausurtermine Container */}
                {klausurTermine.length > 0 && (
                    <div className="card mt-4 shadow">
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
            </div>
        </div>
    );

}

export default ModuleDetail;
