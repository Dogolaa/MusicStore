import React from "react";
import { Link } from "react-router-dom";
import Header from "../../Components/Header/Header";
import "./PainelAdm.css";

const PainelAdm = () => {
    return (
        <div className="app-container">
            <Header />
            <main className="main-content">
                <h1>MusicStore Admin Control Panel</h1>
                <nav className="admin-nav">
                    <ul>
                        <li>
                            <Link to="/admin/products">Manage Products</Link>
                        </li>
                        <li>
                            <Link to="/admin/products/add">
                                Add New Product
                            </Link>
                        </li>
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

export default PainelAdm;
