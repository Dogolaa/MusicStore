import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import PainelAdm from './Screens/PainelAdm/PainelAdm';
import ProductList from './Screens/ProductList/ProductList';
import AddProduct from './Screens/AddProduct/AddProduct';
import EditProduct from './Screens/EditProduct/EditProduct';
import ViewProduct from './Screens/ViewProduct/ViewProduct';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* Página inicial redirecionando para PainelAdm */}
        <Route path="/" element={<Navigate to="/admin" replace />} />

        {/* Painel ADM */}
        <Route path="/admin" element={<PainelAdm />} />

        {/* Gerenciamento de produtos */}
        <Route path="/admin/products" element={<ProductList />} />
        <Route path="/admin/products/add" element={<AddProduct />} />
        <Route path="/admin/products/edit/:id" element={<EditProduct />} />
        <Route path="/admin/products/view/:id" element={<ViewProduct />} />
      </Routes>
    </Router>
  );
};

export default App;
