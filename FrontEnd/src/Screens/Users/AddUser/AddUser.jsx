import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../Components/Header/Header";
import "./AddUser.css";

const AddUser = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    user_email: "",
    user_name: "",
    user_last_name: "",
    user_password: "",
    user_status: 1, // 1 para ativo, 0 para inativo
    user_ph_content: "",
  });

  const [message, setMessage] = useState("");

  // Função para atualizar os dados do formulário
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? (checked ? 1 : 0) : value, // Lógica para checkbox
    });
  };

  // Função de envio do formulário
  const handleSubmit = (e) => {
    e.preventDefault();

    // Validações
    if (!formData.user_email || !formData.user_name || !formData.user_last_name || !formData.user_password) {
      setMessage("Por favor, preencha todos os campos obrigatórios.");
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(formData.user_email)) {
      setMessage("Por favor, insira um e-mail válido.");
      return;
    }

    if (formData.user_password.length < 6) {
      setMessage("A senha deve ter pelo menos 6 caracteres.");
      return;
    }

    const userPayload = {
      user_email: formData.user_email,
      user_name: formData.user_name,
      user_last_name: formData.user_last_name,
      user_password: formData.user_password,
      user_status: formData.user_status,
      user_ph_content: formData.user_ph_content,
    };

    // Envio da requisição para a API
    fetch("http://localhost:8080/api/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "API_KEY_HERE", // Substitua com a sua chave de API
      },
      body: JSON.stringify(userPayload),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Erro: ${response.statusText}`);
        }
        return response.json(); // Processa a resposta como JSON
      })
      .then((data) => {
        // Verifica se a resposta contém sucesso
        if (data && data.success) {
          setMessage("Os dados do usuário foram salvos com sucesso.");
          setTimeout(() => navigate("/admin/users"), 2000); // Redireciona para lista após sucesso
        } else {
          setMessage("Erro ao salvar o usuário: " + (data.message || "Desconhecido"));
        }
      })
      .catch((err) => {
        setMessage("Erro ao conectar ao servidor: " + err.message); // Mensagem de erro de conexão
      });
  };

  // Função para cancelar a criação de um novo usuário
  const handleCancel = () => {
    navigate("/admin/users");
  };

  return (
    <div className="add-user">
      <Header />
      <h1>Criar Novo Usuário</h1>
      {message && (
        <p className={`message ${message.includes("sucesso") ? "success" : "error"}`}>
          {message}
        </p>
      )}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>E-mail</label>
          <input
            type="email"
            name="user_email"
            value={formData.user_email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Nome</label>
          <input
            type="text"
            name="user_name"
            value={formData.user_name}
            onChange={handleChange}
            required
            minLength={2}
            maxLength={60}
          />
        </div>

        <div className="form-group">
          <label>Sobrenome</label>
          <input
            type="text"
            name="user_last_name"
            value={formData.user_last_name}
            onChange={handleChange}
            required
            minLength={2}
            maxLength={60}
          />
        </div>

        <div className="form-group">
          <label>Senha</label>
          <input
            type="password"
            name="user_password"
            value={formData.user_password}
            onChange={handleChange}
            required
            maxLength={64}
          />
        </div>

        <div className="form-group">
          <label>Habilitado</label>
          <input
            type="checkbox"
            name="user_status"
            checked={formData.user_status === 1} // Marca como habilitado se status for 1
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Conteúdo da Foto (opcional)</label>
          <input
            type="text"
            name="user_ph_content"
            value={formData.user_ph_content}
            onChange={handleChange}
            maxLength={80}
          />
        </div>

        <div className="form-actions">
          <button type="submit">Salvar</button>
          <button type="button" onClick={handleCancel}>
            Cancelar
          </button>
        </div>
      </form>
    </div>
  );
};

export default AddUser;
