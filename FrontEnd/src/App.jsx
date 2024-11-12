import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import PainelAdm from './Screens/PainelAdm';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/admin" replace />} />
        <Route path="/admin" element={<PainelAdm />} />
      </Routes>
    </Router>
  );
};

export default App;
