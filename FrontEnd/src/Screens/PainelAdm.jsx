import React from 'react';
import Header from '../Components/Header/Header';
import './PainelAdm.css';

const PainelAdm = () => {
  return (
    <div className="app-container">
      <Header />
      <main className="main-content">
        <h1>MusicStore Admin Control Panel</h1>
        <p>MusicStore Admin Control Panel - Copyright © MusicStore</p>
      </main>
    </div>
  );
};

export default PainelAdm;
