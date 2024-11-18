import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./EditProduct.css";

const EditProduct = () => {
  const { id } = useParams();
  const [product, setProduct] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`http://localhost:8080/api/products/${id}`, {
      headers: { "api-key": "your-api-key" },
    })
      .then((res) => res.json())
      .then((data) => setProduct(data))
      .catch((err) => console.error(err));
  }, [id]);

  const handleChange = (e) => {
    setProduct({ ...product, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch(`http://localhost:8080/api/products/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "api-key": "your-api-key",
      },
      body: JSON.stringify(product),
    })
      .then(() => navigate("/"))
      .catch((err) => console.error(err));
  };

  return (
    <div className="edit-product">
      <h1>Edit Product</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="product_name"
          value={product.product_name || ""}
          onChange={handleChange}
        />
        <textarea
          name="product_short_desc"
          value={product.product_short_desc || ""}
          onChange={handleChange}
        ></textarea>
        <textarea
          name="product_long_desc"
          value={product.product_long_desc || ""}
          onChange={handleChange}
        ></textarea>
        <input
          type="number"
          name="product_price"
          value={product.product_price || ""}
          onChange={handleChange}
        />
        <button type="submit">Update Product</button>
      </form>
    </div>
  );
};

export default EditProduct;
