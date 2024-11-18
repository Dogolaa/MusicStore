import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./ProductList.css";

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/api/products", {
      headers: { "api-key": "your-api-key" },
    })
      .then((res) => res.json())
      .then((data) => setProducts(data))
      .catch((err) => console.error(err));
  }, []);

  const handleDelete = (id) => {
    if (window.confirm("Are you sure you want to delete this product?")) {
      fetch(`http://localhost:8080/api/products/${id}`, {
        method: "DELETE",
        headers: { "api-key": "your-api-key" },
      })
        .then(() => setProducts(products.filter((product) => product.id !== id)))
        .catch((err) => console.error(err));
    }
  };

  return (
    <div className="product-list">
      <h1>BlueVelvet Music Store</h1>
      <button onClick={() => navigate("/admin/products/add")}>Add Product</button>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Image</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Category</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>
                <img
                  src={`http://localhost:8080/images/${product.product_main_photo}`}
                  alt={product.product_name}
                  width="50"
                />
              </td>
              <td>{product.product_name}</td>
              <td>{product.brand}</td>
              <td>{product.category}</td>
              <td>
                <button onClick={() => navigate(`/admin/products/view/${product.id}`)}>
                  View
                </button>
                <button onClick={() => navigate(`/admin/products/edit/${product.id}`)}>
                  Edit
                </button>
                <button onClick={() => handleDelete(product.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProductList;
