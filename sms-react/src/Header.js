import React, { useState, useEffect, useRef } from 'react';
import { fetchNutzerInfo, logout } from './authService';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import data from "bootstrap/js/src/dom/data"; // Importieren des Haus-Symbols

function Header({ onNutzerNameUpdate }) {
    const [sessionId, setSessionId] = useState(localStorage.getItem('sessionId') || null);
    const [nutzername, setNutzername] = useState('');
    const [nutzerData, setNutzerData] = useState({
        nutzername: '',
        email: '',
        semester: 0  // Semester als Zahl
    });
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchNutzerName();

    }, [onNutzerNameUpdate]);

    const fetchNutzerName = async () => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            try {
                const info = await fetchNutzerInfo(sessionId);
                setNutzerData(info);
                setNutzername(info.nutzername);

                onNutzerNameUpdate(info.nutzername);

            } catch (error) {
                console.error('Fehler beim Laden der Nutzerinformationen:', error);
            }
        }
    };

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setDropdownOpen(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [dropdownRef]);

    const handleLogout = async () => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            try {
                await logout(sessionId);
                localStorage.removeItem('sessionId');
                setNutzername('');
                navigate('/auth');
            } catch (error) {
                console.error('Fehler beim Logout:', error);
            }
        }
    };

    const toggleDropdown = () => {
        setDropdownOpen(!dropdownOpen);
    };

    const dropdownStyle = {
        right: '0px',
        left: 'auto',
        position: 'absolute', // Stellen Sie sicher, dass das Dropdown-Menü richtig positioniert wird
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" href="#" onClick={() => navigate('/dashboard')}>
                    <FontAwesomeIcon icon={faHome} /> {/* FontAwesome Haus-Symbol */}
                </a>
                <div className="collapse navbar-collapse justify-content-between" id="navbarNavDropdown">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <a className="nav-link" href="#" onClick={() => navigate('/notenverwaltung')}>Notenverwaltung</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" href="#" onClick={() => navigate('/klausurverwaltung')}>Klausurverwaltung</a>
                        </li>
                        {/* Hinzugefügte Navigation zum Kanban Board */}
                        <li className="nav-item">
                            <a className="nav-link" href="#" onClick={() => navigate('/kanban')}>Kanban Board</a>
                        </li>
                    </ul>
                    <ul className="navbar-nav">
                        <li className="nav-item dropdown" ref={dropdownRef}>
                            <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
                               onClick={toggleDropdown} aria-haspopup="true"
                               aria-expanded={dropdownOpen ? "true" : "false"}>
                                {nutzername ? nutzername : 'Nutzer'}
                            </a>
                            <div className={"dropdown-menu dropdown-menu-right" + (dropdownOpen ? " show" : "")} aria-labelledby="navbarDropdownMenuLink" style={dropdownStyle}>
                                <a className="dropdown-item" href="/kontodaten">Kontodaten ändern</a>
                                <a className="dropdown-item" href="#" onClick={handleLogout}>Logout</a>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default Header;
