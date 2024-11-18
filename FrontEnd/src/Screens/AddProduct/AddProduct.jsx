import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./AddProduct.css";

const AddProduct = () => {
  const [formData, setFormData] = useState({});
  const [brands, setBrands] = useState([]);
  const [categories, setCategories] = useState([]);
  const navigate = useNavigate();

  // Fetch brands and categories on component mount
  useEffect(() => {
    fetch("http://localhost:8080/api/brands", {
      headers: {
        "api-key": "your-api-key",
      },
    })
      .then((res) => res.json())
      .then((data) => setBrands(data))
      .catch((err) => console.error("Error fetching brands:", err));

    fetch("http://localhost:8080/api/categories", {
      headers: {
        "api-key": "your-api-key",
      },
    })
      .then((res) => res.json())
      .then((data) => setCategories(data))
      .catch((err) => console.error("Error fetching categories:", err));
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Validate required fields
    if (!formData.brand || !formData.category) {
      alert("Please select a brand and a category.");
      return;
    }

    fetch("http://localhost:8080/api/products", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "api-key": "your-api-key",
      },
      body: JSON.stringify({
        ...formData,
        id_brand: formData.brand,
        id_category: formData.category,
      }),
    })
      .then(() => navigate("/"))
      .catch((err) => console.error("Error submitting product:", err));
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

        {/* Brand Dropdown */}
        <select name="brand" onChange={handleChange}>
          <option value="">Select Brand</option>
          {brands.map((brand) => (
            <option key={brand.id} value={brand.id}>
              {brand.name}
            </option>
          ))}
        </select>

        {/* Category Dropdown */}
        <select name="category" onChange={handleChange}>
          <option value="">Select Category</option>
          {categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>

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
