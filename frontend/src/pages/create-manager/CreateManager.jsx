import './CreateManager.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function CreateManager() {

    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const handleSubmit = (e) => {
        e.preventDefault();
        try {
            const user = {
                name: name,
                email: email,
                passwordHash: password,
                role: 'manager'
            };
            fetch('http://localhost:8080/admin/create-manager', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(user)
            });
        } catch (error) {
            console.log(error);
        }
        navigate(-1);
    };

    return (
        <div className="CreateManager">
            <h1>Create Manager</h1>
            <form onSubmit={handleSubmit} className="manager-form">
                <div className="form-group">
                    <label htmlFor="name">Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button className="button-manager-creation" type="submit">Create Manager</button>
            </form>
        </div>
    );
}

export default CreateManager;