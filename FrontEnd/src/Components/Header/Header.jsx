import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <header className="header">
      <div className="logo">
        <Link to="/" className="logo-link">
          <img src="/path/to/logo.png" alt="Music Store Logo" className="logo-image" />
        </Link>
      </div>
      <div className="menu-icon" onClick={toggleMenu}>
        ☰
      </div>
      <nav className={isOpen ? "nav-links show" : "nav-links"}>
        <Link to="/articles">Articles</Link>
        <Link to="/brands">Brands</Link>
        <Link to="/categories">Categories</Link>
        <Link to="/customers">Customers</Link>
        <Link to="/menus">Menus</Link>
        <Link to="/orders">Orders</Link>
        <Link to="/admin/products">Products</Link>
        <Link to="/sales-report">Sales Report</Link>
        <Link to="/settings">Settings</Link>
        <Link to="/shipping">Shipping</Link>
        <Link to="/users">Users</Link>
      </nav>
      {isOpen && (
        <div className="dropdown-menu">
          <Link to="/articles" onClick={toggleMenu}>Articles</Link>
          <Link to="/brands" onClick={toggleMenu}>Brands</Link>
          <Link to="/categories" onClick={toggleMenu}>Categories</Link>
          <Link to="/customers" onClick={toggleMenu}>Customers</Link>
          <Link to="/menus" onClick={toggleMenu}>Menus</Link>
          <Link to="/orders" onClick={toggleMenu}>Orders</Link>
          <Link to="/products" onClick={toggleMenu}>Products</Link>
          <Link to="/sales-report" onClick={toggleMenu}>Sales Report</Link>
          <Link to="/settings" onClick={toggleMenu}>Settings</Link>
          <Link to="/shipping" onClick={toggleMenu}>Shipping</Link>
          <Link to="/users" onClick={toggleMenu}>Users</Link>
        </div>
      )}
    </header>
  );
};

export default Header;
