import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import AuthPage from './AuthPage'; // Angenommen, Login und Register sind hier kombiniert
import Dashboard from './Dashboard';
import Module from "./Module";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/auth" element={<AuthPage />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/module" element={<Module />} />
                <Route path="/" element={<Navigate replace to="/auth" />} />
            </Routes>
        </Router>
    );
}


export default App;
