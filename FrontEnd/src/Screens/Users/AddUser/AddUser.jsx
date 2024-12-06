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
        user_role: "", // Agora é uma string, para um único papel
    });

    const [imageData, setImageData] = useState(null); // Estado para armazenar dados da imagem
    const [previewUrl, setPreviewUrl] = useState(""); // Estado para armazenar a URL da pré-visualização
    const [message, setMessage] = useState("");

    // Função para atualizar os dados do formulário
    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (name === "user_role") {
            // Atualiza o papel selecionado (agora único)
            setFormData({ ...formData, user_role: value });
        } else {
            setFormData({
                ...formData,
                [name]: type === "checkbox" ? (checked ? 1 : 0) : value, // Lógica para checkbox
            });
        }
    };

    // Função para lidar com o upload de imagem
    const handleImageUpload = (e) => {
        const file = e.target.files[0];

        if (!file) return;

        // Verifica o tipo de arquivo
        const allowedTypes = ["image/jpeg", "image/png", "image/jpg"];
        if (!allowedTypes.includes(file.type)) {
            setMessage(
                "Formato de arquivo não suportado. Use .jpg, .jpeg ou .png.",
            );
            return;
        }

        // Verifica o tamanho do arquivo
        const maxSize = 1 * 1024 * 1024; // 1MB
        if (file.size > maxSize) {
            setMessage("O arquivo deve ter no máximo 1MB.");
            return;
        }

        // Cria uma URL para pré-visualização
        const fileUrl = URL.createObjectURL(file);
        setPreviewUrl(fileUrl);
        setImageData(file); // Armazena o arquivo no estado
    };

    // Função de envio do formulário
    const handleSubmit = (e) => {
        e.preventDefault();

        // Validações
        if (
            !formData.user_email ||
            !formData.user_name ||
            !formData.user_last_name ||
            !formData.user_password
        ) {
            setMessage("Por favor, preencha todos os campos obrigatórios.");
            return;
        }

        if (!formData.user_role) {
            setMessage("Por favor, selecione um papel.");
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

        const userPayload = new FormData(); // Usar FormData para incluir imagem
        userPayload.append("user_email", formData.user_email);
        userPayload.append("user_name", formData.user_name);
        userPayload.append("user_last_name", formData.user_last_name);
        userPayload.append("user_password", formData.user_password);
        userPayload.append("user_status", formData.user_status);
        userPayload.append("user_ph_content", formData.user_ph_content);
        userPayload.append("user_role", formData.user_role);
        if (imageData) {
            userPayload.append("user_image", imageData); // Adiciona a imagem ao payload
        }

        // Envio da requisição para a API
        fetch("http://localhost:8080/api/users", {
            method: "POST",
            headers: {
                Authorization: "API_KEY_HERE", // Substitua com a sua chave de API
            },
            body: userPayload,
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Erro: ${response.statusText}`);
                }
                return response.json(); // Processa a resposta como JSON
            })
            .then((data) => {
                if (data && data.success) {
                    setMessage("Os dados do usuário foram salvos com sucesso.");
                    setTimeout(() => navigate("/admin/users"), 2000); // Redireciona para lista após sucesso
                } else {
                    setMessage(
                        "Erro ao salvar o usuário: " +
                            (data.message || "Desconhecido"),
                    );
                }
            })
            .catch((err) => {
                setMessage("Erro ao conectar ao servidor: " + err.message);
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
                <p
                    className={`message ${message.includes("sucesso") ? "success" : "error"}`}
                >
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
                        checked={formData.user_status === 1}
                        onChange={handleChange}
                    />
                </div>

                {/* <div className="form-group">
          <label>Conteúdo da Foto (opcional)</label>
          <input
            type="text"
            name="user_ph_content"
            value={formData.user_ph_content}
            onChange={handleChange}
            maxLength={80}
          />
        </div> */}

                <div className="form-group">
                    <label>Papel</label>
                    <select
                        name="user_role"
                        value={formData.user_role}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Selecione um papel</option>
                        <option value="Administrator">Administrador</option>
                        <option value="Sales Manager">Gerente de Vendas</option>
                        <option value="Editor">Editor</option>
                        <option value="Assistant">Assistente</option>
                        <option value="Shipping Manager">
                            Gerente de Logística
                        </option>
                    </select>
                </div>

                {/* Upload de Imagem */}
                <div className="form-group image-upload">
                    <label
                        htmlFor="imageUpload"
                        className="custom-upload-button"
                    >
                        Selecione uma Imagem
                    </label>
                    <input
                        id="imageUpload"
                        type="file"
                        accept="image/png, image/jpeg"
                        onChange={handleImageUpload}
                    />
                    <div className="image-preview">
                        {previewUrl ? (
                            <img src={previewUrl} alt="Pré-visualização" />
                        ) : (
                            <span>Nenhuma imagem selecionada</span>
                        )}
                    </div>
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
