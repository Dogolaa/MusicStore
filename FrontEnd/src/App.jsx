import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './Components/Header/Header';
import './App.css';


const App = () => {
  return (
    <Router>
      <div className="app-container">
        <Header />
        <main className="main-content">
          <h1>MusicStore Admin Control Panel</h1>
          <p>MusicStore Admin Control Panel - Copyright © MusicStore</p>
        </main>
      </div>
    </Router>
  );
};

export default App;
