import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./AddProduct.css";

const AddProduct = () => {
    const [formData, setFormData] = useState({});
    const [brands, setBrands] = useState([]);
    const navigate = useNavigate();

    // Fetch brands on component mount
    useEffect(() => {
        fetch("http://localhost:8080/api/brands", {
            headers: { "api-key": "your-api-key" },
        })
            .then((res) => res.json())
            .then((data) => setBrands(data))
            .catch((err) => console.error("Error fetching brands:", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // Ensure required fields are filled
        if (!formData.product_name || !formData.id_brand) {
            alert("Please fill in all required fields.");
            return;
        }

        // Prepare FormData for the request
        const productData = new FormData();
        productData.append("image", document.getElementById("image").files[0]); // Append the image

        const product = {
            id_brand: formData.id_brand,
            product_name: formData.product_name,
            product_short_desc: formData.product_short_desc,
            product_long_desc: formData.product_long_desc,
            product_price: formData.product_price,
            product_discount: formData.product_discount,
            product_status: formData.product_status,
            product_has_stocks: formData.product_has_stocks,
            product_width: formData.product_width,
            product_lenght: formData.product_lenght,
            product_height: formData.product_height,
            product_cost: formData.product_cost,
        };

        console.log(product);

        productData.append("product", JSON.stringify(product)); // Append the rest of the product data as a JSON string

        fetch("http://localhost:8080/api/products", {
            method: "POST",
            body: productData,
        })
            .then((response) => response)
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
                    required
                />
                <input
                    type="file"
                    id="image"
                    name="image"
                    placeholder="Main Photo Filename"
                    required
                />
                <textarea
                    name="product_short_desc"
                    placeholder="Short Description"
                    onChange={handleChange}
                ></textarea>
                <textarea
                    name="product_long_desc"
                    placeholder="Long Description"
                    onChange={handleChange}
                ></textarea>
                <input
                    type="number"
                    name="product_price"
                    placeholder="Price"
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="product_discount"
                    placeholder="Discount (%)"
                    onChange={handleChange}
                />
                <select name="id_brand" onChange={handleChange} required>
                    <option value="">Select Brand</option>
                    {brands.map((brand) => (
                        <option key={brand.id} value={brand.id}>
                            {brand.brand_name}
                        </option>
                    ))}
                </select>
                <select name="product_status" onChange={handleChange}>
                    <option value="">Select Status</option>
                    <option value="1">Active</option>
                    <option value="0">Inactive</option>
                </select>
                <input
                    type="number"
                    name="product_has_stocks"
                    placeholder="Stock Status (1 for Yes, 0 for No)"
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="product_width"
                    placeholder="Width (cm)"
                    step="0.01"
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="product_lenght"
                    placeholder="Length (cm)"
                    step="0.01"
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="product_height"
                    placeholder="Height (cm)"
                    step="0.01"
                    onChange={handleChange}
                />
                <input
                    type="number"
                    name="product_cost"
                    placeholder="Cost"
                    step="0.01"
                    onChange={handleChange}
                />
                <button type="submit">Add Product</button>
            </form>
        </div>
    );
};

export default AddProduct;
