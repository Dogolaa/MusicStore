import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../Components/Header/Header";
import "./UserList.css";

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  // Função para buscar todos os usuários (sem paginação)
  const fetchUsers = () => {
    fetch("http://localhost:8080/api/users", {
      method: "GET",
      headers: {
        "Authorization": "API_KEY_HERE", // Substitua pela chave correta
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
      })
      .then((data) => {
        console.log(data); // Verifique os dados retornados
        setUsers(data || []); // Defina diretamente a lista de usuários
      })
      .catch((err) => {
        setMessage("Erro ao carregar a lista de usuários: " + err.message);
      });
  };

  // Chama a função para buscar os usuários logo após a montagem do componente
  useEffect(() => {
    fetchUsers();
  }, []);

  // Função para adicionar um novo usuário
  const handleAddUser = () => {
    navigate("/admin/users/add");
  };

  // Função para remover um usuário
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
          fetchUsers(); // Atualiza a lista após remoção
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
            <th>Email</th>
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
                <td>{user.user_email}</td>
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
    </div>
  );
};

export default UserList;
