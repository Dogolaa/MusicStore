import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../Components/Header/Header";
import "./UserList.css";

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [filters, setFilters] = useState({ name: "", role: "", status: "" });
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    // Função para buscar a lista filtrada de usuários
    const fetchUsers = () => {
        const queryParams = new URLSearchParams(filters).toString();
        console.log(localStorage.getItem("token"));
        fetch(`http://localhost:8080/api/admin/users?${queryParams}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`, // Substitua pela chave correta
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(
                        "Erro na requisição: " + response.statusText,
                    );
                }
                return response.json();
            })
            .then((data) => {
                setUsers(data.items); // ou setUsers(data.users) caso a resposta seja { users: [...] }
            })
            .catch((err) => {
                setMessage(
                    "Erro ao carregar a lista de usuários: " + err.message,
                );
            });
    };

    // Atualiza a lista ao montar o componente ou mudar filtros
    useEffect(() => {
        fetchUsers();
    }, [filters]);

    // Função para adicionar um novo usuário
    const handleAddUser = () => {
        navigate("/admin/users/add");
    };

    // Função para editar um usuário
    const handleEditUser = (userId) => {
        navigate(`/admin/users/edit/${userId}`);
    };

    // Função para remover um usuário
    const handleRemoveUser = (userId) => {
        const confirmRemove = window.confirm(
            "Tem certeza de que deseja remover este usuário? Esta ação não pode ser desfeita.",
        );
        if (confirmRemove) {
            fetch(`http://localhost:8080/api/admin/users/${userId}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            })
                .then(() => {
                    fetchUsers();
                    alert("Usuário removido com sucesso!");
                })
                .catch((err) => {
                    setMessage("Erro ao remover o usuário: " + err.message);
                });
        }
    };

    // Atualiza filtros
    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters((prevFilters) => ({
            ...prevFilters,
            [name]: value,
        }));
    };

    return (
        <div className="user-list">
            <Header />
            <h1>Lista de Usuários</h1>
            {message && <p className="message">{message}</p>}

            {/* Barra de Filtros */}
            <div className="filters">
                <input
                    type="text"
                    name="name"
                    placeholder="Buscar por Nome"
                    value={filters.name}
                    onChange={handleFilterChange}
                />
                <select
                    name="role"
                    value={filters.role}
                    onChange={handleFilterChange}
                >
                    <option value="">Todas as Funções</option>
                    <option value="Administrador">Administrador</option>
                    <option value="Editor">Editor</option>
                </select>
                <select
                    name="status"
                    value={filters.status}
                    onChange={handleFilterChange}
                >
                    <option value="">Todos os Status</option>
                    <option value="Ativo">Ativo</option>
                    <option value="Inativo">Inativo</option>
                </select>
                <button onClick={fetchUsers}>Aplicar Filtros</button>
            </div>

            <button onClick={handleAddUser}>Adicionar Novo Usuário</button>

            <table>
                <thead>
                    <tr>
                        <th>Foto</th>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Função</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {users.length > 0 ? (
                        users.map((user) => (
                            <tr key={user.id}>
                                <td>
                                    {user.profilePhoto ? (
                                        <img
                                            src={user.profilePhoto}
                                            alt="Foto de perfil"
                                            className="profile-thumbnail"
                                        />
                                    ) : (
                                        <div className="profile-placeholder">
                                            👤
                                        </div>
                                    )}
                                </td>
                                <td>{user.id}</td>
                                <td>
                                    {user.user_name} {user.user_last_name}
                                </td>
                                <td>{user.user_email}</td>
                                <td>{user.user_role}</td>
                                <td>
                                    {user.user_status === 1
                                        ? "Ativo"
                                        : "Inativo"}
                                </td>
                                <td>
                                    <button
                                        onClick={() => handleEditUser(user.id)}
                                    >
                                        Editar
                                    </button>
                                    <button
                                        onClick={() =>
                                            handleRemoveUser(user.id)
                                        }
                                    >
                                        Remover
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="7">Nenhum usuário encontrado</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default UserList;
