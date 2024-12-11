import React from "react";
import { Link } from "react-router-dom";
import Header from "../../Components/Header/Header";
import "./HomePage.css";

const HomePage = () => {
    return (
        <div className="app-container">
            <Header />
            <main className="main-content">
                <h1>MusicStore User Control Panel</h1>
                <nav className="admin-nav">
                    <ul>
                        <li>
                            <Link to="/login">Logout</Link>
                        </li>
                    </ul>
                </nav>
                <p>Copyright © MusicStore</p>
            </main>
        </div>
    );
};

export default HomePage;
