import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../Components/Header/Header";
import "./UserList.css";

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  const fetchUsers = (page = 1) => {
    fetch(`http://localhost:8080/api/users?page=${page}&limit=5`, {
      method: "GET",
      headers: {
        "Authorization": "API_KEY_HERE", // Substitua pela chave correta
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setUsers(data.items || []);
        setTotalPages(data.totalPages || 1); // Supondo que a API forneça totalPages
      })
      .catch((err) => {
        setMessage("Erro ao carregar a lista de usuários: " + err.message);
      });
  };

  useEffect(() => {
    fetchUsers(currentPage);
  }, [currentPage]);

  const handleAddUser = () => {
    navigate("/admin/users/add");
  };

  const handleRemoveUser = (userId) => {
    const confirmRemove = window.confirm(
      "Tem certeza de que deseja remover este usuário? Esta ação não pode ser desfeita."
    );
    if (confirmRemove) {
      fetch(`http://localhost:8080/api/users/${userId}`, {
        method: "DELETE",
        headers: {
          "Authorization": "API_KEY_HERE", // Substitua pela chave correta
        },
      })
        .then(() => {
          fetchUsers(currentPage); // Atualiza a lista após remoção
          alert("Usuário removido com sucesso!");
        })
        .catch((err) => {
          setMessage("Erro ao remover o usuário: " + err.message);
        });
    }
  };

  return (
    <div className="user-list">
      <Header />
      <h1>Lista de Usuários</h1>
      {message && <p className="message">{message}</p>}
      <button onClick={handleAddUser}>Adicionar Novo Usuário</button>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Função</th>
            <th>Status</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.user_name} {user.user_last_name}</td>
                <td>{user.user_role || "Sem função"}</td> {/* Exibe função */}
                <td>{user.user_status === 1 ? "Ativo" : "Inativo"}</td>
                <td>
                  <button onClick={() => handleRemoveUser(user.id)}>
                    Remover
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5">Nenhum usuário encontrado</td>
            </tr>
          )}
        </tbody>
      </table>
      <div className="pagination">
        <button
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => prev - 1)}
        >
          Anterior
        </button>
        <span>{`Página ${currentPage} de ${totalPages}`}</span>
        <button
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage((prev) => prev + 1)}
        >
          Próxima
        </button>
      </div>
    </div>
  );
};

export default UserList;
