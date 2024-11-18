import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./EditProduct.css";

const EditProduct = () => {
  const { id } = useParams();
  const [product, setProduct] = useState({});
  const navigate = useNavigate();

  // Fetch product data on component mount
  useEffect(() => {
    fetch(`http://localhost:8080/api/products/${id}`, {
      headers: { "api-key": "your-api-key" },
    })
      .then((res) => res.json())
      .then((data) => setProduct(data))
      .catch((err) => console.error("Error fetching product:", err));
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduct({ ...product, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Construct updated product object
    const updatedProduct = {
      product_name: product.product_name,
      product_short_desc: product.product_short_desc,
      product_long_desc: product.product_long_desc,
      product_price: parseFloat(product.product_price),
      product_main_photo: product.product_main_photo,
      product_discount: parseFloat(product.product_discount),
      product_status: parseInt(product.product_status),
      product_has_stocks: parseInt(product.product_has_stocks),
      product_width: parseFloat(product.product_width),
      product_lenght: parseFloat(product.product_lenght),
      product_height: parseFloat(product.product_height),
      product_cost: parseFloat(product.product_cost),
    };

    fetch(`http://localhost:8080/api/products/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "api-key": "your-api-key",
      },
      body: JSON.stringify(updatedProduct),
    })
      .then(() => navigate("/"))
      .catch((err) => console.error("Error updating product:", err));
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
          placeholder="Product Name"
        />
        <textarea
          name="product_short_desc"
          value={product.product_short_desc || ""}
          onChange={handleChange}
          placeholder="Short Description"
        ></textarea>
        <textarea
          name="product_long_desc"
          value={product.product_long_desc || ""}
          onChange={handleChange}
          placeholder="Long Description"
        ></textarea>
        <input
          type="text"
          name="product_main_photo"
          value={product.product_main_photo || ""}
          onChange={handleChange}
          placeholder="Photo Filename"
        />
        <input
          type="number"
          name="product_price"
          value={product.product_price || ""}
          onChange={handleChange}
          placeholder="Price"
          step="0.01"
        />
        <input
          type="number"
          name="product_discount"
          value={product.product_discount || ""}
          onChange={handleChange}
          placeholder="Discount (%)"
          step="0.01"
        />
        <select
          name="product_status"
          value={product.product_status || ""}
          onChange={handleChange}
        >
          <option value="1">Active</option>
          <option value="0">Inactive</option>
        </select>
        <input
          type="number"
          name="product_has_stocks"
          value={product.product_has_stocks || ""}
          onChange={handleChange}
          placeholder="Stock Status (1 for Yes, 0 for No)"
        />
        <input
          type="number"
          name="product_width"
          value={product.product_width || ""}
          onChange={handleChange}
          placeholder="Width (cm)"
          step="0.01"
        />
        <input
          type="number"
          name="product_lenght"
          value={product.product_lenght || ""}
          onChange={handleChange}
          placeholder="Length (cm)"
          step="0.01"
        />
        <input
          type="number"
          name="product_height"
          value={product.product_height || ""}
          onChange={handleChange}
          placeholder="Height (cm)"
          step="0.01"
        />
        <input
          type="number"
          name="product_cost"
          value={product.product_cost || ""}
          onChange={handleChange}
          placeholder="Cost"
          step="0.01"
        />
        <button type="submit">Update Product</button>
      </form>
    </div>
  );
};

export default EditProduct;
