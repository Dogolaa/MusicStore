import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../Components/Header/Header";
import "./ProductList.css";

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [totalPages, setTotalPages] = useState(1);
    const [currentPage, setCurrentPage] = useState(1);
    const [searchQuery, setSearchQuery] = useState("");
    const [sortField, setSortField] = useState("product_name");
    const [sortOrder, setSortOrder] = useState("asc");
    const navigate = useNavigate();

    const fetchProducts = () => {
        const url = new URL("http://localhost:8080/api/products");
        url.searchParams.append("page", currentPage);
        url.searchParams.append("asc", sortOrder === "asc");
        url.searchParams.append("sortField", sortField); // Adiciona o campo de ordenação

        if (searchQuery) {
            url.searchParams.append("search", searchQuery); // Adiciona pesquisa se houver
        }

        fetch(url, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then((res) => res.json())
            .then((data) => {
                setProducts(data.items || []);
                setTotalPages(data.totalPages || 1);
            })
            .catch((err) => console.error("Failed to fetch products:", err));
    };

    useEffect(() => {
        fetchProducts();
    }, [currentPage, sortOrder, sortField, searchQuery]);

    const handleDelete = (id) => {
        if (window.confirm("Are you sure you want to delete this product?")) {
            fetch(`http://localhost:8080/api/products/${id}`, {
                method: "DELETE",
                headers: { "api-key": "your-api-key" },
            })
                .then(() => {
                    setProducts(
                        products.filter((product) => product.id !== id),
                    );
                })
                .catch((err) => console.error(err));
        }
    };

    const handleSearch = (e) => {
        setSearchQuery(e.target.value);
    };

    const handleSortChange = (e) => {
        const [field, order] = e.target.value.split("-");
        setSortField(field);
        setSortOrder(order);
    };

    const handlePageChange = (direction) => {
        if (direction === "next" && currentPage < totalPages) {
            setCurrentPage((prev) => prev + 1);
        } else if (direction === "prev" && currentPage > 1) {
            setCurrentPage((prev) => prev - 1);
        }
    };

    const handleReset = () => {
        // Realiza uma solicitação para resetar os produtos para o estado inicial (se suportado pelo backend)
        fetch("http://localhost:8080/api/reset-products", {
            method: "POST",
            headers: { "api-key": "your-api-key" },
        })
            .then(() => {
                setCurrentPage(1);
                setSortField("product_name");
                setSortOrder("asc");
                setSearchQuery("");
                fetchProducts();
            })
            .catch((err) => console.error("Failed to reset products:", err));
    };

    return (
        <div className="product-list">
            <Header />
            <h1>BlueVelvet Music Store</h1>
            <p>
                Bem-vindo, {"username"} ({"role"})
            </p>
            <button onClick={handleReset}>Logout</button>

            <div className="controls">
                <button onClick={handleReset}>
                    Reset Products to Initial State
                </button>
                <button onClick={() => navigate("/admin/products/add")}>
                    Add Product
                </button>
                <input
                    type="text"
                    placeholder="Search products..."
                    value={searchQuery}
                    onChange={handleSearch}
                />
                <select
                    onChange={handleSortChange}
                    value={`${sortField}-${sortOrder}`}
                >
                    <option value="product_name-asc">Sort by Name (A-Z)</option>
                    <option value="product_name-desc">
                        Sort by Name (Z-A)
                    </option>
                    <option value="id-asc">Sort by ID (Ascending)</option>
                    <option value="id-desc">Sort by ID (Descending)</option>
                    <option value="brand-asc">Sort by Brand (A-Z)</option>
                    <option value="brand-desc">Sort by Brand (Z-A)</option>
                    <option value="category-asc">Sort by Category (A-Z)</option>
                    <option value="category-desc">
                        Sort by Category (Z-A)
                    </option>
                </select>
            </div>

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
                    {products.length > 0 ? (
                        products.map((product) => (
                            <tr key={product.id}>
                                <td>{product.id}</td>
                                <td>
                                    <img
                                        src={`http://localhost:8080/images/products/${product.product_main_photo}`}
                                        alt={product.product_name}
                                        width="50"
                                    />
                                </td>
                                <td>{product.product_name}</td>
                                <td>{product.brand}</td>
                                <td>{product.category}</td>
                                <td>
                                    <button
                                        onClick={() =>
                                            navigate(
                                                `/admin/products/view/${product.id}`,
                                            )
                                        }
                                    >
                                        View
                                    </button>
                                    <button
                                        onClick={() =>
                                            navigate(
                                                `/admin/products/edit/${product.id}`,
                                            )
                                        }
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={() => handleDelete(product.id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="6">No products found</td>
                        </tr>
                    )}
                </tbody>
            </table>

            <div className="pagination">
                <button
                    onClick={() => handlePageChange("prev")}
                    disabled={currentPage === 1}
                >
                    Previous
                </button>
                <span>
                    Page {currentPage} of {totalPages}
                </span>
                <button
                    onClick={() => handlePageChange("next")}
                    disabled={currentPage === totalPages}
                >
                    Next
                </button>
            </div>
        </div>
    );
};

export default ProductList;
