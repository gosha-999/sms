import React, { useState, useEffect, useRef } from 'react';
import { fetchNutzerInfo, logout } from './authService';
import { useNavigate } from 'react-router-dom';

function Header() {
    const [sessionId, setSessionId] = useState(localStorage.getItem('sessionId') || null);
    const [nutzername, setNutzername] = useState('');
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchNutzerName(); // Laden Sie den Nutzernamen beim ersten Rendern
    }, []);

    const fetchNutzerName = async () => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            try {
                const info = await fetchNutzerInfo(sessionId);
                setNutzername(info.nutzername);
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
                // Aktualisieren den Nutzernamen im Header
                setNutzername('');
                // Navigieren Sie zur Authentifizierungsseite
                navigate('/auth');

            } catch (error) {
                console.error('Fehler beim Logout:', error);
            }
        }
    };

    const toggleDropdown = () => {
        setDropdownOpen(!dropdownOpen);
    };

    // Dropdown-Positionsstil basierend auf der Fensterbreite
    const dropdownStyle = {
        right: 0,
        left: 'auto',
    };

    if (dropdownRef.current) {
        const dropdownRect = dropdownRef.current.getBoundingClientRect();
        const spaceRight = window.innerWidth - dropdownRect.right;
        if (spaceRight < 0) {
            dropdownStyle.right = -spaceRight + 'px';
            dropdownStyle.left = 'auto';
        }
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" href="/">MeineApp</a>
                <div className="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
                    <ul className="navbar-nav">
                        <li className="nav-item dropdown" ref={dropdownRef}>
                            <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" onClick={toggleDropdown} aria-haspopup="true" aria-expanded={dropdownOpen ? "true" : "false"}>
                                {nutzername ? nutzername : 'Nutzer'}
                            </a>
                            <div className={"dropdown-menu" + (dropdownOpen ? " show" : "")} aria-labelledby="navbarDropdownMenuLink" style={dropdownStyle}>
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
