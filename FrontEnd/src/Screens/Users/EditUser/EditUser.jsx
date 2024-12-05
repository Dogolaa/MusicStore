import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "./EditUser.css";

const EditUser = () => {
  const { id } = useParams();
  const [user, setUser] = useState({});
  const [isAccessEnabled, setIsAccessEnabled] = useState(false); // Controle do toggle
  const navigate = useNavigate();

  // Fetch user data on component mount
  useEffect(() => {
    fetch(`http://localhost:8080/api/users/${id}`, {
      headers: { "Authorization": "your-api-key" },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao buscar o usuário");
        }
        return res.json();
      })
      .then((data) => {
        setUser(data);
        setIsAccessEnabled(data.user_status === 1); // Define o estado inicial do toggle
      })
      .catch((err) => console.error("Error fetching user:", err));
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser({ ...user, [name]: value });
  };

  const handleToggleAccess = () => {
    setIsAccessEnabled((prev) => !prev); // Alterna o estado
    setUser((prevUser) => ({
      ...prevUser,
      user_status: prevUser.user_status === 1 ? 0 : 1, // Atualiza o estado do backend
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Construct updated user object
    const updatedUser = {
      ...user,
      user_status: isAccessEnabled ? 1 : 0, // Garante o envio do estado correto
    };

    fetch(`http://localhost:8080/api/users/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "your-api-key",
      },
      body: JSON.stringify(updatedUser),
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Erro ao atualizar o usuário");
        }
        alert("Usuário atualizado com sucesso!");
        navigate("/admin/users");
      })
      .catch((err) => console.error("Error updating user:", err));
  };

  return (
    <div className="edit-user">
      <h1>Editar Usuário</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          name="user_email"
          value={user.user_email || ""}
          onChange={handleChange}
          placeholder="E-mail"
        />
        <input
          type="text"
          name="user_name"
          value={user.user_name || ""}
          onChange={handleChange}
          placeholder="Nome"
        />
        <input
          type="text"
          name="user_last_name"
          value={user.user_last_name || ""}
          onChange={handleChange}
          placeholder="Sobrenome"
        />
        <input
          type="text"
          name="user_ph_content"
          value={user.user_ph_content || ""}
          onChange={handleChange}
          placeholder="Foto de Perfil (Conteúdo Base64 ou URL)"
        />

        {/* Toggle para habilitar/desabilitar acesso */}
        <div className="access-toggle">
          <label>
            <input
              type="checkbox"
              checked={isAccessEnabled}
              onChange={handleToggleAccess}
            />
            {isAccessEnabled ? "Habilitar acesso" : "Desabilitar acesso"}
          </label>
        </div>

        <button type="submit">Atualizar Usuário</button>
      </form>
    </div>
  );
};

export default EditUser;
