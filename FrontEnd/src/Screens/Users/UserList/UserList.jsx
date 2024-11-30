import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../Components/Header/Header";
import "./UserList.css";

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const fetchUsers = () => {
    fetch("http://localhost:8080/api/users", {
      method: "GET",
      headers: {
        Authorization: "API_KEY_HERE", // Substitua com a sua chave de API
      },
    })
      .then((response) => response.json())
      .then((data) => setUsers(data))
      .catch((err) => {
        setMessage("Erro ao carregar a lista de usuários: " + err.message);
      });
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAddUser = () => {
    navigate("/admin/users/add");
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
            <th>E-mail</th>
            <th>Nome</th>
            <th>Status</th>
            <th>Conteúdo da Foto</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.user_email}</td>
                <td>
                  {user.user_name} {user.user_last_name}
                </td>
                <td>{user.user_status === 1 ? "Ativo" : "Inativo"}</td>
                <td>{user.user_ph_content}</td>
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
