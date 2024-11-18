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
      <h1>{product.product_name}</h1>
      <img
        src={`http://localhost:8080/images/${product.product_main_photo}`}
        alt={product.product_name}
      />
      <p>{product.product_short_desc}</p>
      <p>{product.product_long_desc}</p>
    </div>
  );
};

export default ViewProduct;
