import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    // useEffect(() => {
    //     console.log(email);
    // }, [email]);

    const handleLogin = () => {
        const loginJson = JSON.stringify({
            email: email,
            password: password,
        });

        console.log(loginJson);

        fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: loginJson,
        })
            .then((res) => res.json())
            .then((data) => {
                if (data.token) {
                    localStorage.setItem("token", data.token);
                    navigate("/admin");
                } else {
                    console.log("Erro ao efetuar login");
                }
            })
            .catch((err) => console.error("Error in login:", err));
    };

    return (
        <div>
            <h1>Login</h1>
            <input
                type="text"
                placeholder="E-mail"
                className="form-control"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input
                type="text"
                placeholder="Senha"
                className="form-control"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button className="btn btn-primary" onClick={handleLogin}>
                Login
            </button>
        </div>
    );
}
