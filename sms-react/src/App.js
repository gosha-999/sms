import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import AuthPage from './AuthPage'; // Angenommen, Login und Register sind hier kombiniert
import Dashboard from './Dashboard';
import AddModule from "./AddModule";
import ModuleDetail from "./ModuleDetail";
import AddKlausurTermine from "./AddKlausurTermine";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/auth" element={<AuthPage />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/addmodule" element={<AddModule />} />
                <Route path="/moduledetail/:moduleId" element={<ModuleDetail />} />
                <Route path="/add-klausur-termin/:moduleId" element={<AddKlausurTermine />} />
                <Route path="/" element={<Navigate replace to="/auth" />} />
            </Routes>
        </Router>
    );
}


export default App;
