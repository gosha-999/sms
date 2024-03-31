import React from 'react';
import Login from './Login';
import Register from './Register';

function AuthPage() {

    const backgroundStyle = {
        backgroundImage: `url('/background.png')`,
        backgroundSize: 'cover', // Damit das Bild den ganzen Container bedeckt
        backgroundPosition: 'center', // Zentriert das Bild
        backgroundRepeat: 'no-repeat', // Verhindert das Wiederholen des Bildes
        height: '100vh', // Stellt sicher, dass der Hintergrund die volle Höhe des Viewports einnimmt
        width: '100vw' // Stellt sicher, dass der Hintergrund die volle Breite des Viewports einnimmt
    };

    return (
        <div className="container mt-5"> {}
            <div className="row justify-content-center">
                <div className="col-md-6">
                    {}
                    <Login />
                    <div className="mt-3"> {}
                        <Register />
                    </div>
                </div>
            </div>
        </div>
    );
}
export default AuthPage;
