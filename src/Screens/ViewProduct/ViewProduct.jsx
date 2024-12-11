import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "./ViewProduct.css";

const ViewProduct = () => {
    const { id } = useParams();
    const [product, setProduct] = useState({});

    useEffect(() => {
        fetch(`http://localhost:8080/api/products/${id}`, {
            headers: { "api-key": "your-api-key" },
        })
            .then((res) => res.json())
            .then((data) => setProduct(data))
            .catch((err) => console.error(err));
    }, [id]);

    return (
        <div className="view-product">
            <h1>BlueVelvet Music Store</h1>
            <h2>Product Details</h2>
            <h3>{product.product_name}</h3>
            <img
                src={`http://localhost:8080/images/products/${product.product_main_photo}`}
                alt={product.product_name}
                style={{ width: "300px", marginBottom: "20px" }}
            />
            <p>
                <strong>Brand:</strong> {product.brand || "N/A"}
            </p>
            <p>
                <strong>Category:</strong> {product.category || "N/A"}
            </p>
            <p>
                <strong>Short Description:</strong> {product.product_short_desc}
            </p>
            <p>
                <strong>Full Description:</strong> {product.product_long_desc}
            </p>
            <p>
                <strong>List Price:</strong> $
                {product.product_price?.toFixed(2)}
            </p>
            <p>
                <strong>Discount:</strong> {product.product_discount}%
            </p>
        </div>
    );
};

export default ViewProduct;
