import React from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate,
} from "react-router-dom";
import PainelAdm from "./Screens/PainelAdm/PainelAdm";
import ProductList from "./Screens/ProductList/ProductList";
import AddProduct from "./Screens/AddProduct/AddProduct";
import EditProduct from "./Screens/EditProduct/EditProduct";
import ViewProduct from "./Screens/ViewProduct/ViewProduct";
import UserList from "./Screens/Users/UserList/UserList";
import AddUser from "./Screens/Users/AddUser/AddUser";
import EditUser from "./Screens/Users/EditUser/EditUser";
import LoginPage from "./Screens/LoginPage/LoginPage";
import HomePage from "./Screens/HomePage/HomePage";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />

                {/* Painel LOGIN */}
                <Route path="/login" element={<LoginPage />} />

                {/* Painel HOME */}
                <Route path="/home" element={<HomePage />} />

                {/* Painel ADM */}
                <Route path="/admin" element={<PainelAdm />} />

                {/* Gerenciamento de produtos */}
                <Route path="/admin/products" element={<ProductList />} />
                <Route path="/admin/products/add" element={<AddProduct />} />
                <Route
                    path="/admin/products/edit/:id"
                    element={<EditProduct />}
                />
                <Route
                    path="/admin/products/view/:id"
                    element={<ViewProduct />}
                />
                {/* Gerenciamento de usuários */}
                <Route path="/admin/users" element={<UserList />} />
                <Route path="/admin/users/add" element={<AddUser />} />
                <Route path="/admin/users/edit/:id" element={<EditUser />} />
            </Routes>
        </Router>
    );
};

export default App;
