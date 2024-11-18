import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AddProduct.css";

const AddProduct = () => {
  const [formData, setFormData] = useState({});
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch("http://localhost:8080/api/products", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "api-key": "your-api-key",
      },
      body: JSON.stringify(formData),
    })
      .then(() => navigate("/"))
      .catch((err) => console.error(err));
  };

  return (
    <div className="add-product">
      <h1>Add Product</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="product_name"
          placeholder="Product Name"
          onChange={handleChange}
        />
        <textarea
          name="product_short_desc"
          placeholder="Short Description"
          onChange={handleChange}
        ></textarea>
        <textarea
          name="product_long_desc"
          placeholder="Full Description"
          onChange={handleChange}
        ></textarea>
        <input
          type="text"
          name="brand"
          placeholder="Brand"
          onChange={handleChange}
        />
        <input
          type="text"
          name="category"
          placeholder="Category"
          onChange={handleChange}
        />
        <input
          type="text"
          name="product_main_photo"
          placeholder="Photo Filename"
          onChange={handleChange}
        />
        <input
          type="number"
          name="product_price"
          placeholder="Price"
          onChange={handleChange}
        />
        <button type="submit">Add Product</button>
      </form>
    </div>
  );
};

export default AddProduct;
